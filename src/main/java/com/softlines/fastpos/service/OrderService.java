package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.CashOperation;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderInfo;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.dto.Message;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.mapping.OrderMapper;
import com.softlines.fastpos.repository.CashOperationRepository;
import com.softlines.fastpos.repository.OrderRepository;
import com.softlines.fastpos.security.securityservice.SessionService;
import com.softlines.fastpos.sse.model.SSEventType;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
public class OrderService {

    @PersistenceContext
    EntityManager em;


    @Language("HQL")
    String selectOrderInfoQuery = "select info from OrderInfo  info where info.date = :date";
    @Language("HQL")
    String deleteTempOrderQuery = "delete Order o where o.id = :id";


    private final SessionService sessionService;

    private final NotificationService notificationService;


    private final NumerationService numerationService;
    private final OrderRepository orderRepository;
    private final CashOperationRepository cashOperationRepository;
    private final OrderMapper orderMapper;

    public static boolean IsActionNewPayment(Order previous, Order current) {
        return (previous.getState() != OrderState.Payed&& previous.getState()!= OrderState.PaidModified  ) && current.getState() == OrderState.Payed;
    }
    public static boolean IsActionRefund(Order previous, Order current) {
        return previous.getState() == OrderState.Payed && current.getState() == OrderState.Refunded;
    }

    public static boolean IsActionModifiedPayment(Order previous, Order current) {
        return previous.getState() == OrderState.PaidModified && current.getState() == OrderState.Payed;
    }

    public static boolean IsActionCancel(Order source, Order incoming) {
        return source.getState() != OrderState.Canceled && incoming.getState() == OrderState.Canceled;
    }

    public String maskOrderNumber(Order order) {
        return numerationService.mask(order.getOrderNumber(), "%03d");
    }

    public OrderInfo getOrderInfoOfTheDay() {
        var query = em.createQuery(selectOrderInfoQuery).setParameter("date", LocalDate.now());
        var result = query.getResultList();
        if (result.size() == 0) {

            return createOrderInfoOfTheDay();
        }
        return (OrderInfo) result.get(0);
    }

    public void SaveOrderInfo(OrderInfo info) {
        var session = em.unwrap(Session.class);
        session.saveOrUpdate(info);
    }


    protected OrderInfo createOrderInfoOfTheDay() {
        var orderInfo = OrderInfo.builder().date(LocalDate.now()).build();
        em.persist(orderInfo);
        return orderInfo;
    }

    @Transactional(transactionManager = "transactionManager")
    public Order saveOrder(Order order) {

        OrderInfo orderInfo = null;
//        if (order.getState()!= OrderState.Temporary){
//            orderInfo = setOrderNumberAndCode(order);
//        }
        orderInfo = setOrderNumberAndCode(order);

        if (order.getState() == OrderState.Payed) {
            var cashOp = CashOperation.builder().amount(order.getNewTotal()).order(order).build();
            order.setCashOperations(Set.of(cashOp));
        }
        var created = orderRepository.saveOrder(order);
        if (orderInfo!= null) {
            SaveOrderInfo(orderInfo);
        }
        return created;
    }

    @NotNull
    private OrderInfo setOrderNumberAndCode(Order order) {
        var orderInfo = getOrderInfoOfTheDay();
        orderInfo.incrementOrderCount();
        order.setOrderNumber(orderInfo.getOrderCount());
        var code = maskOrderNumber(order);
        order.setOrderCode(code);
        return orderInfo;
    }

    public List<Long> getAndUnlockOrdersLockedBy(String source) {
        var orders = orderRepository.findLockedOrdersBySessionId(source);
        var ids = orders.stream().map(Order::getId).collect(Collectors.toList());
        orders.forEach(o -> {
            o.setLocked(false);
            o.setLockedBy(null);
        });
        orderRepository.saveAll(orders);
        return ids;
    }


    public Order onPaidOrderModified(Order previous, Order current) {
        var payedAmount = current.getGivenAmount() - current.getReturnedAmount();
        var refunded = previous.getNewTotal()- current.getNewTotal();
        if (payedAmount != 0) {
            var cashOperation = CashOperation.builder().amount(payedAmount).order(current).build();
            cashOperationRepository.saveAndFlush(cashOperation);
        }else {
            if (refunded!= 0){
                var cashOperation = CashOperation.builder().amount(refunded).order(current).build();
                cashOperationRepository.saveAndFlush(cashOperation);
            }
        }

        return current;
    }
    @Transactional(transactionManager = "transactionManager")
    public OrderDto updateOrder(Order order, Object pub) {
        var previousState = orderRepository.findByIdWithCashOperations(order.getId()).get();
        String eventType = SSEventType.UPDATE_ORDER;
        OrderInfo orderInfo = null;
        if (order.getOrderNumber() == null){
            orderInfo = setOrderNumberAndCode(order);
        }

        if (OrderService.IsActionRefund(previousState,order)){
           var amount = -previousState.getNewTotal();
            var cashOperation = CashOperation.builder().amount(amount).order(order).build();
            cashOperationRepository.saveAndFlush(cashOperation);
        }
        if (OrderService.IsActionCancel(previousState, order)) {
            order = onOrderCanceled(order, previousState);
            eventType = SSEventType.CANCEL_ORDER;
        }

        order = orderRepository.saveOrder(order);

        if (OrderService.IsActionNewPayment(previousState, order)) {
            var payedAmount = order.getGivenAmount() + order.getReturnedAmount();
            Set<CashOperation> cashOperations = Set.of(CashOperation.builder().order(order).amount(payedAmount).build());
//            order.setCashOperations(cashOperations);
            Order finalOrder = order;
            cashOperations.forEach(co->co.setOrder(finalOrder));
            cashOperationRepository.saveAll(cashOperations);
            cashOperationRepository.flush();
            eventType = SSEventType.PAY_ORDER;
        }

        if (OrderService.IsActionModifiedPayment(previousState, order)) {
            order = onPaidOrderModified(previousState, order);
            eventType = SSEventType.PAY_ORDER;
        }

        if (orderInfo!= null) {
            SaveOrderInfo(orderInfo);
        }

        var dto = orderMapper.toOrderDto(order);
        sendOrderMessage(pub, eventType, order.getModificationSessionId(), dto);

        return dto;
    }


    @NotNull
    private Order onOrderCanceled(Order order, Order original) {
        var previousState = original.getState();
        order = orderRepository.saveOrder(order);

        var canceledBy = sessionService.getUserFullNameFromSession(order.getModificationSessionId());
        order.setCanceledInfo(previousState, canceledBy);
        return order;
    }


    private void sendOrderMessage(Object pub, String eventType, String source, OrderDto dto) {

        var message = Message.builder()
                .type(eventType)
                .content(dto)
                .source(source)
                .build();
        notificationService.publish(pub, message);
    }
    @Transactional
    public void deleteTempOrder(Long id){
        var query = em.createQuery(deleteTempOrderQuery).setParameter("id",id);
//        return query.executeUpdate() ==1;
        query.executeUpdate() ;
    }

    @Transactional(transactionManager = "transactionManager")
    public Order splitOrderFrom(Order subOrder, Order originalOrder){
        var orderInfo = setOrderNumberAndCode(subOrder);
        removeTransferredItemsFromOriginalOrder(subOrder, originalOrder);
        updateOrderItemQuantitiesOfOriginalOrder(subOrder, originalOrder);
        originalOrder.setState(OrderState.Splitted);
        var savedOriginalOrder = orderRepository.saveAll(List.of(originalOrder,subOrder)).get(0);
        SaveOrderInfo(orderInfo);
        return savedOriginalOrder;
    }

    private void removeTransferredItemsFromOriginalOrder(Order subOrder, Order originalOrder) {
        originalOrder.getOrderItems().removeIf(orderItem -> {
            if (orderItem.getQuantity()==1){
                return subOrder.getOrderItems().stream().anyMatch(item -> Objects.equals(item.getSplitFromOrderItemId(), orderItem.getId()));
            }
            var count = subOrder.getOrderItems().stream().filter(item-> Objects.equals(item.getSplitFromOrderItemId(), orderItem.getId())).count();
            return count == orderItem.getQuantity();
        });
    }

    private void updateOrderItemQuantitiesOfOriginalOrder(Order subOrder, Order originalOrder) {
        originalOrder.getOrderItems().forEach(orderItem -> {
            if (orderItem.getQuantity()>1){
                var count = (int)subOrder.getOrderItems().stream().filter(item-> Objects.equals(item.getSplitFromOrderItemId(), orderItem.getId())).count();
                if(orderItem.getQuantity()>count){
                    orderItem.setQuantity(orderItem.getQuantity()-count);
                }
            }
        });
    }

}

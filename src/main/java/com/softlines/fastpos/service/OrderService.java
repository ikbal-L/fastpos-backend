package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.dto.Message;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.mapping.OrderMapper;
import com.softlines.fastpos.repository.CashOperationRepository;
import com.softlines.fastpos.repository.OrderRepository;
import com.softlines.fastpos.security.securityservice.SessionService;
import com.softlines.fastpos.sse.model.EventType;
import org.hibernate.Session;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
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

    public OrderService(SessionService sessionService, NotificationService notificationService, NumerationService numerationService, OrderRepository orderRepository, CashOperationRepository cashOperationRepository, OrderMapper orderMapper) {
        this.sessionService = sessionService;
        this.notificationService = notificationService;
        this.numerationService = numerationService;
        this.orderRepository = orderRepository;
        this.cashOperationRepository = cashOperationRepository;
        this.orderMapper = orderMapper;
        notificationService.registerPublisher(this, "/topic/messages");
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
    public OrderDto saveOrder(Order order) {

        OrderInfo orderInfo;

        orderInfo = setOrderNumberAndCode(order);

        if (order.getState() == OrderState.Payed) {
            var cashOp = CashOperation.builder().amount(order.getNewTotal()).order(order).build();
            order.setCashOperations(Set.of(cashOp));
        }
        
        var created = orderRepository.saveOrder(order);

        SaveOrderInfo(orderInfo);

        var dto  = orderMapper.toOrderDto(created);
        sendCreateOrderMessage(created,dto);
        return dto;
    }

    private void sendCreateOrderMessage(Order createdOder, OrderDto createdOderDto) {
        List<OrderState> states = new ArrayList<>();
        states.add(OrderState.Payed);
        states.add(OrderState.Delivered);
        states.add(OrderState.Credit);
        if (!states.contains(createdOderDto.getState())) {
            var message = Message.builder()
                    .type(EventType.CREATE_ORDER)
                    .content(createdOderDto)
                    .source(createdOder.getModificationSessionId())
                    .build();

            notificationService.publish(this, message);
        }
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



    @Transactional(transactionManager = "transactionManager")
    public OrderDto updateOrder(Order order) {
        //noinspection OptionalGetWithoutIsPresent
        var previousState = orderRepository.findByIdWithCashOperations(order.getId()).get();
        String eventType = EventType.UPDATE_ORDER;

        if (previousState.getState()!= OrderState.PaidModified&& order.getState() == OrderState.PaidModified){
            eventType = EventType.MODIFY_PAID_ORDER;
        }

        if (previousState.getState()== OrderState.PaidModified&& order.getState() == OrderState.Payed){
            eventType = EventType.UNDO_MODIFY_PAID_ORDER;
        }
        OrderInfo orderInfo = null;
        if (order.getOrderNumber() == null){
            orderInfo = setOrderNumberAndCode(order);
        }

        if (OrderService.IsActionCancel(previousState, order)) {
            order = onOrderCanceled(order, previousState);
            eventType = EventType.CANCEL_ORDER;
        }

        order = orderRepository.saveOrder(order);

        if (orderInfo!= null) {
            SaveOrderInfo(orderInfo);
        }

        var dto = orderMapper.toOrderDto(order);
        sendOrderMessage(this, eventType, order.getModificationSessionId(), dto);

        return dto;
    }


    @Transactional(transactionManager = "transactionManager")
    public OrderDto payOrder(Order order) {
        var payedAmount = order.getGivenAmount() + order.getReturnedAmount();
        order.setState(OrderState.Payed);
        order = orderRepository.saveOrder(order);
        var cashOp = CashOperation.builder().order(order).amount(payedAmount).type(CashOperationType.Payment).build();
        cashOperationRepository.saveAndFlush(cashOp);

        var dto = orderMapper.toOrderDto(order);
        sendOrderMessage(this, EventType.PAY_ORDER, order.getModificationSessionId(), dto);
        return dto;
    }
    @Transactional(transactionManager = "transactionManager")
    public OrderDto refundOder(Order order) {
        //noinspection OptionalGetWithoutIsPresent
        var previous  = orderRepository.findById(order.getId()).get();
        var amount = -previous.getNewTotal();
        order.setState(OrderState.Refunded);
        order = orderRepository.saveOrder(order);
        var cashOperation = CashOperation.builder().amount(amount).order(order).type(CashOperationType.Refund).build();
        cashOperationRepository.saveAndFlush(cashOperation);

        var dto = orderMapper.toOrderDto(order);
        sendOrderMessage(this, EventType.PAY_ORDER, order.getModificationSessionId(), dto);
        return dto;
    }

    @Transactional(transactionManager = "transactionManager")
    public OrderDto partiallyRefundOrder(Order order) {
        //noinspection OptionalGetWithoutIsPresent
        var previous  = orderRepository.findById(order.getId()).get();
        var refunded = order.getNewTotal()- order.getPreModifyNewTotal();
        order.setState(OrderState.Payed);
        order = orderRepository.saveOrder(order);
        if (refunded!= 0){
            var cashOperation = CashOperation.builder().amount(refunded).order(order).type(CashOperationType.PartialRefund).build();
            cashOperationRepository.saveAndFlush(cashOperation);
        }

        var dto = orderMapper.toOrderDto(order);
        sendOrderMessage(this, EventType.PAY_ORDER, order.getModificationSessionId(), dto);
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
        query.executeUpdate() ;
    }

    @Transactional(transactionManager = "transactionManager")
    public OrderDto splitOrderFrom(Order subOrder, Order originalOrder){
        var orderInfo = setOrderNumberAndCode(subOrder);
        removeTransferredItemsFromOriginalOrder(subOrder, originalOrder);
        updateOrderItemQuantitiesOfOriginalOrder(subOrder, originalOrder);
        originalOrder.setState(OrderState.Splitted);
        var savedOriginalOrder = orderRepository.saveAll(List.of(originalOrder,subOrder)).get(0);
        SaveOrderInfo(orderInfo);
        return orderMapper.toOrderDto(savedOriginalOrder);
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

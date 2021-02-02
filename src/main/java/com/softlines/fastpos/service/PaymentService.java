package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.CashOperation;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.dto.PaymentDto;
import com.softlines.fastpos.dto.PaymentSavedDto;
import com.softlines.fastpos.dto.mapping.DeliverymanMapper;
import com.softlines.fastpos.dto.mapping.OrderMapper;
import com.softlines.fastpos.dto.mapping.PaymentMapper;
import com.softlines.fastpos.repository.DeliverymanRepository;
import com.softlines.fastpos.repository.OrderRepository;
import com.softlines.fastpos.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentMapper paymentMapper;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    DeliverymanRepository deliverymanRepository;
    @Autowired
    DeliverymanMapper deliverymanMapper;
    @Autowired
    OrderMapper orderMapper;

    @Transactional
    public PaymentSavedDto doPaymentDeliveryMan(PaymentDto paymentDto){
        var payment=paymentMapper.toPayment(paymentDto);
        payment.setCashOperation(CashOperation.builder().amount(payment.getAmount()).payment(payment).build());
        var savedPayment=  paymentRepository.save(payment);
        var orders= orderRepository.getByStates(new OrderState[]{ OrderState.Delivered},savedPayment.getDeliveryMan().getId(),true);
        var deliveryMan= deliverymanRepository.findById(savedPayment.getDeliveryMan().getId()).get();
        var paymentAmount= savedPayment.getAmount()+deliveryMan.getBalance();
        if (orders!=null&&!orders.isEmpty()){
            for (var order:orders) {
                if (paymentAmount==0||order.getTotal()>paymentAmount){
                    break;
                }
               else {
                    order.setState(OrderState.DeliveredPaid);
                    orderRepository.saveOrder(order);
                    paymentAmount=paymentAmount-order.getTotal();
                }
            }
        }
            deliveryMan.setBalance(paymentAmount);
            deliverymanRepository.save(deliveryMan);

        return PaymentSavedDto.builder()
                .deliveryMan(deliverymanMapper.toDeliverymanDto(deliveryMan))
                .paidOrders(orderMapper.toOrderDTOs(orders.stream().filter(x->x.getState()==OrderState.DeliveredPaid).collect(Collectors.toList())))
                .payment(paymentMapper.toPaymentDto(savedPayment))
                .build();
    }
    @Transactional(rollbackOn  = Exception.class)
    public PaymentSavedDto editPaymentDeliveryMan(PaymentDto paymentDto){
        var  oldAmount=paymentRepository.findById(paymentDto.getId()).get().getAmount();
        var payment=paymentMapper.toPayment(paymentDto);
        payment.getCashOperation().setAmount(payment.getAmount());
        payment.getCashOperation().setPayment(payment);
        var savedPayment=  paymentRepository.save(payment);
        var deliveryMan= deliverymanRepository.findById(payment.getDeliveryMan().getId()).get();

       var paymentAmount=(payment.getAmount()+deliveryMan.getBalance())- oldAmount;
       List<Order> orders=null;
       if(paymentAmount>0){
         orders= orderRepository.getByStates(new OrderState[]{ OrderState.Delivered},savedPayment.getDeliveryMan().getId(),true);
        if (orders!=null&&!orders.isEmpty()){
            for (var order:orders) {
                if (paymentAmount==0||order.getTotal()>paymentAmount){
                    break;
                }
                else {
                    order.setState(OrderState.DeliveredPaid);
                    orderRepository.saveOrder(order);
                    paymentAmount=paymentAmount-order.getTotal();
                }
            }
        }
       }else if (paymentAmount<0){
            orders= orderRepository.getByStates(new OrderState[]{ OrderState.DeliveredPaid},savedPayment.getDeliveryMan().getId(),false);
           if (orders!=null&&!orders.isEmpty()){
               for (var order:orders) {
                   if (paymentAmount>=0){
                       break;
                   }
                   else {
                       order.setState(OrderState.Delivered);
                       orderRepository.saveOrder(order);
                       paymentAmount=paymentAmount+order.getTotal();
                   }
               }
           }
       }

        deliveryMan.setBalance(paymentAmount);
        deliverymanRepository.save(deliveryMan);
       var resBuilder= PaymentSavedDto.builder()
               .payment(paymentMapper.toPaymentDto(savedPayment))
               .deliveryMan(deliverymanMapper.toDeliverymanDto(deliveryMan));
       if(orders!=null) {
           var paidOrders = orders.stream().filter(x -> x.getState() == OrderState.DeliveredPaid);
           if (paidOrders != null) {
               resBuilder.paidOrders(orderMapper.toOrderDTOs(paidOrders.collect(Collectors.toList())));
           }
           var notPaidOrders = orders.stream().filter(x -> x.getState() == OrderState.Delivered);
           if (notPaidOrders != null) {
               resBuilder.notPaidOrders(orderMapper.toOrderDTOs(notPaidOrders.collect(Collectors.toList())));
           }
       }
       return resBuilder.build();

    }
    public PaymentSavedDto deletePayment(long Id){
        var payment=paymentRepository.findById(Id).get();
        if(payment!=null) {
            paymentRepository.deleteById(Id);
            var deliveryMan = deliverymanRepository.findById(payment.getDeliveryMan().getId()).get();
            var paymentAmount = payment.getAmount() - deliveryMan.getBalance();
            var orders = orderRepository.getByStates(new OrderState[]{OrderState.DeliveredPaid}, payment.getDeliveryMan().getId(), false);
            if (orders != null && !orders.isEmpty()) {
                for (var order : orders) {
                    if (paymentAmount <= 0) {
                        break;
                    } else {
                        order.setState(OrderState.Delivered);
                        orderRepository.saveOrder(order);
                        paymentAmount = paymentAmount - order.getTotal();
                    }
                }
            }


            deliveryMan.setBalance((-paymentAmount));
            deliverymanRepository.save(deliveryMan);

        return PaymentSavedDto.builder()
                    .deliveryMan(deliverymanMapper.toDeliverymanDto(deliveryMan))
                    .notPaidOrders(orderMapper.toOrderDTOs(orders.stream().filter(x->x.getState()==OrderState.Delivered).collect(Collectors.toList())))
                    .build();
        }
        return null;
    }
}

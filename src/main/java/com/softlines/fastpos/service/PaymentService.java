package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.CashOperation;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.dto.PaymentDto;
import com.softlines.fastpos.dto.mapping.DeliverymanMapper;
import com.softlines.fastpos.dto.mapping.OrderMapper;
import com.softlines.fastpos.dto.mapping.PaymentMapper;
import com.softlines.fastpos.repository.DeliverymanRepository;
import com.softlines.fastpos.repository.OrderRepository;
import com.softlines.fastpos.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(transactionManager = "transactionManager")
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
    public PaymentDto doPaymentDeliveryMan(PaymentDto paymentDto){

        List<Long> paidDeliveryOrdersIds = new ArrayList<>();
        var payment=paymentMapper.toPayment(paymentDto);
        payment.setCashOperation(CashOperation.builder().amount(payment.getAmount()).payment(payment).build());
        var savedPayment=  paymentRepository.save(payment);
        var orders= orderRepository.getByStates(new OrderState[]{ OrderState.Delivered},savedPayment.getDeliveryman().getId(),true);
        var deliveryMan= deliverymanRepository.findById(savedPayment.getDeliveryman().getId()).get();
        var paymentAmount= savedPayment.getAmount()+deliveryMan.getBalance();
        if (!orders.isEmpty()){
            for (var order:orders) {
                if (paymentAmount==0||order.getTotal()>paymentAmount){
                    break;
                }
               else {
                    order.setState(OrderState.DeliveredPaid);
                    orderRepository.save(order);
                    paidDeliveryOrdersIds.add(order.getId());
                    paymentAmount=paymentAmount-order.getTotal();
                }
            }
        }
            deliveryMan.setBalance(paymentAmount);
            deliverymanRepository.save(deliveryMan);
        var savedPaymentDTO = paymentMapper.toPaymentDto(savedPayment);
        savedPaymentDTO.setOrderIds(paidDeliveryOrdersIds);
        return savedPaymentDTO;
    }
    public void editPaymentDeliveryMan(PaymentDto paymentDto){
        var  oldAmount=paymentRepository.findById(paymentDto.getId()).get().getAmount();
        var payment=paymentMapper.toPayment(paymentDto);
        payment.getCashOperation().setAmount(payment.getAmount());
        payment.getCashOperation().setPayment(payment);
        var savedPayment=  paymentRepository.save(payment);
        var deliveryMan= deliverymanRepository.findById(payment.getDeliveryman().getId()).get();

       var paymentAmount=payment.getAmount()+deliveryMan.getBalance()- oldAmount;
       if(paymentAmount>0){
     var orders= orderRepository.getByStates(new OrderState[]{ OrderState.Delivered},savedPayment.getDeliveryman().getId(),true);
        if (orders!=null&&!orders.isEmpty()){
            for (var order:orders) {
                if (paymentAmount==0||order.getTotal()>paymentAmount){
                    break;
                }
                else {
                    order.setState(OrderState.DeliveredPaid);
                    orderRepository.save(order);
                    paymentAmount=paymentAmount-order.getTotal();
                }
            }
        }
       }else if (paymentAmount<0){
           while (true)
           {
               var order = orderRepository.findFirstByStateAndDeliveryman(OrderState.DeliveredPaid,payment.getDeliveryman(),Sort.by("orderTime").ascending());
               if (paymentAmount>=0||order==null){
                       break;
                   }
                   else {
                       order.setState(OrderState.Delivered);
                       orderRepository.save(order);
                       paymentAmount=paymentAmount+order.getTotal();
                   }
               }
           }


        deliveryMan.setBalance(paymentAmount);
        deliverymanRepository.save(deliveryMan);


    }
    public void deletePayment(long Id){
        var payment=paymentRepository.findById(Id).get();
        if(payment!=null) {
            paymentRepository.deleteById(Id);
            var deliveryMan = deliverymanRepository.findById(payment.getDeliveryman().getId()).get();
            var paymentAmount = payment.getAmount() - deliveryMan.getBalance();
            Order order =null;
            while (true)
            {
                order = orderRepository.findFirstByStateAndDeliveryman(OrderState.DeliveredPaid,payment.getDeliveryman(),Sort.by("orderTime").ascending());
                    if (paymentAmount <= 0||order==null) {
                        break;
                    } else {
                        order.setState(OrderState.Delivered);
                        orderRepository.save(order);
                        paymentAmount = paymentAmount - order.getTotal();
                    }
            }


            deliveryMan.setBalance((-paymentAmount));
            deliverymanRepository.save(deliveryMan);


        }
    }
}

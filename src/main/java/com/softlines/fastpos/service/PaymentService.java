package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.PaymentDto;
import com.softlines.fastpos.dto.mapping.CustomerMapper;
import com.softlines.fastpos.dto.mapping.DeliverymanMapper;
import com.softlines.fastpos.dto.mapping.OrderMapper;
import com.softlines.fastpos.dto.mapping.PaymentMapper;
import com.softlines.fastpos.repository.CustomerRepository;
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
    CustomerRepository customerRepository;
    @Autowired
    DeliverymanMapper deliverymanMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    OrderMapper orderMapper;
    public PaymentDto processPayment(PaymentDto paymentDto){

        Deliveryman deliveryman = null;
        Customer customer = null;
        List<Order> orders = null;
        List<OrderDto> paymentOrders = new ArrayList<>();

        var payment=paymentMapper.toPayment(paymentDto);
        payment.setCashOperation(CashOperation.builder().amount(payment.getAmount()).payment(payment).build());
        var savedPayment=  paymentRepository.save(payment);

        if (paymentDto.getPaymentSource() == PaymentSource.Delivery){
            orders= orderRepository.getByStates(new OrderState[]{ OrderState.Delivered,OrderState.DeliveredPartiallyPaid},savedPayment.getDeliveryman().getId(), "deliveryman", null, true);
            deliveryman= deliverymanRepository.findById(savedPayment.getDeliveryman().getId()).get();
        }
        if (paymentDto.getPaymentSource() == PaymentSource.Customer){
            orders= orderRepository.getByStates(new OrderState[]{ OrderState.Credit,OrderState.CreditPartiallyRePaid},savedPayment.getCustomer().getId(), "customer", null, true);
            customer = customerRepository.findById(savedPayment.getCustomer().getId()).get();
        }


        double paymentAmount;
        if (savedPayment.getDiscountAmount()!= null){
            paymentAmount = savedPayment.getAmount()+ savedPayment.getDiscountAmount();
        }else {
            paymentAmount = savedPayment.getAmount();
        }
        assert orders != null;
        if (!orders.isEmpty()){
            for (var  order:orders) {

                if (paymentAmount==0){
                    break;
                }

                if (order.getState() == OrderState.DeliveredPartiallyPaid||order.getState() == OrderState.CreditPartiallyRePaid ){
                    var remaining = order.getNewTotal() - order.getGivenAmount();
                    if (paymentAmount>=remaining){
                        order.setGivenAmount(order.getNewTotal());
                        if (paymentDto.getPaymentSource() == PaymentSource.Delivery){
                            order.setState(OrderState.DeliveredPaid);
                        }
                        if (paymentDto.getPaymentSource() == PaymentSource.Customer){
                            order.setState(OrderState.CreditRePaid);
                        }

                        orderRepository.save(order);
                        paymentAmount-=remaining;
                    }else {
                        order.setGivenAmount( order.getGivenAmount()+paymentAmount);


                        if (paymentDto.getPaymentSource() == PaymentSource.Delivery){
                            order.setState(OrderState.DeliveredPartiallyPaid);
                        }
                        if (paymentDto.getPaymentSource() == PaymentSource.Customer){
                            order.setState(OrderState.CreditPartiallyRePaid);
                        }
                        orderRepository.save(order);
                        paymentAmount = 0D;
                    }

                }

                if (order.getState() == OrderState.Delivered|| order.getState() == OrderState.Credit){



                    if (order.getTotal()>paymentAmount){

                        if (paymentDto.getPaymentSource() == PaymentSource.Delivery){
                            order.setState(OrderState.DeliveredPartiallyPaid);
                        }
                        if (paymentDto.getPaymentSource() == PaymentSource.Customer){
                            order.setState(OrderState.CreditPartiallyRePaid);
                        }
                        order.setGivenAmount(paymentAmount);
                        orderRepository.save(order);

                        paymentAmount = 0D;
                    }
                    else {

                        if (paymentDto.getPaymentSource() == PaymentSource.Delivery){
                            order.setState(OrderState.DeliveredPaid);
                        }
                        if (paymentDto.getPaymentSource() == PaymentSource.Customer){
                            order.setState(OrderState.CreditRePaid);
                        }

                        orderRepository.save(order);

                        paymentAmount=paymentAmount-order.getTotal();
                    }

                }
                var orderDto = orderMapper.toOrderDto(order);
                paymentOrders.add(orderDto);

            }
        }


        if (paymentDto.getPaymentSource() == PaymentSource.Delivery) {
            assert deliveryman != null;
            deliverymanRepository.save(deliveryman);
        }

        if (paymentDto.getPaymentSource() == PaymentSource.Customer){
            assert customer != null;
            customerRepository.save(customer);
        }
        var savedPaymentDTO = paymentMapper.toPaymentDto(savedPayment);
        savedPaymentDTO.setOrders(paymentOrders);
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
     var orders= orderRepository.getByStates(new OrderState[]{ OrderState.Delivered},savedPayment.getDeliveryman().getId(), "deliveryman", null, true);
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

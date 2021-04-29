package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.dto.DailyExpenseReportInputDataDto;
import com.softlines.fastpos.repository.OrderRepository;
import com.softlines.fastpos.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(transactionManager = "transactionManager")
public class DailyExpenseReportService {

    OrderRepository orderRepository;

    PaymentRepository paymentRepository;

    public DailyExpenseReportService(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    public DailyExpenseReport generateDailyExpenseReport(DailyExpenseReportInputDataDto inputData) {


        var date = new Date();
        var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        var dateString = simpleDateFormat.format(date);
        var ordersOfTheDay = orderRepository.findAllByOrderTime(dateString).stream().filter(order -> order.getState() == OrderState.Payed|| order.getState() == OrderState.DeliveredPaid).collect(Collectors.toList());
        var payedOrdersOfTheDay = orderRepository.findAllByOrderTime(dateString).stream().filter(order -> order.getState() == OrderState.Payed).collect(Collectors.toList());
        var paymentsOfTheDay = paymentRepository.findAllByDate(dateString);

        Map<String, Double> cashPayments = new HashMap<>();
        Map<String,Double> deliveryPayments = new HashMap<>();
        payedOrdersOfTheDay.stream().forEach(order -> cashPayments.put(order.getId()+"" , order.getNewTotal()));
        paymentsOfTheDay.stream().forEach(payment -> deliveryPayments.put(payment.getId()+"",payment.getAmount()));
        var cashPaymentsSum = payedOrdersOfTheDay.stream().mapToDouble(Order::getGivenAmount).sum();
        var deliveryPaymentsSum = paymentsOfTheDay.stream().mapToDouble(Payment::getAmount).sum();
        var expensesSum = inputData.getExpenses().values().stream().mapToDouble(Double::doubleValue).sum();
        var cashRegisterDepositedAmount = cashPaymentsSum + deliveryPaymentsSum;
        var cashRegisterWithDrawnAmount = payedOrdersOfTheDay.stream().mapToDouble(Order::getReturnedAmount).sum();
        var cashRegisterExpectedAmount = inputData.
                getCashRegisterInitialAmount()
                + cashRegisterDepositedAmount
                +deliveryPaymentsSum
                + cashRegisterWithDrawnAmount //negative value
                - expensesSum;
        var groupbycat = ordersOfTheDay
                .stream()
                .map(Order::getOrderItems)
                .flatMap(Collection::stream).collect(Collectors.groupingBy(orderItem -> orderItem.getProduct().getCategory()));
        List<EarningsCategoryGrouping> items =groupbycat.entrySet().stream().map(categoryListEntry -> {
           return EarningsCategoryGrouping.builder().category(categoryListEntry.getKey().getName())
                    .quantityOfItems(categoryListEntry.getValue().stream().mapToInt(OrderItem::getQuantity).sum()).amount(categoryListEntry.getValue().stream().mapToDouble(OrderItem::getTotal).sum()).build();
        }).collect(Collectors.toList());

        var report = DailyExpenseReport.builder()
                .issuedDate(new Date())
                .CashPayments(cashPayments)
                .deliveryPayments(deliveryPayments)
                .expenses(inputData.getExpenses())
                .cashRegisterInitialAmount(inputData.getCashRegisterInitialAmount())
                .cashRegisterDepositedAmount(cashRegisterDepositedAmount)
                .cashRegisterWithdrawnAmount(cashRegisterWithDrawnAmount)
                .cashRegisterExpectedAmount(cashRegisterExpectedAmount)
                .cashRegisterActualAmount(inputData.getCashRegisterActualAmount())
                .earningsByCategory(items)
                .build();
        return report;
    }
}

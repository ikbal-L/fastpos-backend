package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.DailyExpenseReport;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.dto.DailyExpenseReportInputDataDto;
import com.softlines.fastpos.repository.OrderRepository;
import com.softlines.fastpos.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Service
@Transactional(transactionManager = "transactionManager")
public class DailyExpenseReportService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PaymentRepository paymentRepository;

    public DailyExpenseReport generateDailyExpenseReport(DailyExpenseReportInputDataDto inputData) {


        var date = new Date();
        var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        var dateString = simpleDateFormat.format(date);
        var ordersOfTheDay = orderRepository.findAllByOrderTime(dateString).stream().filter(order -> order.getState() == OrderState.Payed).collect(Collectors.toList());
        var paymentsOfTheDay = paymentRepository.findAllByDate(dateString);

        Map<String, Double> cashPayments = new HashMap<>();
        Set<Double> deliveryPayments = new HashSet<>();
        ordersOfTheDay.stream().forEach(order -> cashPayments.put(order.getId() + "", order.getNewTotal()));
        paymentsOfTheDay.stream().forEach(payment -> deliveryPayments.add(payment.getAmount()));
        var cashPaymentsSum = ordersOfTheDay.stream().mapToDouble(value -> value.getGivenAmount()).sum();
        var deliveryPaymentsSum = paymentsOfTheDay.stream().mapToDouble(value -> value.getAmount()).sum();
        var expensesSum = inputData.getExpenses().stream().mapToDouble(Double::doubleValue).sum();
        var cashRegisterDepositedAmount = cashPaymentsSum + deliveryPaymentsSum;
        var cashRegisterWithDrawnAmount = ordersOfTheDay.stream().mapToDouble(value -> value.getReturnedAmount()).sum();
        var cashRegisterExpectedAmount = inputData.
                getCashRegisterInitialAmount()
                + cashRegisterDepositedAmount
                + cashRegisterWithDrawnAmount //negative value
                - expensesSum;

        var report = DailyExpenseReport.builder()
                .issuedDate(new Date())
                .CashPayments(cashPayments)
                .deliveryPayments(deliveryPayments)
                .expenses(inputData.getExpenses())
                .cashRegisterInitialAmount(inputData.getCashRegisterInitialAmount())
                .cashRegisterDepositedAmount(cashRegisterDepositedAmount)
                .cashRegisterWithdrawnAmount(cashRegisterWithDrawnAmount)
                .cashRegisterExpectedAmount(cashRegisterExpectedAmount)
                .cashRegisterActualAmount(inputData.getCashRegisterActualAmount()).build();
        return report;
    }
}

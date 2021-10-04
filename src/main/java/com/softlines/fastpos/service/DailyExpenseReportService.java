package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.dto.DailyExpenseReportInputDataDto;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Session;
import com.softlines.fastpos.jwtsecurity.securityrepository.SessionRepository;
import com.softlines.fastpos.repository.CashRegisterExpenseRepository;
import com.softlines.fastpos.repository.OrderRepository;
import com.softlines.fastpos.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(transactionManager = "transactionManager")
public class DailyExpenseReportService {

    OrderRepository orderRepository;

    PaymentRepository paymentRepository;

    SessionRepository sessionRepository;

    CashRegisterExpenseRepository cashRegisterExpenseRepository;

    public DailyExpenseReportService(
            OrderRepository orderRepository,
            PaymentRepository paymentRepository,
            SessionRepository sessionRepository,
            CashRegisterExpenseRepository cashRegisterExpenseRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.sessionRepository = sessionRepository;
        this.cashRegisterExpenseRepository = cashRegisterExpenseRepository;
    }

    public DailyExpenseReportInputDataDto getInputData(DailyExpenseReport report) {

        return DailyExpenseReportInputDataDto.builder()
                .expenses(report.getExpenses())
                .cashRegisterInitialAmount(report.getCashRegisterInitialAmount())
                .cashRegisterActualAmount(report.getCashRegisterActualAmount()).build();
    }

    public DailyExpenseReport generateDailyExpenseReport(DailyExpenseReportInputDataDto inputData,boolean update) throws ParseException {


        var date = new Date();
        var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        var dateString = simpleDateFormat.format(date);
        var ordersOfTheDay = orderRepository.findAllByOrderTime(dateString).stream().filter(order -> order.getState() == OrderState.Payed || order.getState() == OrderState.DeliveredPaid).collect(Collectors.toList());
        var payedOrdersOfTheDay = orderRepository.findAllByOrderTime(dateString).stream().filter(order -> order.getState() == OrderState.Payed).collect(Collectors.toList());
        var refundedOrdersOfTheDay = orderRepository.findAllByOrderTime(dateString).stream().filter(order -> order.getState() == OrderState.Refunded).collect(Collectors.toList());
        var paymentList = paymentRepository.findAllByDate(dateString);
        HashSet<Payment> paymentsOfTheDay = new HashSet(paymentList) ;


//        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        var cashRegisterExpenses = cashRegisterExpenseRepository.findAllByIssuedDate(dateString);

        Map<String, Double> cashPayments = new HashMap<>();

//        Map<String, Double> deliveryPayments = new HashMap<>();

        payedOrdersOfTheDay.stream().forEach(order -> cashPayments.put(order.getId() + "", order.getNewTotal()));

//        paymentsOfTheDay.stream().forEach(payment -> deliveryPayments.put(payment.getId() + "", payment.getAmount()));

        var cashPaymentsSum = payedOrdersOfTheDay.stream().mapToDouble(Order::getGivenAmount).sum();
        var deliveryPaymentsSum = paymentsOfTheDay.stream().mapToDouble(Payment::getAmount).sum();
        var expensesSum = cashRegisterExpenses.stream().mapToDouble(CashRegisterExpense::getAmount).sum();

        var cashRegisterDepositedAmount = cashPaymentsSum + deliveryPaymentsSum;

        var cashRegisterWithDrawnAmount = payedOrdersOfTheDay.stream().mapToDouble(Order::getReturnedAmount).sum();

        var cashRegisterExpectedAmount = inputData.
                getCashRegisterInitialAmount()
                + cashRegisterDepositedAmount
                + deliveryPaymentsSum
                + cashRegisterWithDrawnAmount //negative value
                - expensesSum;

        var items = Set.copyOf(getGroupingByCategory(ordersOfTheDay));

        var refunds = Set.copyOf(getOrderRefunds(refundedOrdersOfTheDay));


        var report = DailyExpenseReport.builder()
                .issuedDate(new Date())
                .CashPayments(cashPayments)
                .deliveryPayments(paymentsOfTheDay)
                .expenses(inputData.getExpenses())
                .cashRegisterInitialAmount(inputData.getCashRegisterInitialAmount())
                .cashRegisterDepositedAmount(cashRegisterDepositedAmount)
                .cashRegisterWithdrawnAmount(cashRegisterWithDrawnAmount)
                .cashRegisterExpectedAmount(cashRegisterExpectedAmount)
                .cashRegisterActualAmount(inputData.getCashRegisterActualAmount())
                .earningsByCategory(items)
                .refunds(refunds)
                .cashRegisterExpenses(Set.copyOf(cashRegisterExpenses))
                .build();
        if (!update){
            cashRegisterExpenses.forEach(expense -> expense.setReport(report));
        }
        return report;
    }

    public DailyExpenseReport updateDailyExpenseReport(DailyExpenseReport report, DailyExpenseReportInputDataDto inputData) throws ParseException {
        var generated = generateDailyExpenseReport(inputData,true);
        report.setCashPayments(generated.getCashPayments());
        report.setDeliveryPayments(generated.getDeliveryPayments());
        report.setExpenses(generated.getExpenses());
        report.setCashRegisterInitialAmount(generated.getCashRegisterInitialAmount());
        report.setCashRegisterDepositedAmount(generated.getCashRegisterDepositedAmount());
        report.setCashRegisterWithdrawnAmount(generated.getCashRegisterWithdrawnAmount());
        report.setCashRegisterExpectedAmount(generated.getCashRegisterExpectedAmount());
        report.setCashRegisterActualAmount(generated.getCashRegisterActualAmount());
        report.setEarningsByCategory(generated.getEarningsByCategory());
        report.setRefunds(generated.getRefunds());
        report.setCashRegisterExpenses(generated.getCashRegisterExpenses());
        report.getCashRegisterExpenses().forEach(expense -> expense.setReport(report));
        return report;
    }

    private Optional<JWTuser> getUserFromSession(String sessionUUID) {
        var session = sessionRepository.findById(UUID.fromString(sessionUUID));
        return session.map(Session::getUser);
    }

    private String getUserFullNameFromSession(String sessionUUID) {
        var user = getUserFromSession(sessionUUID);
        if (user.isPresent()) return String.format("%s %s", user.get().getFirstName(), user.get().getLastName());
        return "";
    }

    private List<OrderRefund> getOrderRefunds(List<Order> refundedOrders) {
        return refundedOrders.stream().map(order ->
                OrderRefund.builder()
                        .orderNumber(order.getOrderNumber())
                        .amount(order.getNewTotal())
                        .issuedBy(getUserFullNameFromSession(order.getModificationSessionId())).build()).collect(Collectors.toList());
    }

    public List<EarningsCategoryGrouping>  getGroupingByCategory(List<Order> orders) {
        return orders
                .stream()
                .map(Order::getOrderItems)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(orderItem -> orderItem.getProduct().getCategory()))
                .entrySet().stream().map(categoryListEntry -> EarningsCategoryGrouping.builder().category(categoryListEntry.getKey().getName())
                .quantityOfItems(categoryListEntry.getValue().stream().mapToInt(OrderItem::getQuantity).sum()).amount(categoryListEntry.getValue().stream().mapToDouble(OrderItem::getTotal).sum()).build()).collect(Collectors.toList());

    }
}

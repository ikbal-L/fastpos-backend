package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Session;
import com.softlines.fastpos.jwtsecurity.securityrepository.SessionRepository;
import com.softlines.fastpos.repository.CashRegisterExpenseRepository;
import com.softlines.fastpos.repository.DailyExpenseReportRepository;
import com.softlines.fastpos.repository.OrderRepository;
import com.softlines.fastpos.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(transactionManager = "transactionManager")
public class DailyExpenseReportService {

    OrderRepository orderRepository;

    PaymentRepository paymentRepository;

    DailyExpenseReportRepository dailyExpenseReportRepository;

    SessionRepository sessionRepository;

    CashRegisterExpenseRepository cashRegisterExpenseRepository;
    private List<Order> allOrdersOfTheDay;
    private LinkedHashSet<Order> payedOrdersOfTheDay;
    private LinkedHashSet<Order> canceledOrdersOfTheDay;
    private LinkedHashSet<Order> refundedOrdersOfTheDay;
    private HashSet<Payment> paymentsOfTheDay;
    private List<CashRegisterExpense> cashRegisterExpenses;

    public DailyExpenseReportService(OrderRepository orderRepository, PaymentRepository paymentRepository, DailyExpenseReportRepository dailyExpenseReportRepository, SessionRepository sessionRepository, CashRegisterExpenseRepository cashRegisterExpenseRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.dailyExpenseReportRepository = dailyExpenseReportRepository;
        this.sessionRepository = sessionRepository;
        this.cashRegisterExpenseRepository = cashRegisterExpenseRepository;
    }


    public DailyEarningsReport generateDailyExpenseReport(boolean update, LocalDate issued) {

        LocalDate date;
        date = Objects.requireNonNullElseGet(issued, LocalDate::now);

        allOrdersOfTheDay = orderRepository.findAllByOrderTime(date);

        var ordersOfTheDay = allOrdersOfTheDay.stream().filter(order -> order.getState() == OrderState.Payed || order.getState() == OrderState.DeliveredPaid).collect(Collectors.toList());


        setPayedOrdersOfTheDay();
        setCanceledOrdersOfTheDay();
        setRefundedOrdersOfTheDay();
        setPaymentsOfTheDay(date);
        setCashRegisterExpenses(date);

        var cashPaymentsSum = payedOrdersOfTheDay.stream().mapToDouble(Order::getGivenAmount).sum();
        var deliveryPaymentsSum = paymentsOfTheDay.stream().mapToDouble(Payment::getAmount).sum();
        var expensesSum = cashRegisterExpenses.stream().mapToDouble(CashRegisterExpense::getAmount).sum();

        var cashRegisterDepositedAmount = cashPaymentsSum + deliveryPaymentsSum;

        var cashRegisterWithDrawnAmount = payedOrdersOfTheDay.stream().mapToDouble(Order::getReturnedAmount).sum();

        var cashRegisterExpectedAmount =
                +cashRegisterDepositedAmount
                        + deliveryPaymentsSum
                        + cashRegisterWithDrawnAmount //negative value
                        - expensesSum;

        var items = Set.copyOf(getGroupingByCategory(ordersOfTheDay));

        var refunds = getOrderRefunds(refundedOrdersOfTheDay);


        var report = DailyEarningsReport.builder()
                .issuedDate(date.atStartOfDay())
                .cashPayments(payedOrdersOfTheDay)
                .canceledOrders(canceledOrdersOfTheDay)
                .refunds(refunds)
                .payments(paymentsOfTheDay)
                .cashRegisterInitialAmount(0)
                .cashRegisterDepositedAmount(cashRegisterDepositedAmount)
                .cashRegisterWithdrawnAmount(cashRegisterWithDrawnAmount)
                .cashRegisterExpectedAmount(cashRegisterExpectedAmount)
                .cashRegisterActualAmount(0)
                .earningsByCategory(items)
                .cashRegisterExpenses(Set.copyOf(cashRegisterExpenses))
                .build();

        if (!update) {

            report.getCashPayments().forEach(order -> order.setDailyEarningsReport(report));
            report.getCashRegisterExpenses().forEach(expense -> expense.setReport(report));
            report.getPayments().forEach(payment -> payment.setDailyEarningsReport(report));
            return dailyExpenseReportRepository.save(report);
        }
        return report;
    }

    private void setCashRegisterExpenses(LocalDate date) {
        cashRegisterExpenses = cashRegisterExpenseRepository.findAllByIssuedDate(date);
    }

    private void setPayedOrdersOfTheDay() {
        payedOrdersOfTheDay = allOrdersOfTheDay.stream().filter(order -> order.getState() == OrderState.Payed).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void setCanceledOrdersOfTheDay() {
        canceledOrdersOfTheDay = allOrdersOfTheDay.stream().filter(order -> order.getState() == OrderState.Canceled).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void setRefundedOrdersOfTheDay() {
        refundedOrdersOfTheDay = allOrdersOfTheDay.stream().filter(order -> order.getState() == OrderState.Refunded).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void setPaymentsOfTheDay(LocalDate date) {
        var paymentList = paymentRepository.findAllByDate(date);
        paymentsOfTheDay = new HashSet<>(paymentList);
    }

    public DailyEarningsReport updateDailyExpenseReport(DailyEarningsReport report) {

        var generated = generateDailyExpenseReport(true, report.getIssuedDate().toLocalDate());
        report.setCashPayments(generated.getCashPayments());
        report.setPayments(generated.getPayments());
        report.setCanceledOrders(generated.getCanceledOrders());
        report.setCashRegisterInitialAmount(generated.getCashRegisterInitialAmount());
        report.setCashRegisterDepositedAmount(generated.getCashRegisterDepositedAmount());
        report.setCashRegisterWithdrawnAmount(generated.getCashRegisterWithdrawnAmount());
        report.setCashRegisterExpectedAmount(generated.getCashRegisterExpectedAmount());
        report.setCashRegisterActualAmount(generated.getCashRegisterActualAmount());
        report.setEarningsByCategory(generated.getEarningsByCategory());
        report.setRefunds(generated.getRefunds());
        report.getCanceledOrders().forEach(c->c.setDailyEarningsReport(report));
        report.setCashRegisterExpenses(generated.getCashRegisterExpenses());
        report.getCashPayments().forEach(orderReportInfo -> orderReportInfo.setDailyEarningsReport(report));
        report.getCashRegisterExpenses().forEach(expense -> expense.setReport(report));
        report.getPayments().forEach(payment -> payment.setDailyEarningsReport(report));

        return dailyExpenseReportRepository.save(report);
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

    private LinkedHashSet<OrderRefund> getOrderRefunds(LinkedHashSet<Order> refundedOrders) {
        return refundedOrders.stream().map(order ->
                OrderRefund.builder()
                        .orderNumber(order.getOrderNumber())
                        .amount(order.getNewTotal())
                        .issuedBy(getUserFullNameFromSession(order.getModificationSessionId())).build()).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public List<EarningsCategoryGrouping> getGroupingByCategory(List<Order> orders) {
        return orders
                .stream()
                .map(Order::getOrderItems)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(orderItem -> orderItem.getProduct().getCategory()))
                .entrySet().stream().map(categoryListEntry -> EarningsCategoryGrouping.builder().category(categoryListEntry.getKey().getName())
                        .quantityOfItems(categoryListEntry.getValue().stream().mapToInt(OrderItem::getQuantity).sum()).amount(categoryListEntry.getValue().stream().mapToDouble(OrderItem::getTotal).sum()).build()).collect(Collectors.toList());

    }

    public boolean isReportUpToDate(DailyEarningsReport report, LocalDate date) {


        allOrdersOfTheDay = orderRepository.findAllByOrderTime(date);
        setPayedOrdersOfTheDay();
        setCanceledOrdersOfTheDay();
        setRefundedOrdersOfTheDay();
        setPaymentsOfTheDay(date);
        setCashRegisterExpenses(date);

        var cashPaymentsCount = report.getCashPayments().size();
        var currentPayedOrdersCount = payedOrdersOfTheDay.size();

        var canceledOrderCount = report.getCanceledOrders().size();
        var currentCanceledOrderCount = canceledOrdersOfTheDay.size();

        var refundCount = report.getRefunds().size();
        var currentRefundedOrdersCount = refundedOrdersOfTheDay.size();

        var paymentCount = report.getPayments().size();
        var currentPaymentCount = paymentsOfTheDay.size();

        var cashRegisterExpensesCount = report.getCashRegisterExpenses().size();
        var currentCashRegisterExpenses = cashRegisterExpenses.size();

        var isCashPaymentsCountUpToDate = cashPaymentsCount == currentPayedOrdersCount;
        var isCanceledOrderCountUpToDate = canceledOrderCount == currentCanceledOrderCount;
        var isRefundCountUpToDate = refundCount == currentRefundedOrdersCount;
        var isPaymentCountUpToDate = paymentCount == currentPaymentCount;
        var isCashRegisterExpensesCountUpToDate = cashRegisterExpensesCount == currentCashRegisterExpenses;

        var isReportUpToDate =
                        isCashPaymentsCountUpToDate &&
                        isCanceledOrderCountUpToDate &&
                        isRefundCountUpToDate &&
                        isPaymentCountUpToDate &&
                        isCashRegisterExpensesCountUpToDate;

        return isReportUpToDate;

    }

    public void updateReportsInRange() {

        var result = dailyExpenseReportRepository.findFirstByOrderByIssuedDateDesc();
        if (result.isEmpty()) return;
        var latestReport = result.get();
        var latestDate = latestReport.getIssuedDate().toLocalDate();
        if (isEveryReportGenerated(latestDate)) return;

        var from = latestDate.plusDays(1);
        var to = LocalDate.now();
        while (from.isBefore(to)) {
            generateDailyExpenseReport(false, from);
            from = from.plusDays(1);
        }

    }

    public boolean isEveryReportGenerated(LocalDate latestDate) {
        return latestDate.isEqual(LocalDate.now()) || latestDate.isEqual(LocalDate.now().minusDays(1));
    }
}

package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.DailyEarningsReport;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.Payment;
import com.softlines.fastpos.domain.PaymentSource;
import com.softlines.fastpos.dto.DailyEarningsReportDto;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.PaymentDto;
import org.mapstruct.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {PaymentMapper.class,OrderMapper.class})
public interface DailyExpenseReportMapper {
    @Mapping(source = "payments",target = "deliveryPayments",qualifiedByName = "ToDeliveryPaymentDto",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "payments",target = "creditRePayments",qualifiedByName = "ToCreditRePaymentDto",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "cashPayments",target = "cashPayments",qualifiedByName = "ToCashPaymentDTOs",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    DailyEarningsReportDto toDailyExpenseReportDto(DailyEarningsReport dailyEarningsReport);

    List<DailyEarningsReportDto> toDailyExpenseReportDtos(List<DailyEarningsReport> dailyEarningsReports);

    @Named("ToDeliveryPaymentDto")
    public static Set<PaymentDto> toDeliveryPaymentDTOs(Set<Payment> payments){
        var deliveryPayments = payments.stream().filter(payment -> payment.getPaymentSource() == PaymentSource.Delivery).map(PaymentMapper.INSTANCE::toPaymentDto).collect(Collectors.toCollection(LinkedHashSet::new));
        return deliveryPayments;
    }

    @Named("ToCreditRePaymentDto")
    public static Set<PaymentDto> toCreditRePaymentDTOs(Set<Payment> payments){
        var creditRePayments = payments.stream().filter(payment -> payment.getPaymentSource() == PaymentSource.Customer).map(PaymentMapper.INSTANCE::toPaymentDto).collect(Collectors.toCollection(LinkedHashSet::new));
        return creditRePayments;
    }
    @Named("ToCashPaymentDTOs")
    public static Set<OrderDto> toCashPayments(Set<Order> orders){
        Set<OrderDto> dtos = orders.stream().map(OrderMapper.INSTANCE::DtoFromLazyOrder).collect(Collectors.toCollection(LinkedHashSet::new));
        return  dtos;
    }
}

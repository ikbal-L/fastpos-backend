package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.DailyExpenseReport;
import com.softlines.fastpos.domain.Payment;
import com.softlines.fastpos.domain.PaymentSource;
import com.softlines.fastpos.dto.DailyExpenseReportDto;
import com.softlines.fastpos.dto.PaymentDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {PaymentMapper.class})
public interface DailyExpenseReportMapper {
    @Mapping(source = "payments",target = "deliveryPayments",qualifiedByName = "ToDeliveryPaymentDto",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "payments",target = "creditRePayments",qualifiedByName = "ToCreditRePaymentDto",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    DailyExpenseReportDto toDailyExpenseReportDto(DailyExpenseReport dailyExpenseReport);
    List<DailyExpenseReportDto> toDailyExpenseReportDtos(List<DailyExpenseReport> dailyExpenseReports);

    @Named("ToDeliveryPaymentDto")
    public static Set<PaymentDto> toDeliveryPaymentDTOs(Set<Payment> payments){
        var deliveryPayments = payments.stream().filter(payment -> payment.getPaymentSource() == PaymentSource.Delivery).map(PaymentMapper.INSTANCE::toPaymentDto).collect(Collectors.toSet());
        return deliveryPayments;
    }

    @Named("ToCreditRePaymentDto")
    public static Set<PaymentDto> toCreditRePaymentDTOs(Set<Payment> payments){
        var creditRePayments = payments.stream().filter(payment -> payment.getPaymentSource() == PaymentSource.Customer).map(PaymentMapper.INSTANCE::toPaymentDto).collect(Collectors.toSet());
        return creditRePayments;
    }
}

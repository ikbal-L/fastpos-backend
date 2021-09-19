package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.CashOperation;
import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.domain.Payment;
import com.softlines.fastpos.dto.PaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.*;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(source = "cashOperation.id", target = "cashOperationId",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "deliveryman.id", target = "deliveryManId",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    PaymentDto toPaymentDto(Payment  payment);
    List<PaymentDto> toPaymentDtos(List<Payment> payments);
    @Mapping(source = "deliveryManId", target = "deliveryman",qualifiedByName = "deliveryManIdToDeliveryMan",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "cashOperationId", target = "cashOperation",qualifiedByName = "cashOperationIdToCashOperation",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)

    Payment toPayment(PaymentDto paymentDto);
    List<Payment> toPayments(List<PaymentDto> paymentDtos);
    @Named("deliveryManIdToDeliveryMan")
     static Deliveryman cashOperationIdToDeliveryMan(Long id){
        return  Deliveryman.builder().id(id).build();
    }
    @Named("cashOperationIdToCashOperation")
    static CashOperation cashOperationIdToCashOperation(Long id){
        return  CashOperation.builder().id(id).build();
    }
}

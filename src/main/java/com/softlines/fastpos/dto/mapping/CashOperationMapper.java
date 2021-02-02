package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.CashOperation;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.dto.CashOperationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.*;

@Mapper(componentModel = "spring")

public interface CashOperationMapper {

    CashOperationMapper INSTANCE = Mappers.getMapper(CashOperationMapper.class);

    @Mapping(source = "payment.id", target = "paymentId",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "order.id", target = "orderId",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    CashOperationDto toCashOperationDto(CashOperation cashOperation);
    List<CashOperationDto> toCashOperationDtos(List<CashOperation> cashOperation);


    CashOperation toCashOperation(CashOperationDto cashOperationDto);
    List<CashOperation> toCashOperations(List<CashOperationDto> cashOperationDtos);
}

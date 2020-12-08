package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.OrderItem;
import com.softlines.fastpos.domain.OrderItemAdditive;
import com.softlines.fastpos.domain.OrderItemAdditiveKey;
import com.softlines.fastpos.dto.OrderItemAdditiveDto;
import com.softlines.fastpos.dto.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderItemAdditiveMapper {
    OrderItemAdditiveMapper INSTANCE = Mappers.getMapper(OrderItemAdditiveMapper.class);


    @Mapping(source = "additiveId", target = "additiveId")
    @Mapping(source = "orderItemId", target = "orderItemId")
    OrderItemAdditiveDto toOrderItemAdditiveDto(OrderItemAdditive orderItemAdditive);

    @Mapping(source = "additiveId", target = "additive.id",qualifiedByName ="AdditiveIdToAdditive" )
    OrderItemAdditive toOrderItemAdditive(OrderItemAdditiveDto orderItemAdditive);

    @Mapping(source = "additiveId", target = "additive.id",qualifiedByName ="AdditiveIdToAdditive" )
    @Mapping(source = "orderItem", target = "orderItem")
    OrderItemAdditive toOrderItemAdditive(OrderItemAdditiveDto orderItemAdditive,OrderItem orderItem);

}

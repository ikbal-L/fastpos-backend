package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.OrderItem;
import com.softlines.fastpos.domain.OrderItemAdditive;
import com.softlines.fastpos.dto.OrderItemAdditiveDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderItemAdditiveMapper {
    OrderItemAdditiveMapper INSTANCE = Mappers.getMapper(OrderItemAdditiveMapper.class);

    @Mapping(source = "additive", target = "AdditiveId", qualifiedByName = "AdditiveToId")
    @Mapping(source = "orderItem.id", target = "orderItemId")
    OrderItemAdditiveDto toOrderItemAdditiveDto(OrderItemAdditive orderItemAdditive);

//<<<<<<< HEAD
//    @Mapping(source = "additiveId", target = "additive.id")
//    OrderItemAdditive toOrderItemAdditive(OrderItemAdditiveDto orderItemAdditive);

//    @Mapping(source = "orderItemAdditive.additiveId", target = "additive.id",qualifiedByName ="AdditiveIdToAdditive" )
//    @Mapping(source = "orderItem", target = "orderItem")
//    OrderItemAdditive toOrderItemAdditive(OrderItemAdditiveDto orderItemAdditive,OrderItem orderItem);
    @Mapping(source = "additiveId", target = "additive", qualifiedByName = "AdditiveIdToAdditive")
    @Mapping(source = "orderItemId", target = "orderItem", qualifiedByName = "OrderItemIdToOrderItem")
    OrderItemAdditive toOrderItemAdditive(OrderItemAdditiveDto orderItemAdditive);

    @Named("AdditiveIdToAdditive")
    public static Additive additiveIdToAdditive(long additiveId) {
        return Additive.builder().id(additiveId).build();
    }

    @Named("OrderItemIdToOrderItem")
    public static OrderItem OrderItemIdToOrderItem(long orderItemId) {
        return OrderItem.builder().id(orderItemId).build();
    }

    @Named("OrderItemToId")
    public static Long OrderItemToId(OrderItem orderItem) {
        return orderItem.getId();
    }

    @Named("AdditiveToId")
    public static Long IdToAdditive(Additive additive) {
        return additive.getId();
    }

}

package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);


    @Mapping(source = "orderItems", target = "orderItems", qualifiedByName = "orderItemsToOrderItemsDto")
    @Mapping(source = "table", target = "tableId", qualifiedByName = "tableToTableDto")
    OrderDto toOrderDto(Order order);

    Order toOrder(OrderDto orderDto);


    List<OrderDto> toOrderItemDTOs(List<Order> order);

    @Named("OrderItemToId")
    public static long OrderToId(Order order) {
        return order.getId();
    }

    @Named("AdditiveToId")
    public static long AdditiveToId(Additive additives) {
        return additives.getId();
    }

    @Named("OrderItemToId")
    public static long OrderItemToId(OrderItem orderItem) {
        return orderItem.getId();
    }

    @Named("tableToTableDto")
    public static long TableToTableDto(Tables tables) {
        return tables.getId();
    }

    @Named("orderItemsToOrderItemsDto")
    public static OrderItemDto orderItemsToOrderItemsDto(OrderItem orderItems) {
        OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);
        return INSTANCE.toOrderItemDto(orderItems);

    }

}

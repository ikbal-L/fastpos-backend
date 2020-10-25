package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderItem;
import com.softlines.fastpos.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

//    @Mapping(source = "orderItems", target = "orderItemsIds", qualifiedByName = "OrderItemToId")
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

}

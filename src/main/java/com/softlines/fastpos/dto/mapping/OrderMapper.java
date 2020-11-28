package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);


    @Mapping(source = "orderItems", target = "orderItems", qualifiedByName = "orderItemsToOrderItemsDto")
    @Mapping(source = "table", target = "tableId", qualifiedByName = "TableToId",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    OrderDto toOrderDto(Order order);

    @Mapping(source = "tableId", target = "table", qualifiedByName = "TableIdToTable")
    @Mapping(source = "orderItems", target = "orderItems", qualifiedByName = "OrderItemsDtoToOrderItems")
    Order toOrder(OrderDto orderDto);


    List<OrderDto> toOrderDTOs(List<Order> order);

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

    @Named("TableToId")
    public static long TableToId(Table table) {
        return table.getId();
    }

    @Named("TableIdToTable")
    public static Table TableIdToTable(long id ) {
        Table table = new Table();
        table.setId(id);
        return table;
    }

    @Named("orderItemsToOrderItemsDto")
    public static OrderItemDto orderItemsToOrderItemsDto(OrderItem orderItems) {
        OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);
        return INSTANCE.toOrderItemDto(orderItems);

    }

    @Named("OrderItemsDtoToOrderItems")
    public static OrderItem OrderItemsDtoToOrderItems(OrderItemDto orderItemsDto) {
        OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);
        return INSTANCE.toOrderItem(orderItemsDto);

    }

    @Named("IdToProduct")
    public static Product ProductToId(long productId) {
        Product product = new Product();
        product.setId(productId);
        return product;
    }


}

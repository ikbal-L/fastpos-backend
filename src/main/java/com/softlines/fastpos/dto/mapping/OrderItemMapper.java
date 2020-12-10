package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.OrderItem;
import com.softlines.fastpos.dto.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",uses = {OrderItemAdditiveMapper.class})
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(source = "dto.productId", target = "product.id")
    @Mapping(source = "dto.orderId", target = "order.id")
    @Mapping(source = "orderItemAdditives",target = "orderItemAdditives",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    OrderItem toOrderItem(OrderItemDto dto);

    List<OrderItem> toOrderItemList(List<OrderItemDto> orderItemDtos);


    @Mapping(source = "product.id",
            target = "productId",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "order.id", target = "orderId",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "orderItemAdditives",target = "orderItemAdditives",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    OrderItemDto toOrderItemDto(OrderItem OrderItem);

    List<OrderItemDto> toOrderItemDTOs(List<OrderItem> orderItem);



}

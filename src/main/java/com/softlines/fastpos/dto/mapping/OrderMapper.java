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

@Mapper(componentModel = "spring",uses = {OrderItemMapper.class})
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);


    @Mapping(source = "table.id", target = "tableId",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    OrderDto toOrderDto(Order order);

    @Mapping(source = "dto.tableId", target = "table.id")
    Order toOrder(OrderDto dto);

    List<Order> toOrderList(List<OrderDto> orderDtoList);

    List<OrderDto> toOrderDTOs(List<Order> order);




}

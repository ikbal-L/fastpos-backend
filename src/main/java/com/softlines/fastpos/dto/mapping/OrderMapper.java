package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.OrderItemDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",uses = {OrderItemMapper.class})
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);


    @Mapping(source = "table.id", target = "tableId",nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    OrderDto toOrderDto(Order order);

    @Mapping(source = "tableId", target = "table",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            qualifiedByName = "tableIdToTable" )
    Order toOrder(OrderDto dto);

    List<Order> toOrderList(List<OrderDto> orderDtoList);

    List<OrderDto> toOrderDTOs(List<Order> order);

    @Named("tableIdToTable")
    public static Table tableIdToTable(Long id){
        if(id == null||id == 0) return null;
        return Table.builder().id(id).build();

    }


}

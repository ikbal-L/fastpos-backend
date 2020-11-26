package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.Table;
import com.softlines.fastpos.dto.TableDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TableMapper {
    TableMapper INSTANCE = Mappers.getMapper(TableMapper.class);

    TableDto toTableDto(Table table);

    List<TableDto> toTableDTOs(List<Table> tables);

    Table toTable(TableDto tableDto);

    @Named("OrdersToId")
    public static long OrdersToId(Order order) {
        return order.getId();
    }

    @Named("idsToOrders")
    public static Order idsToOrders(long id) {
        Order order = new Order();
        order.setId(id);
        return order;
    }
}

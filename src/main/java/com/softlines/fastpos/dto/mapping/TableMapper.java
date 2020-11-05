package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Category;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.domain.Tables;
import com.softlines.fastpos.dto.CategoryDto;
import com.softlines.fastpos.dto.TableDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TableMapper {
    TableMapper INSTANCE = Mappers.getMapper(TableMapper.class);

    @Mapping(source = "tableOrders", target = "tableOrdersId", qualifiedByName = "OrdersToId")
    TableDto toTableDto(Tables tables);

    List<TableDto> toTableDTOs(List<Tables> tables);

    @Mapping(source = "tableOrdersId", target = "tableOrders", qualifiedByName = "idsToOrders")
    Tables toTable(TableDto tableDto);

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

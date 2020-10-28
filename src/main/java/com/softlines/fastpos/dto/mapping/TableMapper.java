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

    @Mapping(source = "tableOrders", target = "tableOrdersId", qualifiedByName = "OrderToId")
    TableDto toTableDto(Tables tables);

    List<TableDto> toTableDTOs(List<Tables> tables);

    Tables toTable(TableDto tableDto);

    @Named("OrderToId")
    public static long OrderToId(Order order) {
        return order.getId();
    }
}

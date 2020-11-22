package com.softlines.fastpos.dto.mapping;


import com.softlines.fastpos.domain.Waiter;
import com.softlines.fastpos.dto.WaiterDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface WaiterMapper {

    WaiterMapper INSTANCE = Mappers.getMapper(WaiterMapper.class);

    WaiterDto toWaiterDto(Waiter waiter);

    List<WaiterDto> toWaiterDTOs(List<Waiter> waiters);

    Waiter toWaiter(WaiterDto waiterDto);

}

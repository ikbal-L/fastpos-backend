package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.dto.DeliverymanDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliverymanMapper {

    DeliverymanMapper INSTANCE = Mappers.getMapper(DeliverymanMapper.class);

    DeliverymanDto toDeliverymanDto(Deliveryman Deliveryman);

    List<DeliverymanDto> toDeliverymanDTOs(List<Deliveryman> Deliverymen);

    Deliveryman toDeliveryman(DeliverymanDto DeliverymanDto);

}

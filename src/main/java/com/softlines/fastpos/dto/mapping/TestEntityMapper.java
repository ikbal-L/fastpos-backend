package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.domain.TestEntity;
import com.softlines.fastpos.dto.AdditiveDto;
import com.softlines.fastpos.dto.TestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper(componentModel = "spring")
public interface TestEntityMapper {
    TestEntityMapper INSTANCE = Mappers.getMapper(TestEntityMapper.class);

    TestDTO toTestDto(TestEntity testEntity);
    List<AdditiveDto> toAdditiveDTOs(List<Additive> additives);

    TestEntity toTestEntity(TestDTO testDTO);
}

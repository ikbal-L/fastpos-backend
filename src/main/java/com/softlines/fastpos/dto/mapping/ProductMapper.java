package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
//    ModelMapper modelMapper = new ModelMapper();
//    ProductDto userDTO = modelMapper.map(product, ProductDto.class);

//    @Mapping(source = "ProductDto.idAdditive", target = "Product.Additives")
    ProductDto toDto(Product product);

    List<ProductDto> toProductDTOs(List<Product> products);

    Product toProduct(ProductDto productDTO);

}

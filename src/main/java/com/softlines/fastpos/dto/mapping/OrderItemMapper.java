package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.OrderItem;
import com.softlines.fastpos.domain.Product;
import com.softlines.fastpos.dto.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(source = "productId", target = "product", qualifiedByName = "IdToProduct")
    @Mapping(source = "idAdditives", target = "additive", qualifiedByName = "IdToAdditive")
    OrderItem toOrderItem(OrderItemDto orderItemDto);

    List<OrderItem> toOrderItemList(List<OrderItemDto> orderItemDtos);

    @Mapping(source = "additive", target = "idAdditives", qualifiedByName = "AdditiveToId")
    @Mapping(source = "product", target = "productId", qualifiedByName = "ProductToId")
    OrderItemDto toOrderItemDto(OrderItem OrderItem);

    List<OrderItemDto> toOrderItemDTOs(List<OrderItem> orderItem);

    @Named("OrderItemToId")
    public static long OrderItemToId(OrderItem orderItem) {
        return orderItem.getId();
    }


    @Named("AdditiveToId")
    public static long AdditiveToId(Additive additives) {
            return additives.getId();
    }

    @Named("ProductToId")
    public static long ProductToId(Product product) {
        return product.getId();
    }


    @Named("IdToProduct")
    public static Product IdToProduct(long productId) {
        Product product = new Product();
        product.setId(productId);
        return product;
    }

    @Named("IdToAdditive")
    public static Additive IdToAdditive(long additiveId) {
        Additive additive = new Additive();
        additive.setId(additiveId);
        return additive;
    }
}

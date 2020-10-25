package com.softlines.fastpos.dto.service;

import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.dto.*;

import java.util.List;

public interface DtoService {
    Product productDtoToProduct(ProductDto pDto);
    Category categoryDtoToCategory(CategoryDto categoryDto);
    OrderItem orderItemDtoToOrderItem(OrderItemDto pDto);
    Order orderDtoToOrder(OrderDto orderDto);
    List<OrderItem> orderItemDtoListToOrderItemList(List<OrderItemDto> oiDtos);
    Additive additiveDtoToAdditive(AdditiveDto additiveDto);
}

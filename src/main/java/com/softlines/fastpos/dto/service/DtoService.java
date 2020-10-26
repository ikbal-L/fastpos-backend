package com.softlines.fastpos.dto.service;

import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.dto.*;

import java.util.List;

public interface DtoService {
    Product productDtoToProduct(ProductDto pDto,boolean getDataFromRepository);
    Category categoryDtoToCategory(CategoryDto categoryDto, boolean getDataFromRepository);
    OrderItem orderItemDtoToOrderItem(OrderItemDto pDto);
    Order orderDtoToOrder(OrderDto orderDto);
    List<OrderItem> orderItemDtoListToOrderItemList(List<OrderItemDto> oiDtos);
    Additive additiveDtoToAdditive(AdditiveDto additiveDto);
}

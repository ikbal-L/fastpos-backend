package com.softlines.fastpos.dto.service;

import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.dto.*;

import java.util.List;

public interface DtoService {
    Product productDtoToProduct(ProductDto pDto, boolean getDataFromRepository);

    List<Product> productDtoListToProductList(List<ProductDto> productDtoList, boolean getDataFromRepository);

    Category categoryDtoToCategory(CategoryDto categoryDto, boolean getDataFromRepository);

    List<Category> categoriesDtoToCategories(List<CategoryDto> categoryDtos, boolean getDataFromRepository);

    OrderItem orderItemDtoToOrderItem(OrderItemDto pDto, boolean getDataFromRepository);

    Order orderDtoToOrder(OrderDto orderDto);

    List<Order> orderDtoListToOrderList(List<OrderDto> orderDtoList);

    List<OrderItem> orderItemDtoListToOrderItemList(List<OrderItemDto> oiDtos, boolean getDataFromRepository);

    Additive additiveDtoToAdditive(AdditiveDto additiveDto, boolean getDataFromRepository);

    List<Additive> additivesDtoToAdditives(List<AdditiveDto> additiveDtoList, boolean getDataFromRepository);

    Person personDtoToPerson(PersonDto personDto, boolean getDataFromRepository);

    Waiter waiterDtoToWaiter(WaiterDto waiterDto, boolean getDataFromRepository);

    Deliveryman deliverymanDtoToDeliveryman(DeliverymanDto deliverymanDto, boolean getDataFromRepository);
}

package com.softlines.fastpos.dto.service;

import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.dto.*;
import com.softlines.fastpos.dto.mapping.*;
import com.softlines.fastpos.repository.AdditiveRepository;
import com.softlines.fastpos.repository.CategoryRepository;
import com.softlines.fastpos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DtoServiceImpl implements DtoService {


    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AdditiveRepository additiveRepository;
    ////////////////////////////
    ///////////////////////////
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    AdditiveMapper additiveMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;

    @Override
    public Product productDtoToProduct(ProductDto pDto, boolean getDataFromRepository) {
        List<Additive> additives = new ArrayList<Additive>();
        Product p = productMapper.toProduct(pDto);
        if (getDataFromRepository) {
            for (Long idAdditive : pDto.getIdAdditives()) {
                additives.add(additiveRepository.findById(idAdditive).get());
            }
            p.setAdditives(additives);
            p.setCategory(categoryRepository.findById(pDto.getCategoryId()).get());
        }
        return p;

    }

    @Override
    public Category categoryDtoToCategory(CategoryDto categoryDto, boolean getDataFromRepository) {
        List<Product> products = new ArrayList<Product>();
        Category category = categoryMapper.toCategory(categoryDto);
        if (getDataFromRepository) {
            for (Long idProduct : categoryDto.getIdProducts()) {
                products.add(productRepository.findById(idProduct).get());
            }
            category.setProducts(products);
        }

        return category;

    }


    @Override
    public OrderItem orderItemDtoToOrderItem(OrderItemDto oiDto) {
        List<Additive> additives = new ArrayList<Additive>();
        OrderItem orderItem = orderItemMapper.toOrderItem(oiDto);
        for (Long idAdditive : oiDto.getIdAdditives()) {
            additives.add(additiveRepository.findById(idAdditive).get());
        }
        orderItem.setAdditive(additives);
        orderItem.setProduct(productRepository.findById(oiDto.getProductId()).get());

//        orderItem.setOrder();
        return orderItem;
    }

    @Override
    public List<OrderItem> orderItemDtoListToOrderItemList(List<OrderItemDto> oiDtos) {
        List<Additive> additives = new ArrayList<Additive>();
        List<OrderItem> orderItems = orderItemMapper.toOrderItemList(oiDtos);
        for (int i = 0; i < oiDtos.size(); i++) {
            additives.clear();
            for (Long idAdditive : oiDtos.get(i).getIdAdditives()) {
                additives.add(additiveRepository.findById(idAdditive).get());
            }
            orderItems.get(i).setAdditive(additives);
            orderItems.get(i).setProduct(productRepository.findById(oiDtos.get(i).getProductId()).get());
        }
        return orderItems;
    }


    @Override
    public Order orderDtoToOrder(OrderDto orderDto) {
        List<Additive> additivesOrderItem = new ArrayList<Additive>();
        Order order = orderMapper.toOrder(orderDto);
        order.setOrderItems(orderItemDtoListToOrderItemList(orderDto.getOrderItems()));
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrder(order);
        }
        return order;
    }

    @Override
    public Additive additiveDtoToAdditive(AdditiveDto additiveDto) {
        Additive additive = additiveMapper.toAditive(additiveDto);
        return additive;
    }

}

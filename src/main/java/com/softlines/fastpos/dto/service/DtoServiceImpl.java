package com.softlines.fastpos.dto.service;

import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.dto.*;
import com.softlines.fastpos.dto.mapping.*;
import com.softlines.fastpos.jwtsecurity.securityrepository.RoleRepository;
import com.softlines.fastpos.repository.AdditiveRepository;
import com.softlines.fastpos.repository.CategoryRepository;
import com.softlines.fastpos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DtoServiceImpl implements DtoService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AdditiveRepository additiveRepository;

    @Autowired
    private RoleRepository roleRepository;
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

    @Autowired
    PersonMapper personMapper;

    @Autowired
    WaiterMapper waiterMapper;

    @Autowired
    DeliverymanMapper deliverymanMapper;


    @Override
    public Product productDtoToProduct(ProductDto pDto, boolean getDataFromRepository) {
        List<Additive> additives = new ArrayList<Additive>();
        Product p = productMapper.toProduct(pDto);
        if (getDataFromRepository) {
            if (pDto.getIdAdditives() != null && pDto.getIdAdditives().stream().count() > 0) {
                for (Long idAdditive : pDto.getIdAdditives()) {
                    additives.add(additiveRepository.findById(idAdditive).get());
                }
                p.setAdditives(additives);
            }
            if (pDto.getCategoryId() != null) {
                p.setCategory(categoryRepository.findById(pDto.getCategoryId()).get());
            }
        }
        return p;

    }

    @Override
    public List<Product> productDtoListToProductList(List<ProductDto> productDtoList, boolean getDataFromRepository) {
        List<Additive> additives = new ArrayList<Additive>();
        List<Product> productList = productMapper.toProductList(productDtoList);

        return productList;

    }

    @Override
    public Category categoryDtoToCategory(CategoryDto categoryDto, boolean getDataFromRepository) {
        List<Product> products = new ArrayList<Product>();
        Category category = categoryMapper.toCategory(categoryDto);

        if (getDataFromRepository) {
            for (Long idProduct : categoryDto.getProductIds()) {
                products.add(productRepository.findById(idProduct).get());
            }
            category.setProducts(products);
        }

        return category;

    }

    @Override
    public List<Category> categoriesDtoToCategories(List<CategoryDto> categoryDtos, boolean getDataFromRepository) {
        List<Product> products = new ArrayList<Product>();
        List<Category> categories = categoryMapper.toCategories(categoryDtos);

        return categories;

    }

    @Override
    public Person personDtoToPerson(PersonDto personDto, boolean getDataFromRepository) {
        Person person = personMapper.toPerson(personDto);
        return person;

    }

    @Override
    public Waiter waiterDtoToWaiter(WaiterDto waiterDto, boolean getDataFromRepository) {
        Waiter waiter = waiterMapper.toWaiter(waiterDto);

        return waiter;
    }

    @Override
    public Deliveryman deliverymanDtoToDeliveryman(DeliverymanDto deliverymanDto, boolean getDataFromRepository) {
        Deliveryman deliveryman = deliverymanMapper.toDeliveryman(deliverymanDto);

        return deliveryman;
    }


    @Override
    public OrderItem orderItemDtoToOrderItem(OrderItemDto oiDto, boolean getDataFromRepository) {
        List<Additive> additives = new ArrayList<Additive>();
        OrderItem orderItem = orderItemMapper.toOrderItem(oiDto);
        if (getDataFromRepository) {
            for (Long idAdditive : oiDto.getAdditiveIds()) {
                additives.add(additiveRepository.findById(idAdditive).get());
            }
//            orderItem.setAdditive(additives);
            orderItem.setProduct(productRepository.findById(oiDto.getProductId()).get());
        }
        return orderItem;
    }

    @Override
    public List<OrderItem> orderItemDtoListToOrderItemList(List<OrderItemDto> orderItemDtos, boolean getDataFromRepository) {
        List<Additive> additives = new ArrayList<Additive>();

        List<OrderItem> orderItems = orderItemMapper.toOrderItemList(orderItemDtos);
        if (getDataFromRepository) {
            for (int i = 0; i < orderItemDtos.size(); i++) {
                additives.clear();
                for (Long idAdditive : orderItemDtos.get(i).getAdditiveIds()) {
                    additives.add(additiveRepository.findById(idAdditive).get());
                }

//                orderItems.get(i).setAdditive(additives);

                orderItems.get(i).setProduct(productRepository.findById(orderItemDtos.get(i).getProductId()).get());
            }
        }


        for (int i = 0; i < orderItems.size(); i++) {
            var orderItemDto = orderItemDtos.get(i);
            var orderItem = orderItems.get(i);
            var orderItemAdditives =
                    orderItemAdditiveDtosToOrderItemAdditives(orderItemDto, orderItem, true);
            orderItem.setOrderItemAdditives(orderItemAdditives);
        }


        return orderItems;
    }


    @Override
    public Order orderDtoToOrder(OrderDto orderDto) {
        Order order = orderMapper.toOrder(orderDto);
        order.setOrderItems(orderItemDtoListToOrderItemList(orderDto.getOrderItems(), false));
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrder(order);
        }
        return order;
    }

    @Override
    public List<Order> orderDtoListToOrderList(List<OrderDto> orderDtoList) {
        List<Order> orderList = orderMapper.toOrderList(orderDtoList);

        for (int i = 0; i < orderList.size(); i++) {
            orderList.get(i)
                    .setOrderItems(orderItemDtoListToOrderItemList(orderDtoList.get(i).getOrderItems(), false));

            for (OrderItem orderItem : orderList.get(i).getOrderItems()) {
                orderItem.setOrder(orderList.get(i));
            }
        }

        return orderList;
    }

    @Override
    public Additive additiveDtoToAdditive(AdditiveDto additiveDto, boolean getDataFromRepository) {
        Additive additive = additiveMapper.toAdditive(additiveDto);
        return additive;
    }


    @Override
    public List<Additive> additivesDtoToAdditives(List<AdditiveDto> additiveDtoList, boolean getDataFromRepository) {
        List<Additive> additiveList = additiveMapper.toAdditiveList(additiveDtoList);
        return additiveList;
    }

    @Override
    public OrderItemAdditive orderItemAdditiveDtoToOrderItemAdditive(OrderItemAdditiveDto orderItemAdditiveDto, OrderItem orderItem, boolean getDataFromRepository) {
        OrderItemAdditiveMapper mapper = OrderItemAdditiveMapper.INSTANCE;
        OrderItemAdditive orderItemAdditive = mapper.toOrderItemAdditive(orderItemAdditiveDto);
        Additive additive;
        if (getDataFromRepository) {
             additive = additiveRepository.findById(orderItemAdditiveDto.getAdditiveId()).get();


//            additive.getOrderItemAdditives().add(orderItemAdditive);
        }else {
            additive = Additive.builder().id(orderItemAdditiveDto.getAdditiveId()).build();
        }
//        if (orderItemAdditiveDto.getOrderItemId() != null&& orderItemAdditiveDto.getAdditiveId()!=null) {
//            OrderItemAdditiveKey key =
//                    OrderItemAdditiveKey.builder().
//                            additiveId(orderItemAdditiveDto.getAdditiveId()).
//                            orderItemId(orderItemAdditiveDto.getOrderItemId()).build();
//            orderItemAdditive.setId(key);
//        }
//        var key = new OrderItemAdditiveKey();
//        orderItemAdditive.setId(key);
        orderItemAdditive.setAdditive(additive);
        orderItemAdditive.setOrderItem(orderItem);
//        orderItemAdditive.setAdditive(additive);
        return orderItemAdditive;
    }

    public List<OrderItemAdditive> orderItemAdditiveDtosToOrderItemAdditives(OrderItemDto orderItemDto, OrderItem orderItem, boolean getDataFromRepository) {
        List<OrderItemAdditive> orderItemAdditives = new ArrayList<>();
        for (OrderItemAdditiveDto orderItemAdditiveDto :
                orderItemDto.getOrderItemAdditives()) {
            var orderItemAdditive = orderItemAdditiveDtoToOrderItemAdditive(orderItemAdditiveDto, orderItem, getDataFromRepository);
            orderItemAdditives.add(orderItemAdditive);
        }
        return orderItemAdditives;
    }
}

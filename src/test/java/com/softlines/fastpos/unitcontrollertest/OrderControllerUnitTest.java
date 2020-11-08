package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.OrderController;
import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.mapping.OrderMapper;
import com.softlines.fastpos.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class OrderControllerUnitTest {

    @MockBean
    OrderRepository orderRepository;

    @Autowired
    OrderController orderController;

    @Autowired
    OrderMapper orderMapper;

    @Test
    public void orderController_getAll_WithNotEmptyOrdersList() throws Exception {

        var orders = Arrays.asList(
                Order.builder()
                        .id(1l)
                        .orderstate(OrderState.Payed)
                        .orderItems(Arrays.asList(OrderItem.builder().additive(Arrays.asList(Additive.builder().build()))
                                .id(1).product(Product.builder().build())
                                .order(Order.builder().build()).build()))
                        .elapsedTime(Duration.ZERO)
                        .orderTime(LocalDateTime.now())
                        .build()
        );

        when(orderRepository.findAll()).thenReturn(orders);

        var res = orderController.getOrders();

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().get(0).getOrderstate(), orders.get(0).getOrderstate());
        assertEquals((res.getBody()).size(), 1);
        assertEquals((res.getBody()).get(0).getOrderItems().get(0), orders.get(0).getOrderItems().get(0).getId());
        assertEquals((res.getBody()).get(0).getOrderItems().size(), 1);

    }

    @Test
    public void orderController_getAll_WithEmptyOrdersList() {
        var orders = new ArrayList<com.softlines.fastpos.domain.Order>();
        when(orderRepository.findAll()).thenReturn(orders);
        var res = orderController.getOrders();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void orderController_getAll_WithNullOrdersList() {
        when(orderRepository.findAll()).thenReturn(null);

        var res = orderController.getOrders();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void orderController_gelAll_getOrdersWithNoDBConnectionException() {

        when(orderRepository.findAll())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = orderController.getOrders();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Save Order Unit Test  <------------------------
     */
//
//    @Test
//    @Order(5)
//    public void orderController_Save_WithData() {
//
//        var product = Order.builder()
//                        .id(1l)
//                        .name("Pizza")
//                        .additives(Arrays.asList(Additive.builder().id(1).description("harrisa").build()))
//                        .category(Category.builder().build())
//                        .build();
//
//        when(orderRepository.save(Mockito.any(Order.class))).thenReturn(product);
//
//        var res = orderController.addOrder(productMapper.toOrderDto(product));
//        assertEquals(res.getStatusCode(), HttpStatus.CREATED);
//        assertEquals((res.getBody()).getDescription(), product.getDescription());
//
//    }
//
//
//    @Test
//    @Order(6)
//    public void orderController_Save_WithoutData() {
//
//        var product = Order.builder().category(Category.builder().build()).build();
//
//        when(orderRepository.save(Mockito.any(Order.class))).thenReturn(product);
//        var res = orderController.addOrder(productMapper.toOrderDto(product));
//
//        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
//
//    }
//
//
//    @Test
//    @Order(5)
//    public void orderController_Save_WithExistOrder() {
//
//        var product = Order.builder()
//                .id(1)
//                .name("tacos")
//                .category(Category.builder().build())
//                .additives(Arrays.asList(Additive.builder().build())).build();
//
//        when(orderRepository.findById(product.getId())).thenReturn(Optional.of(product));
//        var res = orderController.addOrder(productMapper.toOrderDto(product) );
//
//        assertEquals(res.getStatusCode(), HttpStatus.FOUND);
//
//    }
//
//
//    @Test
//    @Order(7)
//    public void orderController_save_WithNoDBConnection() {
//
//        var product =
//                Order.builder()
//                        .id(1l)
//                        .name("harrisa")
//                        .backgroundString("red")
//                        .rank(2)
//                        .category(Category.builder().build())
//                        .build();
//
//        when(orderRepository.save(Mockito.any(Order.class))).thenThrow(DataAccessResourceFailureException.class);
//        var res = orderController.addOrder(productMapper.toOrderDto(product));
//
//        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
//
//    }
//
//
//    /**
//     * ------------------>  GetById Order Unit Test  <------------------------
//     */
//
//    @Test
//    @Order(9)
//    public void AdditiveController_getById_WithNotEmptyAdditive() {
//
//        var product =
//                Order.builder()
//                        .id(1)
//                        .rank(5)
//                        .description("harrisa")
//                        .additives(Arrays.asList(Additive.builder().build()))
//                        .category(Category.builder().build())
//                        .build();
//
//        when(orderRepository.findById(1l)).thenReturn(Optional.ofNullable(product));
//
//        var res = orderController.getOrder(1);
//        assertEquals(res.getStatusCode(), HttpStatus.OK);
//        assertEquals((res.getBody()).getDescription(), product.getDescription());
//    }
//
//
//    @Test
//    @Order(10)
//    public void orderController_getById_WithEmptyOrder() {
//
//        var additive = new Order();
//        when(orderRepository.findById(0l)).thenReturn(Optional.of(additive));
//        var res = orderController.getOrder(0);
//        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
//
//    }
//
//    @Test
//    @Order(11)
//    public void orderController_getById_WithNullOrder() {
//
//        var res = orderController.getOrder(1);
//        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
//
//    }
//
//    @Test
//    @Order(12)
//    public void additiveController_getById_getAdditivesWithNoDBConnection() {
//
//        when(orderRepository.findById(5l))
//                .thenThrow(DataAccessResourceFailureException.class);
//
//        var res = orderController.getOrder(5);
//
//        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
//
//    }
//
//
//
//    /**
//     * ------------------>  Delete Order Unit Test  <------------------------
//     */
//
//    @Test
//    public void orderController_Delete_WithOrderId() {
//
//        var product =
//                Order.builder()
//                        .id(1l)
//                        .name("name")
//                        .build();
//
//        when(orderRepository.findById(1l)).thenReturn(Optional.ofNullable(product));
//        orderController.deleteOrder(1);
//
//        verify(orderRepository, times(1)).delete(product);
//
//
//    }
//
//    @Test
//    public void orderController_Delete_WithNotExistOrderId() {
//
//        when(orderRepository.findById(1l)).thenReturn(null);
//
//        orderController.deleteOrder(1);
//
//        verify(orderRepository, times(1)).findById(1l);
//        verifyNoMoreInteractions(orderRepository);
//
//    }
//
//    @Test
//    @Order(12)
//    public void orderController_Delete_getOrderesWithNoDBConnection() {
//
//        when(orderRepository.findById(5l))
//                .thenThrow(DataAccessResourceFailureException.class);
//
//        var res = orderController.getOrder(5);
//
//        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
//
//    }
//
//
//    /**
//     * ------------------>  Put Order Unit Test  <------------------------
//     */
//
//    @Test
//    public void orderController_Put_WithData() {
//
//        var product = Order.builder()
//                .id(1l)
//                .name("harrisa")
//                .category(Category.builder().build())
//                .build();
//
//        when(orderRepository.findById(product.getId())).thenReturn(Optional.of(product));
//        ResponseEntity<OrderDto> returned = orderController.editOrder(1,productMapper.toOrderDto( product));
//
//        verify(orderRepository, times(1)).findById(product.getId());
//        assertEquals(returned.getStatusCode(), HttpStatus.OK);
//
//    }
//
//    @Test
//    public void orderController_Put_WithIdNotExist() {
//
//        var product = Order.builder()
//                .id(10l)
//                .name("harrisa")
//                .category(Category.builder().build())
//                .additives(Arrays.asList(Additive.builder().build()))
//                .build();
//
//        when(orderRepository.findById(product.getId())).thenReturn(Optional.empty());
//        ResponseEntity<OrderDto> returned = orderController.editOrder(10, productMapper.toOrderDto( product));
//
//        verify(orderRepository, times(1)).findById(product.getId());
//        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);
//
//    }
//
//    @Test
//    public void orderController_Put_WithNullData() {
//
//        var product = Order.builder()
//                .id(1)
//                .category(Category.builder().build())
//                .build();
//
//        when(orderRepository.findById(product.getId())).thenReturn(Optional.of(product));
//        ResponseEntity<OrderDto> returned = orderController.editOrder(1, productMapper.toOrderDto( product));
//
//        verify(orderRepository, times(1)).findById(product.getId());
//        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);
//
//    }
//
//    @Test
//    @Order(12)
//    public void orderController_Put_getOrderesWithNoDBConnection() {
//
//        when(orderRepository.findById(5l))
//                .thenThrow(DataAccessResourceFailureException.class);
//
//        var res = orderController.getOrder(5);
//
//        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
//
//    }
//

}

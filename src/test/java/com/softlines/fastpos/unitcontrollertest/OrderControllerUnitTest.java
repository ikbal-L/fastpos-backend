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
    public void orderController_getAll_WithNotEmptyOrdersList() {

        var orders = Arrays.asList(
                Order.builder()
                        .id(1L)
                        .orderstate(OrderState.Payed)
                        .orderItems(Arrays.asList(OrderItem.builder().additive(Arrays.asList(Additive.builder().build()))
                                .id(1).product(Product.builder().build())
                                .order(Order.builder().build()).build()))
                        .elapsedTime(Duration.ZERO)
                        .orderTime(LocalDateTime.now())
                        .table(Tables.builder().build())
                        .build()
        );

        when(orderRepository.findAllOrdersWithOrderItems()).thenReturn(orders);
        var res = orderController.getOrders();

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().get(0).getOrderstate(), orders.get(0).getOrderstate());
        assertEquals((res.getBody()).size(), 1);
        assertEquals((res.getBody()).get(0).getOrderItems().get(0).getId(), orders.get(0).getOrderItems().get(0).getId());
        assertEquals((res.getBody()).get(0).getOrderItems().size(), 1);
        assertEquals((res.getBody()).get(0).getOrderItems().get(0).getIdAdditives().size(), orders.get(0).getOrderItems().get(0).getAdditive().size());
        assertEquals((res.getBody()).get(0).getOrderItems().get(0).getProductId(), orders.get(0).getOrderItems().get(0).getProduct().getId());

    }

    @Test
    public void orderController_getAll_WithEmptyOrdersList() {
        var orders = new ArrayList<Order>();
        when(orderRepository.findAllOrdersWithOrderItems()).thenReturn(orders);
        var res = orderController.getOrders();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }


    @Test
    public void orderController_getAll_WithNullOrdersList() {
        when(orderRepository.findAllOrdersWithOrderItems()).thenReturn(null);

        var res = orderController.getOrders();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }


    @Test
    public void orderController_gelAll_WithNoDBConnectionException() {

        when(orderRepository.findAllOrdersWithOrderItems())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = orderController.getOrders();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }

    /**
     * ------------------>  Save Order Unit Test  <------------------------
     */

    @Test
    public void orderController_Save_WithData() {

        var orders =
                Order.builder()
                        .id(1l)
                        .orderstate(OrderState.Payed)
                        .orderItems(Arrays.asList(OrderItem.builder().additive(Arrays.asList(Additive.builder().build()))
                                .id(1).name("pizza")
                                .product(Product.builder().build())
                                .order(Order.builder().build()).build()))
                        .elapsedTime(Duration.ZERO)
                        .orderTime(LocalDateTime.now())
                        .table(Tables.builder().build())
                        .build();

        when(orderRepository.save(Mockito.any(Order.class))).thenReturn(orders);

        var res = orderController.addOrder(orderMapper.toOrderDto(orders));
        assertEquals(res.getStatusCode(), HttpStatus.CREATED);
        assertEquals((res.getBody()).getOrderItems().get(0).getName(), orders.getOrderItems().get(0).getName());
        assertEquals((res.getBody()).getOrderItems().get(0).getProductId(), orders.getOrderItems().get(0).getProduct().getId());
        assertEquals((res.getBody()).getOrderItems().get(0).getIdAdditives().get(0), orders.getOrderItems().get(0).getAdditive().get(0).getId());

    }


    @Test
    public void orderController_Save_WithoutData() {

        var order = Order.builder()
                .orderItems(Arrays.asList())
                .table(Tables.builder().build())
                .build();

        when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);
        var res = orderController.addOrder(orderMapper.toOrderDto(order));

        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }


    @Test
    public void orderController_Save_WithExistOrder() {

        var order =
                Order.builder()
                        .id(1)
                        .orderItems(Arrays.asList(OrderItem.builder()
                                .additive(Arrays.asList(Additive.builder().build()))
                                .product(Product.builder().build())
                                .order(Order.builder().id(1).build())
                                .build()))

                        .table(Tables.builder().build())
                        .build();

        when(orderRepository.findByIdOrderWithOrderItems(order.getId())).thenReturn(order);
        var res = orderController.addOrder(orderMapper.toOrderDto(order));

        assertEquals(res.getStatusCode(), HttpStatus.FOUND);

    }

    @Test
    public void orderController_save_WithNoDBConnection() {

        var order =
                Order.builder()
                        .id(1l)
                        .orderstate(OrderState.Payed)
                        .orderItems(Arrays.asList(OrderItem.builder().additive(Arrays.asList(Additive.builder().build()))
                                .id(1).name("pizza")
                                .product(Product.builder().build())
                                .order(Order.builder().build()).build()))
                        .elapsedTime(Duration.ZERO)
                        .orderTime(LocalDateTime.now())
                        .table(Tables.builder().build())
                        .build();


        when(orderRepository.save(Mockito.any(Order.class))).thenThrow(DataAccessResourceFailureException.class);
        var res = orderController.addOrder(orderMapper.toOrderDto(order));

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  GetById Order Unit Test  <------------------------
     */

    @Test
    public void OrderController_getById_WithNotEmptyOrder() {

        var order =
                Order.builder()
                        .id(1l)
                        .orderstate(OrderState.Payed)
                        .orderItems(Arrays.asList(OrderItem.builder().additive(Arrays.asList(Additive.builder().build()))
                                .id(1).name("pizza")
                                .product(Product.builder().build())
                                .order(Order.builder().build()).build()))
                        .elapsedTime(Duration.ZERO)
                        .orderTime(LocalDateTime.now())
                        .table(Tables.builder().build())
                        .build();

        when(orderRepository.findByIdOrderWithOrderItems(1l)).thenReturn(order);

        var res = orderController.getOrder(1);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).getBuyerId(), order.getBuyerId());
    }


    @Test
    public void orderController_getById_WithEmptyOrder() {

        var order = new Order();
        when(orderRepository.findByIdOrderWithOrderItems(0l)).thenReturn(order);
        var res = orderController.getOrder(0);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void orderController_getById_WithNullOrder() {

        var res = orderController.getOrder(1);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void OrderController_getById_WithNoDBConnection() {

        when(orderRepository.findByIdOrderWithOrderItems(5L))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = orderController.getOrder(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Delete Order Unit Test  <------------------------
     */

    @Test
    public void orderController_Delete_WithOrderId() {

        var order =
                Order.builder()
                        .id(1L)
                        .build();

        when(orderRepository.findByIdOrderWithOrderItems(1l)).thenReturn(order);
        orderController.deleteOrder(1);

        verify(orderRepository, times(1)).delete(order);


    }

    @Test
    public void orderController_Delete_WithNotExistOrderId() {

        when(orderRepository.findByIdOrderWithOrderItems(1L)).thenReturn(null);

        orderController.deleteOrder(1);

        verify(orderRepository, times(1)).findByIdOrderWithOrderItems(1L);
        verifyNoMoreInteractions(orderRepository);

    }

    @Test
    public void orderController_Delete_WithNoDBConnection() {

        when(orderRepository.findByIdOrderWithOrderItems(5L))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = orderController.getOrder(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Put Order Unit Test  <------------------------
     */

    @Test
    public void orderController_Put_WithData() {
        var orders = Order.builder()
                .id(1L)
                .orderstate(OrderState.Payed)
                .orderItems(Arrays.asList(OrderItem.builder()
                        .id(1).name("pizza")
                        .additive(Arrays.asList(Additive.builder().build()))
                        .product(Product.builder().build())
                        .order(Order.builder().id(1).build()).build()))
                .elapsedTime(Duration.ZERO)
                .orderTime(LocalDateTime.now())
                .table(Tables.builder().id(1).build())
                .build();

        when(orderRepository.findByIdOrderWithOrderItems(orders.getId())).thenReturn(orders);
        ResponseEntity<OrderDto> returned = orderController.editOrder(1, orderMapper.toOrderDto(orders));

        verify(orderRepository, times(1)).findByIdOrderWithOrderItems(orders.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void orderController_Put_WithIdNotExist() {

        var order =
                Order.builder()
//                        .id(1)
                        .orderstate(OrderState.Payed)
                        .orderItems(Arrays.asList(OrderItem.builder().additive(Arrays.asList(Additive.builder().build()))
                                .name("pizza")
                                .product(Product.builder().build())
                                .order(Order.builder().build()).build()))
                        .elapsedTime(Duration.ZERO)
                        .orderTime(LocalDateTime.now())
                        .table(Tables.builder().build())
                        .build();

        when(orderRepository.findByIdOrderWithOrderItems(order.getId())).thenReturn(null);
        ResponseEntity<OrderDto> returned = orderController.editOrder(0L, orderMapper.toOrderDto(order));

        verify(orderRepository, times(1)).findByIdOrderWithOrderItems(order.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void orderController_Put_WithNullData() {

        var order = Order.builder()
                .id(1L)
                .table(Tables.builder().id(1).build())
                .build();

        when(orderRepository.findByIdOrderWithOrderItems(order.getId())).thenReturn(order);
        ResponseEntity<OrderDto> returned = orderController.editOrder(1L, orderMapper.toOrderDto(order));

        verify(orderRepository, times(1)).findByIdOrderWithOrderItems(order.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }


    @Test
    public void orderController_Put_WithNoDBConnection() {

        when(orderRepository.findByIdOrderWithOrderItems(5L))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = orderController.getOrder(5L);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


}

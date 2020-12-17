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

import java.time.LocalTime;
import java.util.*;

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

        List<Order> orders = Arrays.asList(
                Order.builder()
                        .id(1L)
                        .state(OrderState.Payed)
                        .orderItems(Arrays.asList(OrderItem.builder()
                                .orderItemAdditives(Arrays.asList(OrderItemAdditive.builder()
                                        .additive(Additive.builder().id(1L).build())
                                        .orderItem(OrderItem.builder().build())
                                        .timestamp(new Date())
                                        .state(AdditiveSate.Added)
                                        .build()))
                                .id(1).product(Product.builder().build())
                                .order(Order.builder().build()).build()))
                        .elapsedTime(LocalTime.now())
                        .orderTime(new Date())
                        .table(Table.builder().build())
                        .build()
        );

        when(orderRepository.getAllOrder()).thenReturn(orders);
        var res = orderController.getOrders(Optional.empty());

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().get(0).getState(), orders.get(0).getState());
        assertEquals((res.getBody()).size(), 1);
        assertEquals((res.getBody()).get(0).getOrderItems().get(0).getId(), orders.get(0).getOrderItems().get(0).getId());
        assertEquals((res.getBody()).get(0).getOrderItems().size(), 1);
        assertEquals((res.getBody()).get(0).getOrderItems().get(0).getOrderItemAdditives().size(), orders.get(0).getOrderItems().get(0).getOrderItemAdditives().size());
        assertEquals((res.getBody()).get(0).getOrderItems().get(0).getProductId(), orders.get(0).getOrderItems().get(0).getProduct().getId());

    }

    @Test
    public void orderController_getAll_WithEmptyOrdersList() {
        var orders = new ArrayList<Order>();
        when(orderRepository.getAllOrder()).thenReturn(orders);
        var res = orderController.getOrders(null);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }


    @Test
    public void orderController_getAll_WithNullOrdersList() {
        when(orderRepository.findAllOrdersWithOrderItems()).thenReturn(null);

        var res = orderController.getOrders(null);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }


    @Test
    public void orderController_gelAll_WithNoDBConnectionException() {

        when(orderRepository.findAllOrdersWithOrderItems())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = orderController.getOrders(null);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }

    /**
     * ------------------>  Save Order Unit Test  <------------------------
     */

    @Test
    public void orderController_Save_WithData() {

        Order order =
                Order.builder()

                        .id(1L)
                        .state(OrderState.Payed)
                        .orderItems(Arrays.asList(OrderItem.builder()
                                .orderItemAdditives(Arrays.asList(
                                        OrderItemAdditive.builder()
                                                .additive(Additive.builder().build())
                                                .orderItem(OrderItem.builder().build())
                                                .state(AdditiveSate.Added)
                                                .timestamp(new Date())
                                                .build()
                                ))
                                .order(Order.builder().build())
                                .productName("sd")
                                .product(Product.builder().build())
                                .build()))
                        .deliveryman(Deliveryman.builder().build())
                        .waiter(Waiter.builder().id(1L).build())
                        .table(Table.builder().number(2).build())
                        .elapsedTime(LocalTime.now())
                        .elapsedTime(LocalTime.now())
                        .total(20)
                        .newTotal(20)
                        .discountAmount(0)
                        .build();


        when(orderRepository.saveOrder(Mockito.any(Order.class))).thenReturn(order);

        var res = orderController.saveOrder(orderMapper.toOrderDto(order));
        assertEquals(res.getStatusCode(), HttpStatus.CREATED);


    }


    @Test
    public void orderController_Save_WithoutData() {

        var order = Order.builder()
                .orderItems(Arrays.asList())
                .table(Table.builder().build())
                .build();

        when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);
        var res = orderController.saveOrder(orderMapper.toOrderDto(order));

        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }


    @Test
    public void orderController_Save_WithExistOrder() {

        Order order =
                Order.builder()
                        .id(1)
                        .orderItems(Arrays.asList(OrderItem.builder()
                                .orderItemAdditives(Arrays.asList(OrderItemAdditive.builder()
                                        .additive(Additive.builder().build())
                                        .orderItem(OrderItem.builder().build())
                                        .timestamp(new Date())
                                        .state(AdditiveSate.Added).build()))
                                .product(Product.builder().build())
                                .order(Order.builder().id(1).build())
                                .build()
                        ))
                        .table(Table.builder().build())
                        .deliveryman(Deliveryman.builder().build())
                        .table(Table.builder().build())
                        .waiter(Waiter.builder().build())
                        .discountAmount(0)
                        .total(50)
                        .build();


        when(orderRepository.findById(order.getId())).thenReturn(java.util.Optional.of(order));
        var res = orderController.saveOrder(orderMapper.toOrderDto(order));

        assertEquals(res.getStatusCode(), HttpStatus.FOUND);

    }

    @Test
    public void orderController_save_WithNoDBConnection() {

        var order =
                Order.builder()
                        .id(1L)
                        .state(OrderState.Payed)
                        .orderItems(Arrays.asList(OrderItem.builder()
                                .orderItemAdditives(Arrays.asList(OrderItemAdditive.builder()
                                        .additive(Additive.builder().build())
                                        .state(AdditiveSate.Added).orderItem(OrderItem.builder().build()
                                        ).timestamp(new Date()).build()))
                                .product(Product.builder().build())
                                .order(Order.builder().build()).build()))
                        .elapsedTime(LocalTime.now())
                        .orderTime(new Date())
                        .table(Table.builder().build())
                        .deliveryman(Deliveryman.builder().build())
                        .waiter(Waiter.builder().build())
                        .build();


        when(orderRepository.save(Mockito.any(Order.class))).thenThrow(DataAccessResourceFailureException.class);
        var res = orderController.saveOrder(orderMapper.toOrderDto(order));

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  GetById Order Unit Test  <------------------------
     */

    @Test
    public void OrderController_getById_WithNotEmptyOrder() {

        var order =
                Order.builder()
                        .id(1L)
                        .state(OrderState.Payed)
                        .orderItems(Arrays.asList(OrderItem.builder()
                                .orderItemAdditives(Arrays.asList(OrderItemAdditive.builder()
                                        .additive(Additive.builder().build())
                                        .orderItem(OrderItem.builder().build())
                                        .timestamp(new Date()).state(AdditiveSate.Added)
                                        .build()))
                                .id(1)
                                .product(Product.builder().build())
                                .order(Order.builder().build()).build()))
                        .elapsedTime(LocalTime.now())
                        .orderTime(new Date())
                        .table(Table.builder().build())
                        .build();

        when(orderRepository.findByIdOrderWithOrderItems(1L)).thenReturn(order);

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

        when(orderRepository.findById(1l)).thenReturn(java.util.Optional.ofNullable(order));
        orderController.deleteOrder(1);

        verify(orderRepository, times(1)).delete(order);


    }

    @Test
    public void orderController_Delete_WithNotExistOrderId() {

        when(orderRepository.findById(1L)).thenReturn(null);

        orderController.deleteOrder(1);

        verify(orderRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(orderRepository);

    }

    @Test
    public void orderController_Delete_WithNoDBConnection() {

        when(orderRepository.findById(5L))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = orderController.deleteOrder(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Put Order Unit Test  <------------------------
     */

    @Test
    public void orderController_Put_WithData() {
        var orders = Order.builder()
                .id(1L)
                .state(OrderState.Payed)
                .orderItems(Arrays.asList(OrderItem.builder()
                        .id(1)
                        .orderItemAdditives(Arrays.asList(OrderItemAdditive.builder()
                                .additive(Additive.builder().build())
                                .state(AdditiveSate.Added)
                                .timestamp(new Date())
                                .orderItem(OrderItem.builder().build())
                                .build()))
                        .id(2L)
                        .product(Product.builder().build())
                        .order(Order.builder().id(1).build()).build()))
                .elapsedTime(LocalTime.now())
                .orderTime(new Date())
                .table(Table.builder().id(1).build())
                .build();

        when(orderRepository.findById(orders.getId())).thenReturn(java.util.Optional.of(orders));
        ResponseEntity<OrderDto> returned = orderController.editOrder(1, orderMapper.toOrderDto(orders));

        verify(orderRepository, times(1)).findById(orders.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void orderController_Put_WithIdNotExist() {

        var order =
                Order.builder()
                        .state(OrderState.Payed)
                        .orderItems(Arrays.asList(OrderItem.builder()
                                .orderItemAdditives(Arrays.asList(OrderItemAdditive.builder()
                                        .additive(Additive.builder().build())
                                        .state(AdditiveSate.Added)
                                        .orderItem(OrderItem.builder().build())
                                        .timestamp(new Date())
                                        .build()))
                                .product(Product.builder().build())
                                .order(Order.builder().build()).build()))
                        .elapsedTime(LocalTime.now())
                        .orderTime(new Date())
                        .table(Table.builder().build())
                        .build();

        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());
        ResponseEntity<OrderDto> returned = orderController.editOrder(0L, orderMapper.toOrderDto(order));

        verify(orderRepository, times(1)).findById(order.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void orderController_Put_WithNullData() {

        var order = Order.builder()
                .id(1L)
                .table(Table.builder().id(1).build())
                .build();

        when(orderRepository.findById(order.getId())).thenReturn(java.util.Optional.of(order));
        ResponseEntity<OrderDto> returned = orderController.editOrder(1L, orderMapper.toOrderDto(order));

        verify(orderRepository, times(1)).findById(order.getId());
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

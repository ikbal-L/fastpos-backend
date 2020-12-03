package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.mapping.OrderMapper;
import com.softlines.fastpos.dto.service.DtoServiceImpl;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DtoServiceImpl dtoService;
    @Autowired
    OrderMapper orderMapper;

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity<OrderDto> addOrder(@Valid @RequestBody OrderDto orderDto) {

        try {
            Optional<Order> foundOrder = orderRepository.findById(orderDto.getId());

            if (foundOrder.isEmpty()) {

                if (orderDto.getOrderItems().size() > 0) {
                    Order order = dtoService.orderDtoToOrder(orderDto);

                    Order createdOder = orderRepository.save(order);

                    OrderDto createdOderDto = orderMapper.toOrderDto(createdOder);
                    return ResponseEntity.status(HttpStatus.CREATED).body(createdOderDto);
//                    return ResponseEntity.status(HttpStatus.CREATED).body(createdOderDto.getId());


                } else {
                    return ResponseEntity.noContent().build();
                }

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @PostMapping(value = "/savemany", consumes = "application/json")
    public ResponseEntity<List<Long>> addManyOrder(@Valid @RequestBody List<OrderDto> orderDtoList) {

        try {
            List<Long> ids = orderDtoList.parallelStream().map(OrderDto::getId).collect(Collectors.toList());
            List<Order> foundOrder = orderRepository.findAllById(ids);


            if (foundOrder.size() == 0) {

                List<Order> order = dtoService.orderDtoListToOrderList(orderDtoList);

                List<Order> createdOder = orderRepository.saveAll(order);
                List<Long> savedIds = createdOder.parallelStream()
                        .map(Order::getId).collect(Collectors.toList());

                return ResponseEntity.status(HttpStatus.CREATED).body(savedIds);

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


    @GetMapping(value = {"/getall", "/getall/{filterByState}"},produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDto>> getOrders(@PathVariable Optional<String> filterByState) {

        try {

            List<Order> orders = orderRepository.findAllOrdersWithOrderItems();

            if (orders == null || orders.isEmpty())
                return ResponseEntity.noContent().build();
            else
                if (filterByState.isPresent() && filterByState.get().equals("unprocessed")){
                    List<OrderState> filteredStates = Arrays.asList(OrderState.Payed,OrderState.Removed,OrderState.Canceled);
                    orders.removeIf(order -> filteredStates.contains(order.getState()));
                }

                return ResponseEntity.ok().body(orderMapper.toOrderDTOs(orders));

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


    @GetMapping("/getmany")
    public ResponseEntity<List<OrderDto>> getManyOrders(@Valid @RequestBody List<Long> ids) {

        try {

            List<Order> orders = orderRepository.findManyOrderWithOrderItems(ids);

            if (orders == null || orders.isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok().body(orderMapper.toOrderDTOs(orders));

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<OrderDto> getOrder(@Valid @PathVariable long id) {
        try {

            Order order = orderRepository.findByIdOrderWithOrderItems(id);

            if (order != null && id != 0)
                return ResponseEntity.ok().body(orderMapper.toOrderDto(order));
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<OrderDto> editOrder(@Valid @PathVariable long id, @Valid @RequestBody OrderDto orderDto) {

        try {
            Optional<Order> optionalOrder = orderRepository.findById(id);


            if (optionalOrder.isPresent() && id != 0 && orderDto.getOrderItems() != null && orderDto.getOrderItems().size() > 0) {

                Order order = dtoService.orderDtoToOrder(orderDto);
                return ResponseEntity.ok().body(orderMapper.toOrderDto(orderRepository.save(order)));

            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteOrder(@Valid @PathVariable long id) {

        try {

            Optional<Order> optionalOrder = orderRepository.findById(id);

            if (optionalOrder.isPresent()) {

                orderRepository.delete(optionalOrder.get());
                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


}

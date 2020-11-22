package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.mapping.OrderMapper;
import com.softlines.fastpos.dto.service.DtoServiceImpl;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderDto orderDto) {

        try {
            Optional<Order> optionalOrder = orderRepository.findById(orderDto.getId());

            if (!optionalOrder.isPresent()) {

//                orderDto.getOrderItems().get(0).getName() != null &&
                if ( orderDto.getOrderItems().size() > 0) {
                    Order order = dtoService.orderDtoToOrder(orderDto);

                    return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toOrderDto(orderRepository.save(order)));
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

    @GetMapping("/getall")
    public ResponseEntity<List<OrderDto>> getOrders() {

        try {

            List<Order> orders = orderRepository.findAll();

            if (orders == null || orders.isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok().body(orderMapper.toOrderDTOs(orders));

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable long id) {

        try {
            Optional<Order> optionalOrder = orderRepository.findById(id);
            if (optionalOrder.isPresent() && id != 0)
                return ResponseEntity.ok().body(orderMapper.toOrderDto(optionalOrder.get()));
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @PutMapping("/put/{id}")
    public ResponseEntity<OrderDto> editOrder(@PathVariable long id, @RequestBody OrderDto orderDto) {

        try {
            Optional<Order> optionalOrder = orderRepository.findById(id);

            Order existingOrder = null;
            if (optionalOrder.isPresent())
                existingOrder = optionalOrder.get();

            if (!optionalOrder.isPresent() && id != 0 && orderDto.getOrderItems() != null && orderDto.getOrderItems().size() > 0) {

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
    public ResponseEntity deleteOrder(@PathVariable long id) {

        try {

            Optional<Order> optionalOrder = orderRepository.findById(id);

            if (optionalOrder.isPresent()) {

                orderRepository.delete(optionalOrder.get());
                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(orderMapper.toOrderDto(optionalOrder.get()));
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }





}

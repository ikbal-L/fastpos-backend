package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.mapping.OrderMapper;
import com.softlines.fastpos.dto.service.DtoServiceImpl;
import com.softlines.fastpos.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DtoServiceImpl dtoService;
    @Autowired
    OrderMapper orderMapper;

    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderDto orderDto) {

        try {
            Optional<Order> optionalOrder = orderRepository.findById(orderDto.getId());

            if (!optionalOrder.isPresent()) {
                Order order = dtoService.orderDtoToOrder(orderDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toOrderDto(orderRepository.save(order)));
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @GetMapping("/getall")
    public ResponseEntity<List<OrderDto>> getOrders() {

        try {
            List<Order> orders = orderRepository.findAll();

            if (orders != null)
                return ResponseEntity.ok().body(orderMapper.toOrderItemDTOs(orders));
            else
                return ResponseEntity.notFound().build();

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable long id) {

        try {
            Optional<Order> optionalOrder = orderRepository.findById(id);
            if (optionalOrder.isPresent() && optionalOrder.get() != null)
                return ResponseEntity.ok().body(orderMapper.toOrderDto(optionalOrder.get()));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception exception) {
            throw new ResponseStatusException (HttpStatus.NOT_FOUND, "Not Found", exception);
        }

    }

    @PutMapping("/put/{id}")
    public ResponseEntity<OrderDto> editOrder(@PathVariable long id, @RequestBody OrderDto orderDto) {

        try {
            Optional<Order> optionalOrder = orderRepository.findById(id);

            Order existingOrder = null;
            if (optionalOrder.isPresent())
                existingOrder = optionalOrder.get();

            if (existingOrder != null) {

                Order order = dtoService.orderDtoToOrder(orderDto);

                return ResponseEntity.ok().body(orderMapper.toOrderDto(orderRepository.save(order)));

            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException (HttpStatus.NOT_FOUND, " Not Found", exception);
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

}

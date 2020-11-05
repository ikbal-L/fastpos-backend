package com.softlines.fastpos.controller;


import com.softlines.fastpos.domain.OrderItem;
import com.softlines.fastpos.dto.OrderItemDto;
import com.softlines.fastpos.dto.mapping.OrderItemMapper;
import com.softlines.fastpos.dto.service.DtoServiceImpl;
import com.softlines.fastpos.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orderitem")
public class OrderItemController {

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private DtoServiceImpl dtoService;
    @Autowired
    OrderItemMapper orderItemMapper;

    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity addOrderItem(@RequestBody OrderItemDto orderItemDto) {
        try {

            Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(orderItemDto.getId());
            if (!optionalOrderItem.isPresent()) {

                OrderItem orderItem = dtoService.orderItemDtoToOrderItem(orderItemDto,false);
                return ResponseEntity.status(HttpStatus.CREATED).body(orderItemMapper.toOrderItemDto(orderItemRepository.save(orderItem)));

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<OrderItemDto>> getOrderItems() {
        try {
            List<OrderItem> orderItems = orderItemRepository.findAll();
            if (orderItems != null)
                return ResponseEntity.ok().body(orderItemMapper.toOrderItemDTOs(orderItems));
            else
                return ResponseEntity.notFound().build();

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<OrderItemDto> getOrderItem(@PathVariable long id) {

        try {
            Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(id);
            if (optionalOrderItem.isPresent() && optionalOrderItem.get() != null)
                return ResponseEntity.ok().body(orderItemMapper.toOrderItemDto(optionalOrderItem.get()));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @GetMapping("/getByname/{name}")
    public ResponseEntity<List<OrderItemDto>> getOrderItemByName(@PathVariable String name) {
        try {

            List<OrderItem> orderItem = orderItemRepository.findByName(name);
            if (orderItem != null) {

                return ResponseEntity.ok().body(orderItemMapper.toOrderItemDTOs(orderItem));

            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<OrderItemDto> editOrderItem(@PathVariable long id, @RequestBody OrderItemDto orderItemDto) {
        try {
            OrderItem existingOrderItem = orderItemRepository.findById(id).get();

            if (existingOrderItem != null) {
                existingOrderItem.setName(orderItemDto.getName());

                return ResponseEntity.ok().body(orderItemMapper.toOrderItemDto(orderItemRepository.save(existingOrderItem)));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(orderItemDto);
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteOrderItem(@PathVariable long id) {

        try {
            Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(id);

            if (optionalOrderItem.isPresent()) {
                orderItemRepository.delete(optionalOrderItem.get());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }
}



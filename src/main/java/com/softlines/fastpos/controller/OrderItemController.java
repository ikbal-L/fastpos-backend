package com.softlines.fastpos.controller;


import com.softlines.fastpos.domain.OrderItem;
import com.softlines.fastpos.dto.OrderItemDto;
import com.softlines.fastpos.dto.mapping.OrderItemMapper;
import com.softlines.fastpos.dto.service.DtoServiceImpl;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.OrderItemRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/orderitem", produces = "application/json")
public class OrderItemController {

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private DtoServiceImpl dtoService;
    @Autowired
    OrderItemMapper orderItemMapper;

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity<Long> addOrderItem(@Valid @RequestBody Data data, OrderItemDto orderItemDto) {
        try {

            Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(orderItemDto.getId());
            if (!optionalOrderItem.isPresent()) {

                OrderItem orderItem = dtoService.orderItemDtoToOrderItem(orderItemDto, false);
                OrderItem savedOrderItem = orderItemRepository.save(orderItem);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedOrderItem.getId());

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
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
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<OrderItemDto> getOrderItem(@Valid @PathVariable long id) {

        try {
            Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(id);
            if (optionalOrderItem.isPresent() && optionalOrderItem.get() != null)
                return ResponseEntity.ok().body(orderItemMapper.toOrderItemDto(optionalOrderItem.get()));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


    @PutMapping("/put/{id}")
    public ResponseEntity<OrderItemDto> editOrderItem(@PathVariable long id,@Valid @RequestBody OrderItemDto orderItemDto) {
        try {
            OrderItem existingOrderItem = orderItemRepository.findById(id).get();

            if (existingOrderItem != null) {

                return ResponseEntity.ok().body(orderItemMapper.toOrderItemDto(orderItemRepository.save(existingOrderItem)));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(orderItemDto);
            }
        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteOrderItem(@Valid @PathVariable long id) {

        try {
            Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(id);

            if (optionalOrderItem.isPresent()) {
                orderItemRepository.delete(optionalOrderItem.get());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }
}



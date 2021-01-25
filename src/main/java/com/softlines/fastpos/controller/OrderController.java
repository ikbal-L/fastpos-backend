package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.PageList;
import com.softlines.fastpos.dto.mapping.OrderMapper;
import com.softlines.fastpos.dto.service.DtoServiceImpl;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.OrderItemAdditiveRepository;
import com.softlines.fastpos.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/order", produces = "application/json")
public class OrderController {

    @Autowired
    OrderItemAdditiveRepository orderItemAdditiveRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DtoServiceImpl dtoService;

    @Autowired
    OrderMapper orderMapper;

    ExceptionManagement exceptionManagement = new ExceptionManagement();


    @PostMapping(value = "/save", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> saveOrder(@Valid @RequestBody OrderDto orderDto) {

        try {

            if (orderDto.getId() == 0) {
                Order order = dtoService.orderDtoToOrder(orderDto);

                Order createdOder = orderRepository.saveOrder(order);

                OrderDto createdOderDto = orderMapper.toOrderDto(createdOder);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdOderDto);


            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @PostMapping(value = "/savemany")
    public ResponseEntity<List<OrderDto>> saveManyOrder(@Valid @RequestBody List<OrderDto> orderDtoList) {

        try {
            List<Long> ids = orderDtoList.parallelStream().map(OrderDto::getId).collect(Collectors.toList());
            List<Order> foundOrder = orderRepository.findAllById(ids);


            if (foundOrder.size() == 0) {

                List<Order> order = dtoService.orderDtoListToOrderList(orderDtoList);

                List<Order> ListCreatedOder = orderRepository.saveListOrder(order);

//                List<Long> savedIds = ListCreatedOder.parallelStream()
//                        .map(Order::getId).collect(Collectors.toList());

                List<OrderDto> orderDtos = orderMapper.toOrderDTOs(ListCreatedOder);

                return ResponseEntity.status(HttpStatus.CREATED).body(orderDtos);

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @PutMapping(value = "/updatemany")
    public ResponseEntity<List<OrderDto>> updateManyOrder(@Valid @RequestBody List<OrderDto> orderDtoList) {

        try {
            List<Long> ids = orderDtoList.parallelStream().map(OrderDto::getId).collect(Collectors.toList());
            List<Order> foundOrder = orderRepository.findAllById(ids);


            if (foundOrder.size() == orderDtoList.size()) {

                List<Order> order = dtoService.orderDtoListToOrderList(orderDtoList);

                List<Order> ListCreatedOder = orderRepository.saveListOrder(order);

//                List<Long> savedIds = ListCreatedOder.parallelStream()
//                        .map(Order::getId).collect(Collectors.toList());

                List<OrderDto> orderDtos = orderMapper.toOrderDTOs(ListCreatedOder);

                return ResponseEntity.status(HttpStatus.CREATED).body(orderDtos);

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping(value = {"/getall", "/getall/{filterByState}"})
    public ResponseEntity<List<OrderDto>> getOrders(@PathVariable Optional<String> filterByState) {

        try {

            List<Order> orders = orderRepository.getAllOrder();

            if (orders == null || orders.isEmpty())
                return ResponseEntity.noContent().build();
            else if (filterByState.isPresent() && filterByState.get().equals("unprocessed")) {
                List<OrderState> filteredStates = Arrays.asList(OrderState.Payed, OrderState.Removed, OrderState.Canceled);
                orders.removeIf(order -> filteredStates.contains(order.getState()));
            }
            var orderDtos = orderMapper.toOrderDTOs(orders);
            return ResponseEntity.ok().body(orderDtos);

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

            Order order = orderRepository.getOrder(id);

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
            var exists = orderRepository.existsById(id);

            if (exists && id != 0 && orderDto.getOrderItems() != null && orderDto.getOrderItems().size() > 0) {

                Order order = dtoService.orderDtoToOrder(orderDto);

                Order createdOrder = orderRepository.saveOrder(order);

                return ResponseEntity.ok().body(orderMapper.toOrderDto(createdOrder));

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
    @PostMapping("/getByStatePage/{pageNumber}/{pageSize}/{deliverymanId}")
    public ResponseEntity<PageList<OrderDto>> getByStatesPage(@PathVariable int pageNumber, @PathVariable int pageSize, @PathVariable  long deliverymanId, @RequestBody  OrderState[] states){
        var orders= orderRepository.getByStates(states,deliverymanId,pageNumber,pageSize);
        if (!orders.getValue1().isEmpty()){
            return  ResponseEntity.ok().body(new PageList<>(orderMapper.toOrderDTOs(orders.getValue1()),orders.getValue0()));
        }
        return  ResponseEntity.noContent().build();
    }
    @GetMapping("/getAllbydeliverymanPage/{pageNumber}/{pageSize}/{deliverymanId}")
    public ResponseEntity<PageList<OrderDto>> getAllByDeliveryManPage(@PathVariable int pageNumber, @PathVariable int pageSize, @PathVariable  long deliverymanId){
        var orders= orderRepository.getAllByDeliveryManPage(pageNumber,pageSize,deliverymanId);
        if (!orders.getValue1().isEmpty()){
            return  ResponseEntity.ok().body(new PageList<>(orderMapper.toOrderDTOs(orders.getValue1()),orders.getValue0()));
        }
        return  ResponseEntity.noContent().build();
    }
}

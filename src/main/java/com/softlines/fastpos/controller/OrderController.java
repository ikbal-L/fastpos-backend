package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.filters.OrderFilter;
import com.softlines.fastpos.dto.PageList;
import com.softlines.fastpos.dto.SyncData;
import com.softlines.fastpos.dto.mapping.OrderMapper;
import com.softlines.fastpos.dto.service.DtoServiceImpl;
import com.softlines.fastpos.dto.service.filtering.OrderFilterService;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.OrderItemAdditiveRepository;
import com.softlines.fastpos.repository.OrderRepository;
import com.softlines.fastpos.service.OrderService;
import com.softlines.fastpos.sse.model.EventDto;
import com.softlines.fastpos.sse.model.SSEventType;
import com.softlines.fastpos.sse.service.SseNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.validation.Valid;
import java.util.*;
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

    @Autowired
    OrderFilterService orderFilterService;

    @Autowired
    SseNotificationService sseNotificationService;

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @Autowired
    private EntityManagerFactory entityManagerFactory;


    @PostMapping(value = "/save", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> saveOrder(@Valid @RequestBody OrderDto orderDto, @RequestHeader(name = "Authorization") String token) {

        try {

            if (orderDto.getId() == 0) {
                Order order = dtoService.orderDtoToOrder(orderDto);

                Order createdOder = orderRepository.saveOrder(order);

                OrderDto createdOderDto = orderMapper.toOrderDto(createdOder);

                var eventDto = EventDto.builder().type(SSEventType.CREATE_ORDER).body(createdOderDto).build();
//                sseNotificationService.sendNotificationForAll(eventDto, token);


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



                List<OrderDto> orderDtos = orderMapper.toOrderDTOs(ListCreatedOder);

                return ResponseEntity.status(HttpStatus.CREATED).body(orderDtos);

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @PostMapping(value = {"/getallbycriteria"})
    ResponseEntity<List<OrderDto>> getOrdersByCriteria(@RequestBody OrderFilter filter){
        try {

            TypedQuery<Order> query = orderFilterService.buildQuery(filter);

            var orders= query.getResultList();
            var orderDtos = orderMapper.toOrderDTOs(orders);
            return  ResponseEntity.ok(orderDtos);
        }catch (Exception e){

           return exceptionManagement.getResponseEntityAccordingToException(e);
        }

    }
    @GetMapping(value = {"/getall","/getall/{filterByState}"})
    public ResponseEntity<List<OrderDto>> getOrders(@PathVariable Optional<String> filterByState) {

        try {

            List<Order> orders;



            if (filterByState.isPresent() ) {
                var state = OrderState.valueOf( StringUtils.capitalize(filterByState.get()));

                if (state.equals(OrderState.Unprocessed)){
                    orders = orderRepository.findAllUnprocessedOrders();
                }else{

                    orders = orderRepository.findAllByState(state);
                }

            }
            else {
                orders = orderRepository.getAllOrder();
            }
            if (orders == null || orders.isEmpty())
                return ResponseEntity.noContent().build();

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
    public ResponseEntity<OrderDto> editOrder(@Valid @PathVariable long id, @Valid @RequestBody OrderDto orderDto, @RequestHeader(name = "Authorization") String token) {

        try {
            var persisted = orderRepository.findById(id);

            if (persisted.isPresent() && id != 0 /*&& orderDto.getOrderItems() != null && orderDto.getOrderItems().size() > 0*/) {

                Order order = dtoService.orderDtoToOrder(orderDto);

                Order createdOrder = orderRepository.saveOrder(order);


                var updatedOderDto = orderMapper.toOrderDto(createdOrder);
                String eventType = "";
                Object eventBody;
                if (OrderService.IsActionPayment(persisted.get(), order)) {
                    eventType = SSEventType.PAY_ORDER;
                    eventBody = createdOrder.getId();
                } else if (OrderService.IsActionCancel(persisted.get(), order)) {
                    eventType = SSEventType.CANCEL_ORDER;
                    eventBody = createdOrder.getId();
                } else {
                    eventType = SSEventType.UPDATE_ORDER;
                    eventBody = updatedOderDto;
                }
                var eventDto = EventDto.builder().type(eventType).body(eventBody).build();
                sseNotificationService.sendNotificationForAll(eventDto, token);

                return ResponseEntity.ok().body(updatedOderDto);

            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


    @PutMapping("/lock/{id}")
    public ResponseEntity<OrderDto> lockOrder(@Valid @PathVariable long id, @RequestBody boolean lockState, @RequestHeader(name = "Authorization") String token) {

        try {
            var persisted = orderRepository.findById(id);

            if (persisted.isPresent() && id != 0 /*&& orderDto.getOrderItems() != null && orderDto.getOrderItems().size() > 0*/) {

                persisted.get().setLocked(lockState);

                Order updatedOrder = orderRepository.saveOrder(persisted.get());


                var body = SyncData
                        .builder()
                        .type(Order.class.getSimpleName())
                        .id(persisted.get().getId())
                        .isLocked(lockState)
                        .lockedBy(lockState?updatedOrder.getModificationSessionId():"")
                        .build();

                var eventDto = EventDto.builder().type(SSEventType.LOCK_ORDER).body(body).build();
                sseNotificationService.sendNotificationForAll(eventDto, token);

                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteOrder(@Valid @PathVariable long id, @RequestHeader(name = "Authorization") String token) {

        try {

            Optional<Order> optionalOrder = orderRepository.findById(id);

            if (optionalOrder.isPresent()) {

                orderRepository.delete(optionalOrder.get());

                var eventDto = EventDto.builder().type(SSEventType.DELETE_ORDER).body(id).build();
                sseNotificationService.sendNotificationForAll(eventDto, token);

                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @PostMapping("/getByState/{deliverymanId}")
    public ResponseEntity<List<OrderDto>> getByStates(@PathVariable long deliverymanId, @RequestBody OrderState[] states) {
        var orders = orderRepository.getByStates(states, deliverymanId, true);
        if (!orders.isEmpty()) {
            return ResponseEntity.ok().body(orderMapper.toOrderDTOs(orders));
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/getAllbyDeliveryManAndStatePage/{pageNumber}/{pageSize}/{deliverymanId}")
    public ResponseEntity<PageList<OrderDto>> getAllByDeliveryManAndStatePage(@PathVariable int pageNumber, @PathVariable int pageSize, @PathVariable long deliverymanId, @RequestBody OrderState[] states) {
        var orders = orderRepository.getAllByDeliveryManAndStatePage(pageNumber, pageSize, deliverymanId, states);
        if (!orders.getValue1().isEmpty()) {
            return ResponseEntity.ok().body(new PageList<>(orderMapper.toOrderDTOs(orders.getValue1()), orders.getValue0()));
        }
        return ResponseEntity.noContent().build();
    }
}

package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.PageList;
import com.softlines.fastpos.dto.filters.OrderFilter;
import com.softlines.fastpos.dto.filters.Page;
import com.softlines.fastpos.dto.mapping.OrderMapper;
import com.softlines.fastpos.dto.service.DtoServiceImpl;
import com.softlines.fastpos.dto.service.filtering.OrderFilterService;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.AdditiveRepository;
import com.softlines.fastpos.repository.OrderItemAdditiveRepository;
import com.softlines.fastpos.repository.OrderRepository;
import com.softlines.fastpos.security.securityservice.SessionService;
import com.softlines.fastpos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import com.softlines.fastpos.sse.service.SseNotificationService;

@RestController
@RequestMapping(value = "/api/order", produces = "application/json; charset=UTF-8")
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


    ExceptionManagement exceptionManagement = new ExceptionManagement();
    @Autowired
    SessionService sessionService;

    @Autowired
    OrderService orderService;


    @Autowired
    AdditiveRepository additiveRepository;


    @PostMapping(value = "/save")
    public ResponseEntity<OrderDto> saveOrder(@Valid @RequestBody OrderDto dto) {

        if (dto.getId() == 0) {
            Order order = dtoService.orderDtoToOrder(dto);
            dto = orderService.saveOrder(order);

            return ResponseEntity.status(HttpStatus.CREATED).body(dto);

        } else {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

    }


    @PostMapping(value = "/savemany")
    public ResponseEntity<List<OrderDto>> saveManyOrder(@Valid @RequestBody List<OrderDto> orderDtoList) {

        List<Long> ids = orderDtoList.parallelStream().map(OrderDto::getId).collect(Collectors.toList());
        List<Order> foundOrder = orderRepository.findAllById(ids);


        if (foundOrder.size() == 0) {

            List<Order> order = dtoService.orderDtoListToOrderList(orderDtoList);

            List<Order> ListCreatedOder = orderRepository.saveListOrder(order);


            List<OrderDto> orderDtos = orderMapper.toOrderDTOs(ListCreatedOder);

            return ResponseEntity.status(HttpStatus.CREATED).body(orderDtos);

        } else {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }


    }

    @PutMapping(value = "/updatemany")
    public ResponseEntity<List<OrderDto>> updateManyOrder(@Valid @RequestBody List<OrderDto> orderDtoList) {

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

    }

    @PostMapping(value = {"/getallbycriteria"})
    ResponseEntity<Page<OrderDto>> getOrdersByCriteria(@RequestBody OrderFilter filter) throws ParseException {
        var orderPage = orderFilterService.buildQuery(filter);
        var orderDtoPage = orderPage.mapToPage(c -> orderMapper.toOrderDTOs(c));
        return ResponseEntity.ok(orderDtoPage);
    }

    @GetMapping(value = {"/getall", "/getall/{filterByState}"})
    public ResponseEntity<List<OrderDto>> getOrders(@PathVariable Optional<String> filterByState) {


        List<Order> orders;


        if (filterByState.isPresent()) {
            var state = OrderState.valueOf(StringUtils.capitalize(filterByState.get()));

            if (state.equals(OrderState.Unprocessed)) {
                orders = orderRepository.findAllUnprocessedOrders();
            } else {

                orders = orderRepository.findAllByState(state);
            }

        } else {
            orders = orderRepository.getAllOrder();
        }
        if (orders == null || orders.isEmpty())
            return ResponseEntity.noContent().build();

        var orderDtos = orderMapper.toOrderDTOs(orders);
        return ResponseEntity.ok().body(orderDtos);

    }


    @GetMapping("/getmany")
    public ResponseEntity<List<OrderDto>> getManyOrders(@Valid @RequestBody List<Long> ids) {

        List<Order> orders = orderRepository.findManyOrderWithOrderItems(ids);

        if (orders == null || orders.isEmpty())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(orderMapper.toOrderDTOs(orders));

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<OrderDto> getOrder(@Valid @PathVariable long id) {
        Order order = orderRepository.getOrder(id);

        if (order != null && id != 0)

            return ResponseEntity.ok().body(orderMapper.toOrderDto(order));

        else
            return ResponseEntity.noContent().build();
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<OrderDto> editOrder(@Valid @PathVariable long id, @Valid @RequestBody OrderDto orderDto, @RequestHeader(name = "Authorization") String token) {

        if (orderRepository.existsById(orderDto.getId())) {

            Order order = dtoService.orderDtoToOrder(orderDto);

            var dto = orderService.updateOrder(order);


            return ResponseEntity.ok().body(dto);

        } else {
            return ResponseEntity.noContent().build();
        }

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteOrder(@Valid @PathVariable long id, @RequestHeader(name = "Authorization") String token) {

        Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isPresent()) {

            if (optionalOrder.get().getState() == OrderState.Temporary) {
                orderService.deleteTempOrder(id);
            } else {
                orderRepository.delete(optionalOrder.get());

            }


            return ResponseEntity.ok().build();

        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

    }

    @PostMapping("/getByState/{deliverymanId}")
    public ResponseEntity<List<OrderDto>> getByStates(@PathVariable long deliverymanId, @RequestBody OrderState[] states) {
        var orders = orderRepository.getByStates(states, deliverymanId, "deliveryman", null, true);
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


    @PostMapping(value = "/split/{id}")
    public ResponseEntity<OrderDto> splitOrder(@PathVariable long id, @Valid @RequestBody OrderDto subOrderDto) {

        var originalOrder = orderRepository.findByIdOrderWithOrderItems(id);
        if (originalOrder == null) return ResponseEntity.notFound().build();
        Order subOrder = dtoService.orderDtoToOrder(subOrderDto);
        var originalOrderDto = orderService.splitOrderFrom(subOrder, originalOrder);
        return ResponseEntity.status(HttpStatus.OK).body(originalOrderDto);

    }


    @PostMapping(value = "/pay/{id}")
    public ResponseEntity<OrderDto> payOrder( @Valid @RequestBody OrderDto dto) {

        if (!orderRepository.existsById(dto.getId())) return ResponseEntity.notFound().build();
        Order order = dtoService.orderDtoToOrder(dto);
        dto = orderService.payOrder(order);
        return ResponseEntity.status(HttpStatus.OK).body(dto);

    }

    @PostMapping(value = "/refund/{id}")
    public ResponseEntity<OrderDto> refundOrder(@Valid @RequestBody OrderDto dto) {

        if (!orderRepository.existsById(dto.getId())) return ResponseEntity.notFound().build();
        Order order = dtoService.orderDtoToOrder(dto);
        dto = orderService.refundOder(order);
        return ResponseEntity.status(HttpStatus.OK).body(dto);

    }

    @PostMapping(value = "/refund/partial/{id}")
    public ResponseEntity<OrderDto> partiallyRefundOrder(@Valid @RequestBody OrderDto dto) {

        if (!orderRepository.existsById(dto.getId())) return ResponseEntity.notFound().build();
        Order order = dtoService.orderDtoToOrder(dto);
        dto = orderService.partiallyRefundOrder(order);
        return ResponseEntity.status(HttpStatus.OK).body(dto);

    }
}

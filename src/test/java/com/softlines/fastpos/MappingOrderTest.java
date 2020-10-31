package com.softlines.fastpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.domain.OrderType;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.OrderItemDto;
import com.softlines.fastpos.dto.mapping.OrderMapper;
import com.softlines.fastpos.dto.service.DtoService;
import com.softlines.fastpos.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class MappingOrderTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    HttpServletResponse response;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderMapper orderMapper;

    OrderItemDto orderItemDto = new OrderItemDto();

    @Autowired
    DtoService dtoService;
    OrderDto orderDto = new OrderDto();
    List<Long> listAdditives = new ArrayList();

    @Test
    public void getorders() throws Exception {

        var orders = orderRepository.findAll();
        mvc.perform(get("/order/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[1].idAdditives[0]").value(orderMapper.toOrderDTOs(orders).get(1)))
                .andExpect(status().isOk());

    }

    @Test
    public void getorder() throws Exception {

       var order= orderRepository.findById((long) 1).get();
        mvc.perform(get("/order/get/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("name").value(orderMapper.toOrderDto(order)))
                .andExpect(status().isOk());

    }

    // TODO Recursive problems when add order in test

//    @Test
//    public void addOrder() throws Exception {
//
//        List<OrderItemDto> orderItemsDto = new ArrayList();
//        orderItemDto.setName("test add");
//        orderItemDto.setIdAdditives(null);
//        orderItemDto.setProductId(1);
//        orderItemsDto.add(orderItemDto);
//        //////////////////////////////////////////////////
//        orderDto.setBuyerId("4");
//        orderDto.setAdditivesVisibility(true);
//        orderDto.setDiscountAmount(62);
//        orderDto.setGivenAmount(54);
//        orderDto.setDiscountPercentage(20);
//        orderDto.setElapsedTime(Duration.ZERO);
//        orderDto.setNewTotal(100);
//        orderDto.setOrderTime(LocalDateTime.now());
//        orderDto.setSplittedFromId(1);
//        orderDto.setReturnedAmount(30);
//        orderDto.setTableId(1);
//        orderDto.setType(OrderType.Delivery);
//        orderDto.setTotal(304);
//        orderDto.setProductsVisibility(false);
//        orderDto.setTotalDiscountAmount(350);
//        orderDto.setOrderstate(OrderState.Payed);
//
//        orderDto.setOrderItems(orderItemsDto);
//
//
//        Order order = dtoService.orderDtoToOrder(orderDto);
//
//        mvc.perform(post("/order/save")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(order)))
//                .andDo(print())
//                .andExpect(jsonPath("buyerId", is("4")))
//                .andExpect(status().isCreated());
//
//    }

    @Test
    public void getOrderWithIdNotExist() throws Exception {
        mvc.perform(get("/order/get/{id}", 10)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

//    @Test
//    public void putorders() throws Exception {
//
////        order.setId(2);
////        order.setAvailableStock(0);
////        order.setBackgroundString("red");
////        order.setDescription("desc");
////        order.setMuchInDemand(true);
////        order.setName("pro put");
////        order.setRank(4);
////        order.setPlatter(true);
////        order.setPrice(30);
////        order.setType("sad");
////        order.setUnit("U");
////        listAdditives.add((long) 2);
////        listAdditives.add((long) 1);
////        order.setCategoryId(1);
////        order.setIdAdditives(listAdditives);
//
//        mvc.perform(put("/order/put/{id}", "2")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(orderDto)))
//                .andDo(print())
//                .andExpect(jsonPath("name", is("pro put")))
//                .andExpect(status().isOk());
//
//    }

    @Test
    public void deleteorders() throws Exception {

        mvc.perform(delete("/order/delete/{id}", "19")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

    }

    public String asJsonString(final Object obj) {

        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}

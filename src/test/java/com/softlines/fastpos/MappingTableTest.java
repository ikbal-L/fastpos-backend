package com.softlines.fastpos;

import static org.hamcrest.CoreMatchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.dto.OrderItemDto;
import com.softlines.fastpos.dto.TableDto;
import com.softlines.fastpos.dto.mapping.TableMapper;
import com.softlines.fastpos.dto.service.DtoService;
import com.softlines.fastpos.repository.TableRepository;
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

import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class MappingTableTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    HttpServletResponse response;

    @Autowired
    TableRepository tableRepository;
    @Autowired
    TableMapper tableMapper;

    OrderItemDto tableItemDto = new OrderItemDto();

    @Autowired
    DtoService dtoService;
    TableDto tableDto = new TableDto();
    List<Long> listAdditives = new ArrayList();

    @Test
    public void getTables() throws Exception {

        var table = tableRepository.findAll();
        mvc.perform(get("/table/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].number").value(tableMapper.toTableDTOs(table).get(0).getNumber()))
                .andExpect(status().isOk());

    }

    @Test
    public void getTable() throws Exception {

        var table = tableRepository.findById((long) 1).get();
        mvc.perform(get("/table/get/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("number").value(tableMapper.toTableDto(table).getNumber()))
                .andExpect(status().isOk());

    }


    @Test
    public void addOrder() throws Exception {

        tableDto.setNumber(1);
        tableDto.setVirtual(true);
        tableDto.setSeats(8);
        //////////////////////////////////////////////////


        mvc.perform(post("/table/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tableDto)))
                .andDo(print())
                .andExpect(jsonPath("number", is("1")))
                .andExpect(status().isCreated());

    }

    @Test
    public void getTableWithIdNotExist() throws Exception {
        mvc.perform(get("/table/get/{id}", 10)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void putTables() throws Exception {


        tableDto.setId(2);
        tableDto.setNumber(3);
        tableDto.setVirtual(true);
        tableDto.setSeats(6);

        mvc.perform(put("/table/put/{id}", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tableDto)))
                .andDo(print())
                .andExpect(jsonPath("number", is(3)))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteTables() throws Exception {

        mvc.perform(delete("/table/delete/{id}", "3")
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

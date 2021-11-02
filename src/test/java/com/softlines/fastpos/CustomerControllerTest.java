package com.softlines.fastpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softlines.fastpos.domain.Customer;
import com.softlines.fastpos.repository.CustomerRepository;
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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    HttpServletResponse response;
    @Autowired
    CustomerRepository customerRepository;

    Customer customer = new Customer();

    @Test
    public void getCustomers() throws Exception {

        var customers = customerRepository.findAll();

        mvc.perform(get("/customer/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(equalTo(customers.size()))))
                .andExpect(jsonPath("$[0].name").value(customers.get(0).getName()))
                .andExpect(status().isOk());

    }

    @Test
    public void getCustomer() throws Exception {

        var customer = customerRepository.findById((long)2);

        mvc.perform(get("/customer/get/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("name").value(customer.get().getName()))
                .andExpect(status().isOk());

    }

    @Test
    public void getCustomerWithIdNotExist() throws Exception {
        var customer = customerRepository.findById((long) 100);
        mvc.perform(get("/customer/get/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void addCustomer() throws Exception {

        customer.setName("ahmed");


        mvc.perform(post("/customer/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customer)))
                .andDo(print())
                .andExpect(jsonPath("name", is("ahmed")))
                .andExpect(status().isCreated());

    }


    @Test
    public void putCustomer() throws Exception {

        customer.setId(2);
        customer.setName("customer 2");


        mvc.perform(put("/customer/put/{id}", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customer)))
                .andDo(print())
                .andExpect(jsonPath("name", is("customer 2")))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteCustomer() throws Exception {

        mvc.perform(delete("/customer/delete/{id}", "1")
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

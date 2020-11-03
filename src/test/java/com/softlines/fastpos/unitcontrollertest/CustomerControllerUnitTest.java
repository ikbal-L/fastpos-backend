package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.CustomerController;
import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.Customer;
import com.softlines.fastpos.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)

public class CustomerControllerUnitTest {

    @MockBean
    CustomerRepository customerRepository;

    @Autowired
    CustomerController customerController;

    @Test
    public void customerController_ReturnsNotEmptyAdditivesList() throws Exception {

        var additives = Arrays.asList(
                Customer.builder()
                        .id(1)
                        .name("Harrisa")
                        .build());
        
        Mockito.when(customerRepository.findAll()).thenReturn(additives);

        var res = customerController.getCustomers();
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(((List<Customer>) res.getBody()).get(0).getName(), additives.get(0).getName());
        assertEquals(((List<Customer>) res.getBody()).size(), 1);
          }

//    @Test
//    public void customerController_ReturnsEmptyAdditivesList() throws Exception {
//        var additives = new ArrayList<Additive>();
//        Mockito.when(customerRepository.findAll()).thenReturn(additives);
//        var res = customerController.getAdditives();
//        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
//    }
//
//    @Test
//    public void customerController_ReturnsNullAdditivesList() throws Exception {
//        Mockito.when(customerRepository.findAll()).thenReturn(null);
//
//        var res = customerController.getAdditives();
//        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
//    }
//
//    @Test
//    public void customerController_getAdditivesWithNoDBConnection_Return502() throws Exception {
//        Mockito.when(customerRepository.findAll())
//                .thenThrow(DataAccessResourceFailureException.class);
//
//        var res = customerController.getAdditives();
//
//        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
//    }

}

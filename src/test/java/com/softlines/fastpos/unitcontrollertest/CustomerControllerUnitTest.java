package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.CustomerController;
import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.domain.Customer;
import com.softlines.fastpos.repository.CustomerRepository;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


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
    @Order(1)
    public void customerController_getAll_WithNotEmptyCustomersList() throws Exception {

        var customers = Arrays.asList(
                Customer.builder()
                        .id(1)
                        .name("harrisa")
                        .build());

        when(customerRepository.findAll()).thenReturn(customers);

        var res = customerController.getCustomers();
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).get(0).getName(), customers.get(0).getName());
        assertEquals((res.getBody()).size(), 1);
    }

    @Test
    @Order(2)
    public void customerController_getAll_WithEmptyCustomersList() {

        var customers = new ArrayList<Customer>();
        when(customerRepository.findAll()).thenReturn(customers);
        var res = customerController.getCustomers();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(3)
    public void customerController_getAll_WithNullCustomersList() throws Exception {
        when(customerRepository.findAll()).thenReturn(null);

        var res = customerController.getCustomers();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(4)
    public void customerController_getAll_getCustomersWithNoDBConnection() {
        when(customerRepository.findAll())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = customerController.getCustomers();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }


    /**
     * ------------------>  Save Customer Unit Test  <------------------------
     */


    @Test
    @Order(5)
    public void customerController_Save_WithData() {

        var customer =Customer.builder().name("tacos").build();

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        var res = customerController.addCustomer(customer);

        assertEquals(res.getStatusCode(), HttpStatus.CREATED);
        assertEquals((res.getBody()), customer);

    }

    @Test
    @Order(5)
    public void customerController_Save_WithExistCustomer() {

        var customer =Customer.builder().id(1).name("tacos").build();

        when(customerRepository.findById(customer.getId())).thenReturn(java.util.Optional.of(customer));
        var res = customerController.addCustomer(customer);

        assertEquals(res.getStatusCode(), HttpStatus.FOUND);

    }


    @Test
    public void customerController_Save_WithNullCustomerName() {

        var customer = Customer.builder().build();

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        var res = customerController.addCustomer(customer);

        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }


    @Test
    public void customerController_save_WithNoDBConnection() {

        var customer = Customer.builder()
                        .id(1l)
                        .name("harrisa")
                        .build();

        when(customerRepository.save(any(Customer.class))).thenThrow(DataAccessResourceFailureException.class);
        var res = customerController.addCustomer( customer);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  GetById Customer Unit Test  <------------------------
     */


    @Test
    @Order(9)
    public void customerController_getById_WithNotEmptyCustomer() {

        var customer =
                Customer.builder()
                        .id(1)
                        .name("ahmed")
                        .build();

        when(customerRepository.findById(1l)).thenReturn(java.util.Optional.ofNullable(customer));

        var res = customerController.getCustomer(1);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).getName(), customer.getName());
    }

    @Test
    @Order(10)
    public void customerController_getById_WithEmptyCustomer() {

        var customer = new Customer();

        when(customerRepository.findById(0l)).thenReturn(java.util.Optional.of(customer));
        var res = customerController.getCustomer(0);

        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }


    @Test
    @Order(11)
    public void customerController_getById_WithNullCustomer() {

        var res = customerController.getCustomer(1);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(12)
    public void customerController_getById_getCustomerWithNoDBConnection() {

        when(customerRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = customerController.getCustomer(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


}

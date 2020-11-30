package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.CustomerController;
import com.softlines.fastpos.domain.Customer;
import com.softlines.fastpos.dto.CustomerDto;
import com.softlines.fastpos.dto.mapping.CustomerMapper;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class CustomerControllerUnitTest {

    @MockBean
    CustomerRepository customerRepository;

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerMapper customerMapper;

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
    public void customerController_getAll_WithNoDBConnection() {
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

        var customer = Customer.builder().name("tacos").build();

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        CustomerDto customerDto = customerMapper.toCustomerDto(customer);
        var res = customerController.addCustomer(customerDto);

        assertEquals(res.getStatusCode(), HttpStatus.CREATED);
        assertEquals((res.getBody()), customerMapper.toCustomerDto(customer));

    }

    @Test
    public void customerController_Save_WithExistCustomer() {

        var customer = Customer.builder().id(1).name("tacos").build();
        CustomerDto customerDto = customerMapper.toCustomerDto(customer);

        when(customerRepository.findById(customer.getId())).thenReturn(java.util.Optional.of(customer));
        var res = customerController.addCustomer(customerDto);

        assertEquals(res.getStatusCode(), HttpStatus.FOUND);

    }

    @Test
    public void customerController_save_WithNoDBConnection() {

        var customer = Customer.builder()
                .id(1L)
                .name("harrisa")
                .build();

        CustomerDto customerDto = customerMapper.toCustomerDto(customer);

        when(customerRepository.save(any(Customer.class))).thenThrow(DataAccessResourceFailureException.class);
        var res = customerController.addCustomer(customerDto);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  GetById Customer Unit Test  <------------------------
     */


    @Test
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
    public void customerController_getById_WithEmptyCustomer() {

        var customer = new Customer();

        when(customerRepository.findById(0l)).thenReturn(java.util.Optional.of(customer));
        var res = customerController.getCustomer(0);

        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }


    @Test
    public void customerController_getById_WithNullCustomer() {

        var res = customerController.getCustomer(1);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void customerController_getById_WithNoDBConnection() {

        when(customerRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = customerController.getCustomer(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Delete Customer Unit Test  <------------------------
     */

    @Test
    public void customerController_Delete_WithCustomerId() {

        var customer =
                Customer.builder()
                        .id(1l)
                        .name("name")
                        .build();

        when(customerRepository.findById(1l)).thenReturn(Optional.ofNullable(customer));
        customerController.deleteCustomer(1);

        verify(customerRepository, times(1)).delete(customer);


    }

    @Test
    public void customerController_Delete_WithNotExistCustomerId() {

        when(customerRepository.findById(1l)).thenReturn(null);

        customerController.deleteCustomer(1);

        verify(customerRepository, times(1)).findById(1l);
        verifyNoMoreInteractions(customerRepository);

    }

    @Test
    public void customerController_Delete_WithNoDBConnection() {

        when(customerRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = customerController.getCustomer(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Put Customer Unit Test  <------------------------
     */

    @Test
    public void customerController_Put_WithData() {

        var customer = Customer.builder()
                .id(1l)
                .name("harrisa")
                .build();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        ResponseEntity<CustomerDto> returned = customerController.editCustomer(1, customer);

        verify(customerRepository, times(1)).findById(customer.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void customerController_Put_WithIdNotExist() {

        var customer = Customer.builder()
                .id(10l)
                .name("harrisa")
                .build();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());
        ResponseEntity<CustomerDto> returned = customerController.editCustomer(10, customer);

        verify(customerRepository, times(1)).findById(customer.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void customerController_Put_WithNullData() {

        var customer = Customer.builder().id(1).build();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        ResponseEntity<CustomerDto> returned = customerController.editCustomer(1, customer);

        verify(customerRepository, times(1)).findById(customer.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void customerController_Put_WithNoDBConnection() {

        when(customerRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = customerController.getCustomer(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


}

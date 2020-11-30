package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Customer;
import com.softlines.fastpos.dto.CustomerDto;
import com.softlines.fastpos.dto.mapping.CustomerMapper;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @PostMapping("/save")
    public ResponseEntity<CustomerDto> addCustomer(@Valid @RequestBody CustomerDto customerDto) {
        try {

            Optional<Customer> optionalCustomer = customerRepository.findById(customerDto.getId());

            if (optionalCustomer.isEmpty()) {
                Customer customer = customerMapper.toCustomer(customerDto);
                Customer savedCustomer = customerRepository.save(customer);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(customerMapper.toCustomerDto(savedCustomer));
            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);

        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<CustomerDto>> getCustomers() {

        try {
            List<Customer> customers = customerRepository.findAll();
            if (customers==null || customers.isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok().body(customerMapper.toCustomerDTOs(customers));

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


    @GetMapping("/getmany")
    public ResponseEntity<List<CustomerDto>> getCustomers(@Valid @RequestBody List<Long> ids) {

        try {

            List<Customer> customers = customerRepository.findAllById(ids);

            if (customers == null || customers.isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok().body(customerMapper.toCustomerDTOs(customers));

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@Valid @PathVariable long id) {

        try {

            Optional<Customer> optionalCustomer = customerRepository.findById(id);

            if (optionalCustomer.isPresent() && id != 0)
                return ResponseEntity.ok().body(customerMapper.toCustomerDto(optionalCustomer.get()));
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @PutMapping("/put/{id}")
    public ResponseEntity<CustomerDto> editCustomer(@Valid @PathVariable long id, @RequestBody Customer customer) {

        try {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);

            if (optionalCustomer.isPresent() && customer.getName() != null) {
                Customer savedCustomer = customerRepository.save(customer);
                return ResponseEntity.ok().body(customerMapper.toCustomerDto(savedCustomer));
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCustomer(@Valid @PathVariable long id) {

        try {

            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            if (optionalCustomer.isPresent()) {

                customerRepository.delete(optionalCustomer.get());
                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

}

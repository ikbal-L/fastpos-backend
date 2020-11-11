package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Customer;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/save")
    public ResponseEntity addCustomer(@RequestBody Customer customer) {
        try {

            Optional<Customer> optionalCustomer = customerRepository.findById(customer.getId());

            if (!optionalCustomer.isPresent() ) {
                if (customer.getName() != null &&
                        !customer.getName().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.CREATED).body(customerRepository.save(customer));
                }else{
                    return ResponseEntity.noContent().build();

                }
            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);

        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Customer>> getCustomers() {

        try {

            List<Customer> customers = customerRepository.findAll();

            if (customers == null || customers.isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok().body(customers);

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable long id) {

        try {
            
            Optional<Customer> optionalCustomer = customerRepository.findById(id);

            if (optionalCustomer.isPresent() && id!=0)
                return ResponseEntity.ok().body(optionalCustomer.get());
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @PutMapping("/put/{id}")
    public ResponseEntity editCustomer(@PathVariable long id, @RequestBody Customer customer) {

        try {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);

            if (optionalCustomer.isPresent() && customer.getName()!=null) {
                return ResponseEntity.ok().body(customerRepository.save(customer));
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCustomer(@PathVariable long id) {

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

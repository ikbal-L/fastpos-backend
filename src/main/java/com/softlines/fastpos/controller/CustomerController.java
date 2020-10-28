package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Customer;
import com.softlines.fastpos.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {


    @Autowired
    private CustomerRepository customerRepository ;

    @PostMapping("/save")
    public ResponseEntity addCustomer(@RequestBody Customer Customer) {
        try {

            Optional<Customer> optionalCustomer = customerRepository.findById(Customer.getId());

            if (!optionalCustomer.isPresent()) {

//                Customer Customer = dtoService.CustomerDtoToCustomer(CustomerDto);

                return ResponseEntity.status(HttpStatus.CREATED).body(customerRepository.save(Customer));
            } else {
                return ResponseEntity.noContent().build();

            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Customer>> getCustomers() {

        try {
            List<Customer> Customeres = customerRepository.findAll();
            if (Customeres != null)
                return ResponseEntity.ok().body(Customeres);
            else
                return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable long id) {

        try {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);

            if (optionalCustomer.isPresent())
                return ResponseEntity.ok().body(optionalCustomer.get());
            else
                return ResponseEntity.notFound().build();

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity editCustomer(@PathVariable long id, @RequestBody Customer Customer) {

        try {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);

            if (optionalCustomer.isPresent()) {
                return ResponseEntity.ok().body(customerRepository.save(Customer));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
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
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }
}

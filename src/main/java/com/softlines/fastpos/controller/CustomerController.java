package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Customer;

import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.dto.CustomerDto;
import com.softlines.fastpos.dto.DeliverymanDto;
import com.softlines.fastpos.dto.filters.OrderFilter;
import com.softlines.fastpos.dto.mapping.CustomerMapper;
import com.softlines.fastpos.dto.service.filtering.OrderFilterService;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.CustomerRepository;
import com.softlines.fastpos.repository.em.RepositoryDecoratorImp;
import com.softlines.fastpos.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController

@RequestMapping(value = "/api/customer", produces = "application/json; charset=UTF-8")
public class CustomerController {

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private OrderFilterService orderFilterService;


    @PostMapping("/save")
    public ResponseEntity<Long> addCustomer(@Valid @RequestBody CustomerDto customerDto) {
        try {


            if (customerDto.getId()==0) {
                Customer customer = customerMapper.toCustomer(customerDto);
                Customer savedCustomer = customerRepository.save(customer);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer.getId());
            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);

        }
    }


    @PostMapping("/savemany")
    public ResponseEntity<List<Long>> addManyCustomer(@Valid @RequestBody List<CustomerDto> customerDtoList) {
        try {

            List<Long> Ids = customerDtoList.parallelStream().map(CustomerDto::getId).collect(Collectors.toList());

            List<Customer> customerList = customerRepository.findAllById(Ids);

            if (customerList.size() == 0) {
                List<Customer> customers = customerMapper.toCustomerList(customerDtoList);
                List<Customer> savedCustomerList = customerRepository.saveAll(customers);
                List<Long> savedIds = savedCustomerList.parallelStream().map(Customer::getId)
                        .collect(Collectors.toList());

                return ResponseEntity.status(HttpStatus.CREATED).body(savedIds);
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
            if (customers == null || customers.isEmpty())
                return ResponseEntity.noContent().build();
            else{
                var customerDTOs = customerMapper.toCustomerDTOs(customers);
                return ResponseEntity.ok().body(customerDTOs);
            }


        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/getallwithbalance")
    public ResponseEntity<List<CustomerDto>> getCustomersWithBalance() {
        try {

            List<Customer> customerList = customerRepository.findAll();
            var states = new ArrayList<OrderState>();
            states.add(OrderState.Credit);
            states.add(OrderState.CreditPartiallyRePaid);


            if (customerList.isEmpty())   return ResponseEntity.noContent().build();
            var ids = customerList.stream().map(Customer::getId).collect(Collectors.toList());
            var filter = OrderFilter
                    .builder()
                    .states(Optional.of(states))
                    .customerIds(Optional.of(ids))
                    .orderTime(Optional.empty())
                    .build();

            var orders = orderFilterService.buildQuery(filter).getResultList();

            for (Customer customer : customerList) {

                CreditService.calculateBalance(customer,orders);

            }

            customerRepository.saveAll(customerList);

            return ResponseEntity.ok().body(customerMapper.toCustomerDTOs(customerList));

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


    @GetMapping("/getmany")

    public ResponseEntity<List<CustomerDto>> getCustomers(@Valid @RequestBody List<Long> ids) {

        try {

            List<Customer> customers = customerRepository.findAllById(ids);

            if (customers.isEmpty())
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


    @GetMapping("/getwithbalance/{id}")
    public ResponseEntity<CustomerDto> getCustomerWithBalance(@Valid @PathVariable long id) {

        try {

            Optional<Customer> optionalDeliveryman = customerRepository.findById(id);

            if (optionalDeliveryman.isPresent() && id != 0) {
                var customer = optionalDeliveryman.get();
                var states = new ArrayList<OrderState>();
                states.add(OrderState.Credit);
                states.add(OrderState.CreditPartiallyRePaid);

                var filter = OrderFilter
                        .builder()
                        .states(Optional.of(states))
                        .customerIds(Optional.of(List.of(customer.getId())))
                        .orderTime(Optional.empty())

                        .build();
                var orders = orderFilterService.buildQuery(filter).getResultList();

                CreditService.calculateBalance(customer,orders);
                customerRepository.save(customer);
                return ResponseEntity.ok().body(customerMapper.toCustomerDto(customer));
            }else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @PutMapping("/put/{id}")
    public ResponseEntity<CustomerDto> editCustomer(@Valid @PathVariable long id, @RequestBody CustomerDto customerDto) {

        try {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);

            if (optionalCustomer.isPresent() && customerDto.getName() != null) {
               var customer= customerMapper.toCustomer(customerDto);
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

            Optional<Customer> customerToDelete = customerRepository.findById(id);
            if (customerToDelete.isPresent()) {

//                EntityManager em = entityManagerFactory.createEntityManager();
//                em.getTransaction().begin();
//
//                customerRepository.delete(customerToDelete.get());
//                var q = em.createNativeQuery("update from `orders`set customer_id = NULL  where `customer_id` = :id").setParameter("id", id);
//                q.executeUpdate();
//                em.getTransaction().commit();
                var em = entityManagerFactory.createEntityManager();
                var repo = new RepositoryDecoratorImp<Customer,Long>(customerRepository,em,Customer.class );
                repo.deleteSetNull(customerToDelete.get(),id);


                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

}

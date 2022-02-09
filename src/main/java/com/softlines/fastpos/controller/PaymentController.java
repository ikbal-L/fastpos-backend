package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Payment;
import com.softlines.fastpos.dto.*;
import com.softlines.fastpos.dto.filters.Page;
import com.softlines.fastpos.dto.filters.PaymentFilter;
import com.softlines.fastpos.dto.mapping.PaymentMapper;
import com.softlines.fastpos.dto.service.filtering.PaymentFilterService;
import com.softlines.fastpos.repository.PaymentRepository;
import com.softlines.fastpos.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.*;
import java.sql.SQLException;

@RestController
@RequestMapping(value = "/api/payment", produces = "application/json; charset=UTF-8")
public class PaymentController {


    @Autowired
    PaymentService paymentService;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentMapper  paymentMapper;
    @Autowired
    PaymentFilterService paymentFilterService;

    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity<PaymentDto> save(@RequestBody PaymentDto paymentDto){
        var dto = paymentService.processPayment(paymentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @GetMapping("/getAllbydeliverymanPage/{pageNumber}/{pageSize}/{deliverymanId}")
    public ResponseEntity<PageList<PaymentDto>> getAllByDeliveryManPage(@PathVariable int pageNumber, @PathVariable int pageSize, @PathVariable  long deliverymanId){
        var orders= paymentRepository.findByDeliveryman_Id(deliverymanId, PageRequest.of(pageNumber, pageSize,Sort.by("date").descending()));
        if (!orders.isEmpty()){
            return  ResponseEntity.ok().body(new PageList<>(paymentMapper.toPaymentDtos(orders.getContent()),orders.getTotalElements()));
        }
        return  ResponseEntity.noContent().build();
    }
    @PutMapping("/put/{id}")
    public ResponseEntity edit(@PathVariable long id, @RequestBody PaymentDto paymentDto) throws SQLException {
        paymentService.editPaymentDeliveryMan(paymentDto);
        return ResponseEntity.ok().build();

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable long id){
        paymentService.deletePayment(id);
        return ResponseEntity.ok().build();

    }
    @PostMapping("/getByDeliverymanAndDate/{deliverymanId}")
    public ResponseEntity<List<PaymentDto>> getByDeliveryManAndDate(@PathVariable long deliverymanId,@RequestBody Date date){

        var payments=paymentRepository.findByDeliveryMan_IdAndDate(deliverymanId,date);

        if (!payments.isEmpty()){
            return ResponseEntity.ok().body(paymentMapper.toPaymentDtos(payments));
        }
        return  ResponseEntity.noContent().build();
    }

    @PostMapping(value = {"/getallbycriteria"})
    ResponseEntity<Page<PaymentDto>> getPaymentsByCriteria(@RequestBody PaymentFilter filter) throws ParseException {
        var paymentPage = paymentFilterService.buildQuery(filter);

        var paymentDtoPage= paymentPage.toPageOf(c-> paymentMapper.toPaymentDtos(c));

        return  ResponseEntity.ok(paymentDtoPage);

    }
}


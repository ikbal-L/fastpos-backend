package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.CashOperation;
import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.dto.*;
import com.softlines.fastpos.dto.mapping.CashOperationMapper;
import com.softlines.fastpos.dto.mapping.PaymentMapper;
import com.softlines.fastpos.repository.CashOperationRepository;
import com.softlines.fastpos.repository.OrderRepository;
import com.softlines.fastpos.repository.PaymentRepository;
import com.softlines.fastpos.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.sql.SQLException;

@RestController
@RequestMapping(value = "/api/payment", produces = "application/json")
public class PaymentController {


    @Autowired
    PaymentService paymentService;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentMapper  paymentMapper;
    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity<PaymentDto> save(@RequestBody PaymentDto paymentDto){
      return ResponseEntity.ok().body(paymentService.doPaymentDeliveryMan(paymentDto));
    }
    @GetMapping("/getAllbydeliverymanPage/{pageNumber}/{pageSize}/{deliverymanId}")
    public ResponseEntity<PageList<PaymentDto>> getAllByDeliveryManPage(@PathVariable int pageNumber, @PathVariable int pageSize, @PathVariable  long deliverymanId){
        var orders= paymentRepository.findByDeliveryMan_Id(deliverymanId, PageRequest.of(pageNumber, pageSize,Sort.by("date").descending()));
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
}


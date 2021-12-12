package com.softlines.fastpos.controller;

import com.softlines.fastpos.dto.CashOperationDto;
import com.softlines.fastpos.dto.OrderDto;
import com.softlines.fastpos.dto.mapping.CashOperationMapper;
import com.softlines.fastpos.repository.CashOperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/cashoperation", produces = "application/json; charset=UTF-8")
public class CashOperationController {

    @Autowired
    CashOperationMapper cashOperationMapper;
    @Autowired
    CashOperationRepository cashOperationRepository;

    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity save(@RequestBody CashOperationDto operationDto){
        var cashOperation=cashOperationMapper.toCashOperation(operationDto);
        return  ResponseEntity.ok().build();
    }
}


package com.softlines.fastpos.controller;


import com.softlines.fastpos.domain.CashOperation;
import com.softlines.fastpos.domain.CashRegisterExpense;
import com.softlines.fastpos.domain.ExpenseDescription;
import com.softlines.fastpos.dto.CashRegisterExpenseDto;
import com.softlines.fastpos.dto.mapping.CashRegisterExpenseMapper;
import com.softlines.fastpos.repository.CashRegisterExpenseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;

@RestController
@RequestMapping(value = "/api/cashregisterexpense", produces = "application/json; charset=UTF-8")
public class CashRegisterExpenseController {

    private CashRegisterExpenseRepository cashRegisterExpenseRepository;

    private CashRegisterExpenseMapper cashRegisterExpenseMapper;
    @PersistenceContext
    private EntityManager em;

    public CashRegisterExpenseController(CashRegisterExpenseRepository cashRegisterExpenseRepository, CashRegisterExpenseMapper cashRegisterExpenseMapper) {
        this.cashRegisterExpenseRepository = cashRegisterExpenseRepository;
        this.cashRegisterExpenseMapper = cashRegisterExpenseMapper;
    }

    @GetMapping("/getall")
    public ResponseEntity<List<CashRegisterExpenseDto>> getAll(){
        var expenses = cashRegisterExpenseRepository.findAll();
        if (expenses.isEmpty()) return ResponseEntity.noContent().build();
        var expenseDTOs= cashRegisterExpenseMapper.toCashRegisterExpenseDTOs(expenses);
        return ResponseEntity.ok(expenseDTOs);
    }

    @PostMapping("/save")
    public ResponseEntity<CashRegisterExpenseDto> save(@RequestBody CashRegisterExpenseDto expenseDto){
        if (expenseDto.getId()!=null&& cashRegisterExpenseRepository.existsById(expenseDto.getId())) return ResponseEntity.status(HttpStatus.FOUND).build();
        var expense = cashRegisterExpenseMapper.toCashRegisterExpense(expenseDto);
        var cashOp = CashOperation.builder().amount(expense.getAmount()).cashRegisterExpense(expense).build();
        expense.setCashOperation(cashOp);
        var savedExpense = cashRegisterExpenseRepository.save(expense);
        var createdExpenseDto = cashRegisterExpenseMapper.toCashRegisterExpenseDto(savedExpense);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExpenseDto);
    }

    @GetMapping("/employees/getall")
    public ResponseEntity<List<String>> getAllEmployees(){
        var query = em.createQuery("SELECT distinct e.employeeName from CashRegisterExpense  AS e", String.class);
        var result = query.getResultList();
        return ResponseEntity.ok(result);
    }
}

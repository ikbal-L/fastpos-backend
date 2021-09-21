package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.ExpenseDescription;
import com.softlines.fastpos.dto.ExpenseDescriptionDto;
import com.softlines.fastpos.dto.mapping.ExpenseDescriptionMapper;
import com.softlines.fastpos.repository.ExpenseDescriptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/expensedescription", produces = "application/json")
public class ExpenseDescriptionController {

    private ExpenseDescriptionRepository repository;
    private ExpenseDescriptionMapper mapper;

    public ExpenseDescriptionController(ExpenseDescriptionRepository repository, ExpenseDescriptionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @GetMapping(value = "/getall")
    public ResponseEntity<List<ExpenseDescriptionDto>> getAll(){
        var descriptions= repository.findAll();
        if (descriptions.isEmpty()) return ResponseEntity.noContent().build();
        var descriptionDTOs = mapper.toExpenseDescriptionDTOs(descriptions);
        return ResponseEntity.ok(descriptionDTOs);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ExpenseDescriptionDto> save(@RequestBody  ExpenseDescriptionDto expenseDescriptionDto){
        if (expenseDescriptionDto.getId()!= null&& repository.existsById(expenseDescriptionDto.getId())) return ResponseEntity.status(HttpStatus.FOUND).build();
        var expenseDescription = mapper.toExpenseDescription(expenseDescriptionDto);
        var saved = repository.save(expenseDescription);
        var savedDto = mapper.toExpenseDescriptionDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id){

        if (id != null&&repository.existsById(id)){
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }

    }
}

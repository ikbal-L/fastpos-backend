package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Table;
import com.softlines.fastpos.dto.TableDto;
import com.softlines.fastpos.dto.mapping.TableMapper;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/table", produces = "application/json")
public class TablesController {
    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @Autowired
    TableMapper tableMapper;

    @Autowired
    private TableRepository tableRepository;

    @PostMapping("/save")
    public ResponseEntity<Long> addTable(@Valid @RequestBody TableDto tableDto) {

        try {

            Optional<Table> table = tableRepository.findById(tableDto.getId());

            if (table.isEmpty() && tableDto.getId() == 0) {

                Table tables = tableMapper.toTable(tableDto);
                Table savedTable = tableRepository.save(tables);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedTable.getId());

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/getall")
    public ResponseEntity<List<TableDto>> getTables() {
        try {
            List<Table> tables = tableRepository.findAllTables();
            if (tables == null || tables.isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok().body(tableMapper.toTableDTOs(tables));

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TableDto> getTable(@Valid @PathVariable long id) {

        try {
            Table table = tableRepository.findByIdTable(id);

            if (table != null && id != 0)
                return ResponseEntity.ok().body(tableMapper.toTableDto(table));
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @GetMapping("/getbynumber/{number}")
    public ResponseEntity<TableDto> getTableByNumber(@Valid @PathVariable int number) {

        try {
            Table optionalTable = tableRepository.findByNumber(number);

            if (optionalTable != null && number != 0)
                return ResponseEntity.ok().body(tableMapper.toTableDto(optionalTable));
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<TableDto> editTable(@Valid @PathVariable long id, @Valid @RequestBody TableDto tableDto) {

        try {
            Optional<Table> optionalTable = tableRepository.findById(id);

            if (optionalTable.isPresent() && tableDto.getNumber() != 0) {
                Table table = tableMapper.toTable(tableDto);
                Table savedTable = tableRepository.save(table);
                return ResponseEntity.ok().body(tableMapper.toTableDto(savedTable));
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteTable(@Valid @PathVariable long id) {

        try {

            Optional<Table> TableToDel = tableRepository.findById(id);

            if (TableToDel.isPresent() && id != 0) {

                tableRepository.delete(TableToDel.get());
                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


}

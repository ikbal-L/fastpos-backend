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

import java.util.List;

@RestController
@RequestMapping("/api/table")
public class TablesController {
    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @Autowired
    TableMapper tableMapper;

    @Autowired
    private TableRepository tableRepository;

    @PostMapping("/save")
    public ResponseEntity<TableDto> addTable(@RequestBody TableDto tableDto) {

        try {

            Table table = tableRepository.findByIdTablesOrders(tableDto.getId());

            if (table == null && tableDto.getId() == 0) {

                if (tableDto.getNumber() != 0) {
                    Table tables = tableMapper.toTable(tableDto);
                    return ResponseEntity.status(HttpStatus.CREATED).body(tableMapper.toTableDto(tableRepository.save(tables)));
                } else {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                }

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
            List<Table> tables = tableRepository.findAllTablesWithTableOrders();
            if (tables == null || tables.isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok().body(tableMapper.toTableDTOs(tables));

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TableDto> getTable(@PathVariable long id) {

        try {
            Table optionalTable = tableRepository.findByIdTablesOrders(id);

            if (optionalTable!=null && id != 0)
                return ResponseEntity.ok().body(tableMapper.toTableDto(optionalTable));
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @GetMapping("/getbynumber/{number}")
    public ResponseEntity<TableDto> getTableByNumber(@PathVariable int number) {

        try {
            Table optionalTable = tableRepository.findByNumberWithOrders(number);

            if (optionalTable!=null && number != 0)
                return ResponseEntity.ok().body(tableMapper.toTableDto(optionalTable));
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity editTable(@PathVariable long id, @RequestBody TableDto tableDto) {

        try {
            Table optionalTable = tableRepository.findByIdTablesOrders(id);

            if (optionalTable !=null && tableDto.getNumber() != 0) {


                Table table = tableMapper.toTable(tableDto);
                return ResponseEntity.ok().body(tableRepository.save(table));
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteTable(@PathVariable long id) {

        try {

            Table TableToDel = tableRepository.findByIdTablesOrders(id);

            if (TableToDel != null && id != 0) {

                tableRepository.delete(TableToDel);
                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


}

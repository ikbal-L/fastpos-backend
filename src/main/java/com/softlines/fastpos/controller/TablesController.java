package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Tables;
import com.softlines.fastpos.dto.TableDto;
import com.softlines.fastpos.dto.mapping.TableMapper;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

            Optional<Tables> optionalTable = tableRepository.findById(tableDto.getId());

            if (!optionalTable.isPresent() && tableDto.getId() == 0) {

                if (tableDto.getNumber() !=0) {
                    Tables tables = tableMapper.toTable(tableDto);
                    return ResponseEntity.status(HttpStatus.CREATED).body(tableMapper.toTableDto(tableRepository.save(tables)));
                }else{
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
            List<Tables> tables = tableRepository.findAll();
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
            Optional<Tables> optionalTable = tableRepository.findById(id);

            if (optionalTable.isPresent() && id != 0)
                return ResponseEntity.ok().body(tableMapper.toTableDto(optionalTable.get()));
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity editTable(@PathVariable long id, @RequestBody TableDto tableDto) {

        try {
            Optional<Tables> optionalTable = tableRepository.findById(id);

            if (optionalTable.isPresent() && tableDto.getNumber()!=0) {


                    Tables tables = tableMapper.toTable(tableDto);
                    return ResponseEntity.ok().body(tableRepository.save(tables));
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

            Optional<Tables> TableToDel = tableRepository.findById(id);

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

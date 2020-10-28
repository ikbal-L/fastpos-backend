package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Tables;
import com.softlines.fastpos.dto.TableDto;
import com.softlines.fastpos.dto.mapping.TableMapper;
import com.softlines.fastpos.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/table")
public class TablesController {

    @Autowired
    TableMapper tableMapper;

    @Autowired
    private TableRepository tableRepository;

    @PostMapping("/save")
    public ResponseEntity addTable(@RequestBody Tables table) {
        try {

            Optional<Tables> optionalTable = tableRepository.findById(table.getId());

            if (!optionalTable.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(tableRepository.save(table));
            } else {
                return ResponseEntity.noContent().build();

            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<TableDto>> getTables() {
        try {
            List<Tables> tables = tableRepository.findAll();
            if (tables != null)

                return ResponseEntity.ok().body(tableMapper.toTableDTOs(tables) );
            else
                return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TableDto> getTable(@PathVariable long id) {

        try {
            Optional<Tables> optionalTable = tableRepository.findById(id);

            if (optionalTable.isPresent())
                return ResponseEntity.ok().body(tableMapper.toTableDto(optionalTable.get()));
            else
                return ResponseEntity.notFound().build();

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity editTable(@PathVariable long id, @RequestBody Tables Table) {

        try {
            Optional<Tables> optionalTable = tableRepository.findById(id);

            if (optionalTable.isPresent()) {

                return ResponseEntity.ok().body(tableRepository.save(optionalTable.get()));

            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteTable(@PathVariable long id) {

        try {

            Optional<Tables> TableToDel = tableRepository.findById(id);
            if (TableToDel.isPresent()) {

                tableRepository.delete(TableToDel.get());
                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }


}

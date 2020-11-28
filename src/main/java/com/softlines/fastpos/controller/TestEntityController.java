package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Table;
import com.softlines.fastpos.domain.TestEntity;
import com.softlines.fastpos.dto.TableDto;
import com.softlines.fastpos.dto.TestDTO;
import com.softlines.fastpos.dto.mapping.TestEntityMapper;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.TestEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/test-entity")
public class TestEntityController {

    @Autowired
    private TestEntityRepository
            testEntityRepository;
    @Autowired
    private TestEntityMapper mapper;
    @Autowired
    ExceptionManagement exceptionManagement;
    @PostMapping("/save")
    public ResponseEntity<TestDTO> addTestEntity(@RequestBody TestDTO testDTO) {

        try {
            Optional<TestEntity> testEntity = null;
            if (testDTO.getId()!=null) {
                testEntity = testEntityRepository.findById(testDTO.getId());
            }

            if (testEntity == null) {
                var mapped = mapper.toTestEntity(testDTO);
                var test = new TestEntity();
                var created = testEntityRepository.save(mapped);
               return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toTestDto(created));

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


    @PutMapping("/put")
    public ResponseEntity<TestDTO> updateTestEntity(@RequestBody TestDTO testDTO) {

        try {
            Optional<TestEntity> testEntity = null;
            if (testDTO.getId()!=null) {
                testEntity = testEntityRepository.findById(testDTO.getId());
            }

            if (testEntity != null) {
                var mapped = mapper.toTestEntity(testDTO);
                var test = new TestEntity();
                var created = testEntityRepository.save(mapped);
                return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toTestDto(created));

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

}

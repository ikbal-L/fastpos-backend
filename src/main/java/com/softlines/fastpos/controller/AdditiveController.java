package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.dto.AdditiveDto;
import com.softlines.fastpos.dto.mapping.AdditiveMapper;
import com.softlines.fastpos.dto.service.DtoService;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.AdditiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/additive")
public class AdditiveController {

    @Autowired
    AdditiveMapper additiveMapper;
    @Autowired
    private AdditiveRepository additiveRepository;
    @Autowired
    DtoService dtoService;

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @PostMapping("/save")
    public ResponseEntity<Additive> addAdditive(@Valid @RequestBody Additive additive) {

        try {

            Optional<Additive> optionalAdditive = additiveRepository.findById(additive.getId());

            if (!(optionalAdditive.isPresent()) && additive.getId()==0) {

                if (additive.getDescription() != null &&
                        !additive.getDescription().isEmpty())
                    return ResponseEntity.status(HttpStatus.CREATED).body(additiveRepository.save(additive));
                else
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/getall")
    public ResponseEntity<List<Additive>> getAdditives() {

        try {

            List<Additive> additives = additiveRepository.findAll();

            if (additives == null || additives.isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok().body(additives);

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<AdditiveDto> getAdditive(@PathVariable long id) {

        try {

            Optional<Additive> optionalAdditive = additiveRepository.findById(id);

            if (optionalAdditive.isPresent() && id != 0)
                return ResponseEntity.ok().body(additiveMapper.toAdditiveDto(additiveRepository.findById(id).get()));
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @PutMapping("/put/{id}")
    public ResponseEntity<Additive> editAdditive(@PathVariable long id, @RequestBody Additive additive) {

        try {
            Optional<Additive> optionalAdditive = additiveRepository.findById(id);

            if (optionalAdditive.isPresent() && id != 0 && additive.getDescription() != null) {
                return ResponseEntity.ok().body(additiveRepository.save(additive));
            } else {
              return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteAdditive(@PathVariable long id) {
        try {

            Optional<Additive> additiveToDel = additiveRepository.findById(id);

            if (additiveToDel.isPresent()) {

                additiveRepository.delete(additiveToDel.get());
                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }
}

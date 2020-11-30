package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.dto.AdditiveDto;
import com.softlines.fastpos.dto.mapping.AdditiveMapper;
import com.softlines.fastpos.dto.service.DtoService;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.AdditiveRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/additive")
public class AdditiveController {

    @Autowired
    AdditiveMapper additiveMapper;
    @Autowired
    private AdditiveRepository additiveRepository;
    @Autowired
    DtoService dtoService;

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @PostMapping("/save")
    public ResponseEntity<Long> addAdditive(@Valid @RequestBody AdditiveDto additiveDto) {
        try {

            Optional<Additive> optionalAdditive = additiveRepository.findById(additiveDto.getId());

            if (!(optionalAdditive.isPresent()) && additiveDto.getId() == 0) {

                if (additiveDto.getDescription() != null &&
                        !additiveDto.getDescription().isEmpty()) {
                    Additive savedAdditve = additiveRepository.save(additiveMapper.toAdditive(additiveDto));
//                    AdditiveDto savedAdditveDto = additiveMapper.toAdditiveDto(savedAdditve);
//                    return ResponseEntity.status(HttpStatus.CREATED).body(savedAdditveDto);
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedAdditve.getId());
                } else
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }


    @PostMapping("/savemany")
    public ResponseEntity<List<AdditiveDto>> addManyAdditive(@Valid @RequestBody List<AdditiveDto> additiveDtoList) {
        try {

            List<Long> ids = additiveDtoList.parallelStream().map(AdditiveDto::getId)
                    .collect(Collectors.toList());

            List<Additive> additives = additiveRepository.findAllById(ids);

            if (additives.isEmpty()) {
                List<Additive> savedAdditve = additiveRepository.saveAll(additiveMapper.toAdditiveList(additiveDtoList));
                List<AdditiveDto> savedAdditveDto = additiveMapper.toAdditiveDTOs(savedAdditve);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedAdditveDto);


        } else{
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

    } catch(
    Exception exception)

    {
        return exceptionManagement.getResponseEntityAccordingToException(exception);
    }

}

    @GetMapping("/getall")
    public ResponseEntity<List<AdditiveDto>> getAdditives() {

        try {

            List<Additive> additives = additiveRepository.findAll();

            if (additives == null || additives.isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok().body(additiveMapper.toAdditiveDTOs(additives));

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/getmany")
    public ResponseEntity<List<AdditiveDto>> getMany(@RequestBody List<Long> ids) {

        try {

            List<Additive> additives = additiveRepository.findAllById(ids);

            if (additives == null || additives.isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok().body(additiveMapper.toAdditiveDTOs(additives));

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping(value = "/get/{id}", produces = "application/json")
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
    public ResponseEntity<AdditiveDto> editAdditive(@PathVariable long id, @RequestBody AdditiveDto additiveDto) {


            Optional<Additive> optionalAdditive = additiveRepository.findById(id);

            if (optionalAdditive.isPresent() && id != 0 && additiveDto.getDescription() != null) {
                Additive additive = dtoService.additiveDtoToAdditive(additiveDto, false);

                return ResponseEntity.ok().body(additiveMapper.toAdditiveDto(additiveRepository.save(additive)));
            } else {
                return ResponseEntity.noContent().build();
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

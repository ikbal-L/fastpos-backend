package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.dto.AdditiveDto;
import com.softlines.fastpos.dto.mapping.AdditiveMapper;
import com.softlines.fastpos.dto.service.DtoService;
import com.softlines.fastpos.repository.AdditiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
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

    @PostMapping("/save")
    public ResponseEntity addAdditive(@RequestBody Additive additive) {
        try {

            Optional<Additive> optionalAdditive = additiveRepository.findById(additive.getId());

            if (!optionalAdditive.isPresent()) {

//                Additive additive = dtoService.additiveDtoToAdditive(additiveDto);

                return ResponseEntity.status(HttpStatus.CREATED).body(additiveRepository.save(additive));
            } else {
                return ResponseEntity.noContent().build();

            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<AdditiveDto>> getAdditives() {
        try {
            List<Additive> additives = additiveRepository.findAll();
            if (additives != null)
                return ResponseEntity.ok().body(additiveMapper.toAdditiveDTOs(additives));
            else
                return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<AdditiveDto> getAdditive(@PathVariable long id) {

        try {

            Optional<Additive> optionalAdditive = additiveRepository.findById(id);
            if (optionalAdditive.isPresent())
                return ResponseEntity.ok().body(additiveMapper.toAdditiveDto(additiveRepository.findById(id).get()));
            else
                return ResponseEntity.notFound().build();

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @PutMapping("/put/{id}")
    public ResponseEntity editAdditive(@PathVariable long id, @RequestBody Additive additive) {

        try {
            Optional<Additive> optionalAdditive = additiveRepository.findById(id);

            if (optionalAdditive.isPresent()) {

//                Additive additive = dtoService.additiveDtoToAdditive(additiveDto);
                return ResponseEntity.ok().body(additiveRepository.save(additive));

            } else {
//                ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//                messageSource.setDefaultEncoding("UTF-8");
//                messageSource.setBasenames("messages");
//                return ResponseEntity.ok().body(messageSource.getMessage("notfound", null,lang!=null ? new Locale(lang):null));
                return ResponseEntity.notFound().build();
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
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
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }
}

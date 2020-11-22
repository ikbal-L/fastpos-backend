package com.softlines.fastpos.controller;


import com.softlines.fastpos.domain.Restaurent;
import com.softlines.fastpos.dto.RestaurentDto;
import com.softlines.fastpos.dto.mapping.RestaurentMapper;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.RestaurentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurent")
public class RestaurentController {

    @Autowired
    private RestaurentMapper restaurentMapper;

    @Autowired
    private RestaurentRepository restaurentRepository;

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @PostMapping("/save")
    public ResponseEntity addRestaurent(@RequestBody RestaurentDto restaurentDto) {

        try {

            Restaurent optionalRestaurent = restaurentRepository.findByIdRestaurentWithAnnexes(restaurentDto.getId());

            if (optionalRestaurent == null && restaurentDto.getName()!=null) {
                
                Restaurent restaurent = restaurentRepository.save(restaurentMapper.toRestaurent(restaurentDto));
                return ResponseEntity.status(HttpStatus.CREATED).build();

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<RestaurentDto>> getRestaurent() {

        try {
            List<Restaurent> restaurentes = restaurentRepository.findAllRestaurentWithAnnexes();

            if (restaurentes != null && restaurentes.size() > 0)
                return ResponseEntity.ok().body(restaurentMapper.toRestaurentDTOs(restaurentes));
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/getmany")
    public ResponseEntity<List<RestaurentDto>> getmanyRestaurent(@RequestBody List<Long> ids) {

        try {
            List<Restaurent> restaurentes = restaurentRepository.findManyRestaurentWithAnnexes(ids);

            if (restaurentes != null && restaurentes.size() > 0)
                return ResponseEntity.ok().body(restaurentMapper.toRestaurentDTOs(restaurentes));
            else
                return ResponseEntity.notFound().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<RestaurentDto> getRestaurent(@PathVariable long id) {

        try {
            Restaurent restaurent = restaurentRepository.findByIdRestaurentWithAnnexes(id);

            if (restaurent != null )
                return ResponseEntity.ok().body(restaurentMapper.toRestaurentDto(restaurent));
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @PutMapping("/put/{id}")
    public ResponseEntity<RestaurentDto> editRestaurent(@PathVariable long id, @RequestBody RestaurentDto restaurentDto) {

        try {

            Restaurent optionalRestaurent = restaurentRepository.findByIdRestaurentWithAnnexes(id);

            if (optionalRestaurent != null && restaurentDto.getName()!=null ) {
                Restaurent restaurent = restaurentRepository.save(restaurentMapper.toRestaurent(restaurentDto));
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteRestaurent(@PathVariable long id) {

        try {

            Restaurent optionalRestaurent = restaurentRepository.findByIdRestaurentWithAnnexes(id);

            if (optionalRestaurent != null) {
                restaurentRepository.delete(optionalRestaurent);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }


}

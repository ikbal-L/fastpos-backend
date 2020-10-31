package com.softlines.fastpos.controller;


import com.softlines.fastpos.domain.Restaurent;
import com.softlines.fastpos.dto.RestaurentDto;
import com.softlines.fastpos.dto.mapping.RestaurentMapper;
import com.softlines.fastpos.repository.RestaurentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restaurent")
public class RestaurentController {

    @Autowired
    private RestaurentMapper restaurentMapper;

    @Autowired
    private RestaurentRepository restaurentRepository;

    @PostMapping("/save")
    public ResponseEntity addRestaurent(@RequestBody RestaurentDto restaurentDto) {

        try {

            Optional<Restaurent> optionalrestaurent = restaurentRepository.findById(restaurentDto.getId());

            if (!optionalrestaurent.isPresent()) {
               Restaurent restaurent= restaurentRepository.save(restaurentMapper.toRestaurent(restaurentDto) );
                return ResponseEntity.status(HttpStatus.CREATED).body(restaurentMapper.toRestaurentDto(restaurent));
            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<RestaurentDto>> getRestaurents() {
        try {
            List<Restaurent> restaurentes = restaurentRepository.findAll();
            if (restaurentes != null)
                return ResponseEntity.ok().body(restaurentMapper.toRestaurentDTOs(restaurentes));
            else
                return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<RestaurentDto> getRestaurent(@PathVariable long id) {

        try {
            Optional<Restaurent> optionalrestaurent = restaurentRepository.findById(id);

            if (optionalrestaurent.isPresent())
                return ResponseEntity.ok().body(restaurentMapper.toRestaurentDto(optionalrestaurent.get()));
            else
                return ResponseEntity.notFound().build();

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity editRestaurent(@PathVariable long id, @RequestBody RestaurentDto restaurentDto) {

        try {
            Optional<Restaurent> optionalRestaurent = restaurentRepository.findById(id);

            if (optionalRestaurent.isPresent()) {
                Restaurent restaurent = restaurentMapper.toRestaurent(restaurentDto);
                return ResponseEntity.ok().body(restaurentMapper.toRestaurentDto(restaurentRepository.save(restaurent)));
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleterestaurent(@PathVariable long id) {

        try {

            Optional<Restaurent> optionalrestaurent = restaurentRepository.findById(id);
            if (optionalrestaurent.isPresent()) {

                restaurentRepository.delete(optionalrestaurent.get());
                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }
}

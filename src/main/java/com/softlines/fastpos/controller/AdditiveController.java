package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.repository.AdditiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/additive")
public class AdditiveController {

    private AdditiveRepository additiveRepository;


    public AdditiveController(AdditiveRepository additiveRepository) {
        this.additiveRepository = additiveRepository;
    }

    @PostMapping("/save")
    public ResponseEntity<Additive> addAdditive(@RequestBody Additive additive) {
        try {


            Additive existingAdditive = additiveRepository.findById(additive.getId()).get();
            if (existingAdditive != null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(existingAdditive);
            }
            Additive createdAdditive = additiveRepository.save(additive);
            if (createdAdditive != null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(additive);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdAdditive);

            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/getall")
    public List<Additive> getAdditives() {
        try {
            return additiveRepository.findAll();
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/get/{id}")
    public Optional<Additive> getAdditive(@PathVariable long id) {
        try {
            return additiveRepository.findById(id);
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PutMapping("/put/{id}")
    public void editAdditive(@PathVariable long id, @RequestBody Additive additive) {
        try {
            Additive existingProduct = additiveRepository.findById(id).get();
            Assert.notNull(existingProduct, "Additive not found");
            existingProduct.setDescription(additive.getDescription());
            existingProduct.setBackgroundString(additive.getBackgroundString());
            existingProduct.setRank(additive.getRank());

            additiveRepository.save(existingProduct);
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAdditive(@PathVariable long id) {
        try {
            Additive productToDel = additiveRepository.findById(id).get();
            additiveRepository.delete(productToDel);
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }
}

package com.softlines.fastpos.security.controllers;

import com.softlines.fastpos.security.securitydomain.Annex;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.security.securityrepository.AnnexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/config/annex")
public class AnnexController {

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @Autowired
    private AnnexRepository annexRepository;

    @PostMapping("/save")
    public ResponseEntity<Annex> addAnnex(@RequestBody Annex annex) {
        try {

            //you didn't take into consideration the case where annex is null
            Optional<Annex> optionalAnnex = annexRepository.findById(annex.getId());

            if (!optionalAnnex.isPresent()) {
                if (annex.getName() != null &&
                        !annex.getName().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.CREATED).body(annexRepository.save(annex));
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
    public ResponseEntity<List<Annex>> getAnnexes() {
        try {
            List<Annex> annexes = annexRepository.findAll();
            if (annexes == null || annexes.isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok().body(annexes);

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Annex> getAnnex(@PathVariable long id) {

        try {
            Optional<Annex> optionalAnnex = annexRepository.findById(id);

            if (optionalAnnex.isPresent() && id != 0)
                return ResponseEntity.ok().body(optionalAnnex.get());
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity editAnnex(@PathVariable long id, @RequestBody Annex Annex) {

        try {
            Optional<Annex> optionalAnnex = annexRepository.findById(id);

            if (optionalAnnex.isPresent() && Annex.getName() != null) {
                return ResponseEntity.ok().body(annexRepository.save(Annex));
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteAnnex(@PathVariable long id) {

        try {

            Optional<Annex> optionalAnnex = annexRepository.findById(id);
            if (optionalAnnex.isPresent()) {

                annexRepository.delete(optionalAnnex.get());
                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }
}

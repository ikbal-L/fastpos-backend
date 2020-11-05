package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Annex;
import com.softlines.fastpos.dto.service.DtoService;
import com.softlines.fastpos.repository.AnnexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/annex")
public class AnnexController {


    @Autowired
    private AnnexRepository annexRepository;

    @PostMapping("/save")
    public ResponseEntity<Annex> addAnnex(@RequestBody Annex annex) {
        try {

            Optional<Annex> optionalAnnex = annexRepository.findById(annex.getId());

            if (!optionalAnnex.isPresent()) {
//                Annex Annex = dtoService.AnnexDtoToAnnex(AnnexDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(annexRepository.save(annex));
            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();

            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Annex>> getAnnexs() {
        try {
            List<Annex> annexes = annexRepository.findAll();
            if (annexes != null)
                return ResponseEntity.ok().body(annexes);
            else
                return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Annex> getAnnex(@PathVariable long id) {

        try {
            Optional<Annex> optionalAnnex = annexRepository.findById(id);

            if (optionalAnnex.isPresent())
                return ResponseEntity.ok().body(optionalAnnex.get());
            else
                return ResponseEntity.notFound().build();

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity editAnnex(@PathVariable long id, @RequestBody Annex Annex) {

        try {
            Optional<Annex> optionalAnnex = annexRepository.findById(id);

            if (optionalAnnex.isPresent()) {
                return ResponseEntity.ok().body(annexRepository.save(Annex));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }
}

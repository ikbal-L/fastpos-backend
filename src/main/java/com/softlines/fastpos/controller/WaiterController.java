package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Waiter;
import com.softlines.fastpos.dto.service.DtoService;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.WaiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api/waiter")
public class WaiterController {

    @Autowired
    private WaiterRepository waiterRepository;


    @Autowired
    DtoService dtoService;

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @PostMapping("/save")
    public ResponseEntity addWaiter(@RequestBody Waiter waiter ) {
        try {

            Optional<Waiter> optionalWaiter = waiterRepository.findById(waiter.getId());

            if (!optionalWaiter.isPresent()) {
                if (waiter.getName() != null && !waiter.getName().isEmpty()) {
                    Waiter createdWaiter = waiterRepository.save(waiter);
                    return ResponseEntity.status(HttpStatus.CREATED).body(createdWaiter);
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
    public ResponseEntity<List<Waiter>> getWaiters() {
        try {

            List<Waiter> categories = waiterRepository.findAll();

            if (categories == null || categories.isEmpty()) {
                return ResponseEntity.noContent().build();

            } else {
                return ResponseEntity.ok().body(categories);
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Waiter> getWaiter(@PathVariable long id) {

        try {

            Optional<Waiter> optionalWaiter = waiterRepository.findById(id);

            if (optionalWaiter.isPresent() && id != 0)
                return ResponseEntity.ok().body(optionalWaiter.get());
            else
                return ResponseEntity.noContent().build();


        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<List<Waiter>> getWaiterByName(@PathVariable String name) {
        try {

            List<Waiter> categories = waiterRepository.findByName(name);

            if (categories != null)
                return ResponseEntity.ok().body(categories);
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<Waiter> editWaiter(@PathVariable long id, @RequestBody Waiter waiter) {
        try {

            Optional<Waiter> optionalWaiter = waiterRepository.findById(id);

            if (optionalWaiter.isPresent() && id != 0 && waiter.getName() != null) {
                return ResponseEntity.ok().body(waiterRepository.save(waiter));
            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteWaiter(@PathVariable long id) {

        try {
            Waiter waiterToDel = waiterRepository.findById(id).get();
            if (waiterToDel != null) {

                waiterRepository.delete(waiterToDel);
                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }
}
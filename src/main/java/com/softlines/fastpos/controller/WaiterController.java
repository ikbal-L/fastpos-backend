package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Waiter;
import com.softlines.fastpos.dto.WaiterDto;
import com.softlines.fastpos.dto.mapping.WaiterMapper;
import com.softlines.fastpos.dto.service.DtoService;
import com.softlines.fastpos.dto.service.DtoServiceImpl;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.WaiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api/waiter")
public class WaiterController {

    @Autowired
    private WaiterRepository waiterRepository;


    @Autowired
    DtoService dtoService;

    @Autowired
    WaiterMapper waiterMapper;


    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @PostMapping("/save")
    public ResponseEntity<WaiterDto> addWaiter(@Valid @RequestBody WaiterDto waiterDto) {

        try {

            Optional<Waiter> optionalWaiter = waiterRepository.findById(waiterDto.getId());

            if (optionalWaiter.isEmpty()) {
                    Waiter waiter=dtoService.waiterDtoToWaiter(waiterDto, false);
                    Waiter createdWaiter = waiterRepository.save(waiter);
                    return ResponseEntity.status(HttpStatus.CREATED).body(waiterMapper.toWaiterDto(createdWaiter));

            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<WaiterDto>> getWaiters() {
        try {

            List<Waiter> waiters = waiterRepository.findAll();

            if (waiters == null || waiters.isEmpty()) {
                return ResponseEntity.noContent().build();

            } else {
                return ResponseEntity.ok().body(waiterMapper.toWaiterDTOs(waiters));
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/getallactive")
    public ResponseEntity<List<WaiterDto>> getAllActiveWaiters() {
        try {

            List<Waiter> waiters = waiterRepository.findAllActiveWaiters(true);

            if (waiters == null || waiters.isEmpty()) {
                return ResponseEntity.noContent().build();

            } else {
                return ResponseEntity.ok().body(waiterMapper.toWaiterDTOs(waiters));
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<WaiterDto> getWaiter(@Valid @PathVariable long id) {

        try {

            Optional<Waiter> optionalWaiter = waiterRepository.findById(id);

            if (optionalWaiter.isPresent() && id != 0)

                return ResponseEntity.ok().body(waiterMapper.toWaiterDto(optionalWaiter.get()));
            else
                return ResponseEntity.noContent().build();


        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }



    @PutMapping("/put/{id}")
    public ResponseEntity editWaiter(@PathVariable long id,@Valid @RequestBody Waiter waiter) {
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
    public ResponseEntity deleteWaiter(@Valid @PathVariable long id) {

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
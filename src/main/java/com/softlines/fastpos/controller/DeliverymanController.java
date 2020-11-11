package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.DeliverymanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/deliveryman")
public class DeliverymanController {

    @Autowired
    private DeliverymanRepository deliverymanRepository;


    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @PostMapping("/save")
    public ResponseEntity addDeliveryman(@RequestBody Deliveryman deliveryman ) {
        try {

            Optional<Deliveryman> optionalDeliveryman = deliverymanRepository.findById(deliveryman.getId());

            if (!optionalDeliveryman.isPresent()) {
                if (deliveryman.getName() != null && !deliveryman.getName().isEmpty()) {
                    Deliveryman createdDeliveryman = deliverymanRepository.save(deliveryman);
                    return ResponseEntity.status(HttpStatus.CREATED).body(createdDeliveryman);
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
    public ResponseEntity<List<Deliveryman>> getDeliverymen() {
        try {

            List<Deliveryman> deliverymanList = deliverymanRepository.findAll();

            if (deliverymanList == null || deliverymanList.isEmpty()) {
                return ResponseEntity.noContent().build();

            } else {
                return ResponseEntity.ok().body(deliverymanList);
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Deliveryman> getDeliveryman(@PathVariable long id) {

        try {

            Optional<Deliveryman> optionalDeliveryman = deliverymanRepository.findById(id);

            if (optionalDeliveryman.isPresent() && id != 0)
                return ResponseEntity.ok().body(optionalDeliveryman.get());
            else
                return ResponseEntity.noContent().build();


        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<List<Deliveryman>> getDeliverymanByName(@PathVariable String name) {
        try {

            List<Deliveryman> categories = deliverymanRepository.findByName(name);

            if (categories != null)
                return ResponseEntity.ok().body(categories);
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<Deliveryman> editDeliveryman(@PathVariable long id, @RequestBody Deliveryman deliveryman) {
        try {

            Optional<Deliveryman> optionalDeliveryman = deliverymanRepository.findById(id);

            if (optionalDeliveryman.isPresent() && id != 0 && deliveryman.getName() != null) {
                return ResponseEntity.ok().body(deliverymanRepository.save(deliveryman));
            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteDeliveryman(@PathVariable long id) {

        try {
            Deliveryman deliverymanToDel = deliverymanRepository.findById(id).get();
            if (deliverymanToDel != null) {

                deliverymanRepository.delete(deliverymanToDel);
                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }
}
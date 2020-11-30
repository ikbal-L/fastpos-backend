package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.dto.DeliverymanDto;
import com.softlines.fastpos.dto.mapping.DeliverymanMapper;
import com.softlines.fastpos.dto.service.DtoService;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.DeliverymanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/deliveryman")
public class DeliverymanController {

    @Autowired
    private DeliverymanRepository deliverymanRepository;

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @Autowired
    DtoService dtoService;


    @Autowired
    DeliverymanMapper deliverymanMapper;

    @PostMapping("/save")
    public ResponseEntity<DeliverymanDto> addDeliveryman(@Valid  @RequestBody DeliverymanDto deliverymanDto) {
        try {

            Optional<Deliveryman> optionalDeliveryman = deliverymanRepository.findById(deliverymanDto.getId());

            if (!optionalDeliveryman.isPresent()) {
                if (deliverymanDto.getName() != null && !deliverymanDto.getName().isEmpty()) {
                    Deliveryman deliveryman = dtoService.deliverymanDtoToDeliveryman(deliverymanDto, false);
                    Deliveryman createdDeliveryman = deliverymanRepository.save(deliveryman);

                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(deliverymanMapper.toDeliverymanDto(deliveryman));
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
    public ResponseEntity<List<DeliverymanDto>> getDeliverymen() {
        try {

            List<Deliveryman> deliverymanList = deliverymanRepository.findAll();

            if (deliverymanList == null || deliverymanList.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok().body(deliverymanMapper.toDeliverymanDTOs(deliverymanList));
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DeliverymanDto> getDeliveryman(@Valid @PathVariable long id) {

        try {

            Optional<Deliveryman> optionalDeliveryman = deliverymanRepository.findById(id);

            if (optionalDeliveryman.isPresent() && id != 0)
                return ResponseEntity.ok().body(deliverymanMapper.toDeliverymanDto(optionalDeliveryman.get()));
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @GetMapping("/getallactive")
    public ResponseEntity<List<DeliverymanDto>> getAllActiveDeliveryman() {
        try {

            List<Deliveryman> deliverymanList = deliverymanRepository.findAllActiveDeliverymen(true);

            if (deliverymanList != null)
                return ResponseEntity.ok().body(deliverymanMapper.toDeliverymanDTOs(deliverymanList));
            else
                return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<DeliverymanDto> editDeliveryman(@Valid @PathVariable long id,@Valid @RequestBody Deliveryman deliveryman) {
        try {

            Optional<Deliveryman> optionalDeliveryman = deliverymanRepository.findById(id);

            if (optionalDeliveryman.isPresent() && id != 0 && deliveryman.getName() != null) {
                Deliveryman updatedDeliveryman = deliverymanRepository.save(deliveryman);
                return ResponseEntity.ok().body(deliverymanMapper.toDeliverymanDto(updatedDeliveryman));
            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteDeliveryman(@Valid @PathVariable long id) {

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
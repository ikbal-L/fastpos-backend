package com.softlines.fastpos.controller;

import com.softlines.fastpos.domain.*;
import com.softlines.fastpos.dto.DeliverymanDto;
import com.softlines.fastpos.dto.filters.OrderFilter;
import com.softlines.fastpos.dto.mapping.DeliverymanMapper;
import com.softlines.fastpos.dto.service.DtoService;
import com.softlines.fastpos.dto.service.filtering.OrderFilterService;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.DeliverymanRepository;
import com.softlines.fastpos.repository.em.RepositoryDecoratorImp;
import com.softlines.fastpos.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController

@RequestMapping(value = "/api/deliveryman", produces = "application/json; charset=UTF-8")
public class DeliverymanController {

    @Autowired
    private DeliverymanRepository deliverymanRepository;

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @Autowired
    DtoService dtoService;


    @Autowired
    DeliverymanMapper deliverymanMapper;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    OrderFilterService orderFilterService;

    @PostMapping("/save")
    public ResponseEntity<Long> addDeliveryman(@Valid  @RequestBody DeliverymanDto deliverymanDto) {
        try {

            Optional<Deliveryman> optionalDeliveryman = deliverymanRepository.findById(deliverymanDto.getId());

            if (deliverymanDto.getId()==0) {
                if (deliverymanDto.getName() != null && !deliverymanDto.getName().isEmpty()) {
                    Deliveryman deliveryman = dtoService.deliverymanDtoToDeliveryman(deliverymanDto, false);
                    Deliveryman createdDeliveryman = deliverymanRepository.save(deliveryman);

                    return ResponseEntity.status(HttpStatus.CREATED).body(deliveryman.getId());
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

    @GetMapping("/getallwithbalance")
    public ResponseEntity<List<DeliverymanDto>> getDeliverymenWithBalance() {
        try {

            List<Deliveryman> deliverymanList = deliverymanRepository.findAll();
            var states = new ArrayList<OrderState>();
            states.add(OrderState.Delivered);
            states.add(OrderState.DeliveredPartiallyPaid);

            if (deliverymanList == null || deliverymanList.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                var ids = deliverymanList.stream().map(Deliveryman::getId).collect(Collectors.toList());
                var filter = OrderFilter
                        .builder()
                        .states(Optional.of(states))
                        .deliverymanIds(Optional.of(ids))
                        .orderTime(Optional.empty())
                        .build();
                var orders = orderFilterService.buildQuery(filter).getResultList();

                for (Deliveryman deliveryman : deliverymanList) {

                    CreditService.calculateBalance(deliveryman,orders);

                }

                deliverymanRepository.saveAll(deliverymanList);

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

    @GetMapping("/getwithbalance/{id}")
    public ResponseEntity<DeliverymanDto> getDeliverymanWith(@Valid @PathVariable long id) {

        try {

            Optional<Deliveryman> optionalDeliveryman = deliverymanRepository.findById(id);

            if (optionalDeliveryman.isPresent() && id != 0) {
                var deliveryman = optionalDeliveryman.get();
                var states = new ArrayList<OrderState>();
                states.add(OrderState.Delivered);
                states.add(OrderState.DeliveredPartiallyPaid);
                var filter = OrderFilter
                        .builder()
                        .states(Optional.of(states))
                        .deliverymanIds(Optional.of(List.of(deliveryman.getId())))
                        .orderTime(Optional.empty())
                        .build();
                var orders = orderFilterService.buildQuery(filter).getResultList();

                CreditService.calculateBalance(deliveryman,orders);
                deliverymanRepository.save(deliveryman);
                return ResponseEntity.ok().body(deliverymanMapper.toDeliverymanDto(deliveryman));
            }else
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
    public ResponseEntity<DeliverymanDto> editDeliveryman(@Valid @PathVariable long id,@Valid @RequestBody DeliverymanDto deliverymanDto) {
        try {

            Optional<Deliveryman> optionalDeliveryman = deliverymanRepository.findById(id);

            if (optionalDeliveryman.isPresent() && id != 0 && deliverymanDto.getName() != null) {
                var deliveryMan = deliverymanMapper.toDeliveryman(deliverymanDto);
                Deliveryman updatedDeliveryman = deliverymanRepository.save(deliveryMan);
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

                var em = entityManagerFactory.createEntityManager();
                var repo = new RepositoryDecoratorImp<>(deliverymanRepository,em, Deliveryman.class);
                repo.deleteSetNull(deliverymanToDel,id);
                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.noContent().build();
            }

        } catch (Exception exception) {
            return exceptionManagement.getResponseEntityAccordingToException(exception);
        }

    }
}
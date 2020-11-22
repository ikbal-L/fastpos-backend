package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.DeliverymanController;
import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.domain.Descriptor;
import com.softlines.fastpos.dto.mapping.DeliverymanMapper;
import com.softlines.fastpos.repository.DeliverymanRepository;
import com.softlines.fastpos.repository.DeliverymanRepository;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@ActiveProfiles("test")
public class DeliverymanControllerUnitTest {

    @MockBean
    DeliverymanRepository deliverymanRepository;


    @Autowired
    DeliverymanController deliverymanController;

    @Autowired
    DeliverymanMapper deliverymanMapper;

    @Test
    @Order(1)
    public void DeliverymanController_getAll_WithNotEmptyDeliverymansList() {

        List<Deliveryman> deliverymen = Arrays.asList(Deliveryman.builder()
                .id(1)
                .name("ahmed")
                .active(true).build());

        when(deliverymanRepository.findAll()).thenReturn(deliverymen);
        var res = deliverymanController.getDeliverymen();

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).get(0).getName(), deliverymen.get(0).getName());
        assertEquals((res.getBody()).size(), 1);
    }

    @Test
    @Order(2)
    public void DeliverymanController_getAll_WithEmptyDeliverymansList() {
        var deliveryean = new ArrayList<Deliveryman>();
        when(deliverymanRepository.findAll()).thenReturn(deliveryean);
        var res = deliverymanController.getDeliverymen();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(3)
    public void DeliverymanController_getAll_WithNullDeliverymansList() {

        when(deliverymanRepository.findAll()).thenReturn(null);

        var res = deliverymanController.getDeliverymen();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(4)
    public void DeliverymanController_getAll_WithNoDBConnection() {
        when(deliverymanRepository.findAll())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = deliverymanController.getDeliverymen();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }


    /**
     * ------------------>  Save Deliveryman Unit Test  <------------------------
     */

    @Test
    @Order(5)
    public void DeliverymanController_Save_WithData() {

        Deliveryman deliveryman = new Deliveryman();

        deliveryman.setName("fatiha");
        deliveryman.setBackgroundString("red");
        deliveryman.setPhoneNumber("0464150");
        deliveryman.setDescriptor(Descriptor.Deliverymen);

        when(deliverymanRepository.save(any(Deliveryman.class))).thenReturn(deliveryman);
        var res = deliverymanController.addDeliveryman(deliverymanMapper.toDeliverymanDto(deliveryman));

        assertEquals(res.getStatusCode(), HttpStatus.CREATED);

    }


    @Test
    @Order(6)
    public void DeliverymanController_Save_WithNullDeliverymanDescription() {

        //Arrange
        Deliveryman deliveryman = new Deliveryman();
        when(deliverymanRepository.save(any(Deliveryman.class))).thenReturn(deliveryman);

        //Act
        var res = deliverymanController.addDeliveryman(deliverymanMapper.toDeliverymanDto(deliveryman));

        //Assert
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }


    @Test
    @Order(7)
    public void DeliverymanController_Save_WithExistDeliverymanId() {

        //Arrange
        Deliveryman deliveryman = new Deliveryman();
        deliveryman.setId(1);
        when(deliverymanRepository.findById(deliveryman.getId())).thenReturn(Optional.of(deliveryman));

        //Act
        var res = deliverymanController.addDeliveryman(deliverymanMapper.toDeliverymanDto(deliveryman));

        //Assert
        assertEquals(res.getStatusCode(), HttpStatus.FOUND);

    }


    @Test
    @Order(8)
    public void deliverymanController_save_WithNoDBConnection() {

        Deliveryman deliveryman = new Deliveryman();

        deliveryman.setName("fatiha");
        deliveryman.setBackgroundString("red");
        deliveryman.setPhoneNumber("072064150");

        when(deliverymanRepository.save(any(Deliveryman.class))).thenThrow(DataAccessResourceFailureException.class);
        var res = deliverymanController.addDeliveryman(deliverymanMapper.toDeliverymanDto(deliveryman));

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }

    /**
     * ------------------>  GetById Deliveryman Unit Test  <------------------------
     */


    @Test
    @Order(9)
    public void DeliverymanController_getById_WithNotEmptyDeliveryman() {

        Deliveryman deliveryman = new Deliveryman();

        deliveryman.setName("fatiha");
        deliveryman.setBackgroundString("red");
        deliveryman.setPhoneNumber("072064150");

        when(deliverymanRepository.findById(1l)).thenReturn(Optional.ofNullable(deliveryman));
        var res = deliverymanController.getDeliveryman(1);

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).getName(), deliveryman.getName());
    }

    @Test
    @Order(10)
    public void DeliverymanController_getById_WithEmptyDeliveryman() {

        var deliveryman = new Deliveryman();
        when(deliverymanRepository.findById(0l)).thenReturn(Optional.of(deliveryman));
        var res = deliverymanController.getDeliveryman(0);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(11)
    public void DeliverymanController_getById_WithNullDeliveryman() {

        var res = deliverymanController.getDeliveryman(1);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(12)
    public void deliverymanController_getById_WithNoDBConnection() {

        when(deliverymanRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = deliverymanController.getDeliveryman(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Delete Deliveryman Unit Test  <------------------------
     */

    @Test
    @Order(13)
    public void deliverymanController_Delete_WithDeliverymanId() {

        Deliveryman deliveryman = new Deliveryman();
        deliveryman.setId(1);

        deliveryman.setName("fatiha");
        deliveryman.setBackgroundString("red");
        deliveryman.setPhoneNumber("072064150");

        when(deliverymanRepository.findById(1l)).thenReturn(Optional.ofNullable(deliveryman));
        deliverymanController.deleteDeliveryman(1);

        verify(deliverymanRepository, times(1)).delete(deliveryman);


    }

    @Test
    @Order(14)
    public void deliverymanController_Delete_WithNotExistDeliverymanId() {

        when(deliverymanRepository.findById(1l)).thenReturn(null);

        deliverymanController.deleteDeliveryman(1);

        verify(deliverymanRepository, times(1)).findById(1l);
        verifyNoMoreInteractions(deliverymanRepository);

    }

    @Test
    @Order(15)
    public void deliverymanController_Delete_WithNoDBConnection() {

        when(deliverymanRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = deliverymanController.getDeliveryman(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Put Deliveryman Unit Test  <------------------------
     */

    @Test
    @Order(16)
    public void deliverymanController_Put_WithData() {

        Deliveryman deliveryman = new Deliveryman();
        deliveryman.setId(1);

        deliveryman.setName("fatiha");
        deliveryman.setBackgroundString("red");
        deliveryman.setPhoneNumber("072064150");

        when(deliverymanRepository.findById(deliveryman.getId())).thenReturn(Optional.of(deliveryman));
        ResponseEntity<Deliveryman> returned = deliverymanController.editDeliveryman(1, deliveryman);

        verify(deliverymanRepository, times(1)).findById(deliveryman.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.OK);

    }

    @Test
    @Order(17)
    public void deliverymanController_Put_WithIdNotExist() {
        Deliveryman deliveryman = new Deliveryman();
        deliveryman.setName("fatiha");
        deliveryman.setBackgroundString("red");
        deliveryman.setPhoneNumber("072064150");

        when(deliverymanRepository.findById(deliveryman.getId())).thenReturn(Optional.empty());
        ResponseEntity<Deliveryman> returned = deliverymanController.editDeliveryman(0, deliveryman);

        verify(deliverymanRepository, times(1)).findById(deliveryman.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(18)
    public void deliverymanController_Put_WithNullData() {

        Deliveryman deliveryman = new Deliveryman();
        deliveryman.setId(1);


        when(deliverymanRepository.findById(deliveryman.getId())).thenReturn(Optional.of(deliveryman));
        ResponseEntity<Deliveryman> returned = deliverymanController.editDeliveryman(1, deliveryman);

        verify(deliverymanRepository, times(1)).findById(deliveryman.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(19)
    public void deliverymanController_Put_WithNoDBConnection() {

        when(deliverymanRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = deliverymanController.getDeliveryman(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


}

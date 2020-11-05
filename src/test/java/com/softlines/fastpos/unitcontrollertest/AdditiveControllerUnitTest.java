package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.AdditiveController;
import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.dto.mapping.AdditiveMapper;
import com.softlines.fastpos.repository.AdditiveRepository;
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
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class AdditiveControllerUnitTest {

    @MockBean
    AdditiveRepository additiveRepository;


    @Autowired
    AdditiveController additiveController;

    @Autowired
    AdditiveMapper additiveMapper;

    @Test
    @Order(1)
    public void AdditiveController_getAll_WithNotEmptyAdditivesList() {

        var additives = Arrays.asList(
                Additive.builder()
                        .id(1)
                        .description("harrisa")
                        .build());

        when(additiveRepository.findAll()).thenReturn(additives);
        var res = additiveController.getAdditives();

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).get(0).getDescription(), additives.get(0).getDescription());
        assertEquals((res.getBody()).size(), 1);
    }

    @Test
    @Order(2)
    public void AdditiveController_getAll_WithEmptyAdditivesList() {
        var additives = new ArrayList<Additive>();
        when(additiveRepository.findAll()).thenReturn(additives);
        var res = additiveController.getAdditives();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(3)
    public void AdditiveController_getAll_WithNullAdditivesList() {

        when(additiveRepository.findAll()).thenReturn(null);

        var res = additiveController.getAdditives();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(4)
    public void AdditiveController_getAll_getAdditivesWithNoDBConnection() {
        when(additiveRepository.findAll())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = additiveController.getAdditives();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }


    /**
     * ------------------>  Save Additive Unit Test  <------------------------
     */

    @Test
    @Order(5)
    public void AdditiveController_Save_WithData() {


        var additive =
                Additive.builder()
                        .id(1)
                        .description("harrisa")
                        .backgroundString("red")
                        .rank(2)
                        .build();

        when(additiveRepository.save(any(Additive.class))).thenReturn(additive);

        var res = additiveController.addAdditive(additive);

        assertEquals(res.getStatusCode(), HttpStatus.CREATED);
        assertEquals((res.getBody()).getDescription(), additive.getDescription());

    }


    @Test
    @Order(6)
    public void AdditiveController_Save_WithNullAdditiveDescription() {

        //Arrange
        var additive = Additive.builder().build();
        when(additiveRepository.save(any(Additive.class))).thenReturn(additive);

        //Act
        var res = additiveController.addAdditive(additive);

        //Assert
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }


    @Test
    @Order(7)
    public void AdditiveController_Save_WithExistAdditiveId() {

        //Arrange
        var additive = Additive.builder().id(1).description("harrisa").build();
        when( additiveRepository.findById(additive.getId())).thenReturn(Optional.of(additive));

        //Act
        var res = additiveController.addAdditive(additive);

        //Assert
        assertEquals(res.getStatusCode(), HttpStatus.FOUND);

    }


    @Test
    @Order(8)
    public void additiveController_save_WithNoDBConnection() {

        var additive =
                Additive.builder()
                        .id(1l)
                        .description("harrisa")
                        .backgroundString("red")
                        .rank(2)
                        .build();

        when(additiveRepository.save(any(Additive.class))).thenThrow(DataAccessResourceFailureException.class);
        var res = additiveController.addAdditive(additive);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  GetById Additive Unit Test  <------------------------
     */


    @Test
    @Order(9)
    public void AdditiveController_getById_WithNotEmptyAdditive() {

        var additive =
                Additive.builder()
                        .id(1)
                        .rank(5)
                        .description("harrisa")
                        .build();

        when(additiveRepository.findById(1l)).thenReturn(java.util.Optional.ofNullable(additive));

        var res = additiveController.getAdditive(1);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).getDescription(), additive.getDescription());
    }

    @Test
    @Order(10)
    public void AdditiveController_getById_WithEmptyAdditive() {

        var additive = new Additive();
        when(additiveRepository.findById(0l)).thenReturn(java.util.Optional.of(additive));
        var res = additiveController.getAdditive(0);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(11)
    public void AdditiveController_getById_WithNullAdditive() {

        var res = additiveController.getAdditive(1);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(12)
    public void additiveController_getById_getAdditivesWithNoDBConnection() {

        when(additiveRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = additiveController.getAdditive(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Delete Additive Unit Test  <------------------------
     */

    @Test
    public void additiveController_Delete_WithAdditiveId() {

        var additive =
                Additive.builder()
                        .id(1l)
                        .rank(5)
                        .description("harrisa")
                        .build();

        when(additiveRepository.findById(1l)).thenReturn(Optional.ofNullable(additive));
        additiveController.deleteAdditive(1);

        verify(additiveRepository, times(1)).delete(additive);


    }

    @Test
    public void additiveController_Delete_WithNotExistAdditiveId() {

        when(additiveRepository.findById(1l)).thenReturn(null);

        additiveController.deleteAdditive(1);

        verify(additiveRepository, times(1)).findById(1l);
        verifyNoMoreInteractions(additiveRepository);

    }

    @Test
    @Order(12)
    public void additiveController_Delete_getAdditivesWithNoDBConnection() {

        when(additiveRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = additiveController.getAdditive(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Put Additive Unit Test  <------------------------
     */

    @Test
    public void additiveController_Put_WithData() {

        var additive = Additive.builder()
                .id(1l)
                .rank(5)
                .description("harrisa")
                .build();

        when(additiveRepository.findById(additive.getId())).thenReturn(Optional.of(additive));
        ResponseEntity<Additive> returned = additiveController.editAdditive(1, additive);

        verify(additiveRepository, times(1)).findById(additive.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void additiveController_Put_WithIdNotExist() {

        var additive = Additive.builder()
                .id(10l)
                .rank(5)
                .description("harrisa")
                .build();

        when(additiveRepository.findById(additive.getId())).thenReturn(Optional.empty());
        ResponseEntity<Additive> returned = additiveController.editAdditive(10, additive);

        verify(additiveRepository, times(1)).findById(additive.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void additiveController_Put_WithNullData() {

        var additive = Additive.builder().id(1).build();

        when(additiveRepository.findById(additive.getId())).thenReturn(Optional.of(additive));
        ResponseEntity<Additive> returned = additiveController.editAdditive(1, additive);

        verify(additiveRepository, times(1)).findById(additive.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(12)
    public void additiveController_Put_getAdditivesWithNoDBConnection() {

        when(additiveRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = additiveController.getAdditive(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


}

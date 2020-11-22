package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.RestaurentController;
import com.softlines.fastpos.domain.Restaurent;
import com.softlines.fastpos.dto.RestaurentDto;
import com.softlines.fastpos.dto.mapping.RestaurentMapper;
import com.softlines.fastpos.exceptionmanagement.ExceptionManagement;
import com.softlines.fastpos.repository.RestaurentRepository;
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

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class RestaurentControllerUnitTest {

    @MockBean
    RestaurentRepository restaurentRepository;

    @Autowired
    RestaurentController restaurentController;

    @Autowired
    RestaurentMapper restaurentMapper;

    ExceptionManagement exceptionManagement = new ExceptionManagement();

    @Test
    @Order(1)
    public void restaurentController_getAll_WithNotEmptyRestaurentList() {

        var restaurents = Arrays.asList(
                Restaurent.builder()
                        .id(1l)
                        .name("Tacos")
                        .build()
        );

        when(restaurentRepository.findAllRestaurentWithAnnexes()).thenReturn(restaurents);

        var res = restaurentController.getRestaurent();
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).get(0).getName(), restaurents.get(0).getName());
        assertEquals((res.getBody()).size(), 1);

    }

    @Test
    @Order(2)
    public void restaurentController_getAll_WithEmptyRestaurentList() {

        var restaurents = new ArrayList<Restaurent>();

        when(restaurentRepository.findAllRestaurentWithAnnexes()).thenReturn(restaurents);
        var res = restaurentController.getRestaurent();

        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(3)
    public void restaurentController_getAll_WithNullRestaurentList() {

        when(restaurentRepository.findAllRestaurentWithAnnexes()).thenReturn(null);

        var res = restaurentController.getRestaurent();

        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }


    @Test
    @Order(4)
    public void restaurentController_getAll_WithNoDBConnection() {
        when(restaurentRepository.findAllRestaurentWithAnnexes())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = restaurentController.getRestaurent();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }

    /**
     * ------------------>  Put Restaurent Unit Test  <------------------------
     */


    @Test
    @Order(1)
    public void restaurentController_getMany_WithNotEmptyRestaurentList() {

        var restaurents = Arrays.asList(
                Restaurent.builder()
                        .id(1l)
                        .name("Tacos")
                        .build()
        );

        when(restaurentRepository.findAllRestaurentWithAnnexes()).thenReturn(restaurents);

        var res = restaurentController.getRestaurent();
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).get(0).getName(), restaurents.get(0).getName());
        assertEquals((res.getBody()).size(), 1);

    }

    @Test
    @Order(2)
    public void restaurentController_getMany_WithEmptyRestaurentList() {
        var restaurents = new ArrayList<Restaurent>();
        when(restaurentRepository.findAllRestaurentWithAnnexes()).thenReturn(restaurents);
        var res = restaurentController.getRestaurent();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(3)
    public void restaurentController_getMany_WithNullRestaurentList() {
        when(restaurentRepository.findAllRestaurentWithAnnexes()).thenReturn(null);

        var res = restaurentController.getRestaurent();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }


    @Test
    @Order(4)
    public void restaurentController_getMany_WithNoDBConnection() {
        when(restaurentRepository.findAllRestaurentWithAnnexes())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = restaurentController.getRestaurent();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }


    /**
     * ------------------>  Save Restaurent Unit Test  <------------------------
     */


    @Test
    @Order(5)
    public void restaurentController_Save_WithData() {

        var restaurent = Restaurent.builder()
                .name("tacos")
                .annexes(Arrays.asList(Annex.builder().id(1).build()))
                .build();

        when(restaurentRepository.save(any(Restaurent.class))).thenReturn(restaurent);

        var res = restaurentController.addRestaurent(restaurentMapper.toRestaurentDto(restaurent));
        assertEquals(res.getStatusCode(), HttpStatus.CREATED);
        assertEquals((res.getBody()), restaurentMapper.toRestaurentDto(restaurent));

    }

    @Test
    @Order(6)
    public void restaurentController_Save_WithExistRestaurent() {

        var restaurent = Restaurent.builder()
                .id(1)
                .name("tacos")
                .annexes(Arrays.asList(Annex.builder().id(1).build()))
                .build();

        when(restaurentRepository.findByIdRestaurentWithAnnexes(restaurent.getId())).thenReturn(restaurent);
        var res = restaurentController.addRestaurent(restaurentMapper.toRestaurentDto(restaurent));

        assertEquals(res.getStatusCode(), HttpStatus.FOUND);

    }


    @Test
    @Order(7)
    public void restaurentController_Save_WithoutData() {

        var restaurent = Restaurent.builder().build();

        when(restaurentRepository.save(any(Restaurent.class))).thenReturn(restaurent);
        var res = restaurentController.addRestaurent(restaurentMapper.toRestaurentDto(restaurent));

        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }


    @Test
    @Order(8)
    public void restaurentController_save_WithNoDBConnection() {

        var restaurent = Restaurent.builder()
                .name("harrisa")
                .annexes(Arrays.asList(Annex.builder().build()))
                .build();

        when(restaurentRepository.save(any(Restaurent.class))).thenThrow(DataAccessResourceFailureException.class);
        var res = restaurentController.addRestaurent(restaurentMapper.toRestaurentDto(restaurent));

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  GetById Restaurent Unit Test  <------------------------
     */


    @Test
    @Order(9)
    public void restaurentController_getById_WithNotEmptyRestaurent() {

        var restaurent =
                Restaurent.builder()
                        .id(1)
                        .build();

        when(restaurentRepository.findByIdRestaurentWithAnnexes(1l)).thenReturn(restaurent);

        var res = restaurentController.getRestaurent(1);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).getName(), restaurent.getName());
    }

    @Test
    @Order(10)
    public void restaurentController_getById_WithEmptyRestaurent() {

        Restaurent restaurent = new Restaurent();

        when(restaurentRepository.findByIdRestaurentWithAnnexes(0l)).thenReturn(null);
        var res = restaurentController.getRestaurent(0);

        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(11)
    public void restaurentController_getById_WithNullRestaurent() {

        var res = restaurentController.getRestaurent(1);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(12)
    public void restaurentController_getById_WithNoDBConnection() {

        when(restaurentRepository.findByIdRestaurentWithAnnexes(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = restaurentController.getRestaurent(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Delete Restaurent Unit Test  <------------------------
     */

    @Test
    @Order(13)
    public void restaurentController_Delete_WithRestaurentId() {

        var restaurent = Restaurent.builder()
                .id(1l)
                .name("name")
                .build();

        when(restaurentRepository.findByIdRestaurentWithAnnexes(1l)).thenReturn(restaurent);
        restaurentController.deleteRestaurent(1);

        verify(restaurentRepository, times(1)).delete(restaurent);


    }

    @Test
    @Order(14)
    public void restaurentController_Delete_WithNotExistRestaurentId() {

        when(restaurentRepository.findByIdRestaurentWithAnnexes(1l)).thenReturn(null);

        restaurentController.deleteRestaurent(1);

        verify(restaurentRepository, times(1)).findByIdRestaurentWithAnnexes(1l);
        verifyNoMoreInteractions(restaurentRepository);

    }

    @Test
    @Order(15)
    public void restaurentController_Delete_WithNoDBConnection() {

        when(restaurentRepository.findByIdRestaurentWithAnnexes(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = restaurentController.getRestaurent(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Put Restaurent Unit Test  <------------------------
     */

    @Test
    @Order(16)
    public void restaurentController_Put_WithData() {

        var restaurent = Restaurent.builder()
                .id(1l)
                .name("harrisa")
                .build();

        when(restaurentRepository.findByIdRestaurentWithAnnexes(restaurent.getId())).thenReturn(restaurent);
        ResponseEntity<RestaurentDto> returned = restaurentController.editRestaurent(1, restaurentMapper.toRestaurentDto(restaurent));

        verify(restaurentRepository, times(1)).findByIdRestaurentWithAnnexes(restaurent.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.OK);

    }

    @Test
    @Order(17)
    public void restaurentController_Put_WithIdNotExist() {

        var restaurent = Restaurent.builder()
                .id(10l)
                .name("harrisa")
                .build();

        when(restaurentRepository.findByIdRestaurentWithAnnexes(restaurent.getId())).thenReturn(null);
        ResponseEntity<RestaurentDto> returned = restaurentController.editRestaurent(10, restaurentMapper.toRestaurentDto(restaurent));

        verify(restaurentRepository, times(1)).findByIdRestaurentWithAnnexes(restaurent.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(18)
    public void restaurentController_Put_WithNullData() {

        var restaurent = Restaurent.builder().id(1).build();

        when(restaurentRepository.findByIdRestaurentWithAnnexes(restaurent.getId())).thenReturn(restaurent);
        var returned = restaurentController.editRestaurent(1, restaurentMapper.toRestaurentDto(restaurent));

        verify(restaurentRepository, times(1)).findByIdRestaurentWithAnnexes(restaurent.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(19)
    public void restaurentController_Put_WithNoDBConnection() {

        when(restaurentRepository.findByIdRestaurentWithAnnexes(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = restaurentController.getRestaurent(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


}

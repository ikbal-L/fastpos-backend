package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.WaiterController;
import com.softlines.fastpos.domain.Person;
import com.softlines.fastpos.domain.Waiter;
import com.softlines.fastpos.dto.WaiterDto;
import com.softlines.fastpos.dto.mapping.WaiterMapper;
import com.softlines.fastpos.repository.WaiterRepository;
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
public class WaiterControllerUnitTest {

    @MockBean
    WaiterRepository waiterRepository;

    @Autowired
    WaiterController waiterController;

    @Autowired
    WaiterMapper waiterMapper;

    @Test
    @Order(1)
    public void WaiterController_getAll_WithNotEmptyWaitersList() {

        List<Waiter> waiters = Arrays.asList(Waiter.builder()
                .id(1)
                .name("ahmed")
                .active(true).build());

        when(waiterRepository.findAll()).thenReturn(waiters);
        var res = waiterController.getWaiters();

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).get(0).getName(), waiters.get(0).getName());
        assertEquals((res.getBody()).size(), 1);
    }

    @Test
    @Order(2)
    public void WaiterController_getAll_WithEmptyWaitersList() {
        var waiters = new ArrayList<Waiter>();
        when(waiterRepository.findAll()).thenReturn(waiters);
        var res = waiterController.getWaiters();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(3)
    public void WaiterController_getAll_WithNullWaitersList() {

        when(waiterRepository.findAll()).thenReturn(null);

        var res = waiterController.getWaiters();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(4)
    public void WaiterController_getAll_WithNoDBConnection() {
        when(waiterRepository.findAll())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = waiterController.getWaiters();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }


    /**
     * ------------------>  Save Waiter Unit Test  <------------------------
     */

    @Test
    @Order(5)
    public void WaiterController_Save_WithData() {

        Waiter waiter = new Waiter();
        waiter.setName("fatiha");
        waiter.setBackgroundString("red");
        waiter.setPhoneNumber("0464150");

        when(waiterRepository.save(any(Waiter.class))).thenReturn(waiter);
        var res = waiterController.addWaiter(waiterMapper.toWaiterDto(waiter));

        assertEquals(res.getStatusCode(), HttpStatus.CREATED);

    }


    @Test
    @Order(7)
    public void WaiterController_Save_WithExistWaiterId() {

        //Arrange
        Waiter waiter = new Waiter();
        waiter.setId(1);
        when(waiterRepository.findById(waiter.getId())).thenReturn(Optional.of(waiter));

        //Act
        var res = waiterController.addWaiter(waiterMapper.toWaiterDto(waiter));

        //Assert
        assertEquals(res.getStatusCode(), HttpStatus.FOUND);

    }


    @Test
    @Order(8)
    public void waiterController_save_WithNoDBConnection() {


        var waiter = Waiter.builder().name("sd").build();

        when(waiterRepository.save(any(Waiter.class))).thenThrow(DataAccessResourceFailureException.class);
        var res = waiterController.addWaiter(waiterMapper.toWaiterDto(waiter));

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }

    /**
     * ------------------>  GetById Waiter Unit Test  <------------------------
     */


    @Test
    @Order(9)
    public void WaiterController_getById_WithNotEmptyWaiter() {

        Waiter waiter = new Waiter();

        waiter.setName("fatiha");
        waiter.setBackgroundString("red");
        waiter.setPhoneNumber("072064150");


        when(waiterRepository.findById(1l)).thenReturn(Optional.ofNullable(waiter));

        var res = waiterController.getWaiter(1);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).getName(), waiter.getName());
    }

    @Test
    @Order(10)
    public void WaiterController_getById_WithEmptyWaiter() {

        var waiter = new Waiter();
        when(waiterRepository.findById(0l)).thenReturn(Optional.of(waiter));
        var res = waiterController.getWaiter(0);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(11)
    public void WaiterController_getById_WithNullWaiter() {

        var res = waiterController.getWaiter(1);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(12)
    public void waiterController_getById_WithNoDBConnection() {

        when(waiterRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = waiterController.getWaiter(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Delete Waiter Unit Test  <------------------------
     */

    @Test
    @Order(13)
    public void waiterController_Delete_WithWaiterId() {

        Waiter waiter = new Waiter();
        waiter.setId(1);

        waiter.setName("fatiha");
        waiter.setBackgroundString("red");
        waiter.setPhoneNumber("072064150");

        when(waiterRepository.findById(1l)).thenReturn(Optional.ofNullable(waiter));
        waiterController.deleteWaiter(1);

        verify(waiterRepository, times(1)).delete(waiter);


    }

    @Test
    @Order(14)
    public void waiterController_Delete_WithNotExistWaiterId() {

        when(waiterRepository.findById(1l)).thenReturn(null);

        waiterController.deleteWaiter(1);

        verify(waiterRepository, times(1)).findById(1l);
        verifyNoMoreInteractions(waiterRepository);

    }

    @Test
    @Order(15)
    public void waiterController_Delete_WithNoDBConnection() {

        when(waiterRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = waiterController.getWaiter(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Put Waiter Unit Test  <------------------------
     */

    @Test
    @Order(16)
    public void waiterController_Put_WithData() {

        Waiter waiter = new Waiter();
        waiter.setId(1);

        waiter.setName("fatiha");
        waiter.setBackgroundString("red");
        waiter.setPhoneNumber("072064150");

        WaiterDto waiterDto= waiterMapper.toWaiterDto(waiter);

        when(waiterRepository.findById(waiter.getId())).thenReturn(Optional.of(waiter));
        ResponseEntity<WaiterDto> returned = waiterController.editWaiter(1, waiterDto);

        verify(waiterRepository, times(1)).findById(waiter.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.OK);

    }

    @Test
    @Order(17)
    public void waiterController_Put_WithIdNotExist() {
        Waiter waiter = new Waiter();
        waiter.setName("fatiha");
        waiter.setBackgroundString("red");
        waiter.setPhoneNumber("072064150");

     WaiterDto waiterDto= waiterMapper.toWaiterDto(waiter);

        when(waiterRepository.findById(waiter.getId())).thenReturn(Optional.empty());
        ResponseEntity<WaiterDto> returned = waiterController.editWaiter(0, waiterDto);

        verify(waiterRepository, times(1)).findById(waiter.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }



    @Test
    @Order(19)
    public void waiterController_Put_WithNoDBConnection() {

        when(waiterRepository.findById(5L))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = waiterController.getWaiter(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }

}

<<<<<<< HEAD
//package com.softlines.fastpos.unitcontrollertest;
//
//import com.softlines.fastpos.ModelApplication;
//import com.softlines.fastpos.controller.AnnexController;
//import com.softlines.fastpos.domain.Annex;
//import com.softlines.fastpos.domain.Annex;
//import com.softlines.fastpos.repository.AnnexRepository;
//import org.junit.Test;
//import org.junit.jupiter.api.Order;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.dao.DataAccessResourceFailureException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
//@AutoConfigureMockMvc
//@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
//public class AnnexControllerUnitTest {
//
//    @MockBean
//    AnnexRepository annexRepository ;
//
//
//    @Autowired
//    AnnexController annexController;
//
//
//    @Test
//    @Order(1)
//    public void annexController_getAll_WithNotEmptyAnnexsList() {
//
//        var annexes = Arrays.asList(
//                Annex.builder()
//                        .id(1)
//                        .name("annex name")
//                        .build());
//
//        when(annexRepository.findAll()).thenReturn(annexes);
//        var res = annexController.getAnnexs();
//
//        assertEquals(res.getStatusCode(), HttpStatus.OK);
//        assertEquals((res.getBody()).get(0).getName(), annexes.get(0).getName());
//        assertEquals((res.getBody()).size(), 1);
//    }
//
//    @Test
//    @Order(2)
//    public void annexController_getAll_WithEmptyAnnexsList() {
//        var annexes = new ArrayList<Annex>();
//
//        when(annexRepository.findAll()).thenReturn(annexes);
//        var res = annexController.getAnnexs();
//
//        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
//    }
//
//    @Test
//    @Order(3)
//    public void annexController_getAll_WithNullAnnexsList() {
//
//        when(annexRepository.findAll()).thenReturn(null);
//
//        var res = annexController.getAnnexs();
//
//        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
//
//    }
//
//    @Test
//    @Order(4)
//    public void annexController_getAll_getAnnexsWithNoDBConnection() {
//        when(annexRepository.findAll())
//                .thenThrow(DataAccessResourceFailureException.class);
//
//        var res = annexController.getAnnexs();
//
//        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
//    }
//
//
//    /**
//     * ------------------>  Save Annex Unit Test  <------------------------
//     */
//
//    @Test
//    @Order(5)
//    public void annexController_Save_WithData() {
//
//
//        var annex =
//                Annex.builder()
//                        .id(1)
//                        .name("name")
//                        .build();
//
//        when(annexRepository.save(any(Annex.class))).thenReturn(annex);
//
//        var res = annexController.addAnnex(annex);
//
//        assertEquals(res.getStatusCode(), HttpStatus.CREATED);
//        assertEquals((res.getBody()).getName(), annex.getName());
//
//    }
//
//
//    @Test
//    @Order(6)
//    public void annexController_Save_WithNullAnnexDescription() {
//
//        //Arrange
//        var annex = Annex.builder().build();
//        when(annexRepository.save(any(Annex.class))).thenReturn(annex);
//
//        //Act
//        var res = annexController.addAnnex(annex);
//
//        //Assert
//        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
//
//    }
//
//
//    @Test
//    @Order(7)
//    public void annexController_Save_WithExistAnnexId() {
//
//        //Arrange
//        var Annex = Annex.builder().id(1).description("harrisa").build();
//        when( annexRepository.findRoleById(Annex.getId())).thenReturn(Optional.of(Annex));
//
//        //Act
//        var res = annexController.addAnnex(Annex);
//
//        //Assert
//        assertEquals(res.getStatusCode(), HttpStatus.FOUND);
//
//    }
//
//
//    @Test
//    @Order(8)
//    public void annexController_save_WithNoDBConnection() {
//
//        var Annex =
//                Annex.builder()
//                        .id(1l)
//                        .description("harrisa")
//                        .backgroundString("red")
//                        .rank(2)
//                        .build();
//
//        when(annexRepository.save(any(Annex.class))).thenThrow(DataAccessResourceFailureException.class);
//        var res = annexController.addAnnex(Annex);
//
//        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
//
//    }
//
//
//    /**
//     * ------------------>  GetById Annex Unit Test  <------------------------
//     */
//
//
//    @Test
//    @Order(9)
//    public void annexController_getById_WithNotEmptyAnnex() {
//
//        var Annex =
//                Annex.builder()
//                        .id(1)
//                        .rank(5)
//                        .description("harrisa")
//                        .build();
//
//        when(annexRepository.findRoleById(1l)).thenReturn(Optional.ofNullable(Annex));
//
//        var res = annexController.getAnnex(1);
//        assertEquals(res.getStatusCode(), HttpStatus.OK);
//        assertEquals((res.getBody()).getDescription(), Annex.getDescription());
//    }
//
//    @Test
//    @Order(10)
//    public void annexController_getById_WithEmptyAnnex() {
//
//        var Annex = new Annex();
//        when(annexRepository.findRoleById(0l)).thenReturn(Optional.of(Annex));
//        var res = annexController.getAnnex(0);
//        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
//
//    }
//
//    @Test
//    @Order(11)
//    public void annexController_getById_WithNullAnnex() {
//
//        var res = annexController.getAnnex(1);
//        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
//
//    }
//
//    @Test
//    @Order(12)
//    public void annexController_getById_getAnnexsWithNoDBConnection() {
//
//        when(annexRepository.findRoleById(5l))
//                .thenThrow(DataAccessResourceFailureException.class);
//
//        var res = annexController.getAnnex(5);
//
//        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
//
//    }
//
//
//    /**
//     * ------------------>  Delete Annex Unit Test  <------------------------
//     */
//
//    @Test
//    public void annexController_Delete_WithAnnexId() {
//
//        var Annex =
//                Annex.builder()
//                        .id(1l)
//                        .rank(5)
//                        .description("harrisa")
//                        .build();
//
//        when(annexRepository.findRoleById(1l)).thenReturn(Optional.ofNullable(Annex));
//        annexController.deleteAnnex(1);
//
//        verify(annexRepository, times(1)).delete(Annex);
//
//
//    }
//
//    @Test
//    public void annexController_Delete_WithNotExistAnnexId() {
//
//        when(annexRepository.findRoleById(1l)).thenReturn(null);
//
//        annexController.deleteAnnex(1);
//
//        verify(annexRepository, times(1)).findRoleById(1l);
//        verifyNoMoreInteractions(annexRepository);
//
//    }
//
//    @Test
//    @Order(12)
//    public void annexController_Delete_getAnnexsWithNoDBConnection() {
//
//        when(annexRepository.findRoleById(5l))
//                .thenThrow(DataAccessResourceFailureException.class);
//
//        var res = annexController.getAnnex(5);
//
//        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
//
//    }
//
//
//    /**
//     * ------------------>  Put Annex Unit Test  <------------------------
//     */
//
//    @Test
//    public void annexController_Put_WithData() {
//
//        var Annex = Annex.builder()
//                .id(1l)
//                .rank(5)
//                .description("harrisa")
//                .build();
//
//        when(annexRepository.findRoleById(Annex.getId())).thenReturn(Optional.of(Annex));
//        ResponseEntity<Annex> returned = annexController.editAnnex(1, Annex);
//
//        verify(annexRepository, times(1)).findRoleById(Annex.getId());
//        assertEquals(returned.getStatusCode(), HttpStatus.OK);
//
//    }
//
//    @Test
//    public void annexController_Put_WithIdNotExist() {
//
//        var Annex = Annex.builder()
//                .id(10l)
//                .rank(5)
//                .description("harrisa")
//                .build();
//
//        when(annexRepository.findRoleById(Annex.getId())).thenReturn(Optional.empty());
//        ResponseEntity<Annex> returned = annexController.editAnnex(10, Annex);
//
//        verify(annexRepository, times(1)).findRoleById(Annex.getId());
//        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);
//
//    }
//
//    @Test
//    public void annexController_Put_WithNullData() {
//
//        var Annex = Annex.builder().id(1).build();
//
//        when(annexRepository.findRoleById(Annex.getId())).thenReturn(Optional.of(Annex));
//        ResponseEntity<Annex> returned = annexController.editAnnex(1, Annex);
//
//        verify(annexRepository, times(1)).findRoleById(Annex.getId());
//        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);
//
//    }
//
//    @Test
//    @Order(12)
//    public void annexController_Put_getAnnexsWithNoDBConnection() {
//
//        when(annexRepository.findRoleById(5l))
//                .thenThrow(DataAccessResourceFailureException.class);
//
//        var res = annexController.getAnnex(5);
//
//        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
//
//    }
//
//
//}
=======
package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.AnnexController;
import com.softlines.fastpos.domain.Annex;
import com.softlines.fastpos.repository.AnnexRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class AnnexControllerUnitTest {

    @MockBean
    AnnexRepository annexRepository ;

    @Autowired
    AnnexController annexController;

    @Test
    @Order(1)
    public void annexController_getAll_WithNotEmptyAnnexesList() {

        var annexes = Arrays.asList(
                Annex.builder()
                        .id(1)
                        .name("annex name")
                        .build());

        when(annexRepository.findAll()).thenReturn(annexes);
        var res = annexController.getAnnexes();

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).get(0).getName(), annexes.get(0).getName());
        assertEquals((res.getBody()).size(), 1);
    }

    @Test
    @Order(2)
    public void annexController_getAll_WithEmptyAnnexesList() {
        var annexes = new ArrayList<Annex>();

        when(annexRepository.findAll()).thenReturn(annexes);
        var res = annexController.getAnnexes();

        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(3)
    public void annexController_getAll_WithNullAnnexesList() {

        when(annexRepository.findAll()).thenReturn(null);

        var res = annexController.getAnnexes();

        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(4)
    public void annexController_getAll_WithNoDBConnection() {
        when(annexRepository.findAll())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = annexController.getAnnexes();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }


    /**
     * ------------------>  Save Annex Unit Test  <------------------------
     */

    @Test
    @Order(5)
    public void annexController_Save_WithData() {


        var annex =
                Annex.builder()
                        .id(1)
                        .name("name")
                        .build();

        when(annexRepository.save(any(Annex.class))).thenReturn(annex);

        var res = annexController.addAnnex(annex);

        assertEquals(res.getStatusCode(), HttpStatus.CREATED);
        assertEquals((res.getBody()).getName(), annex.getName());

    }


    @Test
    @Order(6)
    public void annexController_Save_WithNullAnnexDescription() {

        //Arrange
        var annex = Annex.builder().build();
        when(annexRepository.save(any(Annex.class))).thenReturn(annex);

        //Act
        var res = annexController.addAnnex(annex);

        //Assert
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }


    @Test
    @Order(7)
    public void annexController_Save_WithExistAnnexId() {

        //Arrange
        var annex = Annex.builder().id(1).name("harrisa").build();
        when( annexRepository.findById(annex.getId())).thenReturn(Optional.of(annex));

        //Act
        var res = annexController.addAnnex(annex);

        //Assert
        assertEquals(res.getStatusCode(), HttpStatus.FOUND);

    }


    @Test
    @Order(8)
    public void annexController_save_WithNoDBConnection() {

        var annex =
                Annex.builder()
                        .id(1l)
                        .name("name")
                        .address("red")
                        .build();

        when(annexRepository.save(any(Annex.class))).thenThrow(DataAccessResourceFailureException.class);
        var res = annexController.addAnnex(annex);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  GetById Annex Unit Test  <------------------------
     */


    @Test
    @Order(9)
    public void annexController_getById_WithNotEmptyAnnex() {

        var annex =
                Annex.builder()
                        .id(1)
                        .name("name")
                        .build();

        when(annexRepository.findById(1l)).thenReturn(Optional.ofNullable(annex));

        var res = annexController.getAnnex(1);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).getName(), annex.getName());
    }

    @Test
    @Order(10)
    public void annexController_getById_WithEmptyAnnex() {

        var Annex = new Annex();
        when(annexRepository.findById(0l)).thenReturn(Optional.of(Annex));
        var res = annexController.getAnnex(0);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(11)
    public void annexController_getById_WithNullAnnex() {

        var res = annexController.getAnnex(1);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(12)
    public void annexController_getById_WithNoDBConnection() {

        when(annexRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = annexController.getAnnex(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Delete Annex Unit Test  <------------------------
     */

    @Test
    @Order(13)
    public void annexController_Delete_WithAnnexId() {

        var annex =
                Annex.builder()
                        .id(1l)
                        .name("name")
                        .build();

        when(annexRepository.findById(1l)).thenReturn(Optional.ofNullable(annex));
        annexController.deleteAnnex(1);

        verify(annexRepository, times(1)).delete(annex);


    }

    @Test
    @Order(14)
    public void annexController_Delete_WithNotExistAnnexId() {

        when(annexRepository.findById(1l)).thenReturn(null);

        annexController.deleteAnnex(1);

        verify(annexRepository, times(1)).findById(1l);
        verifyNoMoreInteractions(annexRepository);

    }

    @Test
    @Order(15)
    public void annexController_Delete_getAnnexesWithNoDBConnection() {

        when(annexRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = annexController.getAnnex(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Put Annex Unit Test  <------------------------
     */

    @Test
    @Order(16)
    public void annexController_Put_WithData() {

        var annex = Annex.builder()
                .id(1l)
                .name("harrisa")
                .build();

        when(annexRepository.findById(annex.getId())).thenReturn(Optional.of(annex));
        ResponseEntity<Annex> returned = annexController.editAnnex(1, annex);

        verify(annexRepository, times(1)).findById(annex.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.OK);

    }

    @Test
    @Order(17)
    public void annexController_Put_WithIdNotExist() {

        var annex = Annex.builder()
                .id(10l)
                .name("harrisa")
                .build();

        when(annexRepository.findById(annex.getId())).thenReturn(Optional.empty());
        ResponseEntity<Annex> returned = annexController.editAnnex(10, annex);

        verify(annexRepository, times(1)).findById(annex.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(18)
    public void annexController_Put_WithNullData() {

        var annex = Annex.builder().id(1).build();

        when(annexRepository.findById(annex.getId())).thenReturn(Optional.of(annex));
        ResponseEntity<Annex> returned = annexController.editAnnex(1, annex);

        verify(annexRepository, times(1)).findById(annex.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    @Order(19)
    public void annexController_Put_WithNoDBConnection() {

        when(annexRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = annexController.getAnnex(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


}
>>>>>>> origin/unit_product

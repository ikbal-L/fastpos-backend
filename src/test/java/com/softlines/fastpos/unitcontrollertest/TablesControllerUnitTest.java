package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.TablesController;
import com.softlines.fastpos.domain.Tables;
import com.softlines.fastpos.dto.TableDto;
import com.softlines.fastpos.dto.mapping.TableMapper;
import com.softlines.fastpos.repository.TableRepository;
import org.junit.Test;
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
public class TablesControllerUnitTest {

    @MockBean
    TableRepository tableRepository;

    @Autowired
    TablesController tablesController;

    @Autowired
    TableMapper tableMapper;

    @Test
    public void tablesController_getAll_WithNotEmptyTablesList() {

        var categories = Arrays.asList(
                Tables.builder()
                        .id(1l)

                        .build()
        );

        when(tableRepository.findAll()).thenReturn(categories);

        var res = tablesController.getTables();
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).get(0).getNumber(), categories.get(0).getNumber());
        assertEquals((res.getBody()).size(), 1);

    }

    @Test
    public void tablesController_getAll_WithEmptyTablesList() {
        var tables = new ArrayList<Tables>();
        when(tableRepository.findAll()).thenReturn(tables);
        var res = tablesController.getTables();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void tablesController_getAll_WithNullTablesList() {
        when(tableRepository.findAll()).thenReturn(null);

        var res = tablesController.getTables();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }


    @Test
    public void tablesController_getAll_getTablesWithNoDBConnection() {
        when(tableRepository.findAll())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = tablesController.getTables();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }


    /**
     * ------------------>  Save Tables Unit Test  <------------------------
     */


    @Test
    public void tablesController_Save_WithData() {

        var table = Tables.builder()
                .number(2)
                .tableOrders(Arrays.asList(com.softlines.fastpos.domain.Order.builder().build()))
                .build();

        when(tableRepository.save(any(Tables.class))).thenReturn(table);

        var res = tablesController.addTable(tableMapper.toTableDto(table));
        assertEquals(res.getStatusCode(), HttpStatus.CREATED);
        assertEquals((res.getBody()), tableMapper.toTableDto(table));

    }

    @Test
    public void tablesController_Save_WithExistTables() {

        var table =
                Tables.builder()
                        .id(1)

                        .number(2)

                        .tableOrders(Arrays.asList(com.softlines.fastpos.domain.Order.builder().build())).build();

        when(tableRepository.findById(table.getId())).thenReturn(Optional.of(table));
        var res = tablesController.addTable(tableMapper.toTableDto(table));

        assertEquals(res.getStatusCode(), HttpStatus.FOUND);

    }


    @Test
    public void tablesController_Save_WithoutData() {

        var table = Tables.builder().tableOrders(Arrays.asList(com.softlines.fastpos.domain.Order.builder().build())).build();

        when(tableRepository.save(any(Tables.class))).thenReturn(table);
        var res = tablesController.addTable(tableMapper.toTableDto(table));

        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }


    @Test
    public void tablesController_save_WithNoDBConnection() {

        var table = Tables.builder()
                .number(2)
                .tableOrders(Arrays.asList(com.softlines.fastpos.domain.Order.builder().build()))
                .build();

        when(tableRepository.save(any(Tables.class))).thenThrow(DataAccessResourceFailureException.class);
        var res = tablesController.addTable(tableMapper.toTableDto(table));

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  GetById Tables Unit Test  <------------------------
     */


    @Test
    public void TablesController_getById_WithNotEmptyTablesList() {

        var table =
                Tables.builder()
                        .id(1).tableOrders(Arrays.asList(com.softlines.fastpos.domain.Order.builder().build()))
                        .build();

        when(tableRepository.findById(1l)).thenReturn(Optional.ofNullable(table));
        var res = tablesController.getTable(1);

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).getNumber(), table.getNumber());
    }

    @Test
    public void TablesController_getById_WithEmptyTablesList() {

        var Tables = new Tables();

        when(tableRepository.findById(0l)).thenReturn(Optional.of(Tables));
        var res = tablesController.getTable(0l);

        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void TablesController_getById_WithNullTables() {

        var res = tablesController.getTable(1);
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void TablesController_getById_getTablesWithNoDBConnection() {

        when(tableRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = tablesController.getTable(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Delete Tables Unit Test  <------------------------
     */

    @Test
    public void tablesController_Delete_WithTablesId() {

        var table =
                Tables.builder()
                        .id(1l)
                        .number(3)
                        .build();

        when(tableRepository.findById(1l)).thenReturn(Optional.ofNullable(table));
        tablesController.deleteTable(1);

        verify(tableRepository, times(1)).delete(table);


    }

    @Test
    public void tablesController_Delete_WithNotExistTablesId() {

        when(tableRepository.findById(1l)).thenReturn(null);

        tablesController.deleteTable(1);

        verify(tableRepository, times(1)).findById(1l);
        verifyNoMoreInteractions(tableRepository);

    }

    @Test
    public void tablesController_Delete_getTablesWithNoDBConnection() {

        when(tableRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = tablesController.getTable(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Put Tables Unit Test  <------------------------
     */

    @Test
    public void tablesController_Put_WithData() {

        var table = Tables.builder()
                .id(1l)
                .number(5)
                .build();

        when(tableRepository.findById(table.getId())).thenReturn(Optional.of(table));
        ResponseEntity<TableDto> returned = tablesController.editTable(1, tableMapper.toTableDto(table));

        verify(tableRepository, times(1)).findById(table.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void tablesController_Put_WithIdNotExist() {

        var table = Tables.builder()
                .id(10l)
                .number(3)
                .build();

        when(tableRepository.findById(table.getId())).thenReturn(Optional.empty());
        ResponseEntity<TableDto> returned = tablesController.editTable(10, tableMapper.toTableDto(table));

        verify(tableRepository, times(1)).findById(table.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void tablesController_Put_WithNullData() {

        var table = Tables.builder().id(1).build();

        when(tableRepository.findById(table.getId())).thenReturn(Optional.of(table));
        var returned = tablesController.editTable(1, tableMapper.toTableDto(table));

        verify(tableRepository, times(1)).findById(table.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void tablesController_Put_getTablesWithNoDBConnection() {

        when(tableRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = tablesController.getTable(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


}

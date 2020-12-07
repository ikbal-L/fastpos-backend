package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.TablesController;
import com.softlines.fastpos.domain.Table;
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
import java.util.List;
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

        List<Table> tables = Arrays.asList(
                Table.builder()
                        .id(1l)
                        .build()
        );

        when(tableRepository.findAllTables()).thenReturn(tables);

        var res = tablesController.getTables();
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).get(0).getNumber(), tables.get(0).getNumber());
        assertEquals((res.getBody()).size(), 1);

    }

    @Test
    public void tablesController_getAll_WithEmptyTablesList() {
        var tables = new ArrayList<Table>();
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
        when(tableRepository.findAllTables())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = tablesController.getTables();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }


    /**
     * ------------------>  Save Table Unit Test  <------------------------
     */


    @Test
    public void tablesController_Save_WithData() {

        var table = Table.builder()
                .number(2)
                .build();

        when(tableRepository.save(any(Table.class))).thenReturn(table);

        var res = tablesController.addTable(tableMapper.toTableDto(table));
        assertEquals(res.getStatusCode(), HttpStatus.CREATED);

    }

    @Test
    public void tablesController_Save_WithExistTables() {

        var table =
                Table.builder()
                        .id(1)
                        .number(2)
                        .build();

        when(tableRepository.findById(table.getId())).thenReturn(Optional.of(table));
        var res = tablesController.addTable(tableMapper.toTableDto(table));

        assertEquals(res.getStatusCode(), HttpStatus.FOUND);

    }



    @Test
    public void tablesController_save_WithNoDBConnection() {

        var table = Table.builder()
                .number(2)
                .build();

        when(tableRepository.save(any(Table.class))).thenThrow(DataAccessResourceFailureException.class);
        var res = tablesController.addTable(tableMapper.toTableDto(table));

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  GetById Table Unit Test  <------------------------
     */


    @Test
    public void TablesController_getById_WithNotEmptyTablesList() {

        var table =
                Table.builder()
                        .id(1)
                        .number(1)
                        .build();

        when(tableRepository.findByIdTable(1L)).thenReturn(table);
        var res = tablesController.getTable(1);

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).getNumber(), table.getNumber());
    }

    @Test
    public void TablesController_getById_WithEmptyTablesList() {

        var Tables = new Table();

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
    public void TablesController_getById_WithNoDBConnection() {

        when(tableRepository.findByIdTable(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = tablesController.getTable(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Delete Table Unit Test  <------------------------
     */

    @Test
    public void tablesController_Delete_WithTablesId() {

        var table =
                Table.builder()
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
    public void tablesController_Delete_WithNoDBConnection() {

        when(tableRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = tablesController.deleteTable(5);

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


    /**
     * ------------------>  Put Table Unit Test  <------------------------
     */

    @Test
    public void tablesController_Put_WithData() {

        var table = Table.builder()
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

        var table = Table.builder()
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

        var table = Table.builder().id(1).build();

        when(tableRepository.findById(table.getId())).thenReturn(Optional.of(table));
        var returned = tablesController.editTable(1, tableMapper.toTableDto(table));

        verify(tableRepository, times(1)).findById(table.getId());
        assertEquals(returned.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void tablesController_Put_WithNoDBConnection() {
        var table = Table.builder()
                .id(10l)
                .number(3)
                .build();

        when(tableRepository.findById(5l))
                .thenThrow(DataAccessResourceFailureException.class);

        var res = tablesController.editTable(5,tableMapper.toTableDto( table));

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);

    }


}

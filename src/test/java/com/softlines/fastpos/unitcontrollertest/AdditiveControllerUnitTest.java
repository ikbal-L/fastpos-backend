package com.softlines.fastpos.unitcontrollertest;

import com.softlines.fastpos.ModelApplication;
import com.softlines.fastpos.controller.AdditiveController;
import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.repository.AdditiveRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ModelApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class AdditiveControllerUnitTest {

    @MockBean
    AdditiveRepository additiveRepository;

    @Autowired
    AdditiveController additiveController;

    @Test
    public void AdditiveController_WithNotEmptyAdditivesList() throws Exception {

        var additives = Arrays.asList(
                Additive.builder()
                        .id(1)
                        .description("Harrisa")
                        .build());
        
        Mockito.when(additiveRepository.findAll()).thenReturn(additives);

        var res = additiveController.getAdditives();
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals((res.getBody()).get(0).getDescription(), additives.get(0).getDescription());
        assertEquals(( res.getBody()).size(), 1);
          }

    @Test
    public void AdditiveController_WithEmptyAdditivesList() throws Exception {
        var additives = new ArrayList<Additive>();
        Mockito.when(additiveRepository.findAll()).thenReturn(additives);
        var res = additiveController.getAdditives();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void AdditiveController_WithNullAdditivesList() throws Exception {
        Mockito.when(additiveRepository.findAll()).thenReturn(null);

        var res = additiveController.getAdditives();
        assertEquals(res.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void AdditiveController_getAdditivesWithNoDBConnection() throws Exception {
        Mockito.when(additiveRepository.findAll())
                .thenThrow(DataAccessResourceFailureException.class);

        var res = additiveController.getAdditives();

        assertEquals(res.getStatusCode(), HttpStatus.BAD_GATEWAY);
    }

}

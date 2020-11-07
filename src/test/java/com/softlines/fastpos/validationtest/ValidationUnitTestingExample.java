package com.softlines.fastpos.validationtest;

import com.softlines.fastpos.controller.AdditiveController;
import com.softlines.fastpos.domain.Additive;
import com.softlines.fastpos.dto.AdditiveDto;
import com.softlines.fastpos.dto.mapping.AdditiveMapper;
import com.softlines.fastpos.repository.AdditiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ValidationUnitTestingExample {

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testAdditiveValidation_newAdditieWithNoData() {
        AdditiveDto additive = new AdditiveDto();
        Set<ConstraintViolation<AdditiveDto>> violations = validator.validate(additive);
        assertFalse(violations.isEmpty());
        assertThat(violations.size(), equalTo(1));
        assertThat(violations.size(), is(1));
    }
}

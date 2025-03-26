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

import javax.swing.text.Position;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

public class ValidationUnitTestingExample {

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testAdditiveValidation_withInvalidAdditive() {
        AdditiveDto additive = new AdditiveDto();
        Set<ConstraintViolation<AdditiveDto>> violations = validator.validate(additive);
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(2);
        assertThat(violations.size()).isEqualTo(2);
    }


    @Test
    public void testAdditiveValidation_withValidAdditive() {
        AdditiveDto additive = new AdditiveDto();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Set<ConstraintViolation<AdditiveDto>> violations = validator.validate(additive);
        assertFalse(violations.isEmpty());
        assertThat(violations.size(), equalTo(2));
        assertThat(violations.size(), is(2));
    }

    @Test
    public void i18nMessageAccesTest() {
        var locale = new Locale("en");
        var messages = ResourceBundle.getBundle("messages", locale);
        var keys = messages.getKeys();
    }

    @Test
    public void TestDuration() {
//        String string = "00:11:00";
//        DateFormat format = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
//        Date date = null;
//        try {
//            date = format.parse(string);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.out.println(date.getTime()); // Sat Jan 02 00:00:00 GMT 2010
//        long timw = date.getTime();

        String myTime = "10:30:55.214154";
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(myTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedTime = sdf.format(date);

        System.out.println(date.getTime());
    }
}
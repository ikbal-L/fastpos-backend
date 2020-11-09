package com.softlines.fastpos.exceptionmanagement;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionHandling {
    Map<Class, ResponseEntity> exceptionMap;

    public ExceptionHandling() {
        exceptionMap = new HashMap<>();
        //when server is down
        exceptionMap.put(DataAccessResourceFailureException.class, ResponseEntity.status(HttpStatus.BAD_GATEWAY).build());
        //when a name is not provided for role/privilege
        exceptionMap.put(NullPointerException.class, ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        //
        //exceptionMap.put(InvalidDataAccessApiUsageException.class, ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        //when password not provided
        exceptionMap.put(IllegalArgumentException.class, ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        //when username not provided
        exceptionMap.put(DataIntegrityViolationException.class, ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    public ResponseEntity getResponseEntityAccordingToException(Exception exception){
        return exceptionMap.get(exception.getClass());
    }

//    public Throwable getMessageAccordingToException(Exception e) {
//
//    }
}

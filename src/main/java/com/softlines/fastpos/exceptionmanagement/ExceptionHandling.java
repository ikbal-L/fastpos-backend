package com.softlines.fastpos.exceptionmanagement;

import org.springframework.dao.DataAccessResourceFailureException;
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
        exceptionMap.put(DataAccessResourceFailureException.class, ResponseEntity.status(HttpStatus.BAD_GATEWAY).build());
        exceptionMap.put(NullPointerException.class, ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    public ResponseEntity getResponseEntityAccordingToException(Exception exception){
        return exceptionMap.get(exception.getClass());
    }

//    public Throwable getMessageAccordingToException(Exception e) {
//
//    }
}

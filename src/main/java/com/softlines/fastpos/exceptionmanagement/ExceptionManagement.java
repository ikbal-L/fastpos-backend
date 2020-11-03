package com.softlines.fastpos.exceptionmanagement;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionManagement {
    Map<Class, ResponseEntity> exceptionMap = new HashMap<>();

    public ExceptionManagement() {

        exceptionMap.put(DataAccessResourceFailureException.class, ResponseEntity.status(HttpStatus.BAD_GATEWAY).build());

    }

    public ResponseEntity getResponseEntityAccordingToException(Exception exception) {

        return exceptionMap.get(exception.getClass());
    }
}

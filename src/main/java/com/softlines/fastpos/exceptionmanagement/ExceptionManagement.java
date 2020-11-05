package com.softlines.fastpos.exceptionmanagement;

import org.hibernate.exception.ConstraintViolationException;
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
        exceptionMap.put(NullPointerException.class, ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        exceptionMap.put(ConstraintViolationException.class, ResponseEntity.status(HttpStatus.FOUND).build());

    }

    public ResponseEntity getResponseEntityAccordingToException(Exception exception) {

        return exceptionMap.get(exception.getClass());
    }
}

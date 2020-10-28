package com.softlines.fastpos.exceptionmanagement;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionManagement {
    Map<Class, ResponseEntity> exceptionMap;

    public ExceptionManagement() {
        exceptionMap = new HashMap<>();
        //exceptionMap.put(DataAccessResourceFailureException.class, ResponseEntity.status(HttpStatus.BAD_GATEWAY).build());
    }

    public ResponseEntity getResponseEntityAccordingToException(Exception exception){
        return exceptionMap.get(exception.getClass());
    }
}

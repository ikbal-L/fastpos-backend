package com.softlines.fastpos.exceptionmanagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.security.access.AccessDeniedException;

import org.springframework.validation.BindingResult;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class FastposExceptionHandler {

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public final ResponseEntity<List<String>> handleHeaderException()
    {
        return new ResponseEntity("Media Type Not Supported: Must be Json", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {

            String errorMessage = error.getDefaultMessage();

            errors.add(errorMessage);
        }
        return new ResponseEntity(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }



    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<Object> handleNullRequestBodyExceptions() {
        return new ResponseEntity("Body must not be null", HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e){
        return  new ResponseEntity<Object>(String.format("%s",e.getMessage()),HttpStatus.UNAUTHORIZED);
    }
    //TODO Discuss how to handle this error on the client side
    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        var message= mapper.writeValueAsString(e.getCause().getMessage());
        return  new ResponseEntity<Object>(message,HttpStatus.CONFLICT);
    }

}

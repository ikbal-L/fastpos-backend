package com.softlines.fastpos.exceptionmanagement;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
            String fieldName = ((FieldError) error).getField();

            String errorMessage = error.getDefaultMessage();

            errors.add(fieldName+ " " +errorMessage);
        }
        return new ResponseEntity(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }



    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<Object> handleNullRequestBodyExceptions() {
        return new ResponseEntity("Body must not be null", HttpStatus.UNPROCESSABLE_ENTITY);
    }

}

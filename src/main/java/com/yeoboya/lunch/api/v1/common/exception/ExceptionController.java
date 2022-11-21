package com.yeoboya.lunch.api.v1.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(BAD_REQUEST) // 404
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ExceptionResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        ExceptionResponse response = ExceptionResponse.builder()
                .code(BAD_REQUEST.value())
                .message(BAD_REQUEST.getReasonPhrase())
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

    @ResponseStatus(NOT_FOUND) // 404
    @ExceptionHandler(EmptyResultDataAccessException.class)
    protected ExceptionResponse emptyResultDataAccessException(EmptyResultDataAccessException e){
        log.error("DataIntegrityViolationException", e);
        return ExceptionResponse.builder()
                .code(NOT_FOUND.value())
                .message(NOT_FOUND.getReasonPhrase())
                .build();
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED) // 405
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ExceptionResponse httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        return ExceptionResponse.builder()
                .code(METHOD_NOT_ALLOWED.value())
                .message(METHOD_NOT_ALLOWED.getReasonPhrase())
                .build();
    }

    @ResponseStatus(CONFLICT) // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ExceptionResponse dataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException", e);
        return ExceptionResponse.builder()
                .code(CONFLICT.value())
                .message(CONFLICT.getReasonPhrase())
                .build();
    }

    @ExceptionHandler(LunchException.class)
    public ResponseEntity<ExceptionResponse> lunchException(LunchException e) {
        int statusCode = e.getStatusCode();

        ExceptionResponse body = ExceptionResponse.builder()
                .code(statusCode)
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(statusCode).body(body);
    }

}
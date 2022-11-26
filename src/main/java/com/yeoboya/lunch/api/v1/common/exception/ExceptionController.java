package com.yeoboya.lunch.api.v1.common.exception;

import com.yeoboya.lunch.config.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(BAD_REQUEST) // 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ExceptionResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        ExceptionResponse response = ExceptionResponse.builder()
                .code(BAD_REQUEST.value())
                .message(BAD_REQUEST.getReasonPhrase())
                .validation(Helper.refineErrors(e))
                .build();
        return response;
    }

//    @ResponseStatus(BAD_REQUEST)
//    @ExceptionHandler(value = ConstraintViolationException.class)
//    protected ExceptionResponse handleException(ConstraintViolationException e) {
//        log.error("DataIntegrityViolationException", e);
//        return Response
//                .builder()
//                .header(Header
//                        .builder()
//                        .isSuccessful(false)
//                        .resultCode(-400)
//                        .resultMessage(getResultMessage(exception.getConstraintViolations().iterator())) // 오류 응답을 생성
//                        .build())
//                .build();
//    }


    @ResponseStatus(BAD_REQUEST) // 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ExceptionResponse httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("MethodArgumentNotValidException", e);
        return ExceptionResponse.builder()
                .code(BAD_REQUEST.value())
                .message(BAD_REQUEST.getReasonPhrase())
                .note("JSON 확인")
                .build();
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

    //todo 테이블의 특정 행을 삭제하고자 할때 그 행을 참조하는 자식 레코드가 있을경우 랑 save 할떄 오류 처리
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
                .build();

        return ResponseEntity.status(statusCode).body(body);
    }

}
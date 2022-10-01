package com.yeoboya.guinGujik.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e){
        if(e.hasErrors()){
            log.error("메서드 인수가 유효하지 않는 예외 발생");
            ErrorResponse response = new ErrorResponse("400", "잘못된 요청입니다.");
//            ErrorResponse response = ErrorResponse.getInstance("400", "잘못된 요청입니다.");
            for (FieldError fieldError : e.getFieldErrors()){
                response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return response;
        }else{
            return null;
        }
    }

}

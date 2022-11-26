package com.yeoboya.lunch.config.util;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;

@Service
public class Helper {

    public static LinkedList<LinkedHashMap<String, String>> refineErrors(final Errors errors) {
        LinkedList<LinkedHashMap<String, String>> errorList = new LinkedList<>();
        errors.getFieldErrors().forEach(e-> {
            LinkedHashMap<String, String> error = new LinkedHashMap<>();
            error.put("field", e.getField());
            error.put("message", e.getDefaultMessage());
            errorList.push(error);
        });
        return errorList;
    }

    public static LinkedList<LinkedHashMap<String, String>> refineErrors(final ConstraintViolationException constraintViolationException) {
        LinkedList<LinkedHashMap<String, String>> errorList = new LinkedList<>();
        constraintViolationException.getConstraintViolations().stream().iterator().forEachRemaining(e->{
            LinkedHashMap<String, String> error = new LinkedHashMap<>();
            String propertyPath = String.valueOf(e.getPropertyPath());
            String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            error.put("field", fieldName);
            error.put("message", e.getMessage());
            errorList.push(error);
        });
        return errorList;
    }



    public static void printBeanNames(ApplicationContext context){
        String[] beanNames = context.getBeanDefinitionNames();
        Arrays.stream(beanNames)
                .filter(n -> !n.contains("springframework"))
                .forEach(System.out::println);
    }
}
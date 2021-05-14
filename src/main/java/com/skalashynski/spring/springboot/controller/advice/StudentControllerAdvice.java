package com.skalashynski.spring.springboot.controller.advice;

import com.skalashynski.spring.springboot.exception.StudentException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class StudentControllerAdvice {
    @ResponseBody
    @ExceptionHandler(StudentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound (StudentException exception) {
        return exception.getMessage();
    }
}

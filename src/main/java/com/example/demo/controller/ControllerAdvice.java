package com.example.demo.controller;

import com.example.demo.exception.StudentException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ResponseBody
    @ExceptionHandler(StudentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound (StudentException exception) {
        return exception.getMessage();
    }
}

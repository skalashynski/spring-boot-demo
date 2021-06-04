package com.skalashynski.spring.springboot.controller.handler;

import com.skalashynski.spring.springboot.exception.StudentException;
import com.skalashynski.spring.springboot.exception.StudentRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class StudentExceptionHandler {
    /*@ResponseBody
    @ExceptionHandler(StudentRequestException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound(StudentRequestException exception) {
        return exception.getMessage();
    }*/

    @ExceptionHandler(value = {StudentRequestException.class})
    public ResponseEntity<Object> handleStudentRequestException(StudentRequestException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StudentException studentException = new StudentException(e.getMessage(),  status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(studentException, status);
    }
}

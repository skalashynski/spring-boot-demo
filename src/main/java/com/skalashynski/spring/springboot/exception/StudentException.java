package com.skalashynski.spring.springboot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
public class StudentException {
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;
}

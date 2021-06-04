package com.skalashynski.spring.springboot.exception;

import com.skalashynski.spring.springboot.controller.StudentController;

/**
 * Custom exception for {@link StudentController}
 * */
public class StudentRequestException extends RuntimeException  {
    public StudentRequestException() {
        super();
    }

    public StudentRequestException(String message) {
        super(message);
    }

    public StudentRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public StudentRequestException(Throwable cause) {
        super(cause);
    }

    protected StudentRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

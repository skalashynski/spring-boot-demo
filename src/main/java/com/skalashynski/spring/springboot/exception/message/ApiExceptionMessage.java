package com.skalashynski.spring.springboot.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiExceptionMessage {
    CAN_NOT_FIND("Can't find student with id: ");

    private final String message;
}

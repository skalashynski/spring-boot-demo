package com.skalashynski.spring.springboot.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
}

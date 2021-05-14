package com.skalashynski.spring.springboot.service;

import com.skalashynski.spring.springboot.model.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final UserDetailsService userDetailsService;

    public String register(RegistrationRequest request) {
        return "temporary registration works";
    }

}

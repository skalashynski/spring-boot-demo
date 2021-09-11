package com.skalashynski.spring.springboot.service;

import com.skalashynski.spring.springboot.entity.TokenConfirmation;

import java.util.Optional;

public interface ConfirmationTokenService {
    void save(TokenConfirmation tokenConfirmation);

    void update(TokenConfirmation tokenConfirmation);

    Optional<TokenConfirmation> getToken(String token);
}

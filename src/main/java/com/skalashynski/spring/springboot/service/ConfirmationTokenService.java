package com.skalashynski.spring.springboot.service;

import com.skalashynski.spring.springboot.bean.AppUser;
import com.skalashynski.spring.springboot.bean.TokenConfirmation;

import java.util.Optional;

public interface ConfirmationTokenService {
    void save(TokenConfirmation tokenConfirmation);

    void update(TokenConfirmation tokenConfirmation);

    Optional<TokenConfirmation> getToken(String token);

    TokenConfirmation generateToken(AppUser appUser);
}

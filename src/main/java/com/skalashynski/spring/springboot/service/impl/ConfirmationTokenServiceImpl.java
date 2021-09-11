package com.skalashynski.spring.springboot.service.impl;

import com.skalashynski.spring.springboot.entity.TokenConfirmation;
import com.skalashynski.spring.springboot.repository.ConfirmationTokenRepository;
import com.skalashynski.spring.springboot.service.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public void save(TokenConfirmation tokenConfirmation) {
        confirmationTokenRepository.save(tokenConfirmation);
    }

    @Override
    public void update(TokenConfirmation tokenConfirmation) {
        confirmationTokenRepository.save(tokenConfirmation);
    }

    @Override
    public Optional<TokenConfirmation> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }
}

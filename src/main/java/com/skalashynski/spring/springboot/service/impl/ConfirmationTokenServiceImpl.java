package com.skalashynski.spring.springboot.service.impl;

import com.skalashynski.spring.springboot.bean.TokenConfirmation;
import com.skalashynski.spring.springboot.repository.ConfirmationTokenRepository;
import com.skalashynski.spring.springboot.service.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public void saveConfirmationToken(TokenConfirmation tokenConfirmation) {
        confirmationTokenRepository.save(tokenConfirmation);
    }
}

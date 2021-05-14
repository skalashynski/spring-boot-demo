package com.skalashynski.spring.springboot.service;

import com.skalashynski.spring.springboot.bean.TokenConfirmation;

public interface ConfirmationTokenService {
    void saveConfirmationToken(TokenConfirmation tokenConfirmation);
}

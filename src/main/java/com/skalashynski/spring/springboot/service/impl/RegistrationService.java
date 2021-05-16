package com.skalashynski.spring.springboot.service.impl;

import com.skalashynski.spring.springboot.bean.AppUser;
import com.skalashynski.spring.springboot.bean.TokenConfirmation;
import com.skalashynski.spring.springboot.model.AppUserRole;
import com.skalashynski.spring.springboot.model.RegistrationRequest;
import com.skalashynski.spring.springboot.service.AppUserService;
import com.skalashynski.spring.springboot.service.ConfirmationTokenService;
import com.skalashynski.spring.springboot.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private static final String LINK = "http://localhost:8080/api/v1/registration/confirm?token=";

    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailSender;

    public String register(RegistrationRequest request) {
        //todo: email validation

        String token = appUserService.signUp(new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                AppUserRole.USER));

        emailSender.send(request.getEmail(), request.getFirstName(), LINK + token);
        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        TokenConfirmation tokenConfirmation = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException("Can't confirm illegal token"));
        LocalDateTime now = LocalDateTime.now();
        if (tokenConfirmation.getConfirmedAt() != null) {
            throw new IllegalStateException("Token already confirmed");
        }
        LocalDateTime expiredAt = tokenConfirmation.getExpiresAt();
        if (expiredAt.isBefore(now)) {
            throw new IllegalStateException("Token expired");
        }
        tokenConfirmation.setConfirmedAt(now);
        tokenConfirmation.getAppUser().setEnabled(true);
        confirmationTokenService.update(tokenConfirmation);
        return "token confirmed";
    }

}

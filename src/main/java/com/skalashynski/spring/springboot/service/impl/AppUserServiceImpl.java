package com.skalashynski.spring.springboot.service.impl;

import com.skalashynski.spring.springboot.bean.AppUser;
import com.skalashynski.spring.springboot.bean.TokenConfirmation;
import com.skalashynski.spring.springboot.repository.AppUserRepository;
import com.skalashynski.spring.springboot.service.AppUserService;
import com.skalashynski.spring.springboot.service.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
    private static final String USER_NOT_FOUND = "user with email %s not found";

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(USER_NOT_FOUND, email)));
    }

    @Override
    public String signUp(AppUser appUser) {
        // todo: check if a user exists by email in repo
        String encodedPass = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPass);
        appUserRepository.save(appUser);
        String random = UUID.randomUUID().toString();
        TokenConfirmation token = new TokenConfirmation(
                random, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), appUser);
        confirmationTokenService.save(token);

        // todo: send email
        return random;
    }
}

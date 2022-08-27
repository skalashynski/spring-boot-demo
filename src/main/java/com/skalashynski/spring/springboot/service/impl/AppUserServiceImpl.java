package com.skalashynski.spring.springboot.service.impl;

import com.skalashynski.spring.springboot.entity.AppUser;
import com.skalashynski.spring.springboot.entity.TokenConfirmation;
import com.skalashynski.spring.springboot.exception.ApiException;
import com.skalashynski.spring.springboot.jwt.JwtService;
import com.skalashynski.spring.springboot.repository.AppUserRepository;
import com.skalashynski.spring.springboot.service.AppUserService;
import com.skalashynski.spring.springboot.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final JwtService jwtService;

    @Override
    @Transactional
    public String signUp(AppUser appUser) throws ApiException {
        if (appUserRepository.findByEmail(appUser.getEmail()).isPresent()) {
            throw new IllegalStateException(String.format("User %s already registered.", appUser.getEmail()));
        }
        String token = jwtService.createJWT("id_value", appUser.getUsername(), appUser.getAuthorities());

        String encodedPass = appUser.getPassword();
        //String encodedPass = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPass);
        appUserRepository.save(appUser);
        TokenConfirmation tokenConfirmation = new TokenConfirmation(token, LocalDateTime.now(), LocalDateTime.now().plusSeconds(jwtService.getJwtConfig().getTokenExpirationSeconds()),
                appUser);
        confirmationTokenService.save(tokenConfirmation);
        return token;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Can't load user with username: " + username));
    }
}

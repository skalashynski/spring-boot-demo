package com.skalashynski.spring.springboot.service.impl;

import com.skalashynski.spring.springboot.bean.AppUser;
import com.skalashynski.spring.springboot.bean.TokenConfirmation;
import com.skalashynski.spring.springboot.repository.Impl.FakeAppUserRepositoryImpl;
import com.skalashynski.spring.springboot.service.AppUserService;
import com.skalashynski.spring.springboot.service.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
    private final FakeAppUserRepositoryImpl appUserRepository;
    private final ConfirmationTokenService confirmationTokenService;


    @Override
    public String signUp(AppUser appUser) {
        if (appUserRepository.findByEmail(appUser.getEmail()).isPresent()) {
            throw new IllegalStateException(String.format("User %s already registered.", appUser.getEmail()));
        }
        String encodedPass = appUser.getPassword();
        //String encodedPass = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPass);
        appUserRepository.save(appUser);
        String random = UUID.randomUUID().toString();
        TokenConfirmation token = new TokenConfirmation(
                random, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), appUser);
        confirmationTokenService.save(token);

        // todo: send email
        return random;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Can't load user with username "+username));
    }
}

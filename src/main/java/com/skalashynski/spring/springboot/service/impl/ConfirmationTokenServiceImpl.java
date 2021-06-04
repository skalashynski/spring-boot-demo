package com.skalashynski.spring.springboot.service.impl;

import com.skalashynski.spring.springboot.bean.AppUser;
import com.skalashynski.spring.springboot.bean.TokenConfirmation;
import com.skalashynski.spring.springboot.repository.ConfirmationTokenRepository;
import com.skalashynski.spring.springboot.service.ConfirmationTokenService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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

    @Override
    public TokenConfirmation generateToken(AppUser appUser) {
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("clientType", "user");
        tokenData.put("userID", appUser.getId().toString());
        tokenData.put("username", appUser.getUsername());
        LocalDateTime now = LocalDateTime.now();
        tokenData.put("token_create_date", now);
        LocalDateTime now100 = now.plusYears(100);
        tokenData.put("token_expiration_date", now100);
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setExpiration(Date.valueOf(now100.toLocalDate()));
        jwtBuilder.setClaims(tokenData);
        String key = "abc123";
        String token = jwtBuilder.signWith(SignatureAlgorithm.HS512, key).compact();
        return new TokenConfirmation(
                token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), appUser);

    }
}

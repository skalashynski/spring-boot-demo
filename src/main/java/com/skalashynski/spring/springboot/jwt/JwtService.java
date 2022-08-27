package com.skalashynski.spring.springboot.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Collection;
import java.util.Date;


@Component
@Getter
public class JwtService {

    private final JwtConfig jwtConfig;
    private final Key signingKey;

    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtConfig.getSecretKey());
        this.signingKey = new SecretKeySpec(apiKeySecretBytes, jwtConfig.getSignatureAlgorithm().getJcaName());
    }

    public String createJWT(String id, String username, Collection<? extends GrantedAuthority> grantedAuthorities) {
        return createJWT(id, username, grantedAuthorities, jwtConfig.getTokenExpirationSeconds());
    }

    public String createJWT(String id, String username, Collection<? extends GrantedAuthority> grantedAuthorities, @Nullable Long ttlSeconds) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .claim("authorities", grantedAuthorities)
                .setSubject(username)
                .setIssuer(jwtConfig.getIssuer())
                .signWith(signingKey, jwtConfig.getSignatureAlgorithm());

        if (ttlSeconds != null) {
            long expMillis = nowMillis + ttlSeconds * 1000;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    public Claims decodeJWT(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(jwtConfig.getSecretKey()))
                .requireIssuer(jwtConfig.getIssuer())
                .build()
                .parseClaimsJws(jwt).getBody();
    }
}

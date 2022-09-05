package com.skalashynski.spring.springboot.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
@ConfigurationProperties(prefix = "application.jwt")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtConfig {
    private String secretKey;
    private String tokenPrefix;
    private Long tokenExpirationSeconds;
    private String issuer;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}

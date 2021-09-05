package com.skalashynski.spring.springboot.jwt;

import com.google.common.net.HttpHeaders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.jwt")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtConfig {
    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;

    public String getAuthorizationHeader(){
        return HttpHeaders.AUTHORIZATION;
    }
}

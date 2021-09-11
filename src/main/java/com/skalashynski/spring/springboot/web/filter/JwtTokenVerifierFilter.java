package com.skalashynski.spring.springboot.web.filter;

import com.skalashynski.spring.springboot.jwt.JwtConfig;
import com.skalashynski.spring.springboot.jwt.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * this filter is executed once per request
 */
@AllArgsConstructor
public class JwtTokenVerifierFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = httpServletRequest.getHeader(jwtConfig.getAuthorizationHeader());
        if (!StringUtils.hasLength(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        String jwt = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
        try {
            Claims body = jwtService.decodeJWT(jwt);
            String username = body.getSubject();
            List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");
            if (authorities != null) {
                var simpleGrantedAuthorities = authorities
                    .stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.get("authority")))
                    .collect(Collectors.toSet());

                var authentication = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException e) {
            throw new IllegalStateException(String.format("Token %s can't be truest", jwt), e);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

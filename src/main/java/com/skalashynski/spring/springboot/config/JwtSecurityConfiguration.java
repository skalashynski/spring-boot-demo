package com.skalashynski.spring.springboot.config;

import com.skalashynski.spring.springboot.jwt.JwtConfig;
import com.skalashynski.spring.springboot.jwt.JwtTokenVerifier;
import com.skalashynski.spring.springboot.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.crypto.SecretKey;

import static com.skalashynski.spring.springboot.model.AppUserPermission.COURSE_WRITE;
import static com.skalashynski.spring.springboot.model.AppUserRole.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .csrf().disable() // this should be disabled for form-based auth
                //disabling csrf allows execute POST/PUT/DELETE HTTP methods on REST
                //spring security by default tries to protect our API
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, secretKey), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                    .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                    .antMatchers("/api/**").hasRole(STUDENT.name())

                    //security for StudentManagementController
                    //there are tow way to implement permission based auth
                    //out users are role aware. They don't know about permissions or authorities
                    //actual order of defined antMatchers does really matter, and we have to be very careful
                    .antMatchers(DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                    .antMatchers(POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                    .antMatchers(PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                    .antMatchers(GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMIN_TRAINEE.name())

                    .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider);
    }
}


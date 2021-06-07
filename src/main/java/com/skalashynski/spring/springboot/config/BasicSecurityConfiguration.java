package com.skalashynski.spring.springboot.config;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static com.skalashynski.spring.springboot.model.AppUserPermission.COURSE_WRITE;
import static com.skalashynski.spring.springboot.model.AppUserRole.*;
import static org.springframework.http.HttpMethod.*;

/*@Configuration
@EnableWebSecurity*/
@AllArgsConstructor
public class BasicSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final DaoAuthenticationProvider daoAuthenticationProvider;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                //.csrf().disable() // TODO: doesn't work correctly with using postman
                //disabling csrf allows execute POST/PUT/DELETE HTTP methods on REST
                //spring security by default tries to protect our API
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
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider);
    }
}

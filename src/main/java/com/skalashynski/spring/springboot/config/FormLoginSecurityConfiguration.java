package com.skalashynski.spring.springboot.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static com.skalashynski.spring.springboot.model.AppUserPermission.COURSE_WRITE;
import static com.skalashynski.spring.springboot.model.AppUserRole.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class FormLoginSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .csrf().disable() // this should be disabled for form-based auth
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
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/courses", true)
                    .usernameParameter("username")
                    .passwordParameter("password")
                .and()
                .rememberMe()// defaults to 2 weeks
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("something_very_secured")
                    .rememberMeParameter("remember-me")
                .and()
                .logout()
                    .logoutSuccessUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))//because CSRF is disabled, you need to
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login");
    }

    //how we retrieve users from DB
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails annaSmith = User.builder()
                .username("annasmith")
                .password(passwordEncoder.encode("password"))
                //.roles(AppUserRole.STUDENT.name())//ROLE_STUDENT

                // our user are role aware. They don't know anything about permissions or authorities
                // go ahead and click on roles method. Inside in the builder class there we can see that it builds a list of granted authorities
                // when we add ROLE, the builder appends the "ROLE_" underscore
                // if we want permissions we simply pass the permissions as is
                // the is no concept of ROLES and PERMISSIONS. Everything is bundled inside of Collection<GrantedAuthority>
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        UserDetails linda = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("password123"))
                //.roles(ADMIN.name())//ROLE_ADMIN
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails tom = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("password123"))
                //.roles(AppUserRole.ADMIN_TRAINEE.name())//ROLE_ADMIN_TRAINEE
                .authorities(ADMIN_TRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                annaSmith,
                linda,
                tom
        );
    }
}

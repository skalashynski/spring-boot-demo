package com.skalashynski.spring.springboot.repository;

import com.skalashynski.spring.springboot.bean.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;
    @Test
    void findByEmail() {
        Optional<AppUser> appUser = appUserRepository.findByEmail("dangote@gmail.com");
        assertSame(true, appUser.isPresent());
    }

    @Test
    void findByUsername() {
        Optional<AppUser> appUser = appUserRepository.findByUsername("gangote123");
        assertSame(true, appUser.isPresent());
    }
}
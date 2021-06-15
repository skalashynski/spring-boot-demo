package com.skalashynski.spring.springboot.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ConfirmationTokenRepositoryTest {
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Test
    void findByToken() {
        boolean present = confirmationTokenRepository.findByToken("AlikoDangotegangote123").isPresent();
        assertTrue(present);
    }
}
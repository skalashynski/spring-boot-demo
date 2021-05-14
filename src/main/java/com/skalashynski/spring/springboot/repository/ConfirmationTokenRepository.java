package com.skalashynski.spring.springboot.repository;

import com.skalashynski.spring.springboot.bean.TokenConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<TokenConfirmation, Long> {
    Optional<TokenConfirmation> findByToken(String token);

}

package com.skalashynski.spring.springboot.repository;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DataJpaTest
class ConfirmationTokenRepositoryTest {
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void before() throws SQLException {
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ClassPathResource("data.sql"));
    }
    @Test
    void findByToken() {
        boolean present = confirmationTokenRepository.findByToken("AlikoDangotegangote123").isPresent();
        assertTrue(present);
    }
}
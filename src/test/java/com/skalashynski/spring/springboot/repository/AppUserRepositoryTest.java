package com.skalashynski.spring.springboot.repository;

import com.skalashynski.spring.springboot.SpringBootRunnerTests;
import com.skalashynski.spring.springboot.bean.AppUser;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = { SpringBootRunnerTests.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional

@SpringBootTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void before() throws SQLException {
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ClassPathResource("data.sql"));
    }
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
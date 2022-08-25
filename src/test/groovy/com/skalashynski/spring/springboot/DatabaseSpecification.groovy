package com.skalashynski.spring.springboot

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import groovy.sql.Sql
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import javax.sql.DataSource

@Testcontainers
@SpringBootTest
class DatabaseSpecification extends Specification {

    static def container;

    static {
        container = new PostgreSQLContainer("postgres:13.3")
                .withDatabaseName("hr_db")
                .withExposedPorts(5432)
                .withReuse(true)

        container.start();

    }

    @DynamicPropertySource
    static def properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
    @Bean
    DataSource dataSource1() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:${container.getMappedPort(5432)}/hr_db");
        config.setUsername("test");
        config.setPassword("test");
        config.setDriverClassName("org.postgresql.Driver");
        config.setConnectionTestQuery("SELECT 1");
        return new HikariDataSource(config);
    }

    @Shared
    Sql sql

    def setupSpec() {
        sql = Sql.newInstance(
                "jdbc:postgresql://localhost:${container.getMappedPort(5432)}/hr_db",
                "test",
                "test"
        )
    }

    def cleanupSpec() {
        sql.close()
    }
}

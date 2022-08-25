package com.skalashynski.spring.springboot.repository

import com.skalashynski.spring.springboot.DatabaseSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ConfirmationTokenRepositoryTest extends DatabaseSpecification {
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @SuppressWarnings("unused")
    def setup() {
        sql.execute(new File('src/test/resources/data.sql').text)
    }

    @SuppressWarnings("unused")
    def cleanup() {
        sql.execute(new File('src/test/resources/cleanup.sql').text)
    }

    def "find by token '#token'"(String token) {
        when:
        boolean present = confirmationTokenRepository.findByToken(token).isPresent();
        then:
        present
        where:
        token                    | _
        "AlikoDangotegangote123" | _
    }
}

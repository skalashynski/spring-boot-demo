package com.skalashynski.spring.springboot.repository

import com.skalashynski.spring.springboot.DatabaseSpecification
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner.class)
@ContextConfiguration
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


    def void "find by token '#token'"(String token) {

        when: "Calling DB"
            boolean present = confirmationTokenRepository.findByToken(token).isPresent();
        then:
            present
        where:
            token                    | _
            "AlikoDangotegangote123" | _
    }
}

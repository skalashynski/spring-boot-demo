package com.skalashynski.spring.springboot.repository

import com.skalashynski.spring.springboot.DatabaseSpecification
import com.skalashynski.spring.springboot.entity.AppUser
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest
class AppUserRepositoryTest extends DatabaseSpecification {
    @Autowired
    private AppUserRepository appUserRepository;

    @SuppressWarnings("unused")
    def setup() {
        sql.execute(new File('src/test/resources/data.sql').text)
    }

    @SuppressWarnings("unused")
    def cleanup() {
        sql.execute(new File('src/test/resources/cleanup.sql').text)
    }


    def void "find by email '#email'"(String email) {

        when: "Calling DB"
            Optional<AppUser> appUser = appUserRepository.findByEmail(email);
        then:
            appUser.isPresent()
        where:
            email               | _
            "dangote@gmail.com" | _
    }
}

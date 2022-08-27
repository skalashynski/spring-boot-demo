package com.skalashynski.spring.springboot.repository

import com.skalashynski.spring.springboot.DatabaseSpecification
import com.skalashynski.spring.springboot.entity.AppUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AppUserRepositoryTest extends DatabaseSpecification {

    @Autowired
    private AppUserRepository appUserRepository;

    def setup() {
        sql.execute(new File('src/test/resources/data.sql').text)
    }

    def cleanup() {
        sql.execute(new File('src/test/resources/cleanup.sql').text)
    }

    def "find by email '#email'"(String email) {
        when:
            Optional<AppUser> appUser = appUserRepository.findByEmail(email);
        then:
            appUser.isPresent()
        where:
            email               | _
            'dangote@gmail.com' | _
            'monster@gmail.com' | _
    }

    def "find by username '#username"(String username) {
        when:
            def appUser = appUserRepository.findByUsername(username);
        then:
            appUser.isPresent();
        where:
            username     | _
            "gangote123" | _
            "monster123" | _
    }
}

package com.skalashynski.spring.springboot.repository

import com.skalashynski.spring.springboot.DatabaseSpecification
import com.skalashynski.spring.springboot.entity.Student
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import java.time.LocalDate

@SpringBootTest
class StudentRepositoryTest extends DatabaseSpecification {

    @Autowired
    private StudentRepository studentRepository;

    def setup() {
        sql.execute(new File('src/test/resources/data.sql').text)
    }

    def cleanup() {
        sql.execute(new File('src/test/resources/cleanup.sql').text)
    }

    def "find by first name"(String firstName) {
        when:
            def list = studentRepository.findByFirstName(firstName)
        then:
            list.size() == amount
        where:
            firstName   | amount
            "Aliko"     | 1
            "Bill"      | 2
            "Folrunsho" | 3
    }

    def "find by last name"(String lastName) {
        when:
            def list = studentRepository.findByLastName(lastName)
        then:
            list.size() == amount
        where:
            lastName  | amount
            "Gates"   | 1
            "Brant"   | 1
            "Ningbro" | 1
    }

    def "find between birthdays"() {
        when:
            List<Student> list = studentRepository.findBetweenBirthdays(
                    LocalDate.of(1900, 3, 17),
                    LocalDate.of(2030, 3, 17))
        then:
            list.size() == 6
    }
}

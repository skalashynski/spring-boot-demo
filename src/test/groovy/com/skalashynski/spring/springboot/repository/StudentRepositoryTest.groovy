package com.skalashynski.spring.springboot.repository

import com.skalashynski.spring.springboot.DatabaseSpecification
import com.skalashynski.spring.springboot.bean.Student
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest
class StudentRepositoryTest extends DatabaseSpecification {
    @Autowired
    private StudentRepository studentRepository;

    @SuppressWarnings("unused")
    def setup() {
        sql.execute(new File('src/test/resources/data.sql').text)
    }

    @SuppressWarnings("unused")
    def cleanup() {
        sql.execute(new File('src/test/resources/cleanup.sql').text)
    }


    def void "find between birthdays"() {
        when: "Calling DB"
            List<Student> list = studentRepository.findBetweenBirthdays(java.sql.Date.valueOf("1900-03-17"), java.sql.Date.valueOf("2030-03-17"));
        then: ""
            list.size() == 3
    }
}

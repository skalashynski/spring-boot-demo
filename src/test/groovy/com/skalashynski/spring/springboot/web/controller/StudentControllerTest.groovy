package com.skalashynski.spring.springboot.web.controller

import com.skalashynski.spring.springboot.Application
import com.skalashynski.spring.springboot.DatabaseSpecification
import com.skalashynski.spring.springboot.entity.Student
import com.skalashynski.spring.springboot.util.ResourceUtils
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Unroll

import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@ActiveProfiles(value = "test")
@ContextConfiguration
class StudentControllerTest extends DatabaseSpecification {


    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    static String setupContent
    static String cleanupContent

    //выполняется 1 раз перед всеми тестами (подгрузка ресурсов)
    def setupSpec() {
        cleanupContent = ResourceUtils.getResourceContents('cleanup.sql')
        setupContent = ResourceUtils.getResourceContents('data.sql')
    }

    //выполняется перед каждым тестом
    @SuppressWarnings("unused")
    def setup() {
        headers.add("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJpZF92YWx1ZSIsImlhdCI6MTYzMTY4ODk4MywiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfU1RVREVOVCJ9XSwic3ViIjoiU2VyZ2lLMTAiLCJpc3MiOiJTcHJpbmdfRGVtb19BUElfU2VydmljZSIsImV4cCI6MTYzMjU1Mjk4M30.aY-4gRWYMxthyLYLC5qKFa0ozDsPbsjp4BLAamOJQkw");
        sql.execute(setupContent)
    }

    //выполняется после каждого теста (очистка таблицы)
    @SuppressWarnings("unused")
    def cleanup() {
        sql.execute(cleanupContent)
    }

    //выполняется после всех тестов 1 раз (освобождает ресурсы)
    def cleanupSpec() {
    }

    @Unroll
    def void "Student get by id"() {
        when:
            Student actualStudent
            def actualErrorMessage
            try {
                ResponseEntity<Student> response = restTemplate.exchange("http://localhost:8080/demo/api/v1/student/" + id, HttpMethod.GET, new HttpEntity<>
                        (headers), Student.class)
                actualStudent = response.getBody()
                println("Response: " + actualStudent)
            } catch (Exception e) {
                actualErrorMessage = e.getMessage()
            }

        then:
            actualStudent.getFirstName() == result.getFirstName()
            actualStudent.getLastName() == result.getLastName()
            //actualErrorMessage == expecterError
        where:
            id || result                                                                                     || expecterError
            1  || new Student(1, 'Aliko', 'Dangote', LocalDate.parse('1997-03-17'), LocalDateTime.now())     || null
            2  || new Student(2, 'Bill', 'Gates', LocalDate.parse('1955-12-31'), LocalDateTime.now())        || null
            3  || new Student(3, 'Folrunsho', 'Alakija', LocalDate.parse('1997-12-31'), LocalDateTime.now()) || null
    }
}

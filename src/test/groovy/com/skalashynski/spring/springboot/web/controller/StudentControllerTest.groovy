package com.skalashynski.spring.springboot.web.controller

import com.skalashynski.spring.springboot.Application
import com.skalashynski.spring.springboot.DatabaseSpecification
import com.skalashynski.spring.springboot.entity.Student
import com.skalashynski.spring.springboot.util.ResourceUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
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

    private static final String STUDENT_URL = "http://localhost:8080/demo/api/v1/student/"

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    @Value('${test.token}')
    private String TOKEN_1_YEAR_DURATION;

    static String setupContent
    static String cleanupContent

    def setupSpec() {
        cleanupContent = ResourceUtils.getResourceContents('cleanup.sql')
        setupContent = ResourceUtils.getResourceContents('data.sql')
    }

    @SuppressWarnings("unused")
    def setup() {
        headers.add("Authorization", TOKEN_1_YEAR_DURATION)
        sql.execute(setupContent)
    }

    @SuppressWarnings("unused")
    def cleanup() {
        sql.execute(cleanupContent)
    }

    @Unroll
    def void "Student get by #id, expected #expectedHttpCode"(Long id) {
        when:
            def response
            def actualErrorMessage
            try {
                response = restTemplate.exchange(
                        STUDENT_URL + id
                        , HttpMethod.GET
                        , new HttpEntity<>(headers)
                        , Student.class)
            } catch (Exception e) {
                actualErrorMessage = e.getMessage()
            }
        then:
            def actualStudent = response.getBody()
            if (actualStudent != null) {
                assert actualStudent.getId() == result.getId()
                assert actualStudent.getFirstName() == result.getFirstName()
                assert actualStudent.getLastName() == result.getLastName()
            } else {
                assert actualStudent == result
            }
            assert response.getStatusCode() == expectedHttpCode
        where:
            id || expectedHttpCode     || result
            1  || HttpStatus.OK        || new Student(1, 'Aliko', 'Dangote', LocalDate.parse('1997-03-17'), LocalDateTime.now())
            2  || HttpStatus.OK        || new Student(2, 'Bill', 'Gates', LocalDate.parse('1955-12-31'), LocalDateTime.now())
            3  || HttpStatus.OK        || new Student(3, 'Folrunsho', 'Alakija', LocalDate.parse('1997-12-31'), LocalDateTime.now())
            7  || HttpStatus.NOT_FOUND || null
    }

    @Unroll
    def void "Students get all, expected 6"() {
        when:
            def response
            def actualErrorMessage
            try {
                response = restTemplate.exchange(
                        STUDENT_URL
                        , HttpMethod.GET
                        , new HttpEntity<>(headers)
                        , List<Student>)
            } catch (Exception e) {
                actualErrorMessage = e.getMessage()
            }
        then:
            List<Student> actualStudents = response.getBody()
            assert actualStudents.size() == 6
    }

    @Unroll
    def void "Students get by #name, expected #count"() {
        when:
            def response
            def actualErrorMessage
            try {
                response = restTemplate.exchange(
                        STUDENT_URL + "search?name=" + name
                        , HttpMethod.GET
                        , new HttpEntity<>(headers)
                        , List<Student>)
            } catch (Exception e) {
                actualErrorMessage = e.getMessage()
            }
        then:
            List<Student> actualStudent = response.getBody()
            assert actualStudent.size() == count
        where:
            name        || count
            'Aliko'     || 1
            'Bill'      || 2
            'Folrunsho' || 3
            'Dima'      || 0
    }
}

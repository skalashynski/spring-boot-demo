package com.skalashynski.spring.springboot.web.controller

import com.skalashynski.spring.springboot.Application
import com.skalashynski.spring.springboot.DatabaseSpecification
import com.skalashynski.spring.springboot.entity.Student
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.RestClientException

import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@ActiveProfiles("test")
class StudentControllerTest extends DatabaseSpecification {

    private static final String STUDENT_URL = "http://localhost:8080/demo/api/v1/student/"

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    @Value('${test.token}')
    private String TOKEN_1_YEAR_DURATION;

    def setup() {
        headers.add("Authorization", TOKEN_1_YEAR_DURATION)
        sql.execute(new File('src/test/resources/data.sql').text)
    }

    def cleanup() {
        sql.execute(new File('src/test/resources/cleanup.sql').text)
    }

    def "Student get by #id, expected #expectedHttpCode"(Long id) {
        when:
        def response
        def actualErrorMessage
        try {
            response = restTemplate.exchange(
                    STUDENT_URL + id
                    , HttpMethod.GET
                    , new HttpEntity<>(headers)
                    , Student)
        } catch (Exception e) {
            actualErrorMessage = e.getMessage()
        }
        then:
        def actualStudent = response.getBody()
        actualStudent == result
        response.getStatusCode() == expectedHttpCode
        where:
        id | expectedHttpCode     || result
        1  | HttpStatus.OK        || new Student(1, 'Aliko', 'Dangote', LocalDate.of(1997, 3, 17), LocalDateTime.of(2021, 12, 12, 1, 2, 3))
        2  | HttpStatus.OK        || new Student(2, 'Bill', 'Gates', LocalDate.of(1955, 12, 31), LocalDateTime.of(2021, 12, 12, 1, 2, 3))
        3  | HttpStatus.OK        || new Student(3, 'Folrunsho', 'Alakija', LocalDate.of(1997, 12, 31), LocalDateTime.of(2021, 12, 12, 1, 2, 3))
        7  | HttpStatus.NOT_FOUND || null
    }

    def "Student update, expected #httpCode"(Long id, Student student){
        when:
        def response
        def actualErrorMessage
        try {
            response = restTemplate.exchange(
                    STUDENT_URL + id
                    , HttpMethod.PUT
                    , new HttpEntity<>(student, headers)
                    , Student)
        } catch (Exception e) {
            actualErrorMessage = e.getMessage()
        }
        then:
        def actualStudent = response.getBody()
        actualStudent == updatedStudent
        response.getStatusCode() == httpCode
        where:
        id    |   student                                                             | httpCode         || updatedStudent
        6     |   new Student("Michael", "Scofield", LocalDate.of(1992, 3, 4), null)  | HttpStatus.OK    || new Student(6, "Michael", "Scofield", LocalDate.of(1992, 3, 4), LocalDateTime.of(2021, 12, 12, 1, 2, 3))
    }

    def "Student save, expected #httpCode"(Student student){
        when:
        def response
        def actualErrorMessage
        try{
            response = restTemplate.exchange(
                    STUDENT_URL,
                    HttpMethod.POST,
                    new HttpEntity<Student>(student, headers),
                    Student)
        } catch (Exception ex){
            actualErrorMessage = ex.getMessage();
        }
        then:
        def actualStudent = response.getBody()
        actualStudent == savedStudent
        response.getStatusCode() == httpCode

        where:
        student                                                                                              | httpCode        | savedStudent
        new Student("Viktor", "Shilay", LocalDate.of(1999, 1, 22), LocalDateTime.of(2022, 8, 13, 9, 20))     | HttpStatus.OK   | new Student(7, "Viktor", "Shilay", LocalDate.of(1999, 1, 22), LocalDateTime.of(2022, 8, 13, 9, 20))
    }

    def "Students get all, expected 6"() {
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
        actualStudents.size() == 6
    }

    def "Students get by #firstName, expected #count"() {
        when:
        def response
        def actualErrorMessage
        try {
            response = restTemplate.exchange(
                    STUDENT_URL + "search?first_name=" + firstName
                    , HttpMethod.GET
                    , new HttpEntity<>(headers)
                    , List<Student>)
        } catch (Exception e) {
            actualErrorMessage = e.getMessage()
        }
        then:
        List<Student> actualStudent = response.getBody()
        actualStudent.size() == count
        where:
        firstName       || count
        'Aliko'         || 1
        'Bill'          || 2
        'Folrunsho'     || 3
        'Dima'          || 0
    }

    def "Students get by #lastName, expected #count"() {
        when:
        def response
        def actualErrorMessage
        try {
            response = restTemplate.exchange(
                    STUDENT_URL + "search?last_name=" + lastName
                    , HttpMethod.GET
                    , new HttpEntity<>(headers)
                    , List<Student>)
        } catch (Exception e) {
            actualErrorMessage = e.getMessage()
        }
        then:
        List<Student> actualStudent = response.getBody()
        actualStudent.size() == count
        where:
        lastName       || count
        'Dangote'      || 1
        'Gates'        || 1
        'Kek'          || 0
    }

    def "Delete student by #id, expected #code"(Long id) {
        when:
        def response
        def actualErrorMessage
        try {
            response = restTemplate.exchange(
                    STUDENT_URL + id
                    , HttpMethod.DELETE
                    , new HttpEntity<>(headers)
                    , void)
        } catch (Exception e) {
            actualErrorMessage = e.getMessage()
        }
        then:
        response.getStatusCode() == code
        where:
        id || code
        1  || HttpStatus.NO_CONTENT
        2  || HttpStatus.NO_CONTENT
        3  || HttpStatus.NO_CONTENT
        8  || HttpStatus.NOT_FOUND
    }

    def "Students get by date between #from and #to, expected #count"() {
        when:
        def response
        def actualErrorMessage
        try {
            response = restTemplate.exchange(
                    STUDENT_URL + "/search/birthdays?from=" + from + "&to=" + to
                    , HttpMethod.GET
                    , new HttpEntity<>(headers)
                    , List<Student>)
        } catch (RestClientException e) {
            actualErrorMessage = e.getMessage()
        }
        then:
        if (response != null) {
            List<Student> actualStudents = response.getBody()
            actualStudents.size() == count
            response.getStatusCode() == status
        } else {
            count == null
            status == HttpStatus.BAD_REQUEST
        }

        where:
        from         | to           | count || status
        '1997-03-17' | '1997-12-31' | 2     || HttpStatus.OK
        '1955-12-31' | '1997-12-31' | 6     || HttpStatus.OK
        '1993-06-03' | '1993-06-03' | 1     || HttpStatus.OK
        '2000-06-03' | '2000-12-03' | 0     || HttpStatus.OK
        ' '          | ' '          | null  || HttpStatus.BAD_REQUEST
    }
}

package com.skalashynski.spring.springboot.service.impl

import com.skalashynski.spring.springboot.DatabaseSpecification
import com.skalashynski.spring.springboot.entity.Student
import com.skalashynski.spring.springboot.service.StudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest
class StudentServiceImplTest extends DatabaseSpecification {

    @Autowired
    private StudentService studentService

    def setup() {
        sql.execute(new File('src/test/resources/data.sql').text)
    }

    def cleanup() {
        sql.execute(new File('src/test/resources/cleanup.sql').text)
    }

    def "save student"(Student student) {
        when:
        def checkStudent = studentService.save(student)

        then:
        checkStudent.id == 7
        checkStudent.firstName == student.firstName
        checkStudent.lastName == student.lastName
        checkStudent.birthday == student.birthday
        checkStudent.createdAt == student.createdAt

        where:
        student                                                                         | _
        new Student("Viktor", "Shilay", LocalDate.of(1999, 1, 22), LocalDateTime.now()) | _
    }


    def "get student by id '#id'"(Long id) {
        when:
        def student = studentService.getById(id).get()

        then:
        student == actualStudent

        where:
        id | actualStudent
        1  | new Student(1, "Aliko", "Dangote", LocalDate.of(1997, 3, 17), LocalDateTime.of(2021, 12, 12, 1, 2, 3))
        2  | new Student(2, "Bill", "Gates", LocalDate.of(1955, 12, 31), LocalDateTime.of(2021, 12, 12, 1, 2, 3))
        3  | new Student(3, "Folrunsho", "Alakija", LocalDate.of(1997, 12, 31), LocalDateTime.of(2021, 12, 12, 1, 2, 3))
        4  | new Student(4, "Bill", "Brant", LocalDate.of(1988, 3, 2), LocalDateTime.of(2021, 12, 12, 1, 2, 3))
        5  | new Student(5, "Folrunsho", "Ningbro", LocalDate.of(1983, 1, 25), LocalDateTime.of(2021, 12, 12, 1, 2, 3))
        6  | new Student(6, "Folrunsho", "Po", LocalDate.of(1993, 6, 3), LocalDateTime.of(2021, 12, 12, 1, 2, 3))
    }

    def "find student by first name '#firstName'"(String firstName) {
        when:
        def list = studentService.findByFirstName(firstName)
        then:
        list.size() == amount
        where:
        firstName   | amount
        'Aliko'     | 1
        'Bill'      | 2
        'Folrunsho' | 3
        'Dima'      | 0
    }

    def "find student by last name '#lastName'"(String lastName) {
        when:
        def list = studentService.findByLastName(lastName)
        then:
        list.size() == amount
        where:
        lastName  | amount
        'Dangote' | 1
        'Gates'   | 1
        'Kek'     | 0
    }

    def "get all students"() {
        when:
        def list = studentService.getAll()
        then:
        list.size() == 6
    }

    def "delete student by id '#id'"(Long id) {
        when:
        def bool = studentService.delete(id)
        then:
        bool == result
        where:
        id | result
        1  | true
        2  | true
        7  | false
    }

    def "update student by id '#id'"(Long id, Student student) {
        when:
        def updStudent = studentService.update(id, student)
        then:
        updStudent == result
        where:
        id | student                                                            || result
        6  | new Student("Michael", "Scofield", LocalDate.of(1992, 3, 4), null) || new Student(6, "Michael", "Scofield", LocalDate.of(1992, 3, 4), LocalDateTime.of(2021, 12, 12, 1, 2, 3))
    }

    def "find students between '#from' and '#to'"(LocalDate from, LocalDate to) {
        when:
        def list = studentService.findBetweenBirthdays(from, to)
        then:
        list.size() == result
        where:
        from                       | to                         || result
        LocalDate.of(1954, 12, 31) | LocalDate.of(2000, 12, 31) || 6
        LocalDate.of(1983, 01, 24) | LocalDate.of(1997, 12, 30) || 4
        LocalDate.of(2000, 12, 31) | LocalDate.of(2022, 12, 31) || 0
    }
}

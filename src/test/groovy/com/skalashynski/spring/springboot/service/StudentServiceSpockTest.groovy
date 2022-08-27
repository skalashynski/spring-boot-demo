package com.skalashynski.spring.springboot.service

import com.skalashynski.spring.springboot.entity.Student
import com.skalashynski.spring.springboot.exception.ApiException
import com.skalashynski.spring.springboot.exception.message.ApiExceptionMessage
import com.skalashynski.spring.springboot.repository.StudentRepository
import com.skalashynski.spring.springboot.service.impl.StudentServiceImpl
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class StudentServiceSpockTest extends Specification {

    private static final LocalDateTime CREATED_AT = LocalDateTime.now()
    private StudentRepository studentRepository = Mock()
    private StudentService studentService = new StudentServiceImpl(studentRepository)

    def "Should save entity #expected and return it"(Student expected) {
        when:
        Student actual = studentService.save(expected)

        then:
        1 * studentRepository.save(expected) >> expected
        expected == actual

        where:
        expected                                                                                    | _
        new Student(Long.MIN_VALUE, "firstName", "lastName", LocalDate.of(1993, 1, 17), CREATED_AT) | _
        new Student(Long.MAX_VALUE, "someName", "lastName", LocalDate.of(2000, 12, 1), CREATED_AT)  | _
        new Student(1L, "", "", LocalDate.of(1980, 5, 28), CREATED_AT)                              | _
        new Student(20L, "Ivan", null, LocalDate.of(2005, 8, 10), CREATED_AT)                       | _
    }

    def "Should return an optional entity by id #id"(long id) {
        when:
        Optional<Student> actual = studentService.getById(id)

        then:
        1 * studentRepository.findById(id) >> expected
        expected == actual

        where:
        id             | expected
        Long.MIN_VALUE | Optional<Student>.of(new Student(Long.MIN_VALUE, "firstName", "lastName", LocalDate.of(1993, 1, 17), CREATED_AT))
        Long.MAX_VALUE | Optional<Student>.of(new Student(Long.MAX_VALUE, "someName", "lastName", LocalDate.of(2000, 12, 1), CREATED_AT))
        20L            | Optional<Student>.of(new Student(20L, "Ivan", null, LocalDate.of(2005, 8, 10), CREATED_AT))
        1234L          | Optional<Student>.empty()
    }

    def "Should return a list of entities by firstname '#firstname'"(String firstname) {
        when:
        List<Student> actual = studentService.findByFirstName(firstname)

        then:
        1 * studentRepository.findByFirstName(firstname) >> expected
        expected == actual

        where:
        firstname      | expected
        ""             | List<Student>.of(new Student(1L, "", "", LocalDate.of(1980, 5, 28), CREATED_AT))
        "Ivan"         | List<Student>.of(
                            new Student(20L, "Ivan", null, LocalDate.of(2005, 8, 10), CREATED_AT),
                            new Student(Long.MAX_VALUE, "someName", "lastName", LocalDate.of(2000, 12, 1), CREATED_AT))
        "non-existent" | List<Student>.of()
    }

    def "Should return a list of all entities"() {
        when:
        List<Student> actual = studentService.getAll()

        then:
        1 * studentRepository.findAll() >> expected
        expected == actual

        where:
        expected                                                     | _
        List<Student>.of(
                new Student(Long.MAX_VALUE, "someName", "lastName",
                        LocalDate.of(2000, 12, 1), CREATED_AT),
                new Student(20L, "Ivan", null,
                        LocalDate.of(2005, 8, 10), CREATED_AT))      | _
        List<Student>.of()                                           | _
    }

    def "Should delete an entity by id #id and return true if exists"(long id) {
        given:
        studentRepository.existsById(id) >> expected

        when:
        boolean actual = studentService.delete(id)

        then:
        i * studentRepository.deleteById(id)
        expected == actual

        where:
        id             | expected | i
        Long.MIN_VALUE | true     | 1
        Long.MAX_VALUE | false    | 0
        20L            | true     | 1
        123L           | false    | 0
    }

    def "Should update a found entity by id #id and return it if exists"(long id, Student student) {
        given:
        studentRepository.findById(id) >> foundById
        Student expected = new Student(id, student.getFirstName(), student.getLastName(),
                student.getBirthday(), foundById.orElseThrow().getCreatedAt())

        when:
        Student actual = studentService.update(id, student)

        then:
        1 * studentRepository.save(expected) >> expected
        expected == actual

        where:
        id  | student                                                                                         | foundById
        1L  | new Student(20L, "Ivan", null, LocalDate.of(2000, 1, 1), CREATED_AT)                            | Optional<Student>.of(new Student(1L, "Petr", "Petrov", LocalDate.of(1995, 10, 10), CREATED_AT))
        10L | new Student("Ivan", null, LocalDate.of(2000, 1, 1), LocalDateTime.parse("2007-12-03T10:15:30")) | Optional<Student>.of(new Student(10L, "Petr", "Petrov", LocalDate.of(1995, 10, 10), CREATED_AT))
    }

    def "Should throw the ApiException if entity doesn't exist with given id #id"(long id, Student student) {
        given:
        studentRepository.findById(id) >> foundById

        when:
        studentService.update(id, student)

        then:
        0 * studentRepository.save(_)
        ApiException e = thrown()
        e.getMessage() == ApiExceptionMessage.CAN_NOT_FIND.getMessage() + id

        where:
        id  | student                                                        | foundById
        1L  | new Student("Ivan", null, LocalDate.of(2000, 1, 1), CREATED_AT)| Optional<Student>.empty()
    }

    def "Should return a list of entities by period of birthday from #from to #to"(Date from, Date to) {
        when:
        List<Student> actual = studentService.findBetweenBirthdays(from, to)

        then:
        1 * studentRepository.findBetweenBirthdays(from, to) >> expected
        expected == actual

        where:
        from                    | to                       | expected
        new Date(473385600000L) | new Date(1123632000000L) | List<Student>.of(
                                                                new Student(Long.MIN_VALUE, "firstName", "lastName",
                                                                        LocalDate.of(1993, 1, 17), CREATED_AT),
                                                                new Student(Long.MAX_VALUE, "someName", "lastName",
                                                                        LocalDate.of(2000, 12, 1), CREATED_AT),
                                                                new Student(20L, "Ivan", null,
                                                                        LocalDate.of(2005, 8, 10), CREATED_AT))
        new Date(975628800000L) | new Date(1136073600000L) | List<Student>.of()
    }
}
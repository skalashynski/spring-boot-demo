package com.skalashynski.spring.springboot.service

import com.skalashynski.spring.springboot.entity.Student
import com.skalashynski.spring.springboot.exception.ApiException
import com.skalashynski.spring.springboot.exception.message.ApiExceptionMessage
import com.skalashynski.spring.springboot.repository.StudentRepository
import com.skalashynski.spring.springboot.service.impl.StudentServiceImpl
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.stream.Stream

class StudentServiceSpockTest extends Specification {

    private static final LocalDateTime CREATED_AT = LocalDateTime.now()
    private StudentRepository studentRepository = Mock()
    private StudentService studentService = new StudentServiceImpl(studentRepository)

    private static Stream<Student> provideStudents() {
        return Stream.of(
                new Student(Long.MIN_VALUE, "firstName", "lastName",
                        LocalDate.of(1993, 1, 17), CREATED_AT),
                new Student(Long.MAX_VALUE, "someName", "lastName",
                        LocalDate.of(2000, 12, 1), CREATED_AT),
                new Student(1L, "", "",
                        LocalDate.of(1980, 5, 28), CREATED_AT),
                new Student(20L, "Ivan", null,
                        LocalDate.of(2005, 8, 10), CREATED_AT)
        )
    }

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
        given:
        Optional<Student> expected = provideStudents()
                .filter(s -> s.getId() == id)
                .findFirst()

        when:
        Optional<Student> actual = studentService.getById(id)

        then:
        1 * studentRepository.findById(id) >> expected
        expected == actual

        where:
        id             | _
        Long.MIN_VALUE | _
        Long.MAX_VALUE | _
        20L            | _
        1234L          | _
    }

    def "Should return a list of entities by firstname '#firstname'"(String firstname) {
        given:
        List<Student> expected = provideStudents()
                .filter(s -> s.getFirstName() == firstname)
                .toList()

        when:
        List<Student> actual = studentService.findByFirstName(firstname)

        then:
        1 * studentRepository.findByFirstName(firstname) >> expected
        expected == actual

        where:
        firstname   | _
        "some name" | _
        ""          | _
        "Ivan"      | _
    }

    def "Should return a list of all entities"() {
        given:
        List<Student> expected = provideStudents().toList()

        when:
        List<Student> actual = studentService.getAll()

        then:
        1 * studentRepository.findAll() >> expected
        expected == actual
    }

    def "Should delete an entity by id #id and return true if exists"(long id) {
        given:
        boolean expected = provideStudents()
                .anyMatch(s -> s.getId() == id)
        studentRepository.existsById(id) >> expected

        when:
        boolean actual = studentService.delete(id)

        then:
        1 * studentRepository.deleteById(id)
        actual

        where:
        id             | _
        Long.MIN_VALUE | _
        Long.MAX_VALUE | _
        20L            | _
    }

    def "Shouldn't delete any entity and return false if doesn't exist"(long id) {
        given:
        boolean expected = provideStudents()
                .anyMatch(s -> s.getId() == id)
        studentRepository.existsById(id) >> expected

        when:
        boolean actual = studentService.delete(id)

        then:
        0 * studentRepository.deleteById(id)
        !actual

        where:
        id    | _
        5L    | _
        100L  | _
        1234L | _
    }

    def "Should update a found entity by id #id and return it if exists"(long id, Student student) {
        given:
        Optional<Student> foundById = provideStudents()
                .filter(s -> s.getId() == id)
                .findFirst()
        studentRepository.findById(id) >> foundById

        and:
        Student expected = new Student(id, student.getFirstName(), student.getLastName(),
                student.getBirthday(), foundById.orElseThrow().getCreatedAt())

        when:
        Student actual = studentService.update(id, student)

        then:
        1 * studentRepository.save(expected) >> expected
        expected == actual

        where:
        id             | student
        1L             | new Student(20L, "Ivan", null, LocalDate.of(2000, 1, 1), CREATED_AT)
        Long.MIN_VALUE | new Student(Long.MAX_VALUE, "someName", "lastName", LocalDate.of(2000, 12, 1), CREATED_AT)
    }

    def "Should throw the ApiException if entity doesn't exist with given id #id"(long id, Student student) {
        given:
        Optional<Student> foundById = provideStudents()
                .filter(s -> s.getId() == id)
                .findFirst()
        studentRepository.findById(id) >> foundById

        when:
        studentService.update(id, student)

        then:
        0 * studentRepository.save(_)
        ApiException e = thrown()
        e.getMessage() == ApiExceptionMessage.CAN_NOT_FIND.getMessage() + id

        where:
        id   | student
        3L   | new Student(20L, "Ivan", null, LocalDate.of(2000, 1, 1), CREATED_AT)
        111L | new Student(Long.MAX_VALUE, "someName", "lastName", LocalDate.of(2000, 12, 1), CREATED_AT)
    }

    def "Should return a list of entities by period of birthday from #from to #to"(Date from, Date to) {
        given:
        List<Student> expected = provideStudents()
                .filter(s -> s.getBirthday().getTime() >= from.getTime()
                        && s.getBirthday().getTime() <= to.getTime())
                .toList()

        when:
        List<Student> actual = studentService.findBetweenBirthdays(from, to)

        then:
        1 * studentRepository.findBetweenBirthdays(from, to) >> expected
        expected == actual

        where:
        from                    | to
        // from 01.01.1985 to 10.08.2005
        new Date(473385600000L) | new Date(1123632000000L)
        // from 01.12.2000 to 01.01.2006
        new Date(975628800000L) | new Date(1136073600000L)
    }
}
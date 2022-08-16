package com.skalashynski.spring.springboot.service;

import com.skalashynski.spring.springboot.entity.Student;
import com.skalashynski.spring.springboot.exception.ApiException;
import com.skalashynski.spring.springboot.repository.StudentRepository;
import com.skalashynski.spring.springboot.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private static Stream<Student> provideStudents() {
        return Stream.of(
            new Student(Long.MIN_VALUE, "firstName", "lastName",
                        LocalDate.of(1993, 1, 17), LocalDateTime.now()),
            new Student(Long.MAX_VALUE, "someName", "lastName",
                        LocalDate.of(2000, 12, 1), LocalDateTime.now()),
            new Student(1L, "", "",
                        LocalDate.of(1980, 5, 28), LocalDateTime.now()),
            new Student(20L, "Ivan", null,
                        LocalDate.of(2005, 8, 10), LocalDateTime.now())
        );
    }

    private static Stream<Arguments> provideArgsForUpdateTest() {
        return Stream.of(
                arguments(1L, new Student(20L, "Ivan", null,
                        LocalDate.of(2000, 1, 1), LocalDateTime.now())),
                arguments(Long.MIN_VALUE, new Student(Long.MAX_VALUE, "someName", "lastName",
                        LocalDate.of(2000, 12, 1), LocalDateTime.now()))
        );
    }

    private static Stream<Arguments> provideArgsForUpdateFailTest() {
        return Stream.of(
                arguments(111L, new Student(20L, "Ivan", null,
                        LocalDate.of(2000, 1, 1), LocalDateTime.now())),
                arguments(3L, new Student(Long.MAX_VALUE, "someName", "lastName",
                        LocalDate.of(2000, 12, 1), LocalDateTime.now()))
        );
    }

    private static Stream<Arguments> provideArgsForFindBetweenBirthdaysTest() {
        return Stream.of(
                // from 01.01.1985 to 10.08.2005
                arguments(new Date(473385600000L), new Date(1123632000000L)),
                // from 01.12.2000 to 01.01.2006
                arguments(new Date(975628800000L), new Date(1136073600000L))
        );
    }

    @ParameterizedTest
    @MethodSource("provideStudents")
    @DisplayName("Should save entity and return it")
    void saveTest(Student expected) {
        given(studentRepository.save(expected))
                .willReturn(expected);

        Student actual = studentService.save(expected);

        then(studentRepository)
                .should()
                .save(expected);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE, Long.MAX_VALUE, 20L, 1234L})
    @DisplayName("Should return an optional entity by id")
    void getByIdTest(long id) {
        List<Student> students = provideStudents().toList();
        Optional<Student> expected = students.stream().filter(s -> s.getId() == id).findFirst();
        given(studentRepository.findById(id))
                .willReturn(expected);

        Optional<Student> actual = studentService.getById(id);

        then(studentRepository)
                .should()
                .findById(id);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"someName", "", "Ivan"})
    @DisplayName("Should return a list of entities by firstname")
    void findByFirstNameTest(String name) {
        List<Student> expected = provideStudents().filter(s -> s.getFirstName().equals(name)).toList();
        given(studentRepository.findByFirstName(name))
                .willReturn(expected);

        List<Student> actual = studentService.findByFirstName(name);

        then(studentRepository)
                .should()
                .findByFirstName(name);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should return a list of all entities")
    void getAllTest() {
        List<Student> expected = provideStudents().toList();
        given(studentRepository.findAll()).willReturn(expected);

        List<Student> actual = studentService.getAll();

        then(studentRepository)
                .should()
                .findAll();
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE, Long.MAX_VALUE, 20L})
    @DisplayName("Should delete an entity by id and return true if exists")
    void deleteTest_ok(long id) {
        boolean expected = provideStudents()
                .anyMatch(s -> s.getId() == id);
        given(studentRepository.existsById(id))
                .willReturn(expected);

        boolean actual = studentService.delete(id);

        then(studentRepository)
                .should()
                .deleteById(id);
        assertTrue(expected);
        assertTrue(actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {1234L, 5L, 100L})
    @DisplayName("Shouldn't delete any entity and return false if doesn't exist")
    void deleteTest_fail(long id) {
        boolean expected = provideStudents()
                .anyMatch(s -> s.getId() == id);
        given(studentRepository.existsById(id))
                .willReturn(expected);

        boolean actual = studentService.delete(id);

        then(studentRepository)
                .should(never())
                .deleteById(id);
        assertFalse(expected);
        assertFalse(actual);
    }

    @ParameterizedTest
    @MethodSource("provideArgsForUpdateTest")
    @DisplayName("Should update an entity by id and return it if exists")
    void updateTest_ok(long id, Student student) {
        Optional<Student> foundById = provideStudents()
                .filter(s -> s.getId() == id)
                .findFirst();
        given(studentRepository.findById(id))
                .willReturn(foundById);

        Student expected = new Student(id, student.getFirstName(), student.getLastName(),
                student.getBirthday(), foundById.orElseThrow().getCreatedAt());
        given(studentRepository.save(expected))
                .willReturn(expected);

        Student actual = studentService.update(id, student);

        then(studentRepository)
                .should()
                .save(expected);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideArgsForUpdateFailTest")
    @DisplayName("Should throw the ApiException if entity doesn't exist with given id")
    void updateTest_fail(long id, Student student) {
        Optional<Student> foundById = provideStudents()
                .filter(s -> s.getId() == id)
                .findFirst();
        given(studentRepository.findById(id))
                .willReturn(foundById);

        assertThrows(ApiException.class,
                () -> studentService.update(id, student),
                "Can't find student with id: " + id);
    }

    @ParameterizedTest
    @MethodSource("provideArgsForFindBetweenBirthdaysTest")
    @DisplayName("Should return a list of entities by period of birthday")
    void findBetweenBirthdaysTest(Date from, Date to) {
        List<Student> expected = provideStudents()
                .filter(s -> s.getBirthday().getTime() >= from.getTime() && s.getBirthday().getTime() <= to.getTime())
                .toList();

        given(studentRepository.findBetweenBirthdays(from, to)).willReturn(expected);

        List<Student> actual = studentService.findBetweenBirthdays(from, to);

        then(studentRepository)
                .should()
                .findBetweenBirthdays(from, to);
        assertEquals(expected, actual);
    }
}
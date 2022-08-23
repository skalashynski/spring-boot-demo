package com.skalashynski.spring.springboot.service;

import com.skalashynski.spring.springboot.entity.Student;
import com.skalashynski.spring.springboot.exception.ApiException;
import com.skalashynski.spring.springboot.exception.message.ApiExceptionMessage;
import com.skalashynski.spring.springboot.repository.StudentRepository;
import com.skalashynski.spring.springboot.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.skalashynski.spring.springboot.util.StudentServiceTestArgsProvider.provideStudents;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @ParameterizedTest
    @MethodSource("com.skalashynski.spring.springboot.util.StudentServiceTestArgsProvider#provideStudents")
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
    @MethodSource("com.skalashynski.spring.springboot.util.StudentServiceTestArgsProvider#provideArgsForGetByIdTest")
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
    @MethodSource("com.skalashynski.spring.springboot.util.StudentServiceTestArgsProvider#provideArgsForFindByFirstNameTest")
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
    @MethodSource("com.skalashynski.spring.springboot.util.StudentServiceTestArgsProvider#provideArgsForDeleteOkTest")
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
    @MethodSource("com.skalashynski.spring.springboot.util.StudentServiceTestArgsProvider#provideArgsForDeleteFailTest")
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
    @MethodSource("com.skalashynski.spring.springboot.util.StudentServiceTestArgsProvider#provideArgsForUpdateTest")
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
    @MethodSource("com.skalashynski.spring.springboot.util.StudentServiceTestArgsProvider#provideArgsForUpdateFailTest")
    @DisplayName("Should throw the ApiException if entity doesn't exist with given id")
    void updateTest_fail(long id, Student student) {
        Optional<Student> foundById = provideStudents()
                .filter(s -> s.getId() == id)
                .findFirst();
        given(studentRepository.findById(id))
                .willReturn(foundById);

        assertThrows(ApiException.class,
                () -> studentService.update(id, student),
                ApiExceptionMessage.CAN_NOT_FIND.getMessage() + id);
    }

    @ParameterizedTest
    @MethodSource("com.skalashynski.spring.springboot.util.StudentServiceTestArgsProvider#provideArgsForFindBetweenBirthdaysTest")
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
package com.skalashynski.spring.springboot.service;

import com.skalashynski.spring.springboot.entity.Student;
import com.skalashynski.spring.springboot.exception.ApiException;
import com.skalashynski.spring.springboot.repository.StudentRepository;
import com.skalashynski.spring.springboot.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    private StudentService studentService;
    @Mock
    private StudentRepository studentRepository;

    private Student student;

    @BeforeEach
    void init() {
        studentService = new StudentServiceImpl(studentRepository);
        student = new Student(1L, "firstName", "lastName",
                LocalDate.of(2000, 1, 1), LocalDateTime.now());
    }

    @Test
    void saveTest_whenMethodInvokes_thenStudentEntitySavesIntoRepAndReturns() {
        Student expected = student;
        given(studentRepository.save(ArgumentMatchers.any(Student.class)))
                .willReturn(student);

        Student actual = studentService.save(student);

        then(studentRepository)
                .should()
                .save(student);
        assertEquals(expected, actual);
    }

    @Test
    void getByIdTest_whenMethodInvokes_thenOptionalOfStudentReturns() {
        Optional<Student> expected = Optional.of(student);
        given(studentRepository.findById(student.getId()))
                .willReturn(Optional.of(student));

        Optional<Student> actual = studentService.getById(student.getId());

        then(studentRepository)
                .should()
                .findById(student.getId());
        assertEquals(expected, actual);
    }

    @Test
    void findByFirstNameTest_whenMethodInvokes_thenListOfStudentsReturns() {
        List<Student> expected = new ArrayList<>();
        expected.add(student);
        given(studentRepository.findByFirstName(student.getFirstName()))
                .willReturn(expected);

        List<Student> actual = studentService.findByFirstName(student.getFirstName());

        then(studentRepository)
                .should()
                .findByFirstName(student.getFirstName());
        assertEquals(expected, actual);
    }

    @Test
    void getAllTest_whenMethodInvokes_thenListOfStudentsReturns() {
        List<Student> expected = new ArrayList<>();
        expected.add(student);
        given(studentRepository.findAll()).willReturn(expected);

        List<Student> actual = studentService.getAll();

        then(studentRepository)
                .should()
                .findAll();
        assertEquals(expected, actual);
    }

    @Test
    void deleteTest_whenEntityExists_thenEntityDeletesFromRepAndReturnsTrue() {
        given(studentRepository.existsById(student.getId()))
                .willReturn(true);

        boolean actual = studentService.delete(student.getId());

        then(studentRepository)
                .should()
                .deleteById(student.getId());
        assertTrue(actual);
    }

    @Test
    void deleteTest_whenEntityDoesNotExist_thenEntityReturnsFalse() {
        given(studentRepository.existsById(student.getId()))
                .willReturn(false);

        boolean actual = studentService.delete(student.getId());

        then(studentRepository)
                .should(never())
                .deleteById(student.getId());
        assertFalse(actual);
    }

    @Test
    void updateTest_whenEntityExists_thenEntitySavesIntoRepAndReturns() {
        String newFirstName = "firstName2";
        String newLastName = "lastName2";
        LocalDate newBirthday = LocalDate.of(2001, 1, 1);

        Student expected = new Student(student.getId(), newFirstName, newLastName, newBirthday,
                student.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        Student forUpdate = new Student(2L, newFirstName, newLastName, newBirthday, LocalDateTime.now());

        given(studentRepository.findById(student.getId()))
                .willReturn(Optional.of(student));
        given(studentRepository.save(expected))
                .willReturn(expected);

        Student actual = studentService.update(student.getId(), forUpdate);

        then(studentRepository)
                .should()
                .save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void updateTest_whenEntityDoesNotExist_thenApiExceptionThrows() {
        String newFirstName = "firstName2";
        String newLastName = "lastName2";
        LocalDate newBirthday = LocalDate.of(2001, 1, 1);

        Student forUpdate = new Student(2L, newFirstName, newLastName, newBirthday, LocalDateTime.now());

        given(studentRepository.findById(student.getId()))
                .willReturn(Optional.empty());

        assertThrows(ApiException.class,
                () -> studentService.update(student.getId(), forUpdate),
                "Can't find student with id: " + student.getId());
    }

    @Test
    void findBetweenBirthdaysTest_whenMethodInvokes_thenListOfStudentsReturns() {
        Date from = new Date(1999, Calendar.FEBRUARY, 1);
        Date to = new Date(2001, Calendar.FEBRUARY, 1);

        List<Student> expected = new ArrayList<>();
        expected.add(student);

        given(studentRepository.findBetweenBirthdays(from, to)).willReturn(expected);

        List<Student> actual = studentService.findBetweenBirthdays(from, to);

        then(studentRepository)
                .should()
                .findBetweenBirthdays(from, to);
        assertEquals(expected, actual);
    }
}
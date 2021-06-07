package com.skalashynski.spring.springboot.repository.Impl;

import com.skalashynski.spring.springboot.bean.Student;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("fakeStudentRepository")
public class FakeStudentRepositoryImpl {
    private static final List<Student> STUDENTS = new ArrayList<>(Arrays.asList(
            new Student(1, "1_firstName", "1_lastName", LocalDate.now(), LocalDateTime.now()),
            new Student(2, "2_firstName", "2_lastName", LocalDate.now(), LocalDateTime.now()),
            new Student(3, "3_firstName", "3_lastName", LocalDate.now(), LocalDateTime.now())
    ));


    public Student save(Student student) {
        student.setId(STUDENTS.size() + 1);
        STUDENTS.add(student);
        return student;
    }

    public Optional<Student> findById(Long id) {
        return STUDENTS.stream().filter(s -> s.getId() == id).findFirst();
    }

    public List<Student> findByFirstName(String name) {
        return STUDENTS.stream().filter(s -> s.getFirstName().equals(name)).collect(Collectors.toList());
    }

    public List<Student> findAll() {
        return STUDENTS;
    }

    public void deleteById(Long id) {
        Optional<Student> student = STUDENTS.stream().filter(s -> s.getId() == id).findFirst();
        student.ifPresent(STUDENTS::remove);
    }

    public Student update(long id, Student student) {
        Optional<Student> findStudent = STUDENTS.stream().filter(s -> s.getId() == id).findFirst();
        if (findStudent.isPresent()) {
            STUDENTS.remove(findStudent.get());
            STUDENTS.add(student);
        }
        return student;
    }
}

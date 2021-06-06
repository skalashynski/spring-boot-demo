package com.skalashynski.spring.springboot.service.impl;

import com.skalashynski.spring.springboot.bean.Student;
import com.skalashynski.spring.springboot.service.StudentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("fakeStudentService")
public class FakeStudentServiceImpl implements StudentService {
    private static final List<Student> STUDENTS = new ArrayList<>(Arrays.asList(
            new Student(1, "1_firstName", "1_lastName", LocalDate.now(), LocalDateTime.now()),
            new Student(2, "2_firstName", "2_lastName", LocalDate.now(), LocalDateTime.now()),
            new Student(3, "3_firstName", "3_lastName", LocalDate.now(), LocalDateTime.now())
    ));


    @Override
    public Student save(Student student) {
        student.setId(STUDENTS.size() + 1);
        STUDENTS.add(student);
        return student;
    }

    @Override
    public Optional<Student> getById(long id) {
        return STUDENTS.stream().filter(s -> s.getId() == id).findFirst();
    }

    @Override
    public List<Student> findByFirstName(String name) {
        return STUDENTS.stream().filter(s -> s.getFirstName().equals(name)).collect(Collectors.toList());
    }

    @Override
    public List<Student> getAll() {
        return STUDENTS;
    }

    @Override
    public void delete(long id) {
        Optional<Student> student = STUDENTS.stream().filter(s -> s.getId() == id).findFirst();
        student.ifPresent(STUDENTS::remove);
    }

    @Override
    public Student update(long id, Student student) {
        Optional<Student> findStudent = STUDENTS.stream().filter(s -> s.getId() == id).findFirst();
        if (findStudent.isPresent()) {
            STUDENTS.remove(findStudent.get());
            STUDENTS.add(student);
        }
        return student;
    }
}

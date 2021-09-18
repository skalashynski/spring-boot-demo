package com.skalashynski.spring.springboot.service;

import com.skalashynski.spring.springboot.entity.Student;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student save(Student student);

    Optional<Student> getById(long id);

    List<Student> findByFirstName(String name);

    List<Student> getAll();

    void delete(long id);

    Student update(long id, Student student);

    List<Student>findBetweenBirthdays(Date from, Date to);
}

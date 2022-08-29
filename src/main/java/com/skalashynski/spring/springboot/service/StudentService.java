package com.skalashynski.spring.springboot.service;

import com.skalashynski.spring.springboot.entity.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentService {

    Student save(Student student);

    Optional<Student> getById(long id);

    List<Student> findByFirstName(String name);

    List<Student> findByLastName(String name);

    List<Student> getAll();

    boolean delete(long id);

    Student update(long id, Student student);

    List<Student> findBetweenBirthdays(LocalDate from, LocalDate to);
}

package com.skalashynski.spring.springboot.service.impl;

import com.skalashynski.spring.springboot.bean.Student;
import com.skalashynski.spring.springboot.exception.StudentException;
import com.skalashynski.spring.springboot.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);
    @Autowired
    private StudentRepository studentRepository;

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> getById(long id) {
        return studentRepository.findById(id);
    }

    public List<Student> findByFirstName(String name) {
        return studentRepository.findByFirstName(name);
    }

    public List<Student> getAll() {
        List<Student> res = new ArrayList<>();
        studentRepository.findAll().forEach(res::add);
        return res;
    }

    public void delete(long id) {
        studentRepository.deleteById(id);
    }

    public Student update(long id, Student student) {
        Optional<Student> studentDao = studentRepository.findById(id);
        if (!studentDao.isPresent()) {
            throw new StudentException("Can't find student with id: " + id);
        }
        student.setId(id);
        return studentRepository.save(student);

    }
}

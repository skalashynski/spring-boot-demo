package com.skalashynski.spring.springboot.service.impl;

import com.skalashynski.spring.springboot.bean.Student;
import com.skalashynski.spring.springboot.exception.StudentRequestException;
import com.skalashynski.spring.springboot.repository.StudentDAO;
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
    private StudentDAO studentDAO;

    public Student save(Student student) {
        return studentDAO.save(student);
    }

    public Optional<Student> getById(long id) {
        return studentDAO.findById(id);
    }

    public List<Student> findByFirstName(String name) {
        return studentDAO.findByFirstName(name);
    }

    public List<Student> getAll() {
        List<Student> res = new ArrayList<>();
        studentDAO.findAll().forEach(res::add);
        return res;
    }

    public void delete(long id) {
        studentDAO.deleteById(id);
    }

    public Student update(long id, Student student) {
        Optional<Student> studentDao = studentDAO.findById(id);
        if (studentDao.isPresent()) {
            student.setId(id);
            return studentDAO.save(student);
        } else {
            throw new StudentRequestException("Can't find student with id: " + id);
        }
    }
}

package com.skalashynski.spring.springboot.service.impl;

import com.skalashynski.spring.springboot.bean.Student;
import com.skalashynski.spring.springboot.exception.StudentException;
import com.skalashynski.spring.springboot.repository.StudentRepository;
import com.skalashynski.spring.springboot.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("studentService")
public class StudentServiceImpl implements StudentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> getById(long id) {
        return studentRepository.findById(id);
    }

    @Override
    public List<Student> findByFirstName(String name) {
        return studentRepository.findByFirstName(name);
    }

    @Override
    public List<Student> getAll() {
        List<Student> res = new ArrayList<>();
        studentRepository.findAll().forEach(res::add);
        return res;
    }

    @Override
    public void delete(long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Student update(long id, Student student) {
        Optional<Student> studentDao = studentRepository.findById(id);
        if (!studentDao.isPresent()) {
            throw new StudentException("Can't find student with id: " + id);
        }
        student.setId(id);
        return studentRepository.save(student);

    }
}

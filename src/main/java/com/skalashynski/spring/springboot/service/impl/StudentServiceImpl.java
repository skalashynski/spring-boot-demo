package com.skalashynski.spring.springboot.service.impl;

import com.skalashynski.spring.springboot.entity.Student;
import com.skalashynski.spring.springboot.exception.ApiException;
import com.skalashynski.spring.springboot.repository.StudentRepository;
import com.skalashynski.spring.springboot.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

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
        return studentRepository.findAll();
    }

    @Override
    public boolean delete(long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Student update(long id, Student student) {
        Optional<Student> studentDao = studentRepository.findById(id);
        if (studentDao.isEmpty()) {
            throw new ApiException("Can't find student with id: " + id);
        }
        Student s = studentDao.get();
        s.setBirthday(student.getBirthday());
        s.setFirstName(student.getFirstName());
        s.setLastName(student.getLastName());
        //s.setCreatedAt(student.getCreatedAt());
        return studentRepository.save(s);
    }

    @Override
    public List<Student> findBetweenBirthdays(Date from, Date to) {
        return studentRepository.findBetweenBirthdays(from,to);
    }
}

package com.skalashynski.spring.springboot.service.impl;

import com.skalashynski.spring.springboot.bean.Student;
import com.skalashynski.spring.springboot.exception.StudentException;
import com.skalashynski.spring.springboot.repository.Impl.FakeStudentRepositoryImpl;
import com.skalashynski.spring.springboot.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private final FakeStudentRepositoryImpl studentRepository;

    public StudentServiceImpl(@Qualifier("fakeStudentRepository")FakeStudentRepositoryImpl studentRepository) {
        this.studentRepository = studentRepository;
    }

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
    public void delete(long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Student update(long id, Student student) {
        Optional<Student> studentDao = studentRepository.findById(id);
        if (studentDao.isEmpty()) {
            throw new StudentException("Can't find student with id: " + id);
        }
        Student s = studentDao.get();
        s.setBirthday(student.getBirthday());
        s.setFirstName(student.getFirstName());
        s.setLastName(student.getLastName());
        s.setCreatedAt(student.getCreatedAt());
        return studentRepository.save(s);
    }
}

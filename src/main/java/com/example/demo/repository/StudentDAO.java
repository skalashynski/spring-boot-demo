package com.example.demo.repository;

import com.example.demo.bean.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDAO extends CrudRepository<Student, Long> {
    List<Student> findByFirstName(String name);
}

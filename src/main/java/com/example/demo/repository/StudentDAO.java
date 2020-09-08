package com.example.demo.repository;

import com.example.demo.bean.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StudentDAO extends CrudRepository<Student, Long> {
    List<Student> findByFirstName(String name);

    @Query(value = "SELECT * FROM STUDENT s WHERE s.birthday BETWEEN :from AND :to", nativeQuery = true)
    List<Student> findBetweenBirthdays(Date from, Date to);
}

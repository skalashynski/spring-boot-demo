package com.skalashynski.spring.springboot.repository;

import com.skalashynski.spring.springboot.bean.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StudentDAO extends CrudRepository<Student, Long> {
    List<Student> findByFirstName(String firstName);

    List<Student> findByLastName(String lastName);

    @Query(value = "SELECT * FROM STUDENT s WHERE s.birthday BETWEEN :from AND :to", nativeQuery = true)
    List<Student> findBetweenBirthdays(Date from, Date to);
}

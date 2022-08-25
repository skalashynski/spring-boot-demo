package com.skalashynski.spring.springboot.repository;

import com.skalashynski.spring.springboot.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByFirstName(String firstName);

    List<Student> findByLastName(String lastName);

    @Query(value = "select * from students where birthday between :from AND :to", nativeQuery = true)
    List<Student> findBetweenBirthdays(@Param("from") LocalDate from, @Param("to")LocalDate to);
}

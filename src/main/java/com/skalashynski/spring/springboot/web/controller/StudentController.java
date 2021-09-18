package com.skalashynski.spring.springboot.web.controller;

import com.skalashynski.spring.springboot.entity.Student;
import com.skalashynski.spring.springboot.exception.ApiException;
import com.skalashynski.spring.springboot.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("student")
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Student save(@RequestBody Student student) {
        log.debug("Saving student: {}", student);
        return studentService.save(student);
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody
    Student getById(@PathVariable("id") Long id) {
        return studentService.getById(id)
            .orElseThrow(ApiException::new);
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    Student update(@PathVariable("id") Long id, @RequestBody Student student) {
        log.debug("Updating student: {}", student);
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }

    @GetMapping
    public @ResponseBody
    List<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/search")
    public List<Student> getByName(@RequestParam Map<String, String> allParams) {
        return studentService.findByFirstName(allParams.get("name"));
    }

    @GetMapping("/search/by/birthdays")
    public List<Student>getByBirthdaysDates(@RequestParam Map<String, String> allParams){
        return studentService.findBetweenBirthdays(java.sql.Date.valueOf(allParams.get("from")),
            java.sql.Date.valueOf(allParams.get("to")));
    }
}

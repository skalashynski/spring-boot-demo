package com.skalashynski.spring.springboot.controller;

import com.skalashynski.spring.springboot.bean.Student;
import com.skalashynski.spring.springboot.exception.StudentRequestException;
import com.skalashynski.spring.springboot.service.impl.StudentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/student")
@AllArgsConstructor
public class StudentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Student save(@RequestBody Student student) {
        LOGGER.debug("Saving student: {}", student);
        return studentService.save(student);
    }

    @GetMapping("/{id}")
    public @ResponseBody Student getById(@PathVariable("id") Integer id) {
        return studentService.getById(id)
                .orElseThrow(StudentRequestException::new);
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody Student update(@PathVariable("id") Long id, @RequestBody Student student) {
        LOGGER.debug("Updating student: {}", student);
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }

    @GetMapping
    public @ResponseBody List<Student> getAll() {
         throw new StudentRequestException("Oops, can't get all students");
        //return studentService.getAll();
    }

    @GetMapping("/search")
    public List<Student> getByName(@RequestParam Map<String,String> allParams) {
        return studentService.findByFirstName(allParams.get("name"));
    }
}

package com.skalashynski.spring.springboot.web.controller;

import com.skalashynski.spring.springboot.entity.Student;
import com.skalashynski.spring.springboot.exception.ApiException;
import com.skalashynski.spring.springboot.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("student")
@Slf4j
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student save(@RequestBody Student student) {
        log.debug("Saving student: {}", student);
        return studentService.save(student);
    }

    @GetMapping(value = "/{id}")
    public Student getById(@PathVariable("id") Long id) {
        return studentService.getById(id)
                .orElseThrow(ApiException::new);
    }

    @PutMapping(value = "/{id}")
    public Student update(@PathVariable("id") Long id, @RequestBody Student student) {
        log.debug("Updating student: {}", student);
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean isExist = studentService.delete(id);
        if (isExist) {
            return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public @ResponseBody List<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/search")
    public List<Student> getByName(@RequestParam Map<String, String> allParams) {
        if (allParams.containsKey("first_name")) {
            return studentService.findByFirstName(allParams.get("first_name"));
        }
        if (allParams.containsKey("last_name")) {
            return studentService.findByLastName(allParams.get("last_name"));
        }
        throw new ApiException("Request parameters are not set!");
    }

    @GetMapping("/search/birthdays")
    public ResponseEntity<?> getByBirthdaysDates(
            @RequestParam(name = "from") String from,
            @RequestParam(name = "to") String to
    ) {
        return new ResponseEntity<>(studentService.findBetweenBirthdays(from, to), HttpStatus.OK);
    }
}

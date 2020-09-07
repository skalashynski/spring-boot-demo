package com.example.demo.controller;

import com.example.demo.bean.Student;
import com.example.demo.exception.StudentException;
import com.example.demo.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);
    @Autowired
    private StudentService studentService;

    @PostMapping
    public Student save(@RequestBody Student student) {
        return studentService.save(student);
    }

    @GetMapping("/{id}")
    public @ResponseBody Student getById(@PathVariable("id") Integer id) {
        return studentService.getById(id)
                .orElseThrow(StudentException::new);
    }
    @PutMapping("/{id}")
    public @ResponseBody Student update(@PathVariable("id") Long id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }

    @GetMapping
    public @ResponseBody List<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/search")
    public List<Student> getByName(@RequestParam Map<String,String> allParams) {
        return studentService.findByFirstName(allParams.get("name"));
    }
}

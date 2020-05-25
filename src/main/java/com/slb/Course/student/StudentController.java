package com.slb.Course.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("students")
public class StudentController {

    @Autowired
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }

    @PostMapping
    public void addNewStudent(@RequestBody @Validated Student student) {
        studentService.addNewStudent(student);
    }

    @GetMapping(path="{studentId}/courses")
    public List<StudentCourse> getAllStudentCourses(@PathVariable UUID studentId){
        return studentService.getAllStudentCourses(studentId);
    }
}

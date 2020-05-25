package com.slb.Course.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    @Autowired
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    List<Student> getAllStudents(){
        return studentRepository.selectAllStudents();
    }

    public void addNewStudent(Student student) {
        addNewStudent(null, student);
    }
        public void addNewStudent(UUID studentId, Student student) {
        UUID newStudentId = Optional.ofNullable(studentId).orElse(UUID.randomUUID());

        studentRepository.insertStudent(newStudentId, student);
    }

    public List<StudentCourse> getAllStudentCourses(UUID studentId) {
        return studentRepository.selectAllCoursesForStudent(studentId);
    }
}

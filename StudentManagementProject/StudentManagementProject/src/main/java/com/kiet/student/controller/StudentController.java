package com.kiet.student.controller;

import com.kiet.student.model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final List<Student> students = new ArrayList<>();

    public StudentController() {
        students.add(new Student(101, "Rahul Sharma",
                "rahul@kiet.edu", "CSE", 2, 8.65));
        students.add(new Student(102, "Priya Singh",
                "priya@kiet.edu", "CSE", 2, 9.20));
        students.add(new Student(103, "Aman Kumar",
                "aman@kiet.edu", "ECE", 3, 8.10));
        students.add(new Student(104, "Neha Gupta",
                "neha@kiet.edu", "CSE", 3, 9.10));
        students.add(new Student(105, "Rohit Verma",
                "rohit@kiet.edu", "IT", 2, 7.90));
    }

    // GET /students
    @GetMapping
    public List<Student> getAllStudents() {
        return students;
    }

    // GET /students/{studentId}
    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable int studentId) {
        for (Student student : students) {
            if (student.getStudentId() == studentId) {
                return ResponseEntity.ok(student);
            }
        }

        return ResponseEntity.status(404)
                .body(Map.of("message", "Student not found"));
    }

    // POST /students
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        students.add(student);
        return student;
    }

    // PUT /students/{studentId}
    @PutMapping("/{studentId}")
    public ResponseEntity<?> updateStudent(
            @PathVariable int studentId,
            @RequestBody Student updatedStudent) {

        for (Student student : students) {
            if (student.getStudentId() == studentId) {
                student.setName(updatedStudent.getName());
                student.setEmail(updatedStudent.getEmail());
                student.setBranch(updatedStudent.getBranch());
                student.setYear(updatedStudent.getYear());
                student.setCgpa(updatedStudent.getCgpa());
                return ResponseEntity.ok(student);
            }
        }

        return ResponseEntity.status(404)
                .body(Map.of("message", "Student not found"));
    }

    // DELETE /students/{studentId}
    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable int studentId) {
        boolean removed = students.removeIf(
                student -> student.getStudentId() == studentId);

        if (removed) {
            return ResponseEntity.ok(
                    Map.of("message", "Student deleted successfully"));
        }

        return ResponseEntity.status(404)
                .body(Map.of("message", "Student not found"));
    }

    // GET /students/search?branch=CSE&year=2
    @GetMapping("/search")
    public List<Student> searchStudents(
            @RequestParam String branch,
            @RequestParam int year) {

        List<Student> result = new ArrayList<>();

        for (Student student : students) {
            if (student.getBranch().equalsIgnoreCase(branch)
                    && student.getYear() == year) {
                result.add(student);
            }
        }

        return result;
    }

    // GET /students/topper?cgpa=9.0
    @GetMapping("/topper")
    public List<Student> getToppers(@RequestParam double cgpa) {
        List<Student> result = new ArrayList<>();

        for (Student student : students) {
            if (student.getCgpa() >= cgpa) {
                result.add(student);
            }
        }

        return result;
    }
}

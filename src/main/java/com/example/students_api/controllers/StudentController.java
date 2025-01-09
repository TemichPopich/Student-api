package com.example.students_api.controllers;

import com.example.students_api.dto.StudentDto;
import com.example.students_api.models.Student;
import com.example.students_api.services.StudentService;
import com.example.students_api.utils.Sex;
import com.example.students_api.utils.Status;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@Tag(name = "Main REST controller")
public class StudentController {
    private final StudentService studentService;

    @GetMapping("students")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(studentService.getAllStudents(), HttpStatus.OK);
    }

    @GetMapping("students/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return new ResponseEntity<>(studentService.getStudentById(id), HttpStatus.OK);
    }

    @GetMapping("filter")
    public ResponseEntity<?> getFiltered(@RequestParam(required = false) String firstname,
                                         @RequestParam(required = false) String surname,
                                         @RequestParam(required = false) String patronymic,
                                         @RequestParam(required = false) String group_name,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
                                         @RequestParam(required = false) Sex sex,
                                         @RequestParam(required = false) Status status,
                                         @RequestParam(required = false) Integer course,
                                         @RequestParam(required = false) Integer birthYear) {

        var student = StudentDto.builder()
                .firstname(firstname)
                .surname(surname)
                .patronymic(patronymic)
                .group(group_name)
                .birthDate(birthDate)
                .sex(sex)
                .status(status)
                .course(course)
                .birthYear(birthYear)
                .build();
        return new ResponseEntity<>(studentService.getStudentsByExample(student), HttpStatus.OK);
    }

    @GetMapping("date")
    public ResponseEntity<?> getFromTimeInterval(@RequestParam
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                 LocalDate from,
                                                 @RequestParam
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                 LocalDate to) {
        return new ResponseEntity<>(studentService.getStudentsByTimeInterval(from, to), HttpStatus.OK);
    }

    @GetMapping("groups/{group_name}")
    public ResponseEntity<?> getStudentsByGroupName(@PathVariable String group_name) {
        return new ResponseEntity<>(studentService.getStudentsByGroup(group_name), HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<?> post(@RequestBody StudentDto student) {
        return new ResponseEntity<>(studentService.addStudent(student), HttpStatus.CREATED);
    }

    @PutMapping("update")
    public ResponseEntity<?> put(Student student) {
        return new ResponseEntity<>(studentService.putStudent(student), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}



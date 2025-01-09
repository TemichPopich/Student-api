package com.example.students_api.controllers;

import com.example.students_api.dto.StudentDto;
import com.example.students_api.models.Student;
import com.example.students_api.services.StudentService;
import com.example.students_api.utils.Sex;
import com.example.students_api.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("students")
    public String getAll() {
        return studentService.getAllStudents();
    }

    @GetMapping("students/{id}")
    public String getById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("filter")
    public String getFiltered(@RequestParam(required = false) String firstname,
                              @RequestParam(required = false) String surname,
                              @RequestParam(required = false) String patronymic,
                              @RequestParam(required = false) String group_name,
                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
                              @RequestParam(required = false) Sex sex,
                              @RequestParam(required = false) Status status,
                              @RequestParam(required = false) Integer course) {

        var student = StudentDto.builder()
                .firstname(firstname)
                .surname(surname)
                .patronymic(patronymic)
                .group(group_name)
                .birthDate(birthDate)
                .sex(sex)
                .status(status)
                .course(course)
                .build();
        return studentService.getStudentsByExample(student);
    }

    @GetMapping("date")
    public String getFromTimeInterval(@RequestParam
                                      @DateTimeFormat(pattern = "yyyy-MM-dd")
                                      LocalDate from,
                                      @RequestParam
                                      @DateTimeFormat(pattern = "yyyy-MM-dd")
                                      LocalDate to) {
        return studentService.getStudentsByTimeInterval(from, to);
    }

    @GetMapping("groups/{group_name}")
    public String getStudentsByGroupName(@PathVariable String group_name) {
        if (group_name == null) {
            return getAll();
        }
        return studentService.getStudentsByGroup(group_name);
    }

    @PostMapping("create")
    public String post(@RequestBody StudentDto student) {
        return studentService.addStudent(student);
    }

    @PutMapping("update")
    public String put(Student student) {
        return studentService.putStudent(student);
    }

    @DeleteMapping("delete")
    public void delete(@RequestParam Long id) {
        studentService.deleteStudentById(id);
    }
}

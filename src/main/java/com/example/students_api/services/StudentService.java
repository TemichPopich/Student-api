package com.example.students_api.services;

import com.example.students_api.dto.StudentDto;
import com.example.students_api.exceptions.NotFoundException;
import com.example.students_api.models.Group;
import com.example.students_api.models.Student;
import com.example.students_api.repos.GroupRepo;
import com.example.students_api.repos.StudentRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final ObjectMapper mapper;
    private final StudentRepo studentRepo;
    private final GroupRepo groupRepo;

    public String addStudent(StudentDto student) {
        try {
            return mapper.writeValueAsString(getDtoFromStudent(studentRepo.save(getStudentFromDto(student))));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String addGroup(Group group) {
        try {
            return mapper.writeValueAsString(groupRepo.save(group));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAllStudents() {
        var students = getSortedStudents(studentRepo.findAll());
        try {
            return mapper.writeValueAsString(students);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStudentsByExample(StudentDto student) {
        Example<Student> example = Example.of(getStudentFromDto(student));
        List<Student> students = studentRepo.findAll(example);
        List<StudentDto> studentsDto;
        if (student.getBirthYear() != null) {
            students = getStudentsByYear(students, student.getBirthYear());
        }
        if (student.getCourse() != null) {
            studentsDto = getStudentsByCourse(students, student.getCourse());
        } else {
            studentsDto = getSortedStudents(students);
        }
        try {
            return mapper.writeValueAsString(studentsDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStudentById(Long id) {
        try {
            return mapper.writeValueAsString(getDtoFromStudent(studentRepo.findById(id).orElseThrow(
                    () -> new NotFoundException("Student with given id doesn't exist")
            )));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStudentsByGroup(String group_name) {
        List<Student> students = studentRepo.findByGroupName(group_name).orElseThrow(
                () -> new NotFoundException("Group doesn't exist")
        );
        try {
            return mapper.writeValueAsString(getSortedStudents(students));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStudentsByTimeInterval(LocalDate from, LocalDate to) {
        try {
            return mapper.writeValueAsString(getSortedStudents(studentRepo.findByBirthDateBetween(from, to).orElseThrow(
                    () -> new NotFoundException("Students doesn't exist")
            )));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String putStudent(Student student) {
        if (!studentRepo.existsById(student.getId())) {
            throw new NotFoundException("Student with given id doesn't exist");
        }
        try {
            return mapper.writeValueAsString(getDtoFromStudent(studentRepo.save(student)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteStudentById(Long id) {
        studentRepo.deleteById(id);
    }

    private List<Student> getStudentsByYear(List<Student> students, Integer birthYear) {
        return students.stream().filter(student -> student.getBirthDate().getYear() == birthYear).toList();
    }

    private List<StudentDto> getStudentsByCourse(List<Student> students, int course) {
        var now = LocalDate.now();
        var year = now.getYear() - course + (now.getMonth().getValue() >= 8 ? 1 : 0);
        students = students.stream().filter(student -> student.getGroup().getRecruitmentYear().getValue() == year).toList();
        return getSortedStudents(students);
    }

    private List<StudentDto> getSortedStudents(List<Student> students) {
        return students.stream().sorted(Comparator
                        .comparing(Student::getFirstname)
                        .thenComparing(Student::getSurname)
                        .thenComparing(Student::getPatronymic))
                .map(this::getDtoFromStudent).toList();
    }

    private Student getStudentFromDto(StudentDto dto) {
        var builder = Student.builder();
        if (dto.getGroup() != null) {
            String name = dto.getGroup();
            Group group = groupRepo.findByName(name.toLowerCase()).orElseThrow(
                    () -> new NotFoundException("Group with given id doesn't exist")
            );
            builder.group(group);
        }
        return builder
                .firstname(dto.getFirstname())
                .surname(dto.getSurname())
                .patronymic(dto.getPatronymic())
                .birthDate(dto.getBirthDate())
                .sex(dto.getSex())
                .status(dto.getStatus())
                .build();
    }

    private StudentDto getDtoFromStudent(Student student) {
        var builder = StudentDto.builder();
        if (student.getGroup() != null) {
            builder.group(student.getGroup().getName().toLowerCase());
            var now = LocalDate.now();
            var course = now.getYear() - student.getGroup().getRecruitmentYear().getValue() +
                    (now.getMonth().getValue() >= 8 ? 1 : 0);
            builder.course(course);
        }
        return builder
                .firstname(student.getFirstname().toLowerCase())
                .surname(student.getSurname().toLowerCase())
                .patronymic(student.getPatronymic().toLowerCase())
                .birthDate(student.getBirthDate())
                .sex(student.getSex())
                .status(student.getStatus())
                .build();
    }
}

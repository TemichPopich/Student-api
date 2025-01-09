package com.example.students_api.controllers;

import com.example.students_api.dto.StudentDto;
import com.example.students_api.models.Student;
import com.example.students_api.services.StudentService;
import com.example.students_api.utils.Sex;
import com.example.students_api.utils.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Nested
@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc
public class StudentControllerTest {
    @MockBean
    private StudentService studentService;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    private final StudentDto firstStudentDto = StudentDto.builder()
            .firstname("Artem")
            .surname("Popov")
            .patronymic("Pavlovich")
            .birthDate(LocalDate.of(2004, Month.FEBRUARY, 8))
            .sex(Sex.MALE)
            .status(Status.STUDYING)
            .build();

    private final StudentDto secondStudentDto = StudentDto.builder()
            .firstname("Pavel")
            .surname("Popov")
            .patronymic("Petrovich")
            .birthDate(LocalDate.of(2002, Month.FEBRUARY, 6))
            .sex(Sex.MALE)
            .status(Status.STUDYING)
            .build();

    private final Student student = Student.builder()
            .id(1L)
            .firstname("Pavel")
            .surname("Popov")
            .patronymic("Petrovich")
            .birthDate(LocalDate.of(2002, Month.FEBRUARY, 6))
            .sex(Sex.MALE)
            .status(Status.STUDYING)
            .build();

    @Test
    @DisplayName("Check correctness of post request")
    public void testPostRequest() throws Exception {
        String requestBody = objectMapper.writeValueAsString(firstStudentDto);

        mockMvc.perform(post("/api/create")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Check correctness of get request by id")
    public void testGetRequest() throws Exception {
        Long id = 1L;

        when(studentService.getStudentById(id)).thenReturn(objectMapper.writeValueAsString(firstStudentDto));

        mockMvc.perform(get("/api/students/1"))
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Check correctness of get request with filters")
    public void testGetRequestWithExampleParam() throws Exception {
        List<StudentDto> students = List.of(firstStudentDto, secondStudentDto);

        StudentDto example = StudentDto.builder()
                .surname("Popov")
                .sex(Sex.MALE)
                .status(Status.STUDYING)
                .build();

        when(studentService.getStudentsByExample(example)).thenReturn(objectMapper.writeValueAsString(students));

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Check correctness of put request")
    public void testPutRequest() throws Exception {
        String requestBody = objectMapper.writeValueAsString(student);

        when(studentService.putStudent(student)).thenReturn(objectMapper.writeValueAsString(secondStudentDto));

        mockMvc.perform(put("/api/update")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Check correctness of delete request")
    public void testDeleteRequest() throws Exception {
        Long id = 1L;

        doNothing().when(studentService).deleteStudentById(id);

        mockMvc.perform(delete("/api/delete/1"))
                .andExpect(status().isNoContent());
    }
}

package com.example.students_api.repos;

import com.example.students_api.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long>, QueryByExampleExecutor<Student> {
    Optional<List<Student>> findByBirthDateBetween(LocalDate from, LocalDate to);
    Optional<List<Student>> findByGroupName(String group_name);
}

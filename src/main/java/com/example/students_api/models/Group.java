package com.example.students_api.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Year;

@Data
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String department;
    private Year recruitmentYear;
}

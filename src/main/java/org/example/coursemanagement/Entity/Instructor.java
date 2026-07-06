package org.example.coursemanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "instructors")
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String specialization;

    @OneToMany(mappedBy = "instructor")
    private List<Course> courses;

    // getters & setters
}

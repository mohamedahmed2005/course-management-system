package org.example.coursemanagement.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "instructors")
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String specialization;

    @OneToMany(mappedBy = "instructor")
    private List<Course> courses;

    // getters & setters
}

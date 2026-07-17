package org.example.coursemanagement.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @JsonIgnore
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments;
}
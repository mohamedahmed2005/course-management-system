package org.example.coursemanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "enrollments")
public class Enrollment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @Setter
    @Getter
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @Setter
    @Getter
    private Course course;

    @Setter
    @Getter
    private String status;
    // مثلا: ENROLLED, COMPLETED, DROPPED

    // getters & setters
}
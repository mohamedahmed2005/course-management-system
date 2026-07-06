package org.example.coursemanagement.Service;

import org.example.coursemanagement.Entity.Enrollment;

import java.util.List;

public interface EnrollmentService {

    Enrollment enrollStudent(Long studentId, Long courseId);

    List<Enrollment> getAllEnrollments();

    List<Enrollment> getEnrollmentsByStudentId(Long studentId);

    List<Enrollment> getEnrollmentsByCourseId(Long courseId);

    Enrollment getEnrollmentById(Long id);

    void deleteEnrollment(Long id);
}
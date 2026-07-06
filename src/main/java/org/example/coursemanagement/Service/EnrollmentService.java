package org.example.coursemanagement.Service;

import org.example.coursemanagement.DTO.EnrollmentDTO;

import java.util.List;

public interface EnrollmentService {

    EnrollmentDTO enrollStudent(Long studentId, Long courseId);

    List<EnrollmentDTO> getAllEnrollments();

    List<EnrollmentDTO> getEnrollmentsByStudentId(Long studentId);

    List<EnrollmentDTO> getEnrollmentsByCourseId(Long courseId);

    EnrollmentDTO getEnrollmentById(Long id);

    void deleteEnrollment(Long id);
}
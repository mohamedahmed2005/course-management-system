package org.example.coursemanagement.Service;

import org.example.coursemanagement.DTO.EnrollmentDTO;

import java.util.List;

public interface EnrollmentService {

    EnrollmentDTO enrollStudent(Long studentId, Long courseId, EnrollmentDTO enrollmentDTO);

    EnrollmentDTO getEnrollmentById(Long id);

    List<EnrollmentDTO> getEnrollmentsByStudentId(Long studentId);

    void deleteEnrollment(Long id);
}
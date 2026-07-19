package org.example.coursemanagement.Service;

import org.example.coursemanagement.DTO.EnrollmentDTO;

import java.util.List;

public interface EnrollmentService {

    EnrollmentDTO enrollStudent(EnrollmentDTO enrollmentDTO);

    EnrollmentDTO getEnrollmentById(Long id);

    List<EnrollmentDTO> getEnrollmentsByStudentId(Long studentId);

    void deleteEnrollment(Long id);
}
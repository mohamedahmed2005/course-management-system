package org.example.coursemanagement.Service;

import org.example.coursemanagement.DTO.EnrollmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EnrollmentService {

    EnrollmentDTO enrollStudent(Long studentId, Long courseId);
    Page<EnrollmentDTO> getAllEnrollments(Pageable pageable);

    List<EnrollmentDTO> getEnrollmentsByStudentId(Long studentId);

    List<EnrollmentDTO> getEnrollmentsByCourseId(Long courseId);

    EnrollmentDTO getEnrollmentById(Long id);

    void deleteEnrollment(Long id);
}
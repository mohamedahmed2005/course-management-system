package org.example.coursemanagement.Service;

import org.example.coursemanagement.DTO.EnrollmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnrollmentService {

    /** Get all enrollments (paginated) */
    Page<EnrollmentDTO> getAllEnrollments(Pageable pageable);

    /** Get a single enrollment by ID */
    EnrollmentDTO getEnrollmentById(Long id);

    /** Get all enrollments for a specific student (paginated) */
    Page<EnrollmentDTO> getEnrollmentsByStudentId(Long studentId, Pageable pageable);

    /** Get all enrollments for a specific course (paginated) */
    Page<EnrollmentDTO> getEnrollmentsByCourseId(Long courseId, Pageable pageable);

    /** Update only the status of an enrollment */
    EnrollmentDTO updateEnrollmentStatus(Long id, String status);

    /** Delete an enrollment */
    void deleteEnrollment(Long id);
}

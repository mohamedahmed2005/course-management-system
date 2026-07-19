package org.example.coursemanagement.Repository;

import org.example.coursemanagement.Entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    // Paginated
    Page<Enrollment> findAll(Pageable pageable);

    Page<Enrollment> findByStudentId(Long studentId, Pageable pageable);

    Page<Enrollment> findByCourseId(Long courseId, Pageable pageable);

    // Non-paginated (kept for public module use)
    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);
}
package org.example.coursemanagement.Repository;

import org.example.coursemanagement.Entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // Returns only non-deleted courses as a proper Page (no LazyStreamable issue)
    Page<Course> findAllByDeletedFalse(Pageable pageable);
}

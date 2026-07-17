package org.example.coursemanagement.Repository;

import org.example.coursemanagement.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course , Long> {
}

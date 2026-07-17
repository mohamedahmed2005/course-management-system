package org.example.coursemanagement.Repository;

import org.example.coursemanagement.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}

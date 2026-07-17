package org.example.coursemanagement.Service;

import org.example.coursemanagement.DTO.CourseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {

    Page<CourseDTO> getAllCourses(Pageable pageable);

    CourseDTO getCourseById(Long id);
}

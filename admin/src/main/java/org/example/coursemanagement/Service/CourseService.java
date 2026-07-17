package org.example.coursemanagement.Service;

import org.example.coursemanagement.DTO.CourseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    CourseDTO addCourse(CourseDTO courseDTO);

    Page<CourseDTO> getAllCourses(Pageable pageable);

    CourseDTO getCourseById(Long id);

    CourseDTO updateCourse(Long id, CourseDTO courseDTO);

    void deleteCourse(Long id); // soft delete
}

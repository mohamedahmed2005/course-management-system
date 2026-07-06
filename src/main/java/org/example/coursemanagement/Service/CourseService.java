package org.example.coursemanagement.Service;

import org.example.coursemanagement.DTO.CourseDTO;

import java.util.List;

public interface CourseService {

    CourseDTO addCourse(CourseDTO courseDTO);

    List<CourseDTO> getAllCourses();

    CourseDTO getCourseById(Long id);

    CourseDTO updateCourse(Long id, CourseDTO courseDTO);

    void deleteCourse(Long id); // soft delete
}

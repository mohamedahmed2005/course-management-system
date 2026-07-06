package org.example.coursemanagement.Service;

import org.example.coursemanagement.Entity.Course;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CourseService {
    Course addCourse(Course course);
    List<Course> getAllCourses();
    Course getCourseById(Long id);
    Course updateCourse(Long id, Course course);

    void deleteCourse(Long id); // soft delete
}

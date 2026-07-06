package org.example.coursemanagement.ServiceImplementation;

import org.example.coursemanagement.Entity.Course;
import org.example.coursemanagement.Repository.CourseRepository;
import org.example.coursemanagement.Service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImplementation implements CourseService {
    private final CourseRepository courseRepository;

    public CourseServiceImplementation(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    @Override
    public Course updateCourse(Long id, Course course) {
        Course existing = getCourseById(id);
        existing.setTitle(course.getTitle());
        existing.setDescription(course.getDescription());
        return courseRepository.save(existing);
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        course.setDeleted(true);
        courseRepository.save(course);
    }
}

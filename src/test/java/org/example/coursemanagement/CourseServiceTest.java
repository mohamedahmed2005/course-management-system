package org.example.coursemanagement;

import org.example.coursemanagement.DTO.CourseDTO;
import org.example.coursemanagement.Entity.Course;
import org.example.coursemanagement.Repository.CourseRepository;
import org.example.coursemanagement.Repository.InstructorRepository;
import org.example.coursemanagement.ServiceImplementation.CourseServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImplementation courseService;


    @Test
    void addCourse_shouldSaveCourse() {

        CourseDTO dto = new CourseDTO(
                null,
                "Spring Boot",
                "Backend course",
                null
        );

        Course course = new Course();
        course.setTitle("Spring Boot");
        course.setDescription("Backend course");


        when(courseRepository.save(any(Course.class)))
                .thenReturn(course);


        CourseDTO result = courseService.addCourse(dto);


        assertEquals("Spring Boot", result.getTitle());

        verify(courseRepository, times(1))
                .save(any(Course.class));
    }


    @Test
    void getCourseById_shouldReturnCourse() {

        Course course = new Course();
        course.setTitle("Java");


        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));


        CourseDTO result = courseService.getCourseById(1L);


        assertEquals("Java", result.getTitle());

        verify(courseRepository)
                .findById(1L);
    }


    @Test
    void getCourseById_shouldThrowExceptionWhenNotFound() {


        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(RuntimeException.class,
                () -> courseService.getCourseById(1L));


        verify(courseRepository)
                .findById(1L);
    }


    @Test
    void updateCourse_shouldUpdateCourse() {

        Course existing = new Course();
        existing.setTitle("Old");


        CourseDTO dto = new CourseDTO(
                1L,
                "New Title",
                "New Description",
                null
        );


        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(existing));


        when(courseRepository.save(any(Course.class)))
                .thenReturn(existing);


        CourseDTO result =
                courseService.updateCourse(1L, dto);


        assertEquals("New Title", result.getTitle());

        verify(courseRepository)
                .save(existing);
    }


    @Test
    void deleteCourse_shouldSoftDeleteCourse() {

        Course course = new Course();
        course.setDeleted(false);


        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));


        courseService.deleteCourse(1L);


        assertTrue(course.isDeleted());


        verify(courseRepository)
                .save(course);
    }
}
package org.example.coursemanagement;

import org.example.coursemanagement.DTO.EnrollmentDTO;
import org.example.coursemanagement.Entity.Course;
import org.example.coursemanagement.Entity.Enrollment;
import org.example.coursemanagement.Entity.Student;
import org.example.coursemanagement.Repository.CourseRepository;
import org.example.coursemanagement.Repository.EnrollmentRepository;
import org.example.coursemanagement.Repository.StudentRepository;
import org.example.coursemanagement.ServiceImplementation.EnrollmentServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentServiceImplementation enrollmentService;


    @Test
    void enrollStudent_shouldCreateEnrollment() {

        Student student = new Student();
        student.setId(1L);
        student.setName("Ahmed");


        Course course = new Course();
        course.setId(1L);
        course.setTitle("Spring Boot");


        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus("ENROLLED");


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 1L))
                .thenReturn(Optional.empty());

        when(enrollmentRepository.save(any(Enrollment.class)))
                .thenReturn(enrollment);


        EnrollmentDTO result =
                enrollmentService.enrollStudent(1L, 1L);


        assertEquals(1L, result.getStudentId());
        assertEquals(1L, result.getCourseId());
        assertEquals("ENROLLED", result.getStatus());


        verify(enrollmentRepository)
                .save(any(Enrollment.class));
    }


    @Test
    void enrollStudent_shouldThrowExceptionWhenStudentNotFound() {


        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(RuntimeException.class,
                () -> enrollmentService.enrollStudent(1L, 1L));


        verify(enrollmentRepository, never())
                .save(any());
    }


    @Test
    void enrollStudent_shouldThrowExceptionWhenCourseNotFound() {

        Student student = new Student();
        student.setId(1L);


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(RuntimeException.class,
                () -> enrollmentService.enrollStudent(1L, 1L));


        verify(enrollmentRepository, never())
                .save(any());
    }


    @Test
    void enrollStudent_shouldPreventDuplicateEnrollment() {

        Student student = new Student();
        student.setId(1L);

        Course course = new Course();
        course.setId(1L);


        Enrollment existing = new Enrollment();


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 1L))
                .thenReturn(Optional.of(existing));


        assertThrows(RuntimeException.class,
                () -> enrollmentService.enrollStudent(1L, 1L));


        verify(enrollmentRepository, never())
                .save(any());
    }


    @Test
    void getEnrollmentById_shouldReturnEnrollment() {

        Student student = new Student();
        student.setId(1L);

        Course course = new Course();
        course.setId(1L);


        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus("ENROLLED");


        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));


        EnrollmentDTO result =
                enrollmentService.getEnrollmentById(1L);


        assertEquals(1L, result.getStudentId());
        assertEquals(1L, result.getCourseId());


        verify(enrollmentRepository)
                .findById(1L);
    }


    @Test
    void deleteEnrollment_shouldDeleteEnrollment() {


        enrollmentService.deleteEnrollment(1L);


        verify(enrollmentRepository)
                .deleteById(1L);
    }
}
package org.example.coursemanagement;

import org.example.coursemanagement.DTO.EnrollmentDTO;
import org.example.coursemanagement.Entity.Course;
import org.example.coursemanagement.Entity.Enrollment;
import org.example.coursemanagement.Entity.Student;
import org.example.coursemanagement.Repository.CourseRepository;
import org.example.coursemanagement.Repository.EnrollmentRepository;
import org.example.coursemanagement.Repository.StudentRepository;
import org.example.coursemanagement.ServiceImplementation.EnrollmentServiceImplementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class EnrollmentServiceTest {


    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;


    @InjectMocks
    private EnrollmentServiceImplementation service;



    @BeforeEach
    void setup(){

        MockitoAnnotations.openMocks(this);

    }




    // ===========================
    // enrollStudent tests
    // ===========================


    @Test
    void enrollStudent_success(){


        Student student = new Student();
        student.setId(1L);


        Course course = new Course();
        course.setId(10L);
        course.setDeleted(false);



        Enrollment saved = new Enrollment();
        saved.setId(100L);
        saved.setStudent(student);
        saved.setCourse(course);
        saved.setStatus("ENROLLED");



        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));


        when(courseRepository.findById(10L))
                .thenReturn(Optional.of(course));


        when(enrollmentRepository
                .findByStudentIdAndCourseId(1L,10L))
                .thenReturn(Optional.empty());



        when(enrollmentRepository.save(any()))
                .thenReturn(saved);



        EnrollmentDTO result =
                service.enrollStudent(1L,10L);



        assertEquals(100L,result.getId());
        assertEquals(1L,result.getStudentId());
        assertEquals(10L,result.getCourseId());
        assertEquals("ENROLLED",result.getStatus());



        verify(enrollmentRepository)
                .save(any(Enrollment.class));

    }






    @Test
    void enrollStudent_studentNotFound(){


        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());



        assertThrows(
                RuntimeException.class,
                () -> service.enrollStudent(1L,10L)
        );


        verify(enrollmentRepository,never())
                .save(any());

    }







    @Test
    void enrollStudent_courseNotFound(){


        Student student = new Student();
        student.setId(1L);


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));


        when(courseRepository.findById(10L))
                .thenReturn(Optional.empty());



        assertThrows(
                RuntimeException.class,
                () -> service.enrollStudent(1L,10L)
        );

    }







    @Test
    void enrollStudent_courseDeleted(){


        Student student = new Student();
        student.setId(1L);


        Course course = new Course();
        course.setId(10L);
        course.setDeleted(true);



        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));


        when(courseRepository.findById(10L))
                .thenReturn(Optional.of(course));



        assertThrows(
                RuntimeException.class,
                () -> service.enrollStudent(1L,10L)
        );


        verify(enrollmentRepository,never())
                .save(any());

    }







    @Test
    void enrollStudent_alreadyEnrolled(){


        Student student = new Student();
        student.setId(1L);


        Course course = new Course();
        course.setId(10L);
        course.setDeleted(false);



        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));


        when(courseRepository.findById(10L))
                .thenReturn(Optional.of(course));



        when(enrollmentRepository
                .findByStudentIdAndCourseId(1L,10L))
                .thenReturn(Optional.of(new Enrollment()));



        assertThrows(
                RuntimeException.class,
                () -> service.enrollStudent(1L,10L)
        );


    }







    @Test
    void enrollStudent_invalidStudentId(){


        assertThrows(
                IllegalArgumentException.class,
                () -> service.enrollStudent(null,10L)
        );

    }






    // ===========================
    // getEnrollmentById
    // ===========================


    @Test
    void getEnrollmentById_success(){


        Student student = new Student();
        student.setId(1L);


        Course course = new Course();
        course.setId(10L);



        Enrollment enrollment = new Enrollment();

        enrollment.setId(5L);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus("ENROLLED");



        when(enrollmentRepository.findById(5L))
                .thenReturn(Optional.of(enrollment));



        EnrollmentDTO result =
                service.getEnrollmentById(5L);



        assertEquals(5L,result.getId());

    }






    @Test
    void getEnrollmentById_notFound(){


        when(enrollmentRepository.findById(5L))
                .thenReturn(Optional.empty());



        assertThrows(
                RuntimeException.class,
                () -> service.getEnrollmentById(5L)
        );


    }






    @Test
    void getEnrollmentById_invalidId(){


        assertThrows(
                IllegalArgumentException.class,
                () -> service.getEnrollmentById(null)
        );


    }






    // ===========================
    // deleteEnrollment
    // ===========================


    @Test
    void deleteEnrollment_success(){


        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);



        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));



        service.deleteEnrollment(1L);



        verify(enrollmentRepository)
                .delete(enrollment);

    }







    @Test
    void deleteEnrollment_notFound(){


        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.empty());



        assertThrows(
                RuntimeException.class,
                () -> service.deleteEnrollment(1L)
        );



        verify(enrollmentRepository,never())
                .delete(any());

    }





}
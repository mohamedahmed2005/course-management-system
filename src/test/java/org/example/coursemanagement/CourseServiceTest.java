package org.example.coursemanagement;


import org.example.coursemanagement.DTO.CourseDTO;
import org.example.coursemanagement.Entity.Course;
import org.example.coursemanagement.Entity.Instructor;
import org.example.coursemanagement.Repository.CourseRepository;
import org.example.coursemanagement.Repository.InstructorRepository;
import org.example.coursemanagement.ServiceImplementation.CourseServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class CourseServiceTest {


    @Mock
    private CourseRepository courseRepository;


    @Mock
    private InstructorRepository instructorRepository;


    @InjectMocks
    private CourseServiceImplementation service;



    @BeforeEach
    void setup(){

        MockitoAnnotations.openMocks(this);

    }



    @Test
    void addCourse_success(){


        CourseDTO dto =
                new CourseDTO(
                        null,
                        "Java",
                        "Spring course",
                        1L
                );


        Instructor instructor = new Instructor();
        instructor.setId(1L);



        Course saved = new Course();
        saved.setId(10L);
        saved.setTitle("Java");
        saved.setDescription("Spring course");
        saved.setInstructor(instructor);



        when(instructorRepository.findById(1L))
                .thenReturn(Optional.of(instructor));


        when(courseRepository.save(any(Course.class)))
                .thenReturn(saved);



        CourseDTO result =
                service.addCourse(dto);



        assertEquals(10L,result.getId());
        assertEquals("Java",result.getTitle());

        verify(courseRepository)
                .save(any(Course.class));

    }




    @Test
    void getCourseById_success(){


        Course course = new Course();

        course.setId(5L);
        course.setTitle("Database");


        when(courseRepository.findById(5L))
                .thenReturn(Optional.of(course));



        CourseDTO result =
                service.getCourseById(5L);



        assertEquals(5L,result.getId());
        assertEquals("Database",result.getTitle());


    }




    @Test
    void getCourseById_notFound(){


        when(courseRepository.findById(99L))
                .thenReturn(Optional.empty());



        assertThrows(
                RuntimeException.class,
                () -> service.getCourseById(99L)
        );


    }


    @Test
    void updateCourse_success(){


        Course existing = new Course();

        existing.setId(1L);
        existing.setTitle("Old");



        CourseDTO dto =
                new CourseDTO(
                        1L,
                        "New",
                        "Updated",
                        null
                );



        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(existing));


        when(courseRepository.save(any()))
                .thenReturn(existing);



        CourseDTO result =
                service.updateCourse(1L,dto);



        assertEquals(
                "New",
                result.getTitle()
        );


        verify(courseRepository)
                .save(existing);


    }




    @Test
    void deleteCourse_success(){


        Course course = new Course();

        course.setId(1L);
        course.setDeleted(false);



        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));



        service.deleteCourse(1L);



        assertTrue(course.isDeleted());


        verify(courseRepository)
                .save(course);


    }




    @Test
    void addCourse_nullDTO(){


        assertThrows(
                IllegalArgumentException.class,
                () -> service.addCourse(null)
        );


    }




    @Test
    void getCourse_invalidId(){


        assertThrows(
                IllegalArgumentException.class,
                () -> service.getCourseById(null)
        );


    }

}
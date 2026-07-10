package org.example.coursemanagement;

import org.example.coursemanagement.DTO.StudentDTO;
import org.example.coursemanagement.Entity.Student;
import org.example.coursemanagement.Repository.StudentRepository;
import org.example.coursemanagement.ServiceImplementation.StudentServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImplementation service;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addStudent_success(){
        StudentDTO dto =
                new StudentDTO(
                        null,
                        "Ahmed",
                        "ahmed@gmail.com"
                );
        Student saved = new Student();

        saved.setId(1L);
        saved.setName("Ahmed");
        saved.setEmail("ahmed@gmail.com");

        when(studentRepository.save(any(Student.class)))
                .thenReturn(saved);

        StudentDTO result =
                service.addStudent(dto);

        assertEquals(1L,result.getId());
        assertEquals("Ahmed",result.getName());
        assertEquals("ahmed@gmail.com",result.getEmail());

        verify(studentRepository)
                .save(any(Student.class));

    }

    @Test
    void addStudent_nullDTO(){
        assertThrows(
                IllegalArgumentException.class,
                () -> service.addStudent(null)
        );

        verify(studentRepository,never())
                .save(any());

    }

    @Test
    void getStudentById_success(){
        Student student = new Student();

        student.setId(5L);
        student.setName("Mohamed");
        student.setEmail("mohamed@gmail.com");

        when(studentRepository.findById(5L))
                .thenReturn(Optional.of(student));

        StudentDTO result =
                service.getStudentById(5L);

        assertEquals(5L,result.getId());
        assertEquals("Mohamed",result.getName());
        assertEquals("mohamed@gmail.com",result.getEmail());

    }

    @Test
    void getStudentById_notFound(){

        when(studentRepository.findById(5L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.getStudentById(5L)
        );

    }

    @Test
    void getStudentById_invalidId(){

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getStudentById(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getStudentById(-1L)
        );
    }

    @Test
    void updateStudent_success(){
        Student existing = new Student();

        existing.setId(1L);
        existing.setName("Old");
        existing.setEmail("old@gmail.com");

        StudentDTO dto =
                new StudentDTO(
                        1L,
                        "New",
                        "new@gmail.com"
                );

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(studentRepository.save(any(Student.class)))
                .thenReturn(existing);

        StudentDTO result =
                service.updateStudent(1L,dto);

        assertEquals(
                "New",
                result.getName()
        );

        assertEquals(
                "new@gmail.com",
                result.getEmail()
        );
        verify(studentRepository)
                .save(existing);

    }

    @Test
    void updateStudent_notFound(){
        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        StudentDTO dto =
                new StudentDTO(
                        1L,
                        "Ahmed",
                        "a@gmail.com"
                );
        assertThrows(
                RuntimeException.class,
                () -> service.updateStudent(1L,dto)
        );
        verify(studentRepository,never())
                .save(any());
    }

    @Test
    void updateStudent_nullDTO(){
        assertThrows(
                IllegalArgumentException.class,
                () -> service.updateStudent(1L,null)
        );
    }

    @Test
    void deleteStudent_success(){
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        service.deleteStudent(1L);

        verify(studentRepository)
                .delete(student);
    }

    @Test
    void deleteStudent_notFound(){
        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertThrows(
                RuntimeException.class,
                () -> service.deleteStudent(1L)
        );
        verify(studentRepository,never())
                .delete(any());
    }

    @Test
    void deleteStudent_invalidId(){
        assertThrows(
                IllegalArgumentException.class,
                () -> service.deleteStudent(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.deleteStudent(0L)
        );
    }
}
package org.example.coursemanagement;

import org.example.coursemanagement.DTO.StudentDTO;
import org.example.coursemanagement.Entity.Student;
import org.example.coursemanagement.Repository.StudentRepository;
import org.example.coursemanagement.ServiceImplementation.StudentServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImplementation studentService;


    @Test
    void addStudent_shouldSaveStudent() {

        StudentDTO dto = new StudentDTO(
                null,
                "Ahmed",
                "ahmed@gmail.com"
        );

        Student student = new Student();
        student.setName("Ahmed");
        student.setEmail("ahmed@gmail.com");


        when(studentRepository.save(any(Student.class)))
                .thenReturn(student);


        StudentDTO result = studentService.addStudent(dto);


        assertEquals("Ahmed", result.getName());
        assertEquals("ahmed@gmail.com", result.getEmail());

        verify(studentRepository, times(1))
                .save(any(Student.class));
    }


    @Test
    void getStudentById_shouldReturnStudent() {

        Student student = new Student();
        student.setName("Mohamed");
        student.setEmail("mohamed@gmail.com");


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));


        StudentDTO result =
                studentService.getStudentById(1L);


        assertEquals("Mohamed", result.getName());
        assertEquals("mohamed@gmail.com", result.getEmail());

        verify(studentRepository)
                .findById(1L);
    }


    @Test
    void getStudentById_shouldThrowExceptionWhenNotFound() {


        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(RuntimeException.class,
                () -> studentService.getStudentById(1L));


        verify(studentRepository)
                .findById(1L);
    }


    @Test
    void updateStudent_shouldUpdateStudent() {

        Student existing = new Student();
        existing.setName("Old Name");
        existing.setEmail("old@gmail.com");


        StudentDTO dto = new StudentDTO(
                1L,
                "New Name",
                "new@gmail.com"
        );


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(existing));


        when(studentRepository.save(any(Student.class)))
                .thenReturn(existing);


        StudentDTO result =
                studentService.updateStudent(1L, dto);


        assertEquals("New Name", result.getName());
        assertEquals("new@gmail.com", result.getEmail());


        verify(studentRepository)
                .save(existing);
    }


    @Test
    void deleteStudent_shouldDeleteStudent() {

        Student student = new Student();

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));


        studentService.deleteStudent(1L);


        verify(studentRepository)
                .delete(student);
    }


    @Test
    void deleteStudent_shouldThrowExceptionWhenNotFound() {


        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(RuntimeException.class,
                () -> studentService.deleteStudent(1L));


        verify(studentRepository, never())
                .delete(any(Student.class));
    }
}
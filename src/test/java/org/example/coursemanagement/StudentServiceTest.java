package org.example.coursemanagement;

import org.example.coursemanagement.DTO.StudentDTO;
import org.example.coursemanagement.Entity.Student;
import org.example.coursemanagement.Exception.InvalidInputException;
import org.example.coursemanagement.Exception.ResourceNotFoundException;
import org.example.coursemanagement.Mapper.StudentMapper;
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

    @Mock private StudentRepository studentRepository;
    @Mock private StudentMapper studentMapper;
    @InjectMocks private StudentServiceImplementation service;

    @BeforeEach
    void setup() { MockitoAnnotations.openMocks(this); }

    // ── helpers ───────────────────────────────────────────────────────────────

    private Student student(Long id, String name, String email) {
        Student s = new Student();
        s.setId(id); s.setName(name); s.setEmail(email);
        return s;
    }

    private StudentDTO dto(Long id, String name, String email) {
        return new StudentDTO(id, name, email);
    }

    // ── addStudent ─────────────────────────────────────────────────────────────

    @Test
    void addStudent_success() {
        StudentDTO dto   = dto(null, "Ahmed", "ahmed@gmail.com");
        Student    saved = student(1L, "Ahmed", "ahmed@gmail.com");
        StudentDTO result = dto(1L, "Ahmed", "ahmed@gmail.com");

        when(studentMapper.toEntity(dto)).thenReturn(saved);
        when(studentRepository.save(saved)).thenReturn(saved);
        when(studentMapper.toDTO(saved)).thenReturn(result);

        StudentDTO actual = service.addStudent(dto);

        assertEquals(1L,                actual.getId());
        assertEquals("Ahmed",           actual.getName());
        assertEquals("ahmed@gmail.com", actual.getEmail());
        verify(studentRepository).save(saved);
    }

    // ── getStudentById ─────────────────────────────────────────────────────────

    @Test
    void getStudentById_success() {
        Student    student = student(5L, "Mohamed", "mohamed@gmail.com");
        StudentDTO result  = dto(5L, "Mohamed", "mohamed@gmail.com");

        when(studentRepository.findById(5L)).thenReturn(Optional.of(student));
        when(studentMapper.toDTO(student)).thenReturn(result);

        StudentDTO actual = service.getStudentById(5L);

        assertEquals(5L,                  actual.getId());
        assertEquals("Mohamed",           actual.getName());
        assertEquals("mohamed@gmail.com", actual.getEmail());
    }

    @Test
    void getStudentById_notFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getStudentById(99L));
    }

    @Test
    void getStudentById_nullId() {
        assertThrows(InvalidInputException.class, () -> service.getStudentById(null));
    }

    @Test
    void getStudentById_negativeId() {
        assertThrows(InvalidInputException.class, () -> service.getStudentById(-1L));
    }

    // ── updateStudent ──────────────────────────────────────────────────────────

    @Test
    void updateStudent_success() {
        Student    existing = student(1L, "Old", "old@gmail.com");
        StudentDTO dto      = dto(1L, "New", "new@gmail.com");
        StudentDTO result   = dto(1L, "New", "new@gmail.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.save(existing)).thenReturn(existing);
        when(studentMapper.toDTO(existing)).thenReturn(result);

        StudentDTO actual = service.updateStudent(1L, dto);

        assertEquals("New",           actual.getName());
        assertEquals("new@gmail.com", actual.getEmail());
        verify(studentRepository).save(existing);
    }

    @Test
    void updateStudent_notFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.updateStudent(1L, dto(1L, "A", "a@b.com")));
        verify(studentRepository, never()).save(any());
    }

    @Test
    void updateStudent_invalidId() {
        assertThrows(InvalidInputException.class,
                () -> service.updateStudent(0L, dto(null, "A", "a@b.com")));
    }

    // ── deleteStudent ──────────────────────────────────────────────────────────

    @Test
    void deleteStudent_success() {
        Student s = student(1L, "Ahmed", "a@b.com");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(s));

        service.deleteStudent(1L);

        verify(studentRepository).delete(s);
    }

    @Test
    void deleteStudent_notFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.deleteStudent(1L));
        verify(studentRepository, never()).delete(any());
    }

    @Test
    void deleteStudent_invalidId() {
        assertThrows(InvalidInputException.class, () -> service.deleteStudent(null));
        assertThrows(InvalidInputException.class, () -> service.deleteStudent(0L));
    }
}
package org.example.coursemanagement;

import org.example.coursemanagement.DTO.InstructorDTO;
import org.example.coursemanagement.Entity.Instructor;
import org.example.coursemanagement.Exception.InvalidInputException;
import org.example.coursemanagement.Exception.ResourceNotFoundException;
import org.example.coursemanagement.Mapper.InstructorMapper;
import org.example.coursemanagement.Repository.InstructorRepository;
import org.example.coursemanagement.ServiceImplementation.InstructorServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructorServiceTest {

    @Mock private InstructorRepository instructorRepository;
    @Mock private InstructorMapper instructorMapper;
    @InjectMocks private InstructorServiceImplementation service;

    @BeforeEach
    void setup() { MockitoAnnotations.openMocks(this); }

    // ── helpers ───────────────────────────────────────────────────────────────

    private Instructor instructor(Long id, String name, String spec) {
        Instructor i = new Instructor();
        i.setId(id); i.setName(name); i.setSpecialization(spec);
        return i;
    }

    private InstructorDTO dto(Long id, String name, String spec) {
        return new InstructorDTO(id, name, spec);
    }

    // ── addInstructor ──────────────────────────────────────────────────────────

    @Test
    void addInstructor_success() {
        InstructorDTO dto    = dto(null, "Ahmed", "AI");
        Instructor    saved  = instructor(1L, "Ahmed", "AI");
        InstructorDTO result = dto(1L, "Ahmed", "AI");

        when(instructorMapper.toEntity(dto)).thenReturn(saved);
        when(instructorRepository.save(saved)).thenReturn(saved);
        when(instructorMapper.toDTO(saved)).thenReturn(result);

        InstructorDTO actual = service.addInstructor(dto);

        assertEquals(1L,     actual.getId());
        assertEquals("Ahmed", actual.getName());
        assertEquals("AI",    actual.getSpecialization());
        verify(instructorRepository).save(saved);
    }

    // ── getInstructorById ──────────────────────────────────────────────────────

    @Test
    void getInstructorById_success() {
        Instructor    entity = instructor(5L, "Mohamed", "Java");
        InstructorDTO result = dto(5L, "Mohamed", "Java");

        when(instructorRepository.findById(5L)).thenReturn(Optional.of(entity));
        when(instructorMapper.toDTO(entity)).thenReturn(result);

        InstructorDTO actual = service.getInstructorById(5L);

        assertEquals(5L,       actual.getId());
        assertEquals("Mohamed", actual.getName());
        assertEquals("Java",    actual.getSpecialization());
    }

    @Test
    void getInstructorById_notFound() {
        when(instructorRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getInstructorById(10L));
    }

    @Test
    void getInstructorById_nullId() {
        assertThrows(InvalidInputException.class, () -> service.getInstructorById(null));
    }

    @Test
    void getInstructorById_negativeId() {
        assertThrows(InvalidInputException.class, () -> service.getInstructorById(-5L));
    }

    // ── updateInstructor ───────────────────────────────────────────────────────

    @Test
    void updateInstructor_success() {
        Instructor    existing = instructor(1L, "Old", "Old");
        InstructorDTO dto      = dto(1L, "New", "Spring");
        InstructorDTO result   = dto(1L, "New", "Spring");

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(instructorRepository.save(existing)).thenReturn(existing);
        when(instructorMapper.toDTO(existing)).thenReturn(result);

        InstructorDTO actual = service.updateInstructor(1L, dto);

        assertEquals("New",    actual.getName());
        assertEquals("Spring", actual.getSpecialization());
        verify(instructorRepository).save(existing);
    }

    @Test
    void updateInstructor_notFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.updateInstructor(1L, dto(1L, "A", "B")));
        verify(instructorRepository, never()).save(any());
    }

    @Test
    void updateInstructor_invalidId() {
        assertThrows(InvalidInputException.class,
                () -> service.updateInstructor(-1L, dto(null, "A", "B")));
    }

    // ── deleteInstructor ───────────────────────────────────────────────────────

    @Test
    void deleteInstructor_success() {
        Instructor i = instructor(1L, "Ahmed", "AI");
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(i));

        service.deleteInstructor(1L);

        verify(instructorRepository).delete(i);
    }

    @Test
    void deleteInstructor_notFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.deleteInstructor(1L));
        verify(instructorRepository, never()).delete(any());
    }

    @Test
    void deleteInstructor_invalidId() {
        assertThrows(InvalidInputException.class, () -> service.deleteInstructor(null));
        assertThrows(InvalidInputException.class, () -> service.deleteInstructor(0L));
    }
}
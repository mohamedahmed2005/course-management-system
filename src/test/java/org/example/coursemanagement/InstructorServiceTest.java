package org.example.coursemanagement;

import org.example.coursemanagement.DTO.InstructorDTO;
import org.example.coursemanagement.Entity.Instructor;
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


    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorServiceImplementation service;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addInstructor_success(){
        InstructorDTO dto =
                new InstructorDTO(
                        null,
                        "Ahmed",
                        "AI"
                );

        Instructor saved = new Instructor();

        saved.setId(1L);
        saved.setName("Ahmed");
        saved.setSpecialization("AI");

        when(instructorRepository.save(any(Instructor.class)))
                .thenReturn(saved);

        InstructorDTO result =
                service.addInstructor(dto);

        assertEquals(1L,result.getId());
        assertEquals("Ahmed",result.getName());
        assertEquals("AI",result.getSpecialization());

        verify(instructorRepository)
                .save(any(Instructor.class));
    }

    @Test
    void addInstructor_nullDTO(){
        assertThrows(
                IllegalArgumentException.class,
                () -> service.addInstructor(null)
        );

        verify(instructorRepository,never())
                .save(any());
    }

    @Test
    void getInstructorById_success(){
        Instructor instructor = new Instructor();

        instructor.setId(5L);
        instructor.setName("Mohamed");
        instructor.setSpecialization("Java");

        when(instructorRepository.findById(5L))
                .thenReturn(Optional.of(instructor));

        InstructorDTO result =
                service.getInstructorById(5L);

        assertEquals(5L,result.getId());
        assertEquals("Mohamed",result.getName());
        assertEquals("Java",result.getSpecialization());

    }
    @Test
    void getInstructorById_notFound(){
        when(instructorRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.getInstructorById(10L)
        );
    }

    @Test
    void getInstructorById_invalidId(){
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getInstructorById(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getInstructorById(-1L)
        );
    }

    @Test
    void updateInstructor_success(){
        Instructor existing = new Instructor();

        existing.setId(1L);
        existing.setName("Old");
        existing.setSpecialization("Old");

        InstructorDTO dto =
                new InstructorDTO(
                        1L,
                        "New",
                        "Spring"
                );

        when(instructorRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(instructorRepository.save(any()))
                .thenReturn(existing);

        InstructorDTO result =
                service.updateInstructor(1L,dto);

        assertEquals(
                "New",
                result.getName()
        );

        assertEquals(
                "Spring",
                result.getSpecialization()
        );

        verify(instructorRepository)
                .save(existing);
    }

    @Test
    void updateInstructor_notFound(){

        when(instructorRepository.findById(1L))
                .thenReturn(Optional.empty());

        InstructorDTO dto =
                new InstructorDTO(
                        1L,
                        "Ahmed",
                        "AI"
                );

        assertThrows(
                RuntimeException.class,
                () -> service.updateInstructor(1L,dto)
        );

        verify(instructorRepository,never())
                .save(any());

    }

    @Test
    void updateInstructor_nullDTO(){

        assertThrows(
                IllegalArgumentException.class,
                () -> service.updateInstructor(1L,null)
        );

    }
    @Test
    void deleteInstructor_success(){
        Instructor instructor = new Instructor();

        instructor.setId(1L);

        when(instructorRepository.findById(1L))
                .thenReturn(Optional.of(instructor));

        service.deleteInstructor(1L);

        verify(instructorRepository)
                .delete(instructor);
    }

    @Test
    void deleteInstructor_notFound(){
        when(instructorRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.deleteInstructor(1L)
        );

        verify(instructorRepository,never())
                .delete(any());

    }

    @Test
    void deleteInstructor_invalidId(){
        assertThrows(
                IllegalArgumentException.class,
                () -> service.deleteInstructor(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.deleteInstructor(0L)
        );
    }
}
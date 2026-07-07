package org.example.coursemanagement;

import org.example.coursemanagement.DTO.InstructorDTO;
import org.example.coursemanagement.Entity.Instructor;
import org.example.coursemanagement.Repository.InstructorRepository;
import org.example.coursemanagement.ServiceImplementation.InstructorServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorServiceImplementation instructorService;


    @Test
    void addInstructor_shouldSaveInstructor() {

        InstructorDTO dto = new InstructorDTO(
                null,
                "Ahmed",
                "Backend Development"
        );


        Instructor instructor = new Instructor();
        instructor.setName("Ahmed");
        instructor.setSpecialization("Backend Development");


        when(instructorRepository.save(any(Instructor.class)))
                .thenReturn(instructor);


        InstructorDTO result =
                instructorService.addInstructor(dto);


        assertEquals("Ahmed", result.getName());
        assertEquals("Backend Development", result.getSpecialization());


        verify(instructorRepository)
                .save(any(Instructor.class));
    }


    @Test
    void getInstructorById_shouldReturnInstructor() {

        Instructor instructor = new Instructor();
        instructor.setName("Mohamed");
        instructor.setSpecialization("Java");


        when(instructorRepository.findById(1L))
                .thenReturn(Optional.of(instructor));


        InstructorDTO result =
                instructorService.getInstructorById(1L);


        assertEquals("Mohamed", result.getName());
        assertEquals("Java", result.getSpecialization());


        verify(instructorRepository)
                .findById(1L);
    }


    @Test
    void getInstructorById_shouldThrowExceptionWhenNotFound() {


        when(instructorRepository.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(RuntimeException.class,
                () -> instructorService.getInstructorById(1L));


        verify(instructorRepository)
                .findById(1L);
    }


    @Test
    void updateInstructor_shouldUpdateInstructor() {

        Instructor existing = new Instructor();
        existing.setName("Old Name");
        existing.setSpecialization("Old Specialization");


        InstructorDTO dto = new InstructorDTO(
                1L,
                "New Name",
                "AI"
        );


        when(instructorRepository.findById(1L))
                .thenReturn(Optional.of(existing));


        when(instructorRepository.save(any(Instructor.class)))
                .thenReturn(existing);


        InstructorDTO result =
                instructorService.updateInstructor(1L, dto);


        assertEquals("New Name", result.getName());
        assertEquals("AI", result.getSpecialization());


        verify(instructorRepository)
                .save(existing);
    }


    @Test
    void deleteInstructor_shouldDeleteInstructor() {

        Instructor instructor = new Instructor();

        when(instructorRepository.findById(1L))
                .thenReturn(Optional.of(instructor));


        instructorService.deleteInstructor(1L);


        verify(instructorRepository)
                .delete(instructor);
    }


    @Test
    void deleteInstructor_shouldThrowExceptionWhenNotFound() {


        when(instructorRepository.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(RuntimeException.class,
                () -> instructorService.deleteInstructor(1L));


        verify(instructorRepository, never())
                .delete(any(Instructor.class));
    }
}
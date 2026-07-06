package org.example.coursemanagement.Service;

import org.example.coursemanagement.DTO.InstructorDTO;

import java.util.List;

public interface InstructorService {

    InstructorDTO addInstructor(InstructorDTO instructorDTO);

    List<InstructorDTO> getAllInstructors();

    InstructorDTO getInstructorById(Long id);

    InstructorDTO updateInstructor(Long id, InstructorDTO instructorDTO);

    void deleteInstructor(Long id);
}

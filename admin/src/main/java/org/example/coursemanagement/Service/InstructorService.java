package org.example.coursemanagement.Service;

import org.example.coursemanagement.DTO.InstructorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.awt.*;
import java.util.List;

public interface InstructorService {

    InstructorDTO addInstructor(InstructorDTO instructorDTO);

    Page<InstructorDTO> getAllInstructors(Pageable pageable);

    InstructorDTO getInstructorById(Long id);

    InstructorDTO updateInstructor(Long id, InstructorDTO instructorDTO);

    void deleteInstructor(Long id);
}

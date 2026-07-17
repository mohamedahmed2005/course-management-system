package org.example.coursemanagement.Mapper;

import org.example.coursemanagement.DTO.InstructorDTO;
import org.example.coursemanagement.Entity.Instructor;
import org.springframework.stereotype.Component;

@Component
public class InstructorMapper {

    public InstructorDTO toDTO(Instructor instructor) {
        return new InstructorDTO(
                instructor.getId(),
                instructor.getName(),
                instructor.getSpecialization()
        );
    }

    public Instructor toEntity(InstructorDTO dto) {
        Instructor instructor = new Instructor();
        instructor.setName(dto.getName());
        instructor.setSpecialization(dto.getSpecialization());
        return instructor;
    }
}

package org.example.coursemanagement.Mapper;

import org.example.coursemanagement.DTO.EnrollmentDTO;
import org.example.coursemanagement.Entity.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {

    public EnrollmentDTO toDTO(Enrollment enrollment) {
        return new EnrollmentDTO(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId(),
                enrollment.getStatus(),
                enrollment.getEnrollmentDate()
        );
    }
}

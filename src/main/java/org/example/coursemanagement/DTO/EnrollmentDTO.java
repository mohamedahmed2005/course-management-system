package org.example.coursemanagement.DTO;

import lombok.Getter;
import lombok.Setter;

public class EnrollmentDTO {
    @Getter
    @Setter
    private Long   id;

    @Getter
    @Setter
    private Long   studentId;

    @Getter
    @Setter
    private Long   courseId;

    @Getter
    @Setter
    private String status;

    public EnrollmentDTO(Long id, Long studentId, Long courseId, String status) {
        this.id        = id;
        this.studentId = studentId;
        this.courseId  = courseId;
        this.status    = status;
    }
}

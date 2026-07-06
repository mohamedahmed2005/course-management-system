package org.example.coursemanagement.DTO;

import lombok.Getter;
import lombok.Setter;

public class InstructorDTO {

    @Setter
    @Getter
    private Long   id;

    @Getter
    @Setter
    private String name;

    @Getter
    private String specialization;

    public InstructorDTO(Long id, String name, String specialization) {
        this.id             = id;
        this.name           = name;
        this.specialization = specialization;
    }


}

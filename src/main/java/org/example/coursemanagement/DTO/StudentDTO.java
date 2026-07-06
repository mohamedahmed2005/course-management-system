package org.example.coursemanagement.DTO;

import lombok.Getter;
import lombok.Setter;

public class StudentDTO {

    @Getter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String email;

    public StudentDTO(Long id, String name, String email) {
        this.id    = id;
        this.name  = name;
        this.email = email;
    }
}

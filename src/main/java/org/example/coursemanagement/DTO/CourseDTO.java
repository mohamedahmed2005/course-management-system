package org.example.coursemanagement.DTO;

import lombok.Getter;

public class CourseDTO {
    @Getter
    private Long   id;

    @Getter
    private String title;

    @Getter
    private String description;

    @Getter
    private Long   instructorId;


    public CourseDTO(Long id, String title, String description, Long instructorId) {
        this.id           = id;
        this.title        = title;
        this.description  = description;
        this.instructorId = instructorId;
    }
}

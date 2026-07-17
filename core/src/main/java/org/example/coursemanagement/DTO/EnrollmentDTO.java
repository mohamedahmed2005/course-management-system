package org.example.coursemanagement.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {

    private Long id;

    private Long studentId;

    private Long courseId;

    private String status;

    @NotNull(message = "Enrollment date is required")
    @PastOrPresent(message = "Enrollment date cannot be in the future")
    private LocalDate enrollmentDate;
}
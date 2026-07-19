package org.example.coursemanagement.DTO;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private Long instructorId;

    @FutureOrPresent(message = "Registration start time must not be in the past")
    private LocalDateTime registrationStartTime;

    private LocalDateTime registrationEndTime;

    /**
     * Cross-field validation: endTime must be after startTime.
     * Only validated when both fields are provided.
     */
    @AssertTrue(message = "Registration end time must be after start time")
    private boolean isRegistrationWindowValid() {
        if (registrationStartTime == null || registrationEndTime == null) {
            return true; // let @NotNull handle missing values if needed
        }
        return registrationEndTime.isAfter(registrationStartTime);
    }
}
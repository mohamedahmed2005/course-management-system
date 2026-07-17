package org.example.coursemanagement.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEnrollmentException extends RuntimeException {

    public DuplicateEnrollmentException(Long studentId, Long courseId) {
        super("Student " + studentId + " is already enrolled in course " + courseId);
    }

    public DuplicateEnrollmentException(String message) {
        super(message);
    }

}

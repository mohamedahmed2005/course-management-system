package org.example.coursemanagement.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RegistrationWindowException extends RuntimeException {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public RegistrationWindowException(String message) {
        super(message);
    }

    /** Registration has not opened yet */
    public static RegistrationWindowException notOpenYet(LocalDateTime startTime) {
        return new RegistrationWindowException(
                "Registration for this course has not opened yet. " +
                "It opens on " + startTime.format(FMT) + "."
        );
    }

    /** Registration period has already closed */
    public static RegistrationWindowException alreadyClosed(LocalDateTime endTime) {
        return new RegistrationWindowException(
                "Registration for this course has already closed. " +
                "It closed on " + endTime.format(FMT) + "."
        );
    }

    /** Course has no registration window configured */
    public static RegistrationWindowException notConfigured() {
        return new RegistrationWindowException(
                "This course does not have a registration window configured. " +
                "Please contact an administrator."
        );
    }
}

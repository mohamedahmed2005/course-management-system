package org.example.coursemanagement.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class ResourceDeletedException extends RuntimeException {

    public ResourceDeletedException(String message) {
        super(message);
    }

    public ResourceDeletedException(String resourceName, Long id) {
        super(resourceName + " with id " + id + " has been deleted");
    }

}

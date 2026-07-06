package org.example.coursemanagement.Service;

import org.example.coursemanagement.Entity.Instructor;

import java.util.List;

public interface InstructorService {
    Instructor addInstructor(Instructor instructor);
    List<Instructor> getAllInstructors();
    Instructor getInstructorById(Long id);
    Instructor updateInstructor(Long id, Instructor instructor);
    void deleteInstructor(Long id);
}

package org.example.coursemanagement.ServiceImplementation;

import org.example.coursemanagement.Entity.Instructor;
import org.example.coursemanagement.Entity.Student;
import org.example.coursemanagement.Repository.InstructorRepository;
import org.example.coursemanagement.Service.InstructorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorServiceImplementation implements InstructorService{
    private final InstructorRepository instructorRepository;

    public InstructorServiceImplementation(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public Instructor addInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    @Override
    public Instructor getInstructorById(Long id) {
        return instructorRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));
    }

    @Override
    public Instructor updateInstructor(Long id, Instructor instructor) {
        Instructor existing = getInstructorById(id);

        existing.setName(instructor.getName());
        existing.setSpecialization(instructor.getSpecialization());

        return instructorRepository.save(existing);
    }

    @Override
    public void deleteInstructor(Long id) {
        Instructor instructor = getInstructorById(id);
        instructorRepository.delete(instructor);
    }
}

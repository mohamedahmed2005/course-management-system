package org.example.coursemanagement.ServiceImplementation;

import org.example.coursemanagement.DTO.InstructorDTO;
import org.example.coursemanagement.Entity.Instructor;
import org.example.coursemanagement.Exception.InvalidInputException;
import org.example.coursemanagement.Exception.ResourceNotFoundException;
import org.example.coursemanagement.Mapper.InstructorMapper;
import org.example.coursemanagement.Repository.InstructorRepository;
import org.example.coursemanagement.Service.InstructorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InstructorServiceImplementation implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final InstructorMapper     instructorMapper;

    public InstructorServiceImplementation(
            InstructorRepository instructorRepository,
            InstructorMapper instructorMapper
    ) {
        this.instructorRepository = instructorRepository;
        this.instructorMapper     = instructorMapper;
    }

    @Override
    public InstructorDTO addInstructor(InstructorDTO instructorDTO) {
        Instructor instructor = instructorMapper.toEntity(instructorDTO);
        return instructorMapper.toDTO(instructorRepository.save(instructor));
    }

    @Override
    public Page<InstructorDTO> getAllInstructors(Pageable pageable) {
        return instructorRepository
                .findAll(pageable)
                .map(instructorMapper::toDTO);
    }

    @Override
    public InstructorDTO getInstructorById(Long id) {
        validateId(id);
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", id));
        return instructorMapper.toDTO(instructor);
    }

    @Override
    public InstructorDTO updateInstructor(Long id, InstructorDTO instructorDTO) {
        validateId(id);
        Instructor existing = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", id));
        existing.setName(instructorDTO.getName());
        existing.setSpecialization(instructorDTO.getSpecialization());
        return instructorMapper.toDTO(instructorRepository.save(existing));
    }

    @Override
    public void deleteInstructor(Long id) {
        validateId(id);
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", id));
        instructorRepository.delete(instructor);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidInputException("ID must be a positive number");
        }
    }
}
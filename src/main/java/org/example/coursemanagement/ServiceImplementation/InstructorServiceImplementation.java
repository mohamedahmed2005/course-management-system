package org.example.coursemanagement.ServiceImplementation;

import org.example.coursemanagement.DTO.InstructorDTO;
import org.example.coursemanagement.Entity.Instructor;
import org.example.coursemanagement.Repository.InstructorRepository;
import org.example.coursemanagement.Service.InstructorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstructorServiceImplementation implements InstructorService {

    private final InstructorRepository instructorRepository;

    public InstructorServiceImplementation(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    // ─── Mapping helpers ────────────────────────────────────────

    private InstructorDTO toDTO(Instructor instructor) {
        return new InstructorDTO(
                instructor.getId(),
                instructor.getName(),
                instructor.getSpecialization()
        );
    }

    private Instructor toEntity(InstructorDTO dto) {
        Instructor instructor = new Instructor();
        instructor.setName(dto.getName());
        instructor.setSpecialization(dto.getSpecialization());
        return instructor;
    }

    // ─── Service methods ────────────────────────────────────────

    @Override
    public InstructorDTO addInstructor(InstructorDTO instructorDTO) {
        Instructor saved = instructorRepository.save(toEntity(instructorDTO));
        return toDTO(saved);
    }

    @Override
    public Page<InstructorDTO> getAllInstructors(Pageable pageable) {

        return instructorRepository.findAll(pageable)
                .map(this::toDTO);
    }

    @Override
    public InstructorDTO getInstructorById(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));
        return toDTO(instructor);
    }

    @Override
    public InstructorDTO updateInstructor(Long id, InstructorDTO instructorDTO) {
        Instructor existing = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));

        existing.setName(instructorDTO.getName());
        existing.setSpecialization(instructorDTO.getSpecialization());

        return toDTO(instructorRepository.save(existing));
    }

    @Override
    public void deleteInstructor(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));
        instructorRepository.delete(instructor);
    }
}

package org.example.coursemanagement.ServiceImplementation;

import org.example.coursemanagement.DTO.EnrollmentDTO;
import org.example.coursemanagement.Entity.Enrollment;
import org.example.coursemanagement.Exception.InvalidInputException;
import org.example.coursemanagement.Exception.ResourceNotFoundException;
import org.example.coursemanagement.Mapper.EnrollmentMapper;
import org.example.coursemanagement.Repository.EnrollmentRepository;
import org.example.coursemanagement.Repository.StudentRepository;
import org.example.coursemanagement.Service.EnrollmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentServiceImplementation implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository    studentRepository;
    private final EnrollmentMapper     enrollmentMapper;

    public EnrollmentServiceImplementation(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            EnrollmentMapper enrollmentMapper
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository    = studentRepository;
        this.enrollmentMapper     = enrollmentMapper;
    }

    @Override
    public Page<EnrollmentDTO> getAllEnrollments(Pageable pageable) {
        return enrollmentRepository.findAll(pageable)
                .map(enrollmentMapper::toDTO);
    }

    @Override
    public EnrollmentDTO getEnrollmentById(Long id) {
        validateId(id);
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", id));
        return enrollmentMapper.toDTO(enrollment);
    }

    @Override
    public Page<EnrollmentDTO> getEnrollmentsByStudentId(Long studentId, Pageable pageable) {
        validateId(studentId);
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student", studentId);
        }
        return enrollmentRepository.findByStudentId(studentId, pageable)
                .map(enrollmentMapper::toDTO);
    }

    @Override
    public Page<EnrollmentDTO> getEnrollmentsByCourseId(Long courseId, Pageable pageable) {
        validateId(courseId);
        return enrollmentRepository.findByCourseId(courseId, pageable)
                .map(enrollmentMapper::toDTO);
    }

    @Override
    public EnrollmentDTO updateEnrollmentStatus(Long id, String status) {
        validateId(id);

        if (status == null || status.isBlank()) {
            throw new InvalidInputException("Status must not be blank");
        }

        List<String> allowedStatuses = List.of("ENROLLED", "CANCELLED", "COMPLETED", "PENDING");
        if (!allowedStatuses.contains(status.toUpperCase())) {
            throw new InvalidInputException(
                "Invalid status. Allowed values: " + String.join(", ", allowedStatuses)
            );
        }

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", id));

        enrollment.setStatus(status.toUpperCase());
        return enrollmentMapper.toDTO(enrollmentRepository.save(enrollment));
    }

    @Override
    public void deleteEnrollment(Long id) {
        validateId(id);
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", id));
        enrollmentRepository.delete(enrollment);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidInputException("ID must be a positive number");
        }
    }
}

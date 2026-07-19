package org.example.coursemanagement.ServiceImplementation;

import org.example.coursemanagement.DTO.StudentDTO;
import org.example.coursemanagement.Entity.Student;
import org.example.coursemanagement.Exception.InvalidInputException;
import org.example.coursemanagement.Exception.ResourceNotFoundException;
import org.example.coursemanagement.Mapper.StudentMapper;
import org.example.coursemanagement.Repository.StudentRepository;
import org.example.coursemanagement.Service.StudentService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImplementation implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper     studentMapper;
    private final Validator         validator;

    public StudentServiceImplementation(
            StudentRepository studentRepository,
            StudentMapper studentMapper,
            Validator validator
    ) {
        this.studentRepository = studentRepository;
        this.studentMapper     = studentMapper;
        this.validator         = validator;
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        validateId(id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
        return studentMapper.toDTO(student);
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        validateId(id);

        // 1. Check ID exists in DB first — throws 404 before any validation
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));

        // 2. Now validate the request body
        Set<ConstraintViolation<StudentDTO>> violations = validator.validate(studentDTO);
        if (!violations.isEmpty()) {
            String messages = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new InvalidInputException(messages);
        }

        existing.setName(studentDTO.getName());
        existing.setEmail(studentDTO.getEmail());
        return studentMapper.toDTO(studentRepository.save(existing));
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidInputException("ID must be a positive number");
        }
    }
}

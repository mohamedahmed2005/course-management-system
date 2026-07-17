package org.example.coursemanagement.ServiceImplementation;

import org.example.coursemanagement.DTO.StudentDTO;
import org.example.coursemanagement.Entity.Student;
import org.example.coursemanagement.Exception.InvalidInputException;
import org.example.coursemanagement.Exception.ResourceNotFoundException;
import org.example.coursemanagement.Mapper.StudentMapper;
import org.example.coursemanagement.Repository.StudentRepository;
import org.example.coursemanagement.Service.StudentService;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImplementation implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper     studentMapper;

    public StudentServiceImplementation(
            StudentRepository studentRepository,
            StudentMapper studentMapper
    ) {
        this.studentRepository = studentRepository;
        this.studentMapper     = studentMapper;
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
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
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

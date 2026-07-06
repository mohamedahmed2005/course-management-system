package org.example.coursemanagement.ServiceImplementation;

import org.example.coursemanagement.DTO.StudentDTO;
import org.example.coursemanagement.Entity.Student;
import org.example.coursemanagement.Repository.StudentRepository;
import org.example.coursemanagement.Service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImplementation implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImplementation(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // ─── Mapping helpers ────────────────────────────────────────

    /** Entity → DTO */
    private StudentDTO toDTO(Student student) {
        return new StudentDTO(
                student.getId(),
                student.getName(),
                student.getEmail()
        );
    }

    /** DTO → Entity (بدون id لأن الـ DB بتولّده) */
    private Student toEntity(StudentDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        return student;
    }

    // ─── Service methods ────────────────────────────────────────

    @Override
    public StudentDTO addStudent(StudentDTO studentDTO) {
        Student saved = studentRepository.save(toEntity(studentDTO));
        return toDTO(saved);
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        return toDTO(student);
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        existing.setName(studentDTO.getName());
        existing.setEmail(studentDTO.getEmail());

        return toDTO(studentRepository.save(existing));
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        studentRepository.delete(student);
    }
}

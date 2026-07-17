package org.example.coursemanagement.ServiceImplementation;

import org.example.coursemanagement.DTO.EnrollmentDTO;
import org.example.coursemanagement.Entity.Course;
import org.example.coursemanagement.Entity.Enrollment;
import org.example.coursemanagement.Entity.Student;
import org.example.coursemanagement.Exception.DuplicateEnrollmentException;
import org.example.coursemanagement.Exception.InvalidInputException;
import org.example.coursemanagement.Exception.RegistrationWindowException;
import org.example.coursemanagement.Exception.ResourceDeletedException;
import org.example.coursemanagement.Exception.ResourceNotFoundException;
import org.example.coursemanagement.Mapper.EnrollmentMapper;
import org.example.coursemanagement.Repository.CourseRepository;
import org.example.coursemanagement.Repository.EnrollmentRepository;
import org.example.coursemanagement.Repository.StudentRepository;
import org.example.coursemanagement.Service.EnrollmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class EnrollmentServiceImplementation implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository    studentRepository;
    private final CourseRepository     courseRepository;
    private final EnrollmentMapper     enrollmentMapper;

    public EnrollmentServiceImplementation(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            EnrollmentMapper enrollmentMapper
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository    = studentRepository;
        this.courseRepository     = courseRepository;
        this.enrollmentMapper     = enrollmentMapper;
    }

    @Override
    public EnrollmentDTO enrollStudent(Long studentId, Long courseId, EnrollmentDTO enrollmentDTO) {
        validateId(studentId);
        validateId(courseId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));

        if (course.isDeleted()) {
            throw new ResourceDeletedException("Cannot enroll in deleted course");
        }

        // ── Registration window validation ────────────────────────────────────
        LocalDateTime start = course.getRegistrationStartTime();
        LocalDateTime end   = course.getRegistrationEndTime();
        LocalDateTime now   = LocalDateTime.now();

        if (start == null || end == null) {
            throw RegistrationWindowException.notConfigured();
        }
        if (now.isBefore(start)) {
            throw RegistrationWindowException.notOpenYet(start);
        }
        if (now.isAfter(end)) {
            throw RegistrationWindowException.alreadyClosed(end);
        }
        // ─────────────────────────────────────────────────────────────────────

        boolean alreadyEnrolled = enrollmentRepository
                .findByStudentIdAndCourseId(studentId, courseId)
                .isPresent();

        if (alreadyEnrolled) {
            throw new DuplicateEnrollmentException(studentId, courseId);
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus("ENROLLED");
        enrollment.setEnrollmentDate(enrollmentDTO.getEnrollmentDate());

        return enrollmentMapper.toDTO(enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentDTO getEnrollmentById(Long id) {
        validateId(id);
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", id));
        return enrollmentMapper.toDTO(enrollment);
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByStudentId(Long studentId) {
        validateId(studentId);
        return enrollmentRepository
                .findByStudentId(studentId)
                .stream()
                .map(enrollmentMapper::toDTO)
                .collect(Collectors.toList());
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
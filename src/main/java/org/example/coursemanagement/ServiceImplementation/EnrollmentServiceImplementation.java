package org.example.coursemanagement.ServiceImplementation;

import org.example.coursemanagement.DTO.EnrollmentDTO;
import org.example.coursemanagement.Entity.Course;
import org.example.coursemanagement.Entity.Enrollment;
import org.example.coursemanagement.Entity.Student;
import org.example.coursemanagement.Repository.CourseRepository;
import org.example.coursemanagement.Repository.EnrollmentRepository;
import org.example.coursemanagement.Repository.StudentRepository;
import org.example.coursemanagement.Service.EnrollmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImplementation implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository    studentRepository;
    private final CourseRepository     courseRepository;

    public EnrollmentServiceImplementation(EnrollmentRepository enrollmentRepository,
                                           StudentRepository studentRepository,
                                           CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository    = studentRepository;
        this.courseRepository     = courseRepository;
    }

    // ─── Mapping helper ─────────────────────────────────────────

    private EnrollmentDTO toDTO(Enrollment enrollment) {
        return new EnrollmentDTO(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId(),
                enrollment.getStatus()
        );
    }

    // ─── Service methods ────────────────────────────────────────

    @Override
    public EnrollmentDTO enrollStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        boolean exists = enrollmentRepository
                .findByStudentIdAndCourseId(studentId, courseId)
                .isPresent();

        if (exists) {
            throw new RuntimeException("Student already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus("ENROLLED");

        return toDTO(enrollmentRepository.save(enrollment));
    }

    @Override
    public List<EnrollmentDTO> getAllEnrollments() {
        return enrollmentRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentDTO getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        return toDTO(enrollment);
    }

    @Override
    public void deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
    }
}
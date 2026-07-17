package org.example.coursemanagement;

import org.example.coursemanagement.DTO.EnrollmentDTO;
import org.example.coursemanagement.Entity.Course;
import org.example.coursemanagement.Entity.Enrollment;
import org.example.coursemanagement.Entity.Student;
import org.example.coursemanagement.Exception.DuplicateEnrollmentException;
import org.example.coursemanagement.Exception.InvalidInputException;
import org.example.coursemanagement.Exception.ResourceDeletedException;
import org.example.coursemanagement.Exception.ResourceNotFoundException;
import org.example.coursemanagement.Mapper.EnrollmentMapper;
import org.example.coursemanagement.Repository.CourseRepository;
import org.example.coursemanagement.Repository.EnrollmentRepository;
import org.example.coursemanagement.Repository.StudentRepository;
import org.example.coursemanagement.ServiceImplementation.EnrollmentServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    @Mock private EnrollmentRepository enrollmentRepository;
    @Mock private StudentRepository    studentRepository;
    @Mock private CourseRepository     courseRepository;
    @Mock private EnrollmentMapper     enrollmentMapper;
    @InjectMocks private EnrollmentServiceImplementation service;

    @BeforeEach
    void setup() { MockitoAnnotations.openMocks(this); }

    // ── helpers ───────────────────────────────────────────────────────────────

    private Student student(Long id) {
        Student s = new Student(); s.setId(id); return s;
    }

    private Course course(Long id, boolean deleted) {
        Course c = new Course(); c.setId(id); c.setDeleted(deleted); return c;
    }

    private Enrollment enrollment(Long id, Student s, Course c, LocalDate date) {
        Enrollment e = new Enrollment();
        e.setId(id); e.setStudent(s); e.setCourse(c);
        e.setStatus("ENROLLED"); e.setEnrollmentDate(date);
        return e;
    }

    private EnrollmentDTO dto(Long enrollmentId, Long studentId, Long courseId, LocalDate date) {
        return new EnrollmentDTO(enrollmentId, studentId, courseId, "ENROLLED", date);
    }

    // ── enrollStudent ──────────────────────────────────────────────────────────

    @Test
    void enrollStudent_success() {
        LocalDate    today    = LocalDate.now();
        Student      student  = student(1L);
        Course       course   = course(10L, false);
        Enrollment   saved    = enrollment(100L, student, course, today);
        EnrollmentDTO inDTO   = dto(null, null, null, today);
        EnrollmentDTO result  = dto(100L, 1L, 10L, today);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 10L)).thenReturn(Optional.empty());
        when(enrollmentRepository.save(any())).thenReturn(saved);
        when(enrollmentMapper.toDTO(saved)).thenReturn(result);

        EnrollmentDTO actual = service.enrollStudent(1L, 10L, inDTO);

        assertEquals(100L,   actual.getId());
        assertEquals(1L,     actual.getStudentId());
        assertEquals(10L,    actual.getCourseId());
        assertEquals(today,  actual.getEnrollmentDate());
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void enrollStudent_studentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.enrollStudent(1L, 10L, dto(null, null, null, LocalDate.now())));
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void enrollStudent_courseNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student(1L)));
        when(courseRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.enrollStudent(1L, 10L, dto(null, null, null, LocalDate.now())));
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void enrollStudent_courseDeleted() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student(1L)));
        when(courseRepository.findById(10L)).thenReturn(Optional.of(course(10L, true)));
        assertThrows(ResourceDeletedException.class,
                () -> service.enrollStudent(1L, 10L, dto(null, null, null, LocalDate.now())));
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void enrollStudent_alreadyEnrolled() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student(1L)));
        when(courseRepository.findById(10L)).thenReturn(Optional.of(course(10L, false)));
        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 10L))
                .thenReturn(Optional.of(new Enrollment()));
        assertThrows(DuplicateEnrollmentException.class,
                () -> service.enrollStudent(1L, 10L, dto(null, null, null, LocalDate.now())));
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void enrollStudent_invalidStudentId() {
        assertThrows(InvalidInputException.class,
                () -> service.enrollStudent(null, 10L, dto(null, null, null, LocalDate.now())));
        assertThrows(InvalidInputException.class,
                () -> service.enrollStudent(-1L, 10L, dto(null, null, null, LocalDate.now())));
    }

    @Test
    void enrollStudent_invalidCourseId() {
        assertThrows(InvalidInputException.class,
                () -> service.enrollStudent(1L, null, dto(null, null, null, LocalDate.now())));
        assertThrows(InvalidInputException.class,
                () -> service.enrollStudent(1L, 0L, dto(null, null, null, LocalDate.now())));
    }

    // ── getEnrollmentById ──────────────────────────────────────────────────────

    @Test
    void getEnrollmentById_success() {
        LocalDate  today = LocalDate.now();
        Enrollment e     = enrollment(5L, student(1L), course(10L, false), today);
        EnrollmentDTO result = dto(5L, 1L, 10L, today);

        when(enrollmentRepository.findById(5L)).thenReturn(Optional.of(e));
        when(enrollmentMapper.toDTO(e)).thenReturn(result);

        EnrollmentDTO actual = service.getEnrollmentById(5L);

        assertEquals(5L,    actual.getId());
        assertEquals(today, actual.getEnrollmentDate());
    }

    @Test
    void getEnrollmentById_notFound() {
        when(enrollmentRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getEnrollmentById(5L));
    }

    @Test
    void getEnrollmentById_invalidId() {
        assertThrows(InvalidInputException.class, () -> service.getEnrollmentById(null));
        assertThrows(InvalidInputException.class, () -> service.getEnrollmentById(-1L));
    }

    // ── getEnrollmentsByStudentId ──────────────────────────────────────────────

    @Test
    void getEnrollmentsByStudentId_success() {
        LocalDate  today = LocalDate.now();
        Enrollment e     = enrollment(1L, student(1L), course(10L, false), today);

        when(enrollmentRepository.findByStudentId(1L)).thenReturn(List.of(e));
        when(enrollmentMapper.toDTO(e)).thenReturn(dto(1L, 1L, 10L, today));

        List<EnrollmentDTO> result = service.getEnrollmentsByStudentId(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getStudentId());
    }

    @Test
    void getEnrollmentsByStudentId_invalidId() {
        assertThrows(InvalidInputException.class, () -> service.getEnrollmentsByStudentId(null));
    }

    // ── getEnrollmentsByCourseId ───────────────────────────────────────────────

    @Test
    void getEnrollmentsByCourseId_success() {
        LocalDate  today = LocalDate.now();
        Enrollment e     = enrollment(1L, student(1L), course(10L, false), today);

        when(enrollmentRepository.findByCourseId(10L)).thenReturn(List.of(e));
        when(enrollmentMapper.toDTO(e)).thenReturn(dto(1L, 1L, 10L, today));

        List<EnrollmentDTO> result = service.getEnrollmentsByCourseId(10L);

        assertEquals(1,   result.size());
        assertEquals(10L, result.get(0).getCourseId());
    }

    @Test
    void getEnrollmentsByCourseId_invalidId() {
        assertThrows(InvalidInputException.class, () -> service.getEnrollmentsByCourseId(0L));
    }

    // ── deleteEnrollment ───────────────────────────────────────────────────────

    @Test
    void deleteEnrollment_success() {
        Enrollment e = enrollment(1L, student(1L), course(10L, false), LocalDate.now());
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(e));

        service.deleteEnrollment(1L);

        verify(enrollmentRepository).delete(e);
    }

    @Test
    void deleteEnrollment_notFound() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.deleteEnrollment(1L));
        verify(enrollmentRepository, never()).delete(any());
    }

    @Test
    void deleteEnrollment_invalidId() {
        assertThrows(InvalidInputException.class, () -> service.deleteEnrollment(null));
        assertThrows(InvalidInputException.class, () -> service.deleteEnrollment(-1L));
    }
}
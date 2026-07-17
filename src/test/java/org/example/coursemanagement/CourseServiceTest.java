package org.example.coursemanagement;

import org.example.coursemanagement.DTO.CourseDTO;
import org.example.coursemanagement.Entity.Course;
import org.example.coursemanagement.Entity.Instructor;
import org.example.coursemanagement.Exception.InvalidInputException;
import org.example.coursemanagement.Exception.ResourceDeletedException;
import org.example.coursemanagement.Exception.ResourceNotFoundException;
import org.example.coursemanagement.Mapper.CourseMapper;
import org.example.coursemanagement.Repository.CourseRepository;
import org.example.coursemanagement.ServiceImplementation.CourseServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock private CourseRepository courseRepository;
    @Mock private CourseMapper courseMapper;
    @InjectMocks private CourseServiceImplementation service;

    @BeforeEach
    void setup() { MockitoAnnotations.openMocks(this); }

    // ── helpers ───────────────────────────────────────────────────────────────

    private Course course(Long id, String title, boolean deleted) {
        Course c = new Course();
        c.setId(id); c.setTitle(title); c.setDeleted(deleted);
        return c;
    }

    private CourseDTO dto(Long id, String title, String desc, Long instructorId) {
        return new CourseDTO(id, title, desc, instructorId);
    }

    private Instructor instructor(Long id) {
        Instructor i = new Instructor();
        i.setId(id);
        return i;
    }

    // ── addCourse ──────────────────────────────────────────────────────────────

    @Test
    void addCourse_success() {
        CourseDTO dto    = dto(null, "Java", "Spring course", 1L);
        Course    entity = course(10L, "Java", false);
        entity.setInstructor(instructor(1L));
        CourseDTO result = dto(10L, "Java", "Spring course", 1L);

        when(courseMapper.toEntity(dto)).thenReturn(entity);
        when(courseRepository.save(entity)).thenReturn(entity);
        when(courseMapper.toDTO(entity)).thenReturn(result);

        CourseDTO actual = service.addCourse(dto);

        assertEquals(10L,   actual.getId());
        assertEquals("Java", actual.getTitle());
        verify(courseRepository).save(entity);
    }

    @Test
    void addCourse_noInstructor() {
        CourseDTO dto    = dto(null, "Java", "Desc", null);
        Course    entity = course(1L, "Java", false);
        CourseDTO result = dto(1L, "Java", "Desc", null);

        when(courseMapper.toEntity(dto)).thenReturn(entity);
        when(courseRepository.save(entity)).thenReturn(entity);
        when(courseMapper.toDTO(entity)).thenReturn(result);

        CourseDTO actual = service.addCourse(dto);

        assertNull(actual.getInstructorId());
        verify(courseRepository).save(entity);
    }

    // ── getCourseById ──────────────────────────────────────────────────────────

    @Test
    void getCourseById_success() {
        Course    entity = course(5L, "Database", false);
        CourseDTO result = dto(5L, "Database", "Desc", null);

        when(courseRepository.findById(5L)).thenReturn(Optional.of(entity));
        when(courseMapper.toDTO(entity)).thenReturn(result);

        CourseDTO actual = service.getCourseById(5L);

        assertEquals(5L,         actual.getId());
        assertEquals("Database", actual.getTitle());
    }

    @Test
    void getCourseById_notFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getCourseById(99L));
    }

    @Test
    void getCourseById_deleted() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course(1L, "Java", true)));
        assertThrows(ResourceDeletedException.class, () -> service.getCourseById(1L));
    }

    @Test
    void getCourseById_nullId() {
        assertThrows(InvalidInputException.class, () -> service.getCourseById(null));
    }

    @Test
    void getCourseById_negativeId() {
        assertThrows(InvalidInputException.class, () -> service.getCourseById(-1L));
    }

    // ── updateCourse ───────────────────────────────────────────────────────────

    @Test
    void updateCourse_success() {
        Course    existing = course(1L, "Old", false);
        CourseDTO dto      = dto(1L, "New", "Updated", null);
        CourseDTO result   = dto(1L, "New", "Updated", null);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(existing)).thenReturn(existing);
        when(courseMapper.toDTO(existing)).thenReturn(result);

        CourseDTO actual = service.updateCourse(1L, dto);

        assertEquals("New",     actual.getTitle());
        assertEquals("Updated", actual.getDescription());
        verify(courseRepository).save(existing);
    }

    @Test
    void updateCourse_notFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.updateCourse(1L, dto(1L, "A", "B", null)));
        verify(courseRepository, never()).save(any());
    }

    @Test
    void updateCourse_onDeletedCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course(1L, "Java", true)));
        assertThrows(ResourceDeletedException.class,
                () -> service.updateCourse(1L, dto(1L, "New", "Desc", null)));
    }

    @Test
    void updateCourse_invalidId() {
        assertThrows(InvalidInputException.class,
                () -> service.updateCourse(0L, dto(null, "A", "B", null)));
    }

    // ── deleteCourse ───────────────────────────────────────────────────────────

    @Test
    void deleteCourse_success() {
        Course c = course(1L, "Java", false);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(c));

        service.deleteCourse(1L);

        assertTrue(c.isDeleted());
        verify(courseRepository).save(c);
    }

    @Test
    void deleteCourse_notFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.deleteCourse(1L));
    }

    @Test
    void deleteCourse_alreadyDeleted() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course(1L, "Java", true)));
        assertThrows(ResourceDeletedException.class, () -> service.deleteCourse(1L));
        verify(courseRepository, never()).save(any());
    }

    @Test
    void deleteCourse_invalidId() {
        assertThrows(InvalidInputException.class, () -> service.deleteCourse(null));
        assertThrows(InvalidInputException.class, () -> service.deleteCourse(-1L));
    }
}
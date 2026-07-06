package org.example.coursemanagement.ServiceImplementation;

import org.example.coursemanagement.DTO.CourseDTO;
import org.example.coursemanagement.Entity.Course;
import org.example.coursemanagement.Entity.Instructor;
import org.example.coursemanagement.Repository.CourseRepository;
import org.example.coursemanagement.Repository.InstructorRepository;
import org.example.coursemanagement.Service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImplementation implements CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    public CourseServiceImplementation(CourseRepository courseRepository,
                                       InstructorRepository instructorRepository) {
        this.courseRepository    = courseRepository;
        this.instructorRepository = instructorRepository;
    }

    // ─── Mapping helpers ────────────────────────────────────────

    private CourseDTO toDTO(Course course) {
        Long instructorId = (course.getInstructor() != null)
                ? course.getInstructor().getId()
                : null;
        return new CourseDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                instructorId
        );
    }

    private Course toEntity(CourseDTO dto) {
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());

        if (dto.getInstructorId() != null) {
            Instructor instructor = instructorRepository.findById(dto.getInstructorId())
                    .orElseThrow(() -> new RuntimeException(
                            "Instructor not found with id: " + dto.getInstructorId()));
            course.setInstructor(instructor);
        }
        return course;
    }

    // ─── Service methods ────────────────────────────────────────

    @Override
    public CourseDTO addCourse(CourseDTO courseDTO) {
        Course saved = courseRepository.save(toEntity(courseDTO));
        return toDTO(saved);
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        return toDTO(course);
    }

    @Override
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        existing.setTitle(courseDTO.getTitle());
        existing.setDescription(courseDTO.getDescription());

        if (courseDTO.getInstructorId() != null) {
            Instructor instructor = instructorRepository.findById(courseDTO.getInstructorId())
                    .orElseThrow(() -> new RuntimeException(
                            "Instructor not found with id: " + courseDTO.getInstructorId()));
            existing.setInstructor(instructor);
        }

        return toDTO(courseRepository.save(existing));
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        course.setDeleted(true);
        courseRepository.save(course);
    }
}

package org.example.coursemanagement.ServiceImplementation;

import org.example.coursemanagement.DTO.CourseDTO;
import org.example.coursemanagement.Entity.Course;
import org.example.coursemanagement.Exception.InvalidInputException;
import org.example.coursemanagement.Exception.ResourceDeletedException;
import org.example.coursemanagement.Exception.ResourceNotFoundException;
import org.example.coursemanagement.Mapper.CourseMapper;
import org.example.coursemanagement.Repository.CourseRepository;
import org.example.coursemanagement.Service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImplementation implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper     courseMapper;

    public CourseServiceImplementation(
            CourseRepository courseRepository,
            CourseMapper courseMapper
    ) {
        this.courseRepository = courseRepository;
        this.courseMapper     = courseMapper;
    }

    @Override
    public CourseDTO addCourse(CourseDTO courseDTO) {
        Course course = courseMapper.toEntity(courseDTO);
        return courseMapper.toDTO(courseRepository.save(course));
    }

    @Override
    public Page<CourseDTO> getAllCourses(Pageable pageable) {
        return courseRepository.findAllByDeletedFalse(pageable)
                .map(courseMapper::toDTO);
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        validateId(id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
        if (course.isDeleted()) {
            throw new ResourceDeletedException("Course", id);
        }
        return courseMapper.toDTO(course);
    }

    @Override
    public CourseDTO updateCourse(Long id, CourseDTO dto) {
        validateId(id);
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
        if (existing.isDeleted()) {
            throw new ResourceDeletedException("Cannot update deleted course");
        }
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setRegistrationStartTime(dto.getRegistrationStartTime());
        existing.setRegistrationEndTime(dto.getRegistrationEndTime());
        courseMapper.setInstructor(existing, dto.getInstructorId());
        return courseMapper.toDTO(courseRepository.save(existing));
    }

    @Override
    public void deleteCourse(Long id) {
        validateId(id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
        if (course.isDeleted()) {
            throw new ResourceDeletedException("Course already deleted");
        }
        course.setDeleted(true);
        courseRepository.save(course);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidInputException("ID must be a positive number");
        }
    }
}
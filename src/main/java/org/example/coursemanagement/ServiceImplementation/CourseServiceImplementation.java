package org.example.coursemanagement.ServiceImplementation;

import org.example.coursemanagement.DTO.CourseDTO;
import org.example.coursemanagement.Entity.Course;
import org.example.coursemanagement.Entity.Instructor;
import org.example.coursemanagement.Exception.InvalidInputException;
import org.example.coursemanagement.Exception.ResourceDeletedException;
import org.example.coursemanagement.Exception.ResourceNotFoundException;
import org.example.coursemanagement.Repository.CourseRepository;
import org.example.coursemanagement.Repository.InstructorRepository;
import org.example.coursemanagement.Service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImplementation implements CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    public CourseServiceImplementation(
            CourseRepository courseRepository,
            InstructorRepository instructorRepository
    ) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }


    private CourseDTO toDTO(Course course) {

        if (course == null) {
            throw new InvalidInputException("Course cannot be null");
        }

        Long instructorId = null;

        if (course.getInstructor() != null) {
            instructorId = course.getInstructor().getId();
        }

        return new CourseDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                instructorId
        );
    }


    private Course toEntity(CourseDTO dto) {

        if (dto == null) {
            throw new InvalidInputException("Course DTO cannot be null");
        }

        Course course = new Course();

        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());

        setInstructor(course, dto.getInstructorId());

        return course;
    }


    private void setInstructor(Course course, Long instructorId) {

        if (instructorId == null) {
            course.setInstructor(null);
            return;
        }

        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Instructor", instructorId));

        course.setInstructor(instructor);
    }


    @Override
    public CourseDTO addCourse(CourseDTO courseDTO) {

        Course course = toEntity(courseDTO);

        return toDTO(
                courseRepository.save(course)
        );
    }

    @Override
    public Page<CourseDTO> getAllCourses(Pageable pageable) {

        if (pageable == null) {
            throw new InvalidInputException("Pageable cannot be null");
        }

        return (Page<CourseDTO>) courseRepository.findAll(pageable)
                .filter(course -> !course.isDeleted())
                .map(this::toDTO);
    }


    @Override
    public CourseDTO getCourseById(Long id) {

        validateId(id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", id));

        if (course.isDeleted()) {
            throw new ResourceDeletedException("Course", id);
        }

        return toDTO(course);
    }



    @Override
    public CourseDTO updateCourse(Long id, CourseDTO dto) {

        validateId(id);

        if (dto == null) {
            throw new InvalidInputException("DTO cannot be null");
        }


        Course existing = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", id));


        if (existing.isDeleted()) {
            throw new ResourceDeletedException("Cannot update deleted course");
        }


        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());

        setInstructor(
                existing,
                dto.getInstructorId()
        );


        return toDTO(
                courseRepository.save(existing)
        );
    }



    @Override
    public void deleteCourse(Long id) {

        validateId(id);


        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", id));


        if (course.isDeleted()) {
            throw new ResourceDeletedException("Course already deleted");
        }


        course.setDeleted(true);

        courseRepository.save(course);
    }



    private void validateId(Long id) {

        if (id == null || id <= 0) {
            throw new InvalidInputException("Invalid id");
        }
    }

}
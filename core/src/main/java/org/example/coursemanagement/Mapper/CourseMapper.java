package org.example.coursemanagement.Mapper;

import org.example.coursemanagement.DTO.CourseDTO;
import org.example.coursemanagement.Entity.Course;
import org.example.coursemanagement.Entity.Instructor;
import org.example.coursemanagement.Exception.ResourceNotFoundException;
import org.example.coursemanagement.Repository.InstructorRepository;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    private final InstructorRepository instructorRepository;

    public CourseMapper(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public CourseDTO toDTO(Course course) {
        Long instructorId = course.getInstructor() != null
                ? course.getInstructor().getId()
                : null;

        return new CourseDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                instructorId,
                course.getRegistrationStartTime(),
                course.getRegistrationEndTime()
        );
    }

    public Course toEntity(CourseDTO dto) {
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setRegistrationStartTime(dto.getRegistrationStartTime());
        course.setRegistrationEndTime(dto.getRegistrationEndTime());
        setInstructor(course, dto.getInstructorId());
        return course;
    }

    public void setInstructor(Course course, Long instructorId) {
        if (instructorId == null) {
            course.setInstructor(null);
            return;
        }

        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", instructorId));

        course.setInstructor(instructor);
    }
}

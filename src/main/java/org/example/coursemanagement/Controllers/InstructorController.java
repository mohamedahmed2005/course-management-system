package org.example.coursemanagement.Controllers;

import org.example.coursemanagement.Entity.Course;
import org.example.coursemanagement.Entity.Instructor;
import org.example.coursemanagement.Service.CourseService;
import org.example.coursemanagement.Service.InstructorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Instructors")
public class InstructorController {
    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping
    public Instructor addInstructor(@RequestBody Instructor instructor) {
        return instructorService.addInstructor(instructor);
    }
    @GetMapping
    public List<Instructor> getAllInstructors() {
        return instructorService.getAllInstructors();
    }

    @GetMapping("/{id}")
    public Instructor getById(@PathVariable Long id) {
        return instructorService.getInstructorById(id);
    }

    @PutMapping("/{id}")
    public Instructor update(@PathVariable Long id, @RequestBody Instructor instructor) {
        return instructorService.updateInstructor(id, instructor);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
    }
}

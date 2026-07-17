package org.example.coursemanagement.Controllers;

import jakarta.validation.Valid;
import org.example.coursemanagement.DTO.EnrollmentDTO;
import org.example.coursemanagement.Service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // Enroll a student into a course
    @PostMapping
    public ResponseEntity<EnrollmentDTO> enroll(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @Valid @RequestBody EnrollmentDTO enrollmentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enrollStudent(studentId, courseId, enrollmentDTO));
    }

    // View a specific enrollment by ID
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    // View all enrollments for a specific student (own enrollments)
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDTO>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudentId(studentId));
    }

    // Cancel (unenroll from) a course
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
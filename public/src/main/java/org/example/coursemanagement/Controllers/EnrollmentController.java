package org.example.coursemanagement.Controllers;

import jakarta.validation.Valid;
import org.example.coursemanagement.DTO.EnrollmentDTO;
import org.example.coursemanagement.Service.EnrollmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<EnrollmentDTO> enroll(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @Valid @RequestBody EnrollmentDTO enrollmentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enrollStudent(studentId, courseId, enrollmentDTO));
    }

    @GetMapping
    public ResponseEntity<Page<EnrollmentDTO>> getAll(
            @RequestParam(defaultValue = "0")    int page,
            @RequestParam(defaultValue = "10")   int size,
            @RequestParam(defaultValue = "id,asc") String sort) {

        Pageable pageable = buildPageable(page, size, sort);
        return ResponseEntity.ok(enrollmentService.getAllEnrollments(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDTO>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudentId(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDTO>> getByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourseId(courseId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    private Pageable buildPageable(int page, int size, String sort) {
        String[] parts     = sort.split(",");
        String   sortBy    = parts[0];
        Sort.Direction dir = parts.length > 1 && parts[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(dir, sortBy));
    }
}
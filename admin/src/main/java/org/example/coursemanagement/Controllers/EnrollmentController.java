package org.example.coursemanagement.Controllers;

import org.example.coursemanagement.DTO.EnrollmentDTO;
import org.example.coursemanagement.Service.EnrollmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // Get all enrollments (paginated)
    @GetMapping
    public ResponseEntity<Page<EnrollmentDTO>> getAllEnrollments(
            @RequestParam(defaultValue = "0")      int page,
            @RequestParam(defaultValue = "10")     int size,
            @RequestParam(defaultValue = "id,asc") String sort) {

        Pageable pageable = buildPageable(page, size, sort);
        return ResponseEntity.ok(enrollmentService.getAllEnrollments(pageable));
    }

    // Get a single enrollment by ID
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    // Get all enrollments for a specific student (paginated)
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<EnrollmentDTO>> getByStudentId(
            @PathVariable Long studentId,
            @RequestParam(defaultValue = "0")      int page,
            @RequestParam(defaultValue = "10")     int size,
            @RequestParam(defaultValue = "id,asc") String sort) {

        Pageable pageable = buildPageable(page, size, sort);
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudentId(studentId, pageable));
    }

    // Get all enrollments for a specific course (paginated)
    @GetMapping("/course/{courseId}")
    public ResponseEntity<Page<EnrollmentDTO>> getByCourseId(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0")      int page,
            @RequestParam(defaultValue = "10")     int size,
            @RequestParam(defaultValue = "id,asc") String sort) {

        Pageable pageable = buildPageable(page, size, sort);
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourseId(courseId, pageable));
    }

    // Update only the status of an enrollment (admin-only action)
    @PatchMapping("/{id}/status")
    public ResponseEntity<EnrollmentDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        return ResponseEntity.ok(enrollmentService.updateEnrollmentStatus(id, status));
    }

    // Delete an enrollment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    private Pageable buildPageable(int page, int size, String sort) {
        String[]       parts  = sort.split(",");
        String         sortBy = parts[0];
        Sort.Direction dir    = parts.length > 1 && parts[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(dir, sortBy));
    }
}

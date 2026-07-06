package org.example.coursemanagement.Controllers;

import org.example.coursemanagement.DTO.EnrollmentDTO;
import org.example.coursemanagement.Service.EnrollmentService;
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

    // ➕ تسجيل طالب في كورس
    @PostMapping
    public ResponseEntity<EnrollmentDTO> enroll(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        EnrollmentDTO newEnrollment = enrollmentService.enrollStudent(studentId, courseId);
        return new ResponseEntity<>(newEnrollment, HttpStatus.CREATED);
    }

    // 📄 جلب كل عمليات التسجيل
    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getAll() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

    // 🔍 جلب تسجيل معين عن طريق الـ ID
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    // 🎓 جلب تسجيلات طالب معين
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDTO>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudentId(studentId));
    }

    // 📚 جلب الطلاب المسجلين في كورس معين
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDTO>> getByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourseId(courseId));
    }

    // ❌ إلغاء تسجيل (حذف)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
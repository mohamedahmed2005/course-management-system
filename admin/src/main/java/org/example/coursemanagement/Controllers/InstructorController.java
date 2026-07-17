package org.example.coursemanagement.Controllers;

import jakarta.validation.Valid;
import org.example.coursemanagement.DTO.InstructorDTO;
import org.example.coursemanagement.Service.InstructorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping
    public ResponseEntity<InstructorDTO> addInstructor(@Valid @RequestBody InstructorDTO instructorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(instructorService.addInstructor(instructorDTO));
    }

    @GetMapping
    public ResponseEntity<Page<InstructorDTO>> getAllInstructors(
            @RequestParam(defaultValue = "0")    int page,
            @RequestParam(defaultValue = "10")   int size,
            @RequestParam(defaultValue = "id,asc") String sort) {

        Pageable pageable = buildPageable(page, size, sort);
        return ResponseEntity.ok(instructorService.getAllInstructors(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstructorDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(instructorService.getInstructorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstructorDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody InstructorDTO instructorDTO) {
        return ResponseEntity.ok(instructorService.updateInstructor(id, instructorDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
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

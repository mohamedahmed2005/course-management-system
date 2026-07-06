package org.example.coursemanagement.Controllers;

import org.example.coursemanagement.DTO.InstructorDTO;
import org.example.coursemanagement.Service.InstructorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping
    public InstructorDTO addInstructor(@RequestBody InstructorDTO instructorDTO) {
        return instructorService.addInstructor(instructorDTO);
    }

    @GetMapping
    public Page<InstructorDTO> getAllInstructors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {

        Sort.Direction direction = sort.split(",")[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        String sortBy = sort.split(",")[0];
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return instructorService.getAllInstructors(pageable);
    }

    @GetMapping("/{id}")
    public InstructorDTO getById(@PathVariable Long id) {
        return instructorService.getInstructorById(id);
    }

    @PutMapping("/{id}")
    public InstructorDTO update(@PathVariable Long id, @RequestBody InstructorDTO instructorDTO) {
        return instructorService.updateInstructor(id, instructorDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
    }
}

package org.example.coursemanagement.Controllers;

import org.example.coursemanagement.DTO.InstructorDTO;
import org.example.coursemanagement.Service.InstructorService;
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
    public List<InstructorDTO> getAllInstructors() {
        return instructorService.getAllInstructors();
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

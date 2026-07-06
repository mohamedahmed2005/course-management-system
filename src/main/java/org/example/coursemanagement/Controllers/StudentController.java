package org.example.coursemanagement.Controllers;

import org.example.coursemanagement.DTO.StudentDTO;
import org.example.coursemanagement.Service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public StudentDTO addStudent(@RequestBody StudentDTO studentDTO) {
        return studentService.addStudent(studentDTO);
    }

    @GetMapping
    public Page<StudentDTO> getAllStudents(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "id,asc") String sort) {

        Sort.Direction direction = sort.split(",")[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        String sortBy = sort.split(",")[0];
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return studentService.getAllStudents(pageable);
    }

    @GetMapping("/{id}")
    public StudentDTO getById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public StudentDTO update(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        return studentService.updateStudent(id, studentDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}
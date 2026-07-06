package org.example.coursemanagement.Service;

import org.example.coursemanagement.DTO.StudentDTO;

import java.util.List;

public interface StudentService {

    StudentDTO addStudent(StudentDTO studentDTO);

    List<StudentDTO> getAllStudents();

    StudentDTO getStudentById(Long id);

    StudentDTO updateStudent(Long id, StudentDTO studentDTO);

    void deleteStudent(Long id);
}
package org.example.coursemanagement.Service;

import org.example.coursemanagement.DTO.StudentDTO;

public interface StudentService {

    StudentDTO getStudentById(Long id);

    StudentDTO updateStudent(Long id, StudentDTO studentDTO);
}

package org.example.coursemanagement.ServiceImplementation;

import org.example.coursemanagement.DTO.StudentDTO;
import org.example.coursemanagement.Entity.Student;
import org.example.coursemanagement.Exception.InvalidInputException;
import org.example.coursemanagement.Exception.ResourceNotFoundException;
import org.example.coursemanagement.Repository.StudentRepository;
import org.example.coursemanagement.Service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class StudentServiceImplementation implements StudentService {


    private final StudentRepository studentRepository;


    public StudentServiceImplementation(
            StudentRepository studentRepository
    ) {
        this.studentRepository = studentRepository;
    }




    // Entity -> DTO

    private StudentDTO toDTO(Student student) {


        if (student == null) {

            throw new InvalidInputException(
                    "Student cannot be null"
            );

        }


        return new StudentDTO(
                student.getId(),
                student.getName(),
                student.getEmail()
        );

    }




    // DTO -> Entity

    private Student toEntity(StudentDTO dto) {


        if (dto == null) {

            throw new InvalidInputException(
                    "Student DTO cannot be null"
            );

        }



        Student student = new Student();

        student.setName(dto.getName());
        student.setEmail(dto.getEmail());


        return student;

    }




    @Override
    public StudentDTO addStudent(StudentDTO studentDTO) {


        Student student =
                toEntity(studentDTO);



        return toDTO(
                studentRepository.save(student)
        );

    }




    @Override
    public Page<StudentDTO> getAllStudents(Pageable pageable) {


        if (pageable == null) {

            throw new InvalidInputException(
                    "Pageable cannot be null"
            );

        }



        return studentRepository
                .findAll(pageable)
                .map(this::toDTO);

    }




    @Override
    public StudentDTO getStudentById(Long id) {


        validateId(id);



        Student student =
                studentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Student", id));




        return toDTO(student);

    }




    @Override
    public StudentDTO updateStudent(
            Long id,
            StudentDTO studentDTO
    ) {


        validateId(id);



        if (studentDTO == null) {

            throw new InvalidInputException(
                    "Student DTO cannot be null"
            );

        }




        Student existing =
                studentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Student", id));




        existing.setName(
                studentDTO.getName()
        );


        existing.setEmail(
                studentDTO.getEmail()
        );



        return toDTO(
                studentRepository.save(existing)
        );

    }




    @Override
    public void deleteStudent(Long id) {


        validateId(id);



        Student student =
                studentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Student", id));



        studentRepository.delete(student);

    }




    private void validateId(Long id) {


        if (id == null || id <= 0) {

            throw new InvalidInputException(
                    "Invalid id"
            );

        }

    }


}
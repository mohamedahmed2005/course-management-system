package org.example.coursemanagement.ServiceImplementation;

import org.example.coursemanagement.DTO.InstructorDTO;
import org.example.coursemanagement.Entity.Instructor;
import org.example.coursemanagement.Exception.InvalidInputException;
import org.example.coursemanagement.Exception.ResourceNotFoundException;
import org.example.coursemanagement.Repository.InstructorRepository;
import org.example.coursemanagement.Service.InstructorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class InstructorServiceImplementation implements InstructorService {


    private final InstructorRepository instructorRepository;


    public InstructorServiceImplementation(
            InstructorRepository instructorRepository
    ) {
        this.instructorRepository = instructorRepository;
    }




    // Mapping


    private InstructorDTO toDTO(Instructor instructor) {

        if (instructor == null) {
            throw new InvalidInputException(
                    "Instructor cannot be null"
            );
        }


        return new InstructorDTO(
                instructor.getId(),
                instructor.getName(),
                instructor.getSpecialization()
        );
    }




    private Instructor toEntity(InstructorDTO dto) {


        if (dto == null) {

            throw new InvalidInputException(
                    "Instructor DTO cannot be null"
            );
        }



        Instructor instructor = new Instructor();

        instructor.setName(dto.getName());
        instructor.setSpecialization(dto.getSpecialization());


        return instructor;
    }




    @Override
    public InstructorDTO addInstructor(InstructorDTO instructorDTO) {


        Instructor instructor =
                toEntity(instructorDTO);



        return toDTO(
                instructorRepository.save(instructor)
        );

    }




    @Override
    public Page<InstructorDTO> getAllInstructors(Pageable pageable) {


        if (pageable == null) {

            throw new InvalidInputException(
                    "Pageable cannot be null"
            );
        }



        return instructorRepository
                .findAll(pageable)
                .map(this::toDTO);

    }




    @Override
    public InstructorDTO getInstructorById(Long id) {


        validateId(id);



        Instructor instructor =
                instructorRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Instructor", id));



        return toDTO(instructor);

    }




    @Override
    public InstructorDTO updateInstructor(
            Long id,
            InstructorDTO instructorDTO
    ) {


        validateId(id);



        if (instructorDTO == null) {

            throw new InvalidInputException(
                    "Instructor DTO cannot be null"
            );
        }



        Instructor existing =
                instructorRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Instructor", id));




        existing.setName(
                instructorDTO.getName()
        );


        existing.setSpecialization(
                instructorDTO.getSpecialization()
        );



        return toDTO(
                instructorRepository.save(existing)
        );

    }




    @Override
    public void deleteInstructor(Long id) {


        validateId(id);



        Instructor instructor =
                instructorRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Instructor", id));



        instructorRepository.delete(instructor);

    }




    private void validateId(Long id) {


        if (id == null || id <= 0) {

            throw new InvalidInputException(
                    "Invalid id"
            );
        }

    }


}
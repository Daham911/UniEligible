package lk.ac.ou.unieligible.service;

import lk.ac.ou.unieligible.dto.StudentDto;
import lk.ac.ou.unieligible.entity.Programme;
import lk.ac.ou.unieligible.entity.Student;
import lk.ac.ou.unieligible.exception.ResourceNotFoundException;
import lk.ac.ou.unieligible.repository.ProgrammeRepository;
import lk.ac.ou.unieligible.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final ProgrammeRepository programmeRepository;

    public StudentService(StudentRepository studentRepository, ProgrammeRepository programmeRepository) {
        this.studentRepository = studentRepository;
        this.programmeRepository = programmeRepository;
    }

    @Transactional(readOnly = true)
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Student getStudentEntity(String studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
    }

    @Transactional(readOnly = true)
    public StudentDto getStudentById(String studentId) {
        return toDto(getStudentEntity(studentId));
    }

    @Transactional
    public StudentDto createOrUpdateStudent(StudentDto dto) {
        Programme programme = programmeRepository.findById(dto.getProgrammeId() != null ? dto.getProgrammeId() : "BSEHONS")
                .orElseGet(() -> programmeRepository.findAll().stream().findFirst().orElse(null));

        Student student = studentRepository.findById(dto.getId()).orElse(new Student());
        student.setId(dto.getId());
        student.setRegistrationNumber(dto.getRegistrationNumber());
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setProgramme(programme);
        student.setCurrentAcademicYear(dto.getCurrentAcademicYear() != null ? dto.getCurrentAcademicYear() : 1);
        student.setCurrentAcademicLevel(dto.getCurrentAcademicLevel() != null ? dto.getCurrentAcademicLevel() : 3);

        Student saved = studentRepository.save(student);
        return toDto(saved);
    }

    public StudentDto toDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getRegistrationNumber(),
                student.getFullName(),
                student.getEmail(),
                student.getProgramme() != null ? student.getProgramme().getId() : "BSEHONS",
                student.getProgramme() != null ? student.getProgramme().getName() : "Bachelor of Software Engineering Honours",
                student.getCurrentAcademicYear(),
                student.getCurrentAcademicLevel()
        );
    }
}


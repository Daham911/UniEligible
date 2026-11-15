package lk.ac.ou.unieligible.service;

import lk.ac.ou.unieligible.dto.*;
import lk.ac.ou.unieligible.entity.*;
import lk.ac.ou.unieligible.exception.ResourceNotFoundException;
import lk.ac.ou.unieligible.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcademicResultService {

    private final StudentAcademicRecordRepository recordRepository;
    private final StudentExemptionRepository exemptionRepository;
    private final ConcurrentRegistrationRepository concurrentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public AcademicResultService(StudentAcademicRecordRepository recordRepository,
                                 StudentExemptionRepository exemptionRepository,
                                 ConcurrentRegistrationRepository concurrentRepository,
                                 StudentRepository studentRepository,
                                 CourseRepository courseRepository) {
        this.recordRepository = recordRepository;
        this.exemptionRepository = exemptionRepository;
        this.concurrentRepository = concurrentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public List<StudentAcademicRecordDto> getStudentResults(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        return recordRepository.findByStudentId(student.getId()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentAcademicRecordDto updateCourseResult(String studentId, String courseCode, UpdateResultRequest req) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        Course course = courseRepository.findById(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with code: " + courseCode));

        StudentAcademicRecord record = recordRepository.findByStudentIdAndCourseCode(studentId, courseCode)
                .orElse(new StudentAcademicRecord(student, course, StudentAcademicRecord.ResultStatus.NOT_ATTEMPTED, req.getAcademicYearTaken()));

        StudentAcademicRecord.ResultStatus newStatus = StudentAcademicRecord.ResultStatus.valueOf(req.getResultStatus());
        record.setResultStatus(newStatus);
        if (req.getAcademicYearTaken() != null) {
            record.setAcademicYearTaken(req.getAcademicYearTaken());
        }
        record.setUpdatedAt(LocalDateTime.now());

        StudentAcademicRecord saved = recordRepository.save(record);
        return toDto(saved);
    }

    @Transactional
    public List<StudentAcademicRecordDto> batchUpdateResults(String studentId, BatchUpdateResultsRequest req) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        if (req.getResults() != null) {
            for (BatchUpdateResultsRequest.ResultItem item : req.getResults()) {
                Course course = courseRepository.findById(item.getCourseCode())
                        .orElseThrow(() -> new ResourceNotFoundException("Course not found with code: " + item.getCourseCode()));

                StudentAcademicRecord record = recordRepository.findByStudentIdAndCourseCode(studentId, item.getCourseCode())
                        .orElse(new StudentAcademicRecord(student, course, StudentAcademicRecord.ResultStatus.NOT_ATTEMPTED, item.getAcademicYearTaken()));

                record.setResultStatus(StudentAcademicRecord.ResultStatus.valueOf(item.getResultStatus()));
                if (item.getAcademicYearTaken() != null) {
                    record.setAcademicYearTaken(item.getAcademicYearTaken());
                }
                record.setUpdatedAt(LocalDateTime.now());
                recordRepository.save(record);
            }
        }
        return getStudentResults(studentId);
    }

    @Transactional
    public StudentExemptionDto recordExemption(String studentId, StudentExemptionDto dto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        StudentExemption exemption = new StudentExemption(
                student,
                dto.getCourseCode(),
                dto.getCourseName(),
                dto.getCategoryCode(),
                dto.getLevel(),
                dto.getCreditValue(),
                dto.getExemptionReference(),
                dto.getVerified() != null ? dto.getVerified() : true
        );
        if (dto.getGrantedDate() != null) {
            exemption.setGrantedDate(dto.getGrantedDate());
        } else {
            exemption.setGrantedDate(LocalDate.now());
        }

        StudentExemption saved = exemptionRepository.save(exemption);
        dto.setId(saved.getId());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<StudentExemptionDto> getExemptions(String studentId) {
        return exemptionRepository.findByStudentId(studentId).stream().map(ex -> {
            StudentExemptionDto dto = new StudentExemptionDto();
            dto.setId(ex.getId());
            dto.setCourseCode(ex.getCourseCode());
            dto.setCourseName(ex.getCourseName());
            dto.setCategoryCode(ex.getCategoryCode());
            dto.setLevel(ex.getLevel());
            dto.setCreditValue(ex.getCreditValue());
            dto.setExemptionReference(ex.getExemptionReference());
            dto.setGrantedDate(ex.getGrantedDate());
            dto.setVerified(ex.getVerified());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public ConcurrentRegistrationDto recordConcurrentRegistration(String studentId, ConcurrentRegistrationDto dto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        String session = dto.getAcademicSession() != null ? dto.getAcademicSession() : "2026/2027";
        ConcurrentRegistration cr = concurrentRepository
                .findByStudentIdAndCourseCodeAndAcademicSession(studentId, dto.getCourseCode(), session)
                .orElse(new ConcurrentRegistration(student, dto.getCourseCode(), session, dto.getActive()));

        cr.setActive(dto.getActive() != null ? dto.getActive() : true);
        ConcurrentRegistration saved = concurrentRepository.save(cr);

        dto.setId(saved.getId());
        dto.setRegisteredAt(saved.getRegisteredAt());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<ConcurrentRegistrationDto> getConcurrentRegistrations(String studentId) {
        return concurrentRepository.findByStudentIdAndActiveTrue(studentId).stream().map(cr -> {
            ConcurrentRegistrationDto dto = new ConcurrentRegistrationDto();
            dto.setId(cr.getId());
            dto.setCourseCode(cr.getCourseCode());
            dto.setAcademicSession(cr.getAcademicSession());
            dto.setActive(cr.getActive());
            dto.setRegisteredAt(cr.getRegisteredAt());

            courseRepository.findById(cr.getCourseCode()).ifPresent(c -> dto.setCourseName(c.getName()));
            return dto;
        }).collect(Collectors.toList());
    }

    public StudentAcademicRecordDto toDto(StudentAcademicRecord record) {
        Course course = record.getCourse();
        return new StudentAcademicRecordDto(
                course != null ? course.getCode() : "",
                course != null ? course.getName() : "",
                record.getResultStatus().name(),
                course != null ? course.getLevel() : 3,
                course != null ? course.getCredits() : 0,
                course != null ? course.getAcademicYear() : 1,
                course != null ? course.getSemester() : "",
                course != null ? course.getCompulsory() : true,
                record.getAcademicYearTaken(),
                record.getUpdatedAt()
        );
    }
}


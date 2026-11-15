package lk.ac.ou.unieligible.service;

import lk.ac.ou.unieligible.dto.EligibilityReasonItemDto;
import lk.ac.ou.unieligible.dto.EligibilityResultDto;
import lk.ac.ou.unieligible.eligibility.PrerequisiteEvaluationService;
import lk.ac.ou.unieligible.entity.*;
import lk.ac.ou.unieligible.exception.ResourceNotFoundException;
import lk.ac.ou.unieligible.repository.CourseRepository;
import lk.ac.ou.unieligible.repository.EligibilityCheckRepository;
import lk.ac.ou.unieligible.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EligibilityService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final PrerequisiteEvaluationService prerequisiteEvaluationService;
    private final EligibilityCheckRepository eligibilityCheckRepository;

    public EligibilityService(StudentRepository studentRepository,
                              CourseRepository courseRepository,
                              PrerequisiteEvaluationService prerequisiteEvaluationService,
                              EligibilityCheckRepository eligibilityCheckRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.prerequisiteEvaluationService = prerequisiteEvaluationService;
        this.eligibilityCheckRepository = eligibilityCheckRepository;
    }

    @Transactional
    public List<EligibilityResultDto> calculateAllEligibility(String studentId, Integer academicYear) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        List<Course> courses;
        if (academicYear != null && academicYear >= 1 && academicYear <= 4) {
            courses = courseRepository.findByAcademicYear(academicYear);
        } else {
            courses = courseRepository.findAllByOrderByAcademicYearAscSemesterAscCodeAsc();
        }

        List<EligibilityResultDto> results = new ArrayList<>();
        for (Course course : courses) {
            EligibilityResultDto dto = prerequisiteEvaluationService.evaluateEligibility(student, course);
            results.add(dto);
            saveEligibilityCheckAudit(studentId, course.getCode(), dto);
        }
        return results;
    }

    @Transactional
    public EligibilityResultDto checkCourseEligibility(String studentId, String courseCode) {
        return checkSingleCourseEligibility(studentId, courseCode);
    }

    @Transactional
    public EligibilityResultDto checkSingleCourseEligibility(String studentId, String courseCode) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        Course course = courseRepository.findById(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with code: " + courseCode));

        EligibilityResultDto dto = prerequisiteEvaluationService.evaluateEligibility(student, course);
        saveEligibilityCheckAudit(studentId, courseCode, dto);
        return dto;
    }

    private void saveEligibilityCheckAudit(String studentId, String courseCode, EligibilityResultDto dto) {
        EligibilityCheck check = eligibilityCheckRepository.findByStudentIdAndCourseCode(studentId, courseCode)
                .orElse(new EligibilityCheck(studentId, courseCode, EligibilityCheck.EligibilityStatus.valueOf(dto.getStatus())));

        check.setStatus(EligibilityCheck.EligibilityStatus.valueOf(dto.getStatus()));
        check.setCheckedAt(LocalDateTime.now());
        check.getReasons().clear();

        if (dto.getDetailedReasons() != null) {
            for (EligibilityReasonItemDto item : dto.getDetailedReasons()) {
                check.addReason(new EligibilityCheckReason(item.getRuleDescription(), item.getReasonMessage(), item.getSatisfied()));
            }
        }

        eligibilityCheckRepository.save(check);
    }
}

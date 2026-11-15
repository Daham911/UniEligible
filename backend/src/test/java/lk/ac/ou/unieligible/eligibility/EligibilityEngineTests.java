package lk.ac.ou.unieligible.eligibility;

import lk.ac.ou.unieligible.dto.EligibilityResultDto;
import lk.ac.ou.unieligible.entity.*;
import lk.ac.ou.unieligible.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EligibilityEngineTests {

    @Autowired
    private PrerequisiteEvaluationService prerequisiteService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentAcademicRecordRepository recordRepository;

    @Autowired
    private StudentExemptionRepository exemptionRepository;

    @Autowired
    private ConcurrentRegistrationRepository concurrentRepository;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = studentRepository.findById("OUSL-SE-2026-001")
                .orElseGet(() -> {
                    Programme p = new Programme("BSEHONS", "Bachelor of Software Engineering Honours", "Faculty of Engineering", 125, 38, "2026/2027");
                    Student s = new Student("TEST-STUDENT-01", "S90000001", "Test Student", "test@ousl.lk", p, 1, 3);
                    return studentRepository.save(s);
                });

        recordRepository.deleteByStudentId(testStudent.getId());
    }

    @Test
    @DisplayName("Scenario 1: Student passes every required prerequisite and becomes eligible")
    void testStudentPassesAllPrerequisitesBecomesEligible() {
        Course eei3467 = courseRepository.findById("EEI3467").orElseThrow();
        Course mhz4359 = courseRepository.findById("MHZ4359").orElseThrow();
        Course mhz2250 = courseRepository.findById("MHZ2250").orElseThrow();
        Course mhz3356 = courseRepository.findById("MHZ3356").orElseThrow();

        // For MHZ4359: requires MHZ2250(P) and MHZ3356(CA)
        recordRepository.save(new StudentAcademicRecord(testStudent, mhz2250, StudentAcademicRecord.ResultStatus.PA, "2026/2027"));
        recordRepository.save(new StudentAcademicRecord(testStudent, mhz3356, StudentAcademicRecord.ResultStatus.CA, "2026/2027"));

        EligibilityResultDto result = prerequisiteService.evaluateEligibility(testStudent, mhz4359);
        assertTrue(result.getEligible(), "Course MHZ4359 should be eligible");
        assertEquals("ELIGIBLE", result.getStatus());
    }

    @Test
    @DisplayName("Scenario 2: Student fails a required prerequisite and is not eligible")
    void testStudentFailsPrerequisiteBecomesIneligible() {
        Course mhz4359 = courseRepository.findById("MHZ4359").orElseThrow();
        Course mhz2250 = courseRepository.findById("MHZ2250").orElseThrow();
        Course mhz3356 = courseRepository.findById("MHZ3356").orElseThrow();

        // Failed MHZ2250
        recordRepository.save(new StudentAcademicRecord(testStudent, mhz2250, StudentAcademicRecord.ResultStatus.FA, "2026/2027"));
        recordRepository.save(new StudentAcademicRecord(testStudent, mhz3356, StudentAcademicRecord.ResultStatus.CA, "2026/2027"));

        EligibilityResultDto result = prerequisiteService.evaluateEligibility(testStudent, mhz4359);
        assertFalse(result.getEligible(), "Course MHZ4359 should not be eligible");
        assertEquals("NOT_ELIGIBLE", result.getStatus());
        assertTrue(result.getReasons().stream().anyMatch(r -> r.contains("MHZ2250 was failed")));
    }

    @Test
    @DisplayName("Scenario 3: Student has a CA result for a CA prerequisite")
    void testStudentHasCAResultForCAPrerequisite() {
        Course eei4370 = courseRepository.findById("EEI4370").orElseThrow(); // Requires EEI3467(CA) and EEI3372(CA)
        Course eei3467 = courseRepository.findById("EEI3467").orElseThrow();
        Course eei3372 = courseRepository.findById("EEI3372").orElseThrow();

        recordRepository.save(new StudentAcademicRecord(testStudent, eei3467, StudentAcademicRecord.ResultStatus.CA, "2026/2027"));
        recordRepository.save(new StudentAcademicRecord(testStudent, eei3372, StudentAcademicRecord.ResultStatus.CA, "2026/2027"));

        EligibilityResultDto result = prerequisiteService.evaluateEligibility(testStudent, eei4370);
        assertTrue(result.getEligible(), "EEI4370 should be eligible with CA in prerequisites");
        assertEquals("ELIGIBLE", result.getStatus());
    }

    @Test
    @DisplayName("Scenario 4: Student has not entered a required result")
    void testStudentHasNotEnteredResult() {
        Course eei4370 = courseRepository.findById("EEI4370").orElseThrow(); // Requires EEI3467(CA) and EEI3372(CA)

        // No records entered
        EligibilityResultDto result = prerequisiteService.evaluateEligibility(testStudent, eei4370);
        assertFalse(result.getEligible());
        assertEquals("NOT_ELIGIBLE", result.getStatus());
        assertTrue(result.getReasons().stream().anyMatch(r -> r.contains("has not been attempted")));
    }

    @Test
    @DisplayName("Scenario 5 & 6: OR prerequisite with one satisfied alternative vs no satisfied alternative")
    void testOrPrerequisiteScenarios() {
        Course eei4360 = courseRepository.findById("EEI4360").orElseThrow(); // Requires (MHZ3356(CA) OR MHZ4359(CA)) AND EEI3372(CA)
        Course mhz3356 = courseRepository.findById("MHZ3356").orElseThrow();
        Course eei3372 = courseRepository.findById("EEI3372").orElseThrow();

        // 1. One satisfied alternative: student has MHZ3356(CA) and EEI3372(CA)
        recordRepository.save(new StudentAcademicRecord(testStudent, mhz3356, StudentAcademicRecord.ResultStatus.CA, "2026/2027"));
        recordRepository.save(new StudentAcademicRecord(testStudent, eei3372, StudentAcademicRecord.ResultStatus.CA, "2026/2027"));

        EligibilityResultDto resultSatisfied = prerequisiteService.evaluateEligibility(testStudent, eei4360);
        assertTrue(resultSatisfied.getEligible(), "EEI4360 should be eligible when one OR alternative is satisfied");

        // 2. No satisfied alternative for the OR condition: delete MHZ3356 record so neither MHZ3356 nor MHZ4359 is satisfied
        recordRepository.findByStudentIdAndCourseCode(testStudent.getId(), "MHZ3356")
                .ifPresent(r -> {
                    recordRepository.delete(r);
                    recordRepository.flush();
                });

        EligibilityResultDto resultUnsatisfied = prerequisiteService.evaluateEligibility(testStudent, eei4360);
        assertFalse(resultUnsatisfied.getEligible(), "EEI4360 should be ineligible when OR condition is unmet");
        assertTrue(resultUnsatisfied.getReasons().stream().anyMatch(r -> r.contains("Must satisfy at least one of the following alternatives")));
    }

    @Test
    @DisplayName("Scenario 7 & 8: Concurrent Registration (CR) satisfied vs missing")
    void testConcurrentRegistrationRequirement() {
        Course eei3347 = courseRepository.findById("EEI3347").orElseThrow(); // Requires EEI3346(CR)

        // Missing CR
        EligibilityResultDto resMissing = prerequisiteService.evaluateEligibility(testStudent, eei3347);
        assertFalse(resMissing.getEligible(), "EEI3347 should be ineligible when EEI3346(CR) is missing");

        // Satisfied CR
        concurrentRepository.save(new ConcurrentRegistration(testStudent, "EEI3346", "2026/2027", true));
        EligibilityResultDto resSatisfied = prerequisiteService.evaluateEligibility(testStudent, eei3347);
        assertTrue(resSatisfied.getEligible(), "EEI3347 should be eligible when EEI3346(CR) is active");
    }

    @Test
    @DisplayName("Scenario 17: Subject marked not offered cannot be recommended as available")
    void testSubjectNotOfferedCannotBeEligible() {
        Course eei5369 = courseRepository.findById("EEI5369").orElseThrow(); // Marked offered = false in 2026/2027

        EligibilityResultDto result = prerequisiteService.evaluateEligibility(testStudent, eei5369);
        assertFalse(result.getEligible());
        assertEquals("CURRENTLY_NOT_OFFERED", result.getStatus());
        assertTrue(result.getReasons().stream().anyMatch(r -> r.contains("not offered")));
    }
}

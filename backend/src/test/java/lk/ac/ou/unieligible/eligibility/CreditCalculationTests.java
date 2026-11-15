package lk.ac.ou.unieligible.eligibility;

import lk.ac.ou.unieligible.dto.CreditBreakdownDto;
import lk.ac.ou.unieligible.dto.EligibilityResultDto;
import lk.ac.ou.unieligible.dto.GraduationAuditDto;
import lk.ac.ou.unieligible.dto.UpdateResultRequest;
import lk.ac.ou.unieligible.entity.*;
import lk.ac.ou.unieligible.repository.*;
import lk.ac.ou.unieligible.service.AcademicResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CreditCalculationTests {

    @Autowired
    private CreditCalculationService creditService;

    @Autowired
    private GraduationAuditService auditService;

    @Autowired
    private PrerequisiteEvaluationService prerequisiteService;

    @Autowired
    private AcademicResultService academicResultService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentAcademicRecordRepository recordRepository;

    @Autowired
    private StudentExemptionRepository exemptionRepository;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = studentRepository.findById("OUSL-SE-2026-001")
                .orElseGet(() -> {
                    Programme p = new Programme("BSEHONS", "Bachelor of Software Engineering Honours", "Faculty of Engineering", 125, 38, "2026/2027");
                    Student s = new Student("TEST-STUDENT-02", "S90000002", "Credit Test Student", "credit.test@ousl.lk", p, 1, 3);
                    return studentRepository.save(s);
                });

        recordRepository.deleteByStudentId(testStudent.getId());
    }

    @Test
    @DisplayName("Scenario 9 & 10: Passed-credit threshold reached vs not reached")
    void testPassedCreditThreshold() {
        Course agm4367 = courseRepository.findById("AGM4367").orElseThrow(); // Requires 18 passed credits at Level 3

        // Student has only 10 credits at Level 3: EEI3346 (3) + EEI3366 (3) + EEI3467 (4) = 10
        Course eei3346 = courseRepository.findById("EEI3346").orElseThrow();
        Course eei3366 = courseRepository.findById("EEI3366").orElseThrow();
        Course eei3467 = courseRepository.findById("EEI3467").orElseThrow();

        recordRepository.save(new StudentAcademicRecord(testStudent, eei3346, StudentAcademicRecord.ResultStatus.PA, "2026/2027"));
        recordRepository.save(new StudentAcademicRecord(testStudent, eei3366, StudentAcademicRecord.ResultStatus.PA, "2026/2027"));
        recordRepository.save(new StudentAcademicRecord(testStudent, eei3467, StudentAcademicRecord.ResultStatus.PA, "2026/2027"));

        EligibilityResultDto resBelow = prerequisiteService.evaluateEligibility(testStudent, agm4367);
        assertFalse(resBelow.getEligible(), "AGM4367 should not be eligible with only 10 credits at Level 3");
        assertTrue(resBelow.getReasons().stream().anyMatch(r -> r.contains("Student has only 10 passed credits at Level 3; 18 are required")));

        // Add more courses to reach >= 18 credits: EEI3372 (3), EEI3347 (3), MHZ3356 (3) = 10 + 9 = 19
        Course eei3372 = courseRepository.findById("EEI3372").orElseThrow();
        Course eei3347 = courseRepository.findById("EEI3347").orElseThrow();
        Course mhz3356 = courseRepository.findById("MHZ3356").orElseThrow();

        recordRepository.save(new StudentAcademicRecord(testStudent, eei3372, StudentAcademicRecord.ResultStatus.PA, "2026/2027"));
        recordRepository.save(new StudentAcademicRecord(testStudent, eei3347, StudentAcademicRecord.ResultStatus.PA, "2026/2027"));
        recordRepository.save(new StudentAcademicRecord(testStudent, mhz3356, StudentAcademicRecord.ResultStatus.PA, "2026/2027"));

        EligibilityResultDto resReached = prerequisiteService.evaluateEligibility(testStudent, agm4367);
        assertTrue(resReached.getEligible(), "AGM4367 should now be eligible with 19 passed credits at Level 3");
    }

    @Test
    @DisplayName("Scenario 11: CA credits are not incorrectly counted as earned final credits")
    void testCACreditsNotCountedAsEarnedCredits() {
        Course eei3346 = courseRepository.findById("EEI3346").orElseThrow(); // 3 credits
        Course eei3366 = courseRepository.findById("EEI3366").orElseThrow(); // 3 credits

        recordRepository.save(new StudentAcademicRecord(testStudent, eei3346, StudentAcademicRecord.ResultStatus.PA, "2026/2027"));
        recordRepository.save(new StudentAcademicRecord(testStudent, eei3366, StudentAcademicRecord.ResultStatus.CA, "2026/2027"));

        CreditBreakdownDto breakdown = creditService.calculateCreditBreakdown(testStudent.getId());
        assertEquals(3, breakdown.getTotalEarnedCredits(), "Only PA course should count as earned credits");
        assertEquals(3, breakdown.getTotalCACredits(), "CA course should be tracked under totalCACredits");
        assertEquals(3, breakdown.getTotalRecognizedCredits());
    }

    @Test
    @DisplayName("Scenario 12: MHZ2250 does not contribute to degree credit totals")
    void testMHZ2250DoesNotContributeToDegreeCredits() {
        Course mhz2250 = courseRepository.findById("MHZ2250").orElseThrow(); // Non-degree credit course
        Course eei3346 = courseRepository.findById("EEI3346").orElseThrow(); // 3 credits

        recordRepository.save(new StudentAcademicRecord(testStudent, mhz2250, StudentAcademicRecord.ResultStatus.PA, "2026/2027"));
        recordRepository.save(new StudentAcademicRecord(testStudent, eei3346, StudentAcademicRecord.ResultStatus.PA, "2026/2027"));

        CreditBreakdownDto breakdown = creditService.calculateCreditBreakdown(testStudent.getId());
        // Should only be 3 credits from EEI3346, MHZ2250 is degreeCredit = false
        assertEquals(3, breakdown.getTotalEarnedCredits(), "MHZ2250 credits must not count in degree totals");
        assertEquals(3, breakdown.getTotalRecognizedCredits());
    }

    @Test
    @DisplayName("Scenario 13: An exemption is not double-counted with an academic record")
    void testExemptionNotDoubleCounted() {
        Course eei3346 = courseRepository.findById("EEI3346").orElseThrow(); // 3 credits

        // Student has both exemption AND a PA record for EEI3346
        exemptionRepository.save(new StudentExemption(
                testStudent, "EEI3346", "Web Application Development", "I", 3, 3, "EX-001", true
        ));
        recordRepository.save(new StudentAcademicRecord(testStudent, eei3346, StudentAcademicRecord.ResultStatus.PA, "2026/2027"));

        CreditBreakdownDto breakdown = creditService.calculateCreditBreakdown(testStudent.getId());
        // Should be 3 credits total recognized, NOT 6!
        assertEquals(3, breakdown.getTotalRecognizedCredits(), "Course should not be double-counted if both exempted and passed");
    }

    @Test
    @DisplayName("Scenario 15: Level 6 elective credit requirements are calculated correctly")
    void testLevel6ElectiveCreditCalculation() {
        Course eei6377 = courseRepository.findById("EEI6377").orElseThrow(); // Level 6 elective (3 credits)
        Course eei6366 = courseRepository.findById("EEI6366").orElseThrow(); // Level 6 elective (3 credits)
        Course eei6369 = courseRepository.findById("EEI6369").orElseThrow(); // Level 6 elective (3 credits)

        recordRepository.save(new StudentAcademicRecord(testStudent, eei6377, StudentAcademicRecord.ResultStatus.PA, "2026/2027"));
        recordRepository.save(new StudentAcademicRecord(testStudent, eei6366, StudentAcademicRecord.ResultStatus.PA, "2026/2027"));
        recordRepository.save(new StudentAcademicRecord(testStudent, eei6369, StudentAcademicRecord.ResultStatus.PA, "2026/2027"));

        CreditBreakdownDto breakdown = creditService.calculateCreditBreakdown(testStudent.getId());
        assertEquals(9, breakdown.getLevel6ElectiveCredits(), "Level 6 elective credits should total 9");
        assertTrue(breakdown.getLevel6ElectiveCredits() >= 8, "Minimum Level 6 elective requirement of 8 credits reached");
    }

    @Test
    @DisplayName("Scenario 16: Graduation summary identifies missing category credits and conditions")
    void testGraduationSummaryIdentifiesMissingRequirements() {
        GraduationAuditDto audit = auditService.auditGraduation(testStudent);
        assertFalse(audit.getGraduationRequirementsSatisfied(), "Graduation should not be satisfied for fresh student");
        assertFalse(audit.getOutstandingConditions().isEmpty(), "Should list unfulfilled graduation conditions");
        assertTrue(audit.getOutstandingConditions().stream().anyMatch(c -> c.contains("Industrial Training")));
        assertTrue(audit.getOutstandingConditions().stream().anyMatch(c -> c.contains("Degree Total Credits")));
    }

    @Test
    @DisplayName("Scenario 18: Result update correctly refreshes cumulative credits and eligibility")
    void testResultUpdateRefreshesCumulativeCreditsAndEligibility() {
        Course mhz4359 = courseRepository.findById("MHZ4359").orElseThrow(); // Requires MHZ2250(P) and MHZ3356(CA)

        // Initially not eligible
        EligibilityResultDto initialRes = prerequisiteService.evaluateEligibility(testStudent, mhz4359);
        assertFalse(initialRes.getEligible());

        // Update results via AcademicResultService
        academicResultService.updateCourseResult(testStudent.getId(), "MHZ2250", new UpdateResultRequest("PA", "2026/2027"));
        academicResultService.updateCourseResult(testStudent.getId(), "MHZ3356", new UpdateResultRequest("CA", "2026/2027"));

        // Recalculate
        EligibilityResultDto updatedRes = prerequisiteService.evaluateEligibility(testStudent, mhz4359);
        assertTrue(updatedRes.getEligible(), "Course should become eligible after result update");

        CreditBreakdownDto breakdown = creditService.calculateCreditBreakdown(testStudent.getId());
        // MHZ3356 is CA (3 CA credits), MHZ2250 is Level 2 non-credit (0 degree credits)
        assertEquals(3, breakdown.getTotalCACredits());
    }
}


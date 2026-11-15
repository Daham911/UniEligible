package lk.ac.ou.unieligible.controller;

import jakarta.validation.Valid;
import lk.ac.ou.unieligible.dto.*;
import lk.ac.ou.unieligible.eligibility.CreditCalculationService;
import lk.ac.ou.unieligible.eligibility.GraduationAuditService;
import lk.ac.ou.unieligible.entity.Student;
import lk.ac.ou.unieligible.service.AcademicResultService;
import lk.ac.ou.unieligible.service.EligibilityService;
import lk.ac.ou.unieligible.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;
    private final AcademicResultService resultService;
    private final EligibilityService eligibilityService;
    private final CreditCalculationService creditService;
    private final GraduationAuditService graduationAuditService;

    public StudentController(StudentService studentService,
                             AcademicResultService resultService,
                             EligibilityService eligibilityService,
                             CreditCalculationService creditService,
                             GraduationAuditService graduationAuditService) {
        this.studentService = studentService;
        this.resultService = resultService;
        this.eligibilityService = eligibilityService;
        this.creditService = creditService;
        this.graduationAuditService = graduationAuditService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDto> getStudentProfile(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }

    @PostMapping
    public ResponseEntity<StudentDto> createOrUpdateStudent(@Valid @RequestBody StudentDto dto) {
        return ResponseEntity.ok(studentService.createOrUpdateStudent(dto));
    }

    @GetMapping("/{studentId}/results")
    public ResponseEntity<List<StudentAcademicRecordDto>> getStudentResults(@PathVariable String studentId) {
        return ResponseEntity.ok(resultService.getStudentResults(studentId));
    }

    @PutMapping("/{studentId}/results/{courseCode}")
    public ResponseEntity<StudentAcademicRecordDto> updateCourseResult(
            @PathVariable String studentId,
            @PathVariable String courseCode,
            @Valid @RequestBody UpdateResultRequest req) {
        return ResponseEntity.ok(resultService.updateCourseResult(studentId, courseCode, req));
    }

    @PostMapping("/{studentId}/results/batch")
    public ResponseEntity<List<StudentAcademicRecordDto>> batchUpdateResults(
            @PathVariable String studentId,
            @RequestBody BatchUpdateResultsRequest req) {
        return ResponseEntity.ok(resultService.batchUpdateResults(studentId, req));
    }

    @PostMapping("/{studentId}/exemptions")
    public ResponseEntity<StudentExemptionDto> addExemption(
            @PathVariable String studentId,
            @Valid @RequestBody StudentExemptionDto dto) {
        return ResponseEntity.ok(resultService.recordExemption(studentId, dto));
    }

    @GetMapping("/{studentId}/exemptions")
    public ResponseEntity<List<StudentExemptionDto>> getExemptions(@PathVariable String studentId) {
        return ResponseEntity.ok(resultService.getExemptions(studentId));
    }

    @PostMapping("/{studentId}/concurrent-registrations")
    public ResponseEntity<ConcurrentRegistrationDto> addConcurrentRegistration(
            @PathVariable String studentId,
            @Valid @RequestBody ConcurrentRegistrationDto dto) {
        return ResponseEntity.ok(resultService.recordConcurrentRegistration(studentId, dto));
    }

    @GetMapping("/{studentId}/concurrent-registrations")
    public ResponseEntity<List<ConcurrentRegistrationDto>> getConcurrentRegistrations(@PathVariable String studentId) {
        return ResponseEntity.ok(resultService.getConcurrentRegistrations(studentId));
    }

    @PostMapping("/{studentId}/eligibility/check")
    public ResponseEntity<EligibilityResultDto> checkSingleEligibility(
            @PathVariable String studentId,
            @RequestParam String courseCode) {
        return ResponseEntity.ok(eligibilityService.checkCourseEligibility(studentId, courseCode));
    }

    @GetMapping("/{studentId}/eligibility")
    public ResponseEntity<List<EligibilityResultDto>> getStudentEligibility(
            @PathVariable String studentId,
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(eligibilityService.calculateAllEligibility(studentId, year));
    }

    @GetMapping("/{studentId}/progress")
    public ResponseEntity<CreditBreakdownDto> getStudentProgress(@PathVariable String studentId) {
        return ResponseEntity.ok(creditService.calculateCreditBreakdown(studentId));
    }

    @GetMapping("/{studentId}/graduation-audit")
    public ResponseEntity<GraduationAuditDto> getGraduationAudit(@PathVariable String studentId) {
        Student student = studentService.getStudentEntity(studentId);
        return ResponseEntity.ok(graduationAuditService.auditGraduation(student));
    }
}


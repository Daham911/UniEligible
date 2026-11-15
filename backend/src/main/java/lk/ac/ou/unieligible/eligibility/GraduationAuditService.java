package lk.ac.ou.unieligible.eligibility;

import lk.ac.ou.unieligible.dto.*;
import lk.ac.ou.unieligible.entity.*;
import lk.ac.ou.unieligible.repository.CourseCategoryRepository;
import lk.ac.ou.unieligible.repository.GraduationRequirementRepository;
import lk.ac.ou.unieligible.repository.StudentAcademicRecordRepository;
import lk.ac.ou.unieligible.repository.StudentExemptionRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraduationAuditService {

    private final CreditCalculationService creditService;
    private final StudentAcademicRecordRepository recordRepository;
    private final StudentExemptionRepository exemptionRepository;
    private final CourseCategoryRepository categoryRepository;
    private final GraduationRequirementRepository requirementRepository;

    public GraduationAuditService(CreditCalculationService creditService,
                                  StudentAcademicRecordRepository recordRepository,
                                  StudentExemptionRepository exemptionRepository,
                                  CourseCategoryRepository categoryRepository,
                                  GraduationRequirementRepository requirementRepository) {
        this.creditService = creditService;
        this.recordRepository = recordRepository;
        this.exemptionRepository = exemptionRepository;
        this.categoryRepository = categoryRepository;
        this.requirementRepository = requirementRepository;
    }

    public GraduationAuditDto auditGraduation(Student student) {
        GraduationAuditDto audit = new GraduationAuditDto();
        audit.setStudentId(student.getId());
        audit.setStudentName(student.getFullName());
        audit.setProgrammeName(student.getProgramme() != null ? student.getProgramme().getName() : "BSEHons");

        CreditBreakdownDto breakdown = creditService.calculateCreditBreakdown(student.getId());
        audit.setTotalRecognizedCredits(breakdown.getTotalRecognizedCredits());
        audit.setTargetTotalCredits(125);

        Map<Integer, Integer> levelCreds = breakdown.getCreditsByLevel();
        Map<String, Integer> displayLevelCredits = new HashMap<>();
        displayLevelCredits.put("Level 3", levelCreds.getOrDefault(3, 0));
        displayLevelCredits.put("Level 4", levelCreds.getOrDefault(4, 0));
        displayLevelCredits.put("Level 5", levelCreds.getOrDefault(5, 0));
        displayLevelCredits.put("Level 6", levelCreds.getOrDefault(6, 0));
        audit.setLevelCredits(displayLevelCredits);

        List<GraduationRequirementCheckDto> checks = new ArrayList<>();
        List<String> outstanding = new ArrayList<>();
        boolean allSatisfied = true;

        // 1. Minimum Total Degree Credits
        int totalRecognized = breakdown.getTotalRecognizedCredits();
        boolean totalSatisfied = totalRecognized >= 125;
        if (!totalSatisfied) {
            allSatisfied = false;
            outstanding.add("Degree Total Credits: " + totalRecognized + "/125 (needs " + (125 - totalRecognized) + " more)");
        }
        checks.add(new GraduationRequirementCheckDto(
                "MIN_TOTAL_CREDITS",
                "Minimum Total Degree Credits",
                "Minimum 125 total recognized credits for BSEHons degree graduation.",
                125,
                totalRecognized,
                totalSatisfied,
                "OUSL BSEHons Guidebook 2026/2027 Section 4.1"
        ));

        // 2. Levels 5 and 6 Credits
        int l5l6 = levelCreds.getOrDefault(5, 0) + levelCreds.getOrDefault(6, 0);
        boolean l5l6Satisfied = l5l6 >= 63;
        if (!l5l6Satisfied) {
            allSatisfied = false;
            outstanding.add("Levels 5 & 6 Credits: " + l5l6 + "/63 (needs " + (63 - l5l6) + " more)");
        }
        checks.add(new GraduationRequirementCheckDto(
                "MIN_L5_L6_CREDITS",
                "Minimum Levels 5 and 6 Credits",
                "At least 63 credits completed at Levels 5 and 6.",
                63,
                l5l6,
                l5l6Satisfied,
                "OUSL BSEHons Guidebook 2026/2027 Table 1"
        ));

        // 3. Level 6 Credits
        int l6 = levelCreds.getOrDefault(6, 0);
        boolean l6Satisfied = l6 >= 30;
        if (!l6Satisfied) {
            allSatisfied = false;
            outstanding.add("Level 6 Credits: " + l6 + "/30 (needs " + (30 - l6) + " more)");
        }
        checks.add(new GraduationRequirementCheckDto(
                "MIN_L6_CREDITS",
                "Minimum Level 6 Credits",
                "At least 30 credits completed at Level 6.",
                30,
                l6,
                l6Satisfied,
                "OUSL BSEHons Guidebook 2026/2027 Table 1"
        ));

        // 4. Level 6 Elective Credits
        int l6Elective = breakdown.getLevel6ElectiveCredits();
        boolean l6ElectiveSatisfied = l6Elective >= 8;
        if (!l6ElectiveSatisfied) {
            allSatisfied = false;
            outstanding.add("Level 6 Elective Credits: " + l6Elective + "/8 (needs " + (8 - l6Elective) + " more)");
        }
        checks.add(new GraduationRequirementCheckDto(
                "MIN_L6_ELECTIVE_CREDITS",
                "Minimum Level 6 Elective Credits",
                "At least 8 elective credits selected from Level 6 courses.",
                8,
                l6Elective,
                l6ElectiveSatisfied,
                "OUSL BSEHons Guidebook 2026/2027 Year 4 Electives Note"
        ));

        // 5. Project Category Level 6 Credits (at least 6 project credits at Level 6, e.g. EER6689)
        int projectL6Credits = calculateCategoryLevelCredits(student.getId(), "R", 6);
        boolean projectL6Satisfied = projectL6Credits >= 6;
        if (!projectL6Satisfied) {
            allSatisfied = false;
            outstanding.add("Level 6 Project Credits: " + projectL6Credits + "/6 (needs " + (6 - projectL6Credits) + " more)");
        }
        checks.add(new GraduationRequirementCheckDto(
                "MIN_L6_PROJECT_CREDITS",
                "Level 6 Project Requirement",
                "At least 6 project credits must be at Level 6 (Final Project EER6689).",
                6,
                projectL6Credits,
                projectL6Satisfied,
                "OUSL BSEHons Guidebook 2026/2027 Section 4.2"
        ));

        // 6. Industrial Training Module (EEW5611)
        boolean itCompleted = isCoursePassedOrExempted(student.getId(), "EEW5611");
        if (!itCompleted) {
            allSatisfied = false;
            outstanding.add("Industrial Training (EEW5611): Not yet completed");
        }
        checks.add(new GraduationRequirementCheckDto(
                "INDUSTRIAL_TRAINING_COMPLETION",
                "Industrial Training Completion",
                "Successful completion of Industrial Training module (EEW5611 - 6 credits).",
                6,
                itCompleted ? 6 : 0,
                itCompleted,
                "OUSL BSEHons Guidebook 2026/2027 Level 5 Curriculum"
        ));

        // 7. Additional Successful Completion Conditions stated in Guidebook
        // Condition: At least 15 credits at Level 6
        boolean completionL6_15 = l6 >= 15;
        checks.add(new GraduationRequirementCheckDto(
                "COMPLETION_L6_15",
                "Successful Completion Condition (Level 6)",
                "Minimum 15 successfully completed credits at Level 6.",
                15,
                l6,
                completionL6_15,
                "OUSL BSEHons Guidebook Section 4.3"
        ));
        if (!completionL6_15) {
            allSatisfied = false;
            outstanding.add("Completion Condition: At least 15 credits at Level 6");
        }

        // Condition: At least 32 credits at Levels 5 and 6
        boolean completionL5L6_32 = l5l6 >= 32;
        checks.add(new GraduationRequirementCheckDto(
                "COMPLETION_L5_L6_32",
                "Successful Completion Condition (Levels 5 & 6)",
                "Minimum 32 successfully completed credits at Levels 5 and 6.",
                32,
                l5l6,
                completionL5L6_32,
                "OUSL BSEHons Guidebook Section 4.3"
        ));
        if (!completionL5L6_32) {
            allSatisfied = false;
            outstanding.add("Completion Condition: At least 32 credits at Levels 5 and 6");
        }

        // Condition: At least 63 credits from Levels 3–6
        int l3to6 = levelCreds.getOrDefault(3, 0) + levelCreds.getOrDefault(4, 0) + l5l6;
        boolean completionL3L6_63 = l3to6 >= 63;
        checks.add(new GraduationRequirementCheckDto(
                "COMPLETION_L3_L6_63",
                "Successful Completion Condition (Levels 3–6)",
                "Minimum 63 successfully completed credits across Levels 3 to 6.",
                63,
                l3to6,
                completionL3L6_63,
                "OUSL BSEHons Guidebook Section 4.3"
        ));
        if (!completionL3L6_63) {
            allSatisfied = false;
            outstanding.add("Completion Condition: At least 63 credits from Levels 3–6");
        }

        // Category-wise Breakdown and Verification
        List<CategoryCreditProgressDto> catList = new ArrayList<>(breakdown.getCreditsByCategory().values());
        catList.sort(Comparator.comparing(CategoryCreditProgressDto::getCategoryCode));
        audit.setCategoryRequirements(catList);

        for (CategoryCreditProgressDto cat : catList) {
            if (!Boolean.TRUE.equals(cat.getSatisfied())) {
                allSatisfied = false;
                outstanding.add("Category " + cat.getCategoryName() + " (" + cat.getCategoryCode() + "): " +
                        cat.getTotalRecognizedCredits() + "/" + cat.getMinRequired() + " credits required");
            }
        }

        audit.setRequirements(checks);
        audit.setOutstandingConditions(outstanding);
        audit.setGraduationRequirementsSatisfied(allSatisfied);

        if (allSatisfied) {
            audit.setStatusMessage("Graduation Requirements Satisfied. The student has met all academic conditions for BSEHons.");
        } else {
            audit.setStatusMessage("Graduation Requirements In Progress. " + outstanding.size() + " requirement(s) remaining.");
        }

        return audit;
    }

    private int calculateCategoryLevelCredits(String studentId, String categoryCode, int level) {
        List<StudentAcademicRecord> records = recordRepository.findByStudentId(studentId);
        List<StudentExemption> exemptions = exemptionRepository.findByStudentId(studentId);

        int total = 0;
        Set<String> counted = new HashSet<>();

        for (StudentExemption ex : exemptions) {
            if (Boolean.TRUE.equals(ex.getVerified()) && categoryCode.equalsIgnoreCase(ex.getCategoryCode()) && ex.getLevel() != null && ex.getLevel() == level) {
                total += (ex.getCreditValue() != null ? ex.getCreditValue() : 0);
                counted.add(ex.getCourseCode());
            }
        }

        for (StudentAcademicRecord rec : records) {
            if (rec.getResultStatus() == StudentAcademicRecord.ResultStatus.PA) {
                Course course = rec.getCourse();
                if (course != null && Boolean.TRUE.equals(course.getDegreeCredit())
                        && course.getLevel() != null && course.getLevel() == level
                        && course.getCategory() != null && categoryCode.equalsIgnoreCase(course.getCategory().getCode())
                        && !counted.contains(course.getCode())) {
                    total += (course.getCredits() != null ? course.getCredits() : 0);
                    counted.add(course.getCode());
                }
            }
        }
        return total;
    }

    private boolean isCoursePassedOrExempted(String studentId, String courseCode) {
        boolean passedInRecord = recordRepository.findByStudentIdAndCourseCode(studentId, courseCode)
                .map(r -> r.getResultStatus() == StudentAcademicRecord.ResultStatus.PA)
                .orElse(false);
        if (passedInRecord) return true;

        return exemptionRepository.findByStudentId(studentId).stream()
                .anyMatch(e -> e.getCourseCode().equalsIgnoreCase(courseCode) && Boolean.TRUE.equals(e.getVerified()));
    }
}


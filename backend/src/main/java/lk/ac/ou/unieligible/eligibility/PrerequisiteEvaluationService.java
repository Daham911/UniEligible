package lk.ac.ou.unieligible.eligibility;

import lk.ac.ou.unieligible.dto.EligibilityReasonItemDto;
import lk.ac.ou.unieligible.dto.EligibilityResultDto;
import lk.ac.ou.unieligible.entity.*;
import lk.ac.ou.unieligible.repository.ConcurrentRegistrationRepository;
import lk.ac.ou.unieligible.repository.StudentAcademicRecordRepository;
import lk.ac.ou.unieligible.repository.StudentExemptionRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PrerequisiteEvaluationService {

    private final StudentAcademicRecordRepository recordRepository;
    private final StudentExemptionRepository exemptionRepository;
    private final ConcurrentRegistrationRepository concurrentRepository;
    private final CreditCalculationService creditService;

    // Configurable academic policy: PA satisfies CA prerequisite
    private boolean paSatisfiesCa = true;

    public PrerequisiteEvaluationService(StudentAcademicRecordRepository recordRepository,
                                         StudentExemptionRepository exemptionRepository,
                                         ConcurrentRegistrationRepository concurrentRepository,
                                         CreditCalculationService creditService) {
        this.recordRepository = recordRepository;
        this.exemptionRepository = exemptionRepository;
        this.concurrentRepository = concurrentRepository;
        this.creditService = creditService;
    }

    public EligibilityResultDto evaluateEligibility(Student student, Course course) {
        EligibilityResultDto result = new EligibilityResultDto();
        result.setCourseCode(course.getCode());
        result.setCourseName(course.getName());
        result.setAcademicYear(course.getAcademicYear());
        result.setLevel(course.getLevel());
        result.setCredits(course.getCredits());
        result.setSemester(course.getSemester());
        result.setCompulsory(course.getCompulsory());
        result.setOffered(course.getOffered());
        result.setPrerequisiteDisplayText(course.getPrerequisiteDisplayText());
        result.setDeliveryCenter(course.getDeliveryCenter());
        result.setSpecialRestriction(course.getSpecialRestriction());
        result.setDiscrepancyFlag(course.getDiscrepancyFlag());

        // Check if course is currently offered
        if (Boolean.FALSE.equals(course.getOffered())) {
            result.setStatus("CURRENTLY_NOT_OFFERED");
            result.setEligible(false);
            result.getReasons().add("This course is currently not offered in the 2026/2027 academic year.");
            result.getDetailedReasons().add(new EligibilityReasonItemDto(
                    "Course Offering Availability",
                    "Course is not offered in the current 2026/2027 curriculum session.",
                    false
            ));
            return result;
        }

        List<StudentAcademicRecord> studentRecords = recordRepository.findByStudentId(student.getId());
        List<StudentExemption> exemptions = exemptionRepository.findByStudentId(student.getId());
        List<ConcurrentRegistration> concurrentRegs = concurrentRepository.findByStudentIdAndActiveTrue(student.getId());

        Map<String, StudentAcademicRecord.ResultStatus> resultsMap = new HashMap<>();
        for (StudentAcademicRecord rec : studentRecords) {
            if (rec.getCourse() != null) {
                resultsMap.put(rec.getCourse().getCode().toUpperCase(), rec.getResultStatus());
            }
        }

        Set<String> verifiedExemptions = new HashSet<>();
        for (StudentExemption ex : exemptions) {
            if (Boolean.TRUE.equals(ex.getVerified())) {
                verifiedExemptions.add(ex.getCourseCode().toUpperCase());
            }
        }

        Set<String> activeConcurrentCourses = new HashSet<>();
        for (ConcurrentRegistration cr : concurrentRegs) {
            activeConcurrentCourses.add(cr.getCourseCode().toUpperCase());
        }

        List<PrerequisiteRuleGroup> ruleGroups = course.getRuleGroups();
        if (ruleGroups == null || ruleGroups.isEmpty()) {
            // No prerequisites required
            result.setStatus("ELIGIBLE");
            result.setEligible(true);
            result.getReasons().add("No prerequisite conditions required. Fully eligible for registration.");
            result.getDetailedReasons().add(new EligibilityReasonItemDto(
                    "Prerequisite Requirements",
                    "None specified. Student is eligible to register.",
                    true
            ));
            return result;
        }

        boolean allGroupsSatisfied = true;
        boolean hasPendingInformation = false;
        List<String> failureReasons = new ArrayList<>();
        List<EligibilityReasonItemDto> detailedReasons = new ArrayList<>();

        for (PrerequisiteRuleGroup group : ruleGroups) {
            String op = group.getLogicalOperator() != null ? group.getLogicalOperator().toUpperCase() : "AND";
            List<PrerequisiteRule> rules = group.getRules();

            if (rules == null || rules.isEmpty()) continue;

            if ("OR".equalsIgnoreCase(op)) {
                boolean groupSatisfied = false;
                List<String> orOptionDescriptions = new ArrayList<>();
                boolean orGroupHasPending = false;

                for (PrerequisiteRule rule : rules) {
                    EvaluationResult eval = evaluateSingleRule(student, rule, resultsMap, verifiedExemptions, activeConcurrentCourses);
                    orOptionDescriptions.add(eval.description);
                    if (eval.satisfied) {
                        groupSatisfied = true;
                    }
                    if (eval.pendingInfo) {
                        orGroupHasPending = true;
                    }
                }

                if (groupSatisfied) {
                    detailedReasons.add(new EligibilityReasonItemDto(
                            group.getDescription() != null ? group.getDescription() : "OR Requirement",
                            "Satisfied one of the required alternatives: " + String.join(" OR ", orOptionDescriptions),
                            true
                    ));
                } else {
                    allGroupsSatisfied = false;
                    String reasonMsg = "Must satisfy at least one of the following alternatives: " + String.join(" OR ", orOptionDescriptions);
                    failureReasons.add(reasonMsg);
                    detailedReasons.add(new EligibilityReasonItemDto(
                            group.getDescription() != null ? group.getDescription() : "OR Requirement",
                            reasonMsg,
                            false
                    ));
                    if (orGroupHasPending) {
                        hasPendingInformation = true;
                    }
                }
            } else {
                // "AND" group: every rule must be satisfied
                for (PrerequisiteRule rule : rules) {
                    EvaluationResult eval = evaluateSingleRule(student, rule, resultsMap, verifiedExemptions, activeConcurrentCourses);
                    if (!eval.satisfied) {
                        allGroupsSatisfied = false;
                        failureReasons.add(eval.failureReason);
                    }
                    if (eval.pendingInfo) {
                        hasPendingInformation = true;
                    }
                    detailedReasons.add(new EligibilityReasonItemDto(
                            rule.getDescription() != null ? rule.getDescription() : (rule.getRuleType() + " Check"),
                            eval.satisfied ? eval.description + " (Satisfied)" : eval.failureReason,
                            eval.satisfied
                    ));
                }
            }
        }

        if (hasPendingInformation && !allGroupsSatisfied) {
            result.setStatus("PENDING_INFORMATION");
            result.setEligible(false);
            result.setReasons(failureReasons);
        } else if (allGroupsSatisfied) {
            result.setStatus("ELIGIBLE");
            result.setEligible(true);
            result.getReasons().add("All prerequisite and credit requirements are satisfied.");
        } else {
            result.setStatus("NOT_ELIGIBLE");
            result.setEligible(false);
            result.setReasons(failureReasons);
        }

        result.setDetailedReasons(detailedReasons);
        return result;
    }

    private EvaluationResult evaluateSingleRule(Student student, PrerequisiteRule rule,
                                                Map<String, StudentAcademicRecord.ResultStatus> resultsMap,
                                                Set<String> verifiedExemptions,
                                                Set<String> activeConcurrentCourses) {
        EvaluationResult eval = new EvaluationResult();
        String cCode = rule.getPrerequisiteCourseCode() != null ? rule.getPrerequisiteCourseCode().toUpperCase() : "";

        switch (rule.getRuleType()) {
            case COURSE_RESULT:
                String reqStatus = rule.getRequiredStatus() != null ? rule.getRequiredStatus().toUpperCase() : "P";
                eval.description = cCode + "(" + reqStatus + ")";

                // Check exemptions first
                if (verifiedExemptions.contains(cCode)) {
                    eval.satisfied = true;
                    return eval;
                }

                StudentAcademicRecord.ResultStatus actual = resultsMap.get(cCode);
                if (actual == null || actual == StudentAcademicRecord.ResultStatus.NOT_ATTEMPTED) {
                    eval.satisfied = false;
                    eval.failureReason = cCode + " has not been attempted (requires " + reqStatus + ").";
                } else if ("P".equals(reqStatus)) {
                    if (actual == StudentAcademicRecord.ResultStatus.PA) {
                        eval.satisfied = true;
                    } else if (actual == StudentAcademicRecord.ResultStatus.CA) {
                        eval.satisfied = false;
                        eval.failureReason = cCode + " has only Continuous Assessment Pass (final examination Pass required).";
                    } else {
                        eval.satisfied = false;
                        eval.failureReason = cCode + " was failed (Pass required).";
                    }
                } else if ("CA".equals(reqStatus)) {
                    if (actual == StudentAcademicRecord.ResultStatus.CA || (paSatisfiesCa && actual == StudentAcademicRecord.ResultStatus.PA)) {
                        eval.satisfied = true;
                    } else if (actual == StudentAcademicRecord.ResultStatus.FA) {
                        eval.satisfied = false;
                        eval.failureReason = cCode + " was failed (CA required).";
                    } else {
                        eval.satisfied = false;
                        eval.failureReason = cCode + " has not obtained the required Continuous Assessment (CA) status.";
                    }
                } else if ("CR".equals(reqStatus)) {
                    // CR in course result rule: satisfied if concurrently registered or already passed/CA
                    if (activeConcurrentCourses.contains(cCode) ||
                            actual == StudentAcademicRecord.ResultStatus.PA ||
                            actual == StudentAcademicRecord.ResultStatus.CA) {
                        eval.satisfied = true;
                    } else {
                        eval.satisfied = false;
                        eval.failureReason = cCode + " requires concurrent registration (CR) or prior completion.";
                    }
                }
                break;

            case CONCURRENT_REGISTRATION:
                eval.description = cCode + "(CR)";
                if (activeConcurrentCourses.contains(cCode)) {
                    eval.satisfied = true;
                } else {
                    // Check if student already passed or obtained CA in previous session
                    StudentAcademicRecord.ResultStatus st = resultsMap.get(cCode);
                    if (st == StudentAcademicRecord.ResultStatus.PA || st == StudentAcademicRecord.ResultStatus.CA || verifiedExemptions.contains(cCode)) {
                        eval.satisfied = true;
                    } else {
                        eval.satisfied = false;
                        eval.failureReason = cCode + " must be included in concurrent registration plan (CR).";
                    }
                }
                break;

            case PASSED_CREDITS_TOTAL:
                int minCreds = rule.getMinCredits() != null ? rule.getMinCredits() : 0;
                eval.description = "Pass in " + minCreds + " credits overall";
                int totalEarned = creditService.getPassedCreditsAtLevels(student.getId(), Set.of(3, 4, 5, 6));
                if (totalEarned >= minCreds) {
                    eval.satisfied = true;
                } else {
                    eval.satisfied = false;
                    eval.failureReason = "Student has only " + totalEarned + " passed credits; " + minCreds + " are required.";
                }
                break;

            case PASSED_CREDITS_AT_LEVEL:
                int lvlMinCreds = rule.getMinCredits() != null ? rule.getMinCredits() : 0;
                Set<Integer> targetL = parseLevels(rule.getTargetLevels());
                eval.description = "Pass in " + lvlMinCreds + " credits at Level " + rule.getTargetLevels();
                int passedAtLvl = creditService.getPassedCreditsAtLevels(student.getId(), targetL);
                if (passedAtLvl >= lvlMinCreds) {
                    eval.satisfied = true;
                } else {
                    eval.satisfied = false;
                    eval.failureReason = "Student has only " + passedAtLvl + " passed credits at Level " +
                            rule.getTargetLevels() + "; " + lvlMinCreds + " are required.";
                }
                break;

            case CA_CREDITS_AT_LEVEL:
                int caMinCreds = rule.getMinCredits() != null ? rule.getMinCredits() : 0;
                Set<Integer> caTargetL = parseLevels(rule.getTargetLevels());
                eval.description = "CA in " + caMinCreds + " credits at Level " + rule.getTargetLevels();
                int caAtLvl = creditService.getCACreditsAtLevels(student.getId(), caTargetL);
                if (caAtLvl >= caMinCreds) {
                    eval.satisfied = true;
                } else {
                    eval.satisfied = false;
                    eval.failureReason = "Student has only " + caAtLvl + " Continuous Assessment (CA) credits at Level " +
                            rule.getTargetLevels() + "; " + caMinCreds + " are required.";
                }
                break;

            case EXEMPTION_CONDITION:
                eval.description = "Recognized exemption in " + cCode;
                if (verifiedExemptions.contains(cCode)) {
                    eval.satisfied = true;
                } else {
                    eval.satisfied = false;
                    eval.failureReason = "Officially recognized exemption in " + cCode + " is required.";
                }
                break;

            case ADMIN_VERIFICATION_REQUIRED:
                eval.description = "Official verification required: " + rule.getRuleNote();
                if (Boolean.TRUE.equals(rule.getVerified())) {
                    eval.satisfied = true;
                } else {
                    eval.satisfied = false;
                    eval.pendingInfo = true;
                    eval.failureReason = "Administrative verification pending: " + rule.getRuleNote();
                }
                break;

            case SPECIAL_RESTRICTION:
                eval.description = "Special restriction check: " + rule.getRuleNote();
                eval.satisfied = true; // Recorded as advisory condition
                break;
        }

        return eval;
    }

    private Set<Integer> parseLevels(String targetLevels) {
        Set<Integer> set = new HashSet<>();
        if (targetLevels == null || targetLevels.isBlank()) {
            set.addAll(Set.of(3, 4, 5, 6));
            return set;
        }
        String[] parts = targetLevels.split(",");
        for (String p : parts) {
            try {
                set.add(Integer.parseInt(p.trim()));
            } catch (NumberFormatException ignored) {}
        }
        return set;
    }

    private static class EvaluationResult {
        boolean satisfied = false;
        boolean pendingInfo = false;
        String description = "";
        String failureReason = "";
    }
}


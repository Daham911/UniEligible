package lk.ac.ou.unieligible.eligibility;

import lk.ac.ou.unieligible.dto.CategoryCreditProgressDto;
import lk.ac.ou.unieligible.dto.CreditBreakdownDto;
import lk.ac.ou.unieligible.entity.*;
import lk.ac.ou.unieligible.repository.CourseCategoryRepository;
import lk.ac.ou.unieligible.repository.StudentAcademicRecordRepository;
import lk.ac.ou.unieligible.repository.StudentExemptionRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CreditCalculationService {

    private final StudentAcademicRecordRepository recordRepository;
    private final StudentExemptionRepository exemptionRepository;
    private final CourseCategoryRepository categoryRepository;

    public CreditCalculationService(StudentAcademicRecordRepository recordRepository,
                                    StudentExemptionRepository exemptionRepository,
                                    CourseCategoryRepository categoryRepository) {
        this.recordRepository = recordRepository;
        this.exemptionRepository = exemptionRepository;
        this.categoryRepository = categoryRepository;
    }

    public CreditBreakdownDto calculateCreditBreakdown(String studentId) {
        List<StudentAcademicRecord> records = recordRepository.findByStudentId(studentId);
        List<StudentExemption> exemptions = exemptionRepository.findByStudentId(studentId);
        List<CourseCategory> categories = categoryRepository.findAll();

        CreditBreakdownDto dto = new CreditBreakdownDto();

        int totalEarned = 0;
        int totalExempted = 0;
        int totalCAOnly = 0;
        int compulsory = 0;
        int elective = 0;
        int l6Electives = 0;

        Map<Integer, Integer> levelCredits = new HashMap<>();
        levelCredits.put(3, 0);
        levelCredits.put(4, 0);
        levelCredits.put(5, 0);
        levelCredits.put(6, 0);

        Map<String, Integer> deptCredits = new HashMap<>();
        Map<String, Integer> categoryEarnedMap = new HashMap<>();
        Map<String, Integer> categoryExemptedMap = new HashMap<>();

        Set<String> passedOrExemptedCourseCodes = new HashSet<>();

        // Process verified exemptions first
        for (StudentExemption ex : exemptions) {
            if (Boolean.TRUE.equals(ex.getVerified())) {
                int cred = ex.getCreditValue() != null ? ex.getCreditValue() : 0;
                totalExempted += cred;
                passedOrExemptedCourseCodes.add(ex.getCourseCode());

                if (ex.getLevel() != null && levelCredits.containsKey(ex.getLevel())) {
                    levelCredits.put(ex.getLevel(), levelCredits.get(ex.getLevel()) + cred);
                }

                if (ex.getCategoryCode() != null) {
                    categoryExemptedMap.put(ex.getCategoryCode(),
                            categoryExemptedMap.getOrDefault(ex.getCategoryCode(), 0) + cred);
                }
            }
        }

        // Process student academic records
        for (StudentAcademicRecord rec : records) {
            Course course = rec.getCourse();
            if (course == null) continue;

            if (rec.getResultStatus() == StudentAcademicRecord.ResultStatus.PA) {
                // Avoid double-counting if also recorded as an exemption
                if (passedOrExemptedCourseCodes.contains(course.getCode())) {
                    // Check if already counted via exemption
                    boolean alreadyCountedAsExemption = exemptions.stream()
                            .anyMatch(e -> e.getCourseCode().equalsIgnoreCase(course.getCode()) && Boolean.TRUE.equals(e.getVerified()));
                    if (alreadyCountedAsExemption) {
                        continue;
                    }
                }
                passedOrExemptedCourseCodes.add(course.getCode());

                // Note: MHZ2250 is marked degreeCredit = false, so it does NOT contribute to graduation credit totals!
                if (Boolean.TRUE.equals(course.getDegreeCredit())) {
                    int cCredits = course.getCredits() != null ? course.getCredits() : 0;
                    totalEarned += cCredits;

                    int lvl = course.getLevel() != null ? course.getLevel() : 3;
                    levelCredits.put(lvl, levelCredits.getOrDefault(lvl, 0) + cCredits);

                    if (Boolean.TRUE.equals(course.getCompulsory())) {
                        compulsory += cCredits;
                    } else {
                        elective += cCredits;
                        if (lvl == 6) {
                            l6Electives += cCredits;
                        }
                    }

                    if (course.getDepartment() != null) {
                        String dCode = course.getDepartment().getCode();
                        deptCredits.put(dCode, deptCredits.getOrDefault(dCode, 0) + cCredits);
                    }

                    if (course.getCategory() != null) {
                        String catCode = course.getCategory().getCode();
                        categoryEarnedMap.put(catCode, categoryEarnedMap.getOrDefault(catCode, 0) + cCredits);
                    }
                }
            } else if (rec.getResultStatus() == StudentAcademicRecord.ResultStatus.CA) {
                if (Boolean.TRUE.equals(course.getDegreeCredit())) {
                    totalCAOnly += (course.getCredits() != null ? course.getCredits() : 0);
                }
            }
        }

        int totalRecognized = totalEarned + totalExempted;
        dto.setTotalEarnedCredits(totalEarned);
        dto.setTotalExemptedCredits(totalExempted);
        dto.setTotalRecognizedCredits(totalRecognized);
        dto.setTotalCACredits(totalCAOnly);
        dto.setTargetTotalCredits(125);
        dto.setRemainingDegreeCredits(Math.max(0, 125 - totalRecognized));
        dto.setCompulsoryCredits(compulsory);
        dto.setElectiveCredits(elective);
        dto.setLevel6ElectiveCredits(l6Electives);
        dto.setCreditsByLevel(levelCredits);
        dto.setCreditsByDepartment(deptCredits);

        // Build category breakdown
        Map<String, CategoryCreditProgressDto> catProgressMap = new HashMap<>();
        List<String> unfulfilled = new ArrayList<>();

        for (CourseCategory cat : categories) {
            String catCode = cat.getCode();
            int catEarned = categoryEarnedMap.getOrDefault(catCode, 0);
            int catExempt = categoryExemptedMap.getOrDefault(catCode, 0);
            int catTotal = catEarned + catExempt;
            int minReq = cat.getMinCredits() != null ? cat.getMinCredits() : 0;
            int maxAllowed = cat.getMaxCredits() != null ? cat.getMaxCredits() : 999;
            boolean satisfied = catTotal >= minReq;
            int remaining = Math.max(0, minReq - catTotal);

            if (!satisfied) {
                unfulfilled.add(cat.getName() + " (" + catCode + "): " + catTotal + "/" + minReq + " credits (needs " + remaining + " more)");
            }

            CategoryCreditProgressDto cDto = new CategoryCreditProgressDto(
                    catCode,
                    cat.getName(),
                    catEarned,
                    catExempt,
                    catTotal,
                    minReq,
                    maxAllowed,
                    satisfied,
                    remaining
            );
            catProgressMap.put(catCode, cDto);
        }

        dto.setCreditsByCategory(catProgressMap);

        if (totalRecognized < 125) {
            unfulfilled.add("Total Credits: " + totalRecognized + "/125 required (needs " + (125 - totalRecognized) + " more)");
        }
        int l5l6 = levelCredits.getOrDefault(5, 0) + levelCredits.getOrDefault(6, 0);
        if (l5l6 < 63) {
            unfulfilled.add("Levels 5 & 6 Credits: " + l5l6 + "/63 required (needs " + (63 - l5l6) + " more)");
        }
        int l6 = levelCredits.getOrDefault(6, 0);
        if (l6 < 30) {
            unfulfilled.add("Level 6 Credits: " + l6 + "/30 required (needs " + (30 - l6) + " more)");
        }
        if (l6Electives < 8) {
            unfulfilled.add("Level 6 Elective Credits: " + l6Electives + "/8 required (needs " + (8 - l6Electives) + " more)");
        }

        dto.setUnfulfilledRequirements(unfulfilled);
        return dto;
    }

    public int getPassedCreditsAtLevels(String studentId, Set<Integer> targetLevels) {
        List<StudentAcademicRecord> records = recordRepository.findByStudentId(studentId);
        List<StudentExemption> exemptions = exemptionRepository.findByStudentId(studentId);

        int total = 0;
        Set<String> processedCourses = new HashSet<>();

        for (StudentExemption ex : exemptions) {
            if (Boolean.TRUE.equals(ex.getVerified()) && ex.getLevel() != null && targetLevels.contains(ex.getLevel())) {
                total += (ex.getCreditValue() != null ? ex.getCreditValue() : 0);
                processedCourses.add(ex.getCourseCode());
            }
        }

        for (StudentAcademicRecord rec : records) {
            if (rec.getResultStatus() == StudentAcademicRecord.ResultStatus.PA) {
                Course course = rec.getCourse();
                if (course != null && Boolean.TRUE.equals(course.getDegreeCredit())
                        && targetLevels.contains(course.getLevel())
                        && !processedCourses.contains(course.getCode())) {
                    total += (course.getCredits() != null ? course.getCredits() : 0);
                    processedCourses.add(course.getCode());
                }
            }
        }
        return total;
    }

    public int getCACreditsAtLevels(String studentId, Set<Integer> targetLevels) {
        List<StudentAcademicRecord> records = recordRepository.findByStudentId(studentId);
        int total = 0;
        Set<String> processedCourses = new HashSet<>();

        for (StudentAcademicRecord rec : records) {
            if (rec.getResultStatus() == StudentAcademicRecord.ResultStatus.CA ||
                rec.getResultStatus() == StudentAcademicRecord.ResultStatus.PA) {
                Course course = rec.getCourse();
                if (course != null && Boolean.TRUE.equals(course.getDegreeCredit())
                        && targetLevels.contains(course.getLevel())
                        && !processedCourses.contains(course.getCode())) {
                    total += (course.getCredits() != null ? course.getCredits() : 0);
                    processedCourses.add(course.getCode());
                }
            }
        }
        return total;
    }
}


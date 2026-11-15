package lk.ac.ou.unieligible.config;

import lk.ac.ou.unieligible.entity.*;
import lk.ac.ou.unieligible.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final ProgrammeRepository programmeRepository;
    private final AcademicYearRepository academicYearRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseCategoryRepository categoryRepository;
    private final CourseRepository courseRepository;
    private final PrerequisiteRuleGroupRepository ruleGroupRepository;
    private final PrerequisiteRuleRepository ruleRepository;
    private final GraduationRequirementRepository requirementRepository;
    private final StudentRepository studentRepository;

    public DataInitializer(ProgrammeRepository programmeRepository,
                           AcademicYearRepository academicYearRepository,
                           DepartmentRepository departmentRepository,
                           CourseCategoryRepository categoryRepository,
                           CourseRepository courseRepository,
                           PrerequisiteRuleGroupRepository ruleGroupRepository,
                           PrerequisiteRuleRepository ruleRepository,
                           GraduationRequirementRepository requirementRepository,
                           StudentRepository studentRepository) {
        this.programmeRepository = programmeRepository;
        this.academicYearRepository = academicYearRepository;
        this.departmentRepository = departmentRepository;
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;
        this.ruleGroupRepository = ruleGroupRepository;
        this.ruleRepository = ruleRepository;
        this.requirementRepository = requirementRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (programmeRepository.count() > 0) {
            log.info("Database already seeded. Skipping initial data loading.");
            return;
        }

        log.info("Seeding initial BSEHons programme data and course catalogue for 2026/2027...");

        // 1. Programme
        Programme bse = new Programme(
                "BSEHONS",
                "Bachelor of Software Engineering Honours",
                "Faculty of Engineering Technology",
                125,
                38,
                "2026/2027"
        );
        programmeRepository.save(bse);

        // 2. Academic Years
        academicYearRepository.save(new AcademicYear(1, 3, "Year 1 – Level 3", "Foundational software engineering, programming, databases, and mathematics."));
        academicYearRepository.save(new AcademicYear(2, 4, "Year 2 – Level 4", "Intermediate software engineering, data structures, UI/UX, AI, and economics."));
        academicYearRepository.save(new AcademicYear(3, 5, "Year 3 – Level 5", "Advanced systems, operating systems, software testing, database systems, and industrial training."));
        academicYearRepository.save(new AcademicYear(4, 6, "Year 4 – Level 6", "Final year project, software architecture, professional practice, and advanced electives."));

        // 3. Departments
        Department ee = departmentRepository.save(new Department("EE", "Electrical and Computer Engineering"));
        Department ag = departmentRepository.save(new Department("AG", "Agriculture and Plantation"));
        Department cv = departmentRepository.save(new Department("CV", "Civil Engineering"));
        Department mh = departmentRepository.save(new Department("MH", "Mathematics and Philosophy"));
        Department dm = departmentRepository.save(new Department("DM", "Mechanical Engineering"));
        Department ta = departmentRepository.save(new Department("TA", "Textile and Apparel Technology"));
        Department ll = departmentRepository.save(new Department("LL", "Legal Studies"));
        Department lt = departmentRepository.save(new Department("LT", "English Language Teaching"));

        // 4. Course Categories (Guidebook Table 1 requirements)
        CourseCategory catI = categoryRepository.save(new CourseCategory("I", "Industrial", 67, 78));
        CourseCategory catM = categoryRepository.save(new CourseCategory("M", "Management", 11, 20));
        CourseCategory catJ = categoryRepository.save(new CourseCategory("J", "General", 3, 6));
        CourseCategory catZ = categoryRepository.save(new CourseCategory("Z", "Mathematics", 12, 12));
        CourseCategory catR = categoryRepository.save(new CourseCategory("R", "Projects", 9, 13));
        CourseCategory catW = categoryRepository.save(new CourseCategory("W", "Industrial Training", 6, 6));
        CourseCategory catL = categoryRepository.save(new CourseCategory("L", "Language", 6, 6));
        CourseCategory catE = categoryRepository.save(new CourseCategory("E", "English Language", 6, 6));
        CourseCategory catS = categoryRepository.save(new CourseCategory("S", "Engineering", 0, 999));

        // 5. Graduation Requirements
        requirementRepository.save(new GraduationRequirement("BSEHONS", "MIN_TOTAL_CREDITS", "Minimum Total Degree Credits", "125 minimum total credits required for graduation.", 125, null, null, true, "Guidebook Section 4.1"));
        requirementRepository.save(new GraduationRequirement("BSEHONS", "MIN_L5_L6_CREDITS", "Minimum Levels 5 & 6 Credits", "At least 63 credits at Levels 5 and 6.", 63, null, null, true, "Guidebook Table 1"));
        requirementRepository.save(new GraduationRequirement("BSEHONS", "MIN_L6_CREDITS", "Minimum Level 6 Credits", "At least 30 credits at Level 6.", 30, null, 6, true, "Guidebook Table 1"));
        requirementRepository.save(new GraduationRequirement("BSEHONS", "MIN_L6_ELECTIVE_CREDITS", "Minimum Level 6 Elective Credits", "At least 8 elective credits from Level 6.", 8, null, 6, true, "Guidebook Year 4 Electives"));
        requirementRepository.save(new GraduationRequirement("BSEHONS", "MIN_L6_PROJECT_CREDITS", "Level 6 Project Requirement", "At least 6 project credits must be at Level 6.", 6, "R", 6, true, "Guidebook Section 4.2"));
        requirementRepository.save(new GraduationRequirement("BSEHONS", "INDUSTRIAL_TRAINING", "Industrial Training Requirement", "Mandatory completion of Industrial Training (EEW5611).", 6, "W", 5, true, "Guidebook Section 4.2"));
        requirementRepository.save(new GraduationRequirement("BSEHONS", "MAX_ANNUAL_CREDITS", "Maximum Annual Credit Limit", "Maximum annual credit registration/earning limit is 38 credits.", 38, null, null, false, "Guidebook Section 3.4"));
        requirementRepository.save(new GraduationRequirement("BSEHONS", "COMPLETION_L6_15", "Level 6 Completion Condition", "At least 15 credits completed at Level 6.", 15, null, 6, true, "Guidebook Section 4.3"));
        requirementRepository.save(new GraduationRequirement("BSEHONS", "COMPLETION_L5_L6_32", "Levels 5 & 6 Completion Condition", "At least 32 credits completed at Levels 5 and 6.", 32, null, null, true, "Guidebook Section 4.3"));
        requirementRepository.save(new GraduationRequirement("BSEHONS", "COMPLETION_L3_L6_63", "Levels 3-6 Completion Condition", "At least 63 credits completed from Levels 3-6.", 63, null, null, true, "Guidebook Section 4.3"));

        // Foundational Reference Courses (outside standard curriculum catalogue)
        Course lte34se = new Course("LTE34SE", "English for General Academic Purposes", lt, catE, 3, 1, 4, null, "1", true, true, false, "None", "Foundational English");
        courseRepository.save(lte34se);

        Course agm3263 = new Course("AGM3263", "Agricultural Economics and Management", ag, catM, 3, 1, 2, null, "1", false, true, true, "None", "Alternative Management Course");
        courseRepository.save(agm3263);

        // ==========================================
        // YEAR 1 – LEVEL 3
        // ==========================================
        // MHZ2250: 2* credits, Level 2 non-degree credit course
        Course mhz2250 = new Course("MHZ2250", "Elementary Mathematics for Computing", mh, catZ, 2, 1, 2, "2*", "1", true, false, true, "None", "Level 2 non-credit course. Does not count towards graduation credit totals.");
        courseRepository.save(mhz2250);

        Course eei3346 = new Course("EEI3346", "Web Application Development", ee, catI, 3, 1, 3, null, "1", true, true, true, "None", null);
        courseRepository.save(eei3346);

        Course eei3366 = new Course("EEI3366", "Database Systems", ee, catI, 3, 1, 3, null, "1", true, true, true, "None", null);
        courseRepository.save(eei3366);

        Course eei3372 = new Course("EEI3372", "Programming in Python", ee, catI, 3, 1, 3, null, "1", true, true, true, "None", null);
        courseRepository.save(eei3372);

        Course eei3467 = new Course("EEI3467", "Software Engineering Concepts and Programming", ee, catI, 3, 1, 4, null, "1", true, true, true, "None", null);
        courseRepository.save(eei3467);

        Course eel3263 = new Course("EEL3263", "Communication Skills", ee, catL, 3, 1, 2, null, "1 & 2", true, true, true, "None", null);
        courseRepository.save(eel3263);

        Course lte34si = new Course("LTE34SI", "English for Academic Purposes – Intermediate", lt, catE, 3, 1, 4, null, "1 & 2", true, true, false, "LTE34SE(P)", "Exception non-GPA course: credits count towards category totals.");
        courseRepository.save(lte34si);
        addRule(lte34si, 1, "AND", "Prerequisite English Course", PrerequisiteRule.RuleType.COURSE_RESULT, "LTE34SE", "P", null, null, "Guidebook Table 3", true);

        Course eei3262 = new Course("EEI3262", "Introduction to Object-Oriented Programming", ee, catI, 3, 1, 2, null, "2", true, true, true, "None", null);
        courseRepository.save(eei3262);

        Course eei3269 = new Course("EEI3269", "Mobile Application Design", ee, catI, 3, 1, 2, null, "2", true, true, true, "None", null);
        courseRepository.save(eei3269);

        Course eei3273 = new Course("EEI3273", "Communication and Computer Technology", ee, catI, 3, 1, 2, null, "2", true, true, true, "EEI3467(CR)", null);
        courseRepository.save(eei3273);
        addRule(eei3273, 1, "AND", "Concurrent Registration in EEI3467", PrerequisiteRule.RuleType.CONCURRENT_REGISTRATION, "EEI3467", "CR", null, null, "Guidebook Table 3", true);

        Course eei3347 = new Course("EEI3347", "Web Technology", ee, catI, 3, 1, 3, null, "2", true, true, true, "EEI3346(CR)", null);
        courseRepository.save(eei3347);
        addRule(eei3347, 1, "AND", "Concurrent Registration in EEI3346", PrerequisiteRule.RuleType.CONCURRENT_REGISTRATION, "EEI3346", "CR", null, null, "Guidebook Table 3", true);

        Course mhz3356 = new Course("MHZ3356", "Mathematics for Computing I", mh, catZ, 3, 1, 3, null, "2", true, true, true, "MHZ2250(CR)", null);
        courseRepository.save(mhz3356);
        addRule(mhz3356, 1, "AND", "Concurrent Registration in MHZ2250", PrerequisiteRule.RuleType.CONCURRENT_REGISTRATION, "MHZ2250", "CR", null, null, "Guidebook Table 3", true);

        // Year 1 Elective
        Course eem3366 = new Course("EEM3366", "Introduction to Business Studies", ee, catM, 3, 1, 3, null, "2", false, true, true, "None", "Recommended for Diploma in Software Development exit.");
        courseRepository.save(eem3366);

        // ==========================================
        // YEAR 2 – LEVEL 4
        // ==========================================
        Course agm4367 = new Course("AGM4367", "Economics and Marketing for Engineers", ag, catM, 4, 2, 3, null, "3", true, true, true, "Pass in 18 credits at Level 3", null);
        courseRepository.save(agm4367);
        addRule(agm4367, 1, "AND", "Passed Credits Threshold", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 18, "3", "Guidebook Table 4", true);

        Course eei4267 = new Course("EEI4267", "Requirement Engineering", ee, catI, 4, 2, 2, null, "3", true, true, true, "EEI3467(CA), EEL3263(CA) OR AGM3263(CA), pass in 8 credits at Level 3", null);
        courseRepository.save(eei4267);
        addRule(eei4267, 1, "AND", "Course CA Condition", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3467", "CA", null, null, "Guidebook Table 4", true);
        PrerequisiteRuleGroup grpEei4267Or = new PrerequisiteRuleGroup(eei4267, 2, "OR", "Communication or Economics Alternative");
        grpEei4267Or.addRule(new PrerequisiteRule(grpEei4267Or, PrerequisiteRule.RuleType.COURSE_RESULT, "EEL3263", "CA", null, null, "Guidebook Table 4", true, "EEL3263(CA)"));
        grpEei4267Or.addRule(new PrerequisiteRule(grpEei4267Or, PrerequisiteRule.RuleType.COURSE_RESULT, "AGM3263", "CA", null, null, "Guidebook Table 4", true, "AGM3263(CA)"));
        ruleGroupRepository.save(grpEei4267Or);
        addRule(eei4267, 3, "AND", "Passed Credits Threshold", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 8, "3", "Guidebook Table 4", true);

        Course eei4361 = new Course("EEI4361", "User Experience Engineering", ee, catI, 4, 2, 3, null, "3", true, true, true, "EEI3269(CA), EEI3346(CA), pass in 8 credits", null);
        courseRepository.save(eei4361);
        addRule(eei4361, 1, "AND", "Mobile Design CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3269", "CA", null, null, "Guidebook Table 4", true);
        addRule(eei4361, 2, "AND", "Web App Dev CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3346", "CA", null, null, "Guidebook Table 4", true);
        addRule(eei4361, 3, "AND", "Passed Credits Threshold", PrerequisiteRule.RuleType.PASSED_CREDITS_TOTAL, null, null, 8, null, "Guidebook Table 4", true);

        Course eei4362 = new Course("EEI4362", "Object-Oriented Design", ee, catI, 4, 2, 3, null, "3", true, true, true, "EEI3262(CA), EEI3467(CA), pass in 8 credits", null);
        courseRepository.save(eei4362);
        addRule(eei4362, 1, "AND", "OOP CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3262", "CA", null, null, "Guidebook Table 4", true);
        addRule(eei4362, 2, "AND", "Software Eng CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3467", "CA", null, null, "Guidebook Table 4", true);
        addRule(eei4362, 3, "AND", "Passed Credits Threshold", PrerequisiteRule.RuleType.PASSED_CREDITS_TOTAL, null, null, 8, null, "Guidebook Table 4", true);

        Course eei4365 = new Course("EEI4365", "Data Structures and Algorithms", ee, catI, 4, 2, 3, null, "3", true, true, true, "EEI3467(CA), (EEI3372(CA) OR EEI3262(CA)), MHZ3356(CR)", null);
        courseRepository.save(eei4365);
        addRule(eei4365, 1, "AND", "Software Eng CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3467", "CA", null, null, "Guidebook Table 4", true);
        PrerequisiteRuleGroup grpEei4365Or = new PrerequisiteRuleGroup(eei4365, 2, "OR", "Python or OOP Alternative");
        grpEei4365Or.addRule(new PrerequisiteRule(grpEei4365Or, PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3372", "CA", null, null, "Guidebook Table 4", true, "EEI3372(CA)"));
        grpEei4365Or.addRule(new PrerequisiteRule(grpEei4365Or, PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3262", "CA", null, null, "Guidebook Table 4", true, "EEI3262(CA)"));
        ruleGroupRepository.save(grpEei4365Or);
        addRule(eei4365, 3, "AND", "Concurrent Math I", PrerequisiteRule.RuleType.CONCURRENT_REGISTRATION, "MHZ3356", "CR", null, null, "Guidebook Table 4", true);

        Course mhz4359 = new Course("MHZ4359", "Mathematics for Computing II", mh, catZ, 4, 2, 3, null, "3", true, true, true, "MHZ2250(P), MHZ3356(CA)", null);
        courseRepository.save(mhz4359);
        addRule(mhz4359, 1, "AND", "Elem Math Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ2250", "P", null, null, "Guidebook Table 4", true);
        addRule(mhz4359, 2, "AND", "Math I CA", PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ3356", "CA", null, null, "Guidebook Table 4", true);

        Course cvm4402 = new Course("CVM4402", "Accounting for Engineers", cv, catM, 4, 2, 4, null, "4", true, true, true, "Pass in 18 credits at Level 3", null);
        courseRepository.save(cvm4402);
        addRule(cvm4402, 1, "AND", "Passed Credits Threshold", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 18, "3", "Guidebook Table 4", true);

        Course eei4360 = new Course("EEI4360", "Introduction to Artificial Intelligence", ee, catI, 4, 2, 3, null, "4", true, true, true, "MHZ3356(CA) OR MHZ4359(CA), EEI3372(CA)", null);
        courseRepository.save(eei4360);
        PrerequisiteRuleGroup grpEei4360Or = new PrerequisiteRuleGroup(eei4360, 1, "OR", "Math I or Math II Alternative");
        grpEei4360Or.addRule(new PrerequisiteRule(grpEei4360Or, PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ3356", "CA", null, null, "Guidebook Table 4", true, "MHZ3356(CA)"));
        grpEei4360Or.addRule(new PrerequisiteRule(grpEei4360Or, PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ4359", "CA", null, null, "Guidebook Table 4", true, "MHZ4359(CA)"));
        ruleGroupRepository.save(grpEei4360Or);
        addRule(eei4360, 2, "AND", "Python CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3372", "CA", null, null, "Guidebook Table 4", true);

        Course eei4370 = new Course("EEI4370", "Computer Security Concepts", ee, catI, 4, 2, 3, null, "4", true, true, true, "EEI3467(CA), EEI3372(CA)", null);
        courseRepository.save(eei4370);
        addRule(eei4370, 1, "AND", "Software Eng CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3467", "CA", null, null, "Guidebook Table 4", true);
        addRule(eei4370, 2, "AND", "Python CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3372", "CA", null, null, "Guidebook Table 4", true);

        Course eer4189 = new Course("EER4189", "Software Design in Group", ee, catR, 4, 2, 1, null, "4", true, true, true, "EEI4267(CR), LTE34SE(P) OR EEL3263(CA), pass in 20 credits at Level 3", null);
        courseRepository.save(eer4189);
        addRule(eer4189, 1, "AND", "Requirement Eng CR", PrerequisiteRule.RuleType.CONCURRENT_REGISTRATION, "EEI4267", "CR", null, null, "Guidebook Table 4", true);
        PrerequisiteRuleGroup grpEer4189Or = new PrerequisiteRuleGroup(eer4189, 2, "OR", "English or Communication Alternative");
        grpEer4189Or.addRule(new PrerequisiteRule(grpEer4189Or, PrerequisiteRule.RuleType.COURSE_RESULT, "LTE34SE", "P", null, null, "Guidebook Table 4", true, "LTE34SE(P)"));
        grpEer4189Or.addRule(new PrerequisiteRule(grpEer4189Or, PrerequisiteRule.RuleType.COURSE_RESULT, "EEL3263", "CA", null, null, "Guidebook Table 4", true, "EEL3263(CA)"));
        ruleGroupRepository.save(grpEer4189Or);
        addRule(eer4189, 3, "AND", "Passed Level 3 Credits Threshold", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 20, "3", "Guidebook Table 4", true);

        Course mhz4377 = new Course("MHZ4377", "Applied Statistics", mh, catZ, 4, 2, 3, null, "4", true, true, true, "MHZ4359(CR)", null);
        courseRepository.save(mhz4377);
        addRule(mhz4377, 1, "AND", "Math II CR", PrerequisiteRule.RuleType.CONCURRENT_REGISTRATION, "MHZ4359", "CR", null, null, "Guidebook Table 4", true);

        // Year 2 Elective
        Course eer4489 = new Course("EER4489", "Higher Diploma Project – Software Engineering", ee, catR, 4, 2, 4, null, "3 & 4", false, true, true, "EER4189(CR), pass in 45 credits", "Offered only when student has acceptable justification for not registering for Industrial Training.");
        eer4489.setSpecialRestriction("Special Restriction: Offered only when student has acceptable justification for not registering for Industrial Training.");
        courseRepository.save(eer4489);
        addRule(eer4489, 1, "AND", "Group Design CR", PrerequisiteRule.RuleType.CONCURRENT_REGISTRATION, "EER4189", "CR", null, null, "Guidebook Table 4", true);
        addRule(eer4489, 2, "AND", "Overall Passed Credits Threshold", PrerequisiteRule.RuleType.PASSED_CREDITS_TOTAL, null, null, 45, null, "Guidebook Table 4", true);

        // ==========================================
        // YEAR 3 – LEVEL 5
        // ==========================================
        Course eei5265 = new Course("EEI5265", "Operating Systems", ee, catI, 5, 3, 2, null, "5", true, true, true, "EEI3273(P), EEI3467(P), pass 24 credits at Level 3, 15 credits CA at Level 4", null);
        courseRepository.save(eei5265);
        addRule(eei5265, 1, "AND", "Communication Tech Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3273", "P", null, null, "Guidebook Table 5", true);
        addRule(eei5265, 2, "AND", "Software Eng Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3467", "P", null, null, "Guidebook Table 5", true);
        addRule(eei5265, 3, "AND", "Passed Level 3 Credits Threshold", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 24, "3", "Guidebook Table 5", true);
        addRule(eei5265, 4, "AND", "CA Level 4 Credits Threshold", PrerequisiteRule.RuleType.CA_CREDITS_AT_LEVEL, null, null, 15, "4", "Guidebook Table 5", true);

        Course eei5364 = new Course("EEI5364", "Data Communication", ee, catI, 5, 3, 3, null, "5", true, true, true, "EEI3273(P), MHZ3356(P), pass 24 credits at Level 3, 15 credits CA at Level 4", "Laboratory sessions held at CRC only.");
        eei5364.setDeliveryCenter("CRC only");
        courseRepository.save(eei5364);
        addRule(eei5364, 1, "AND", "Communication Tech Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3273", "P", null, null, "Guidebook Table 5", true);
        addRule(eei5364, 2, "AND", "Math I Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ3356", "P", null, null, "Guidebook Table 5", true);
        addRule(eei5364, 3, "AND", "Passed Level 3 Credits Threshold", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 24, "3", "Guidebook Table 5", true);
        addRule(eei5364, 4, "AND", "CA Level 4 Credits Threshold", PrerequisiteRule.RuleType.CA_CREDITS_AT_LEVEL, null, null, 15, "4", "Guidebook Table 5", true);

        Course llm5281 = new Course("LLM5281", "Basic Laws Relating to Engineering and Technology", ll, catM, 5, 3, 2, null, "5", true, true, true, "24 credits P at Level 3", null);
        courseRepository.save(llm5281);
        addRule(llm5281, 1, "AND", "Passed Level 3 Credits Threshold", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 24, "3", "Guidebook Table 5", true);

        Course mhj5383 = new Course("MHJ5383", "Technology, Society and Environment", mh, catJ, 5, 3, 3, null, "5", true, true, true, "24 credits P at Level 3", null);
        courseRepository.save(mhj5383);
        addRule(mhj5383, 1, "AND", "Passed Level 3 Credits Threshold", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 24, "3", "Guidebook Table 5", true);

        Course mhz5375 = new Course("MHZ5375", "Discrete Mathematics", mh, catZ, 5, 3, 3, null, "5", true, true, true, "MHZ3356(P), MHZ4359(CA)", null);
        courseRepository.save(mhz5375);
        addRule(mhz5375, 1, "AND", "Math I Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ3356", "P", null, null, "Guidebook Table 5", true);
        addRule(mhz5375, 2, "AND", "Math II CA", PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ4359", "CA", null, null, "Guidebook Table 5", true);

        Course eei5263 = new Course("EEI5263", "Computer Organization and Architecture", ee, catI, 5, 3, 2, null, "6", true, true, true, "EEI3273(P), MHZ3356(P), pass 24 credits at Level 3, 15 credits CA at Level 4", null);
        courseRepository.save(eei5263);
        addRule(eei5263, 1, "AND", "Communication Tech Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3273", "P", null, null, "Guidebook Table 5", true);
        addRule(eei5263, 2, "AND", "Math I Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ3356", "P", null, null, "Guidebook Table 5", true);
        addRule(eei5263, 3, "AND", "Passed Level 3 Credits Threshold", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 24, "3", "Guidebook Table 5", true);
        addRule(eei5263, 4, "AND", "CA Level 4 Credits Threshold", PrerequisiteRule.RuleType.CA_CREDITS_AT_LEVEL, null, null, 15, "4", "Guidebook Table 5", true);

        Course eei5467 = new Course("EEI5467", "Software Testing and Quality Assurance", ee, catI, 5, 3, 4, null, "6", true, true, true, "EEI3467(P), EEI3346(P), EEI3366(P), 15 credits CA at Level 4", null);
        courseRepository.save(eei5467);
        addRule(eei5467, 1, "AND", "Software Eng Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3467", "P", null, null, "Guidebook Table 5", true);
        addRule(eei5467, 2, "AND", "Web App Dev Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3346", "P", null, null, "Guidebook Table 5", true);
        addRule(eei5467, 3, "AND", "Database Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3366", "P", null, null, "Guidebook Table 5", true);
        addRule(eei5467, 4, "AND", "CA Level 4 Credits Threshold", PrerequisiteRule.RuleType.CA_CREDITS_AT_LEVEL, null, null, 15, "4", "Guidebook Table 5", true);

        Course eei5486 = new Course("EEI5486", "Advanced Database Systems", ee, catI, 5, 3, 4, null, "6", true, true, true, "EEI3366(P), EEI3467(P), pass in 24 credits", null);
        courseRepository.save(eei5486);
        addRule(eei5486, 1, "AND", "Database Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3366", "P", null, null, "Guidebook Table 5", true);
        addRule(eei5486, 2, "AND", "Software Eng Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3467", "P", null, null, "Guidebook Table 5", true);
        addRule(eei5486, 3, "AND", "Passed Credits Threshold", PrerequisiteRule.RuleType.PASSED_CREDITS_TOTAL, null, null, 24, null, "Guidebook Table 5", true);

        Course eew5611 = new Course("EEW5611", "Industrial Training – Software", ee, catW, 5, 3, 6, null, "6", true, true, true, "EEI3467(P), EEI4362(CA), EEI4365(CA), pass in 40 credits", null);
        courseRepository.save(eew5611);
        addRule(eew5611, 1, "AND", "Software Eng Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3467", "P", null, null, "Guidebook Table 5", true);
        addRule(eew5611, 2, "AND", "Object Oriented Design CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI4362", "CA", null, null, "Guidebook Table 5", true);
        addRule(eew5611, 3, "AND", "Data Structures CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI4365", "CA", null, null, "Guidebook Table 5", true);
        addRule(eew5611, 4, "AND", "Passed Credits Threshold", PrerequisiteRule.RuleType.PASSED_CREDITS_TOTAL, null, null, 40, null, "Guidebook Table 5", true);

        Course eer6289 = new Course("EER6289", "Research Methodology and Project Identification", ee, catR, 6, 3, 2, null, "5 & 6", true, true, true, "LTE34SI(CR), EEL3263(P), EEI4365(CA), EER4189(CA), pass in 24 credits at Level 3, CA in 15 credits at Level 4", null);
        courseRepository.save(eer6289);
        addRule(eer6289, 1, "AND", "English CR", PrerequisiteRule.RuleType.CONCURRENT_REGISTRATION, "LTE34SI", "CR", null, null, "Guidebook Table 5", true);
        addRule(eer6289, 2, "AND", "Communication Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEL3263", "P", null, null, "Guidebook Table 5", true);
        addRule(eer6289, 3, "AND", "Data Structures CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI4365", "CA", null, null, "Guidebook Table 5", true);
        addRule(eer6289, 4, "AND", "Group Design CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EER4189", "CA", null, null, "Guidebook Table 5", true);
        addRule(eer6289, 5, "AND", "Passed Level 3 Credits", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 24, "3", "Guidebook Table 5", true);
        addRule(eer6289, 6, "AND", "CA Level 4 Credits", PrerequisiteRule.RuleType.CA_CREDITS_AT_LEVEL, null, null, 15, "4", "Guidebook Table 5", true);

        // Year 3 Electives
        Course eei5369 = new Course("EEI5369", "Mobile App Development with Android", ee, catI, 5, 3, 3, null, "5", false, true, true, "EEI3269(CA), EEI3262(CA), EEI3366(CA), CA in 8 credits at Level 3", "Not offered in 2026/2027 academic year.");
        eei5369.setOffered(false);
        courseRepository.save(eei5369);
        addRule(eei5369, 1, "AND", "Mobile Design CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3269", "CA", null, null, "Guidebook Table 5", true);
        addRule(eei5369, 2, "AND", "OOP CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3262", "CA", null, null, "Guidebook Table 5", true);
        addRule(eei5369, 3, "AND", "Database CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3366", "CA", null, null, "Guidebook Table 5", true);
        addRule(eei5369, 4, "AND", "CA Level 3 Credits", PrerequisiteRule.RuleType.CA_CREDITS_AT_LEVEL, null, null, 8, "3", "Guidebook Table 5", true);

        Course eei5373 = new Course("EEI5373", "Data Science", ee, catI, 5, 3, 3, null, "6", false, true, true, "EEI5486(CR), MHZ4377(CA), CA in 8 credits at Level 3", null);
        courseRepository.save(eei5373);
        addRule(eei5373, 1, "AND", "Advanced Database CR", PrerequisiteRule.RuleType.CONCURRENT_REGISTRATION, "EEI5486", "CR", null, null, "Guidebook Table 5", true);
        addRule(eei5373, 2, "AND", "Statistics CA", PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ4377", "CA", null, null, "Guidebook Table 5", true);
        addRule(eei5373, 3, "AND", "CA Level 3 Credits", PrerequisiteRule.RuleType.CA_CREDITS_AT_LEVEL, null, null, 8, "3", "Guidebook Table 5", true);

        Course eei5376 = new Course("EEI5376", "Embedded Systems and IoT", ee, catI, 5, 3, 3, null, "6", false, true, true, "EEI3366(P), EEI3372(P), EEI3273(P), pass in 24 credits at Level 3, CA in 15 credits at Level 4", "Laboratory sessions held at CRC only.");
        eei5376.setDeliveryCenter("CRC only");
        courseRepository.save(eei5376);
        addRule(eei5376, 1, "AND", "Database Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3366", "P", null, null, "Guidebook Table 5", true);
        addRule(eei5376, 2, "AND", "Python Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3372", "P", null, null, "Guidebook Table 5", true);
        addRule(eei5376, 3, "AND", "Communication Tech Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3273", "P", null, null, "Guidebook Table 5", true);
        addRule(eei5376, 4, "AND", "Pass Level 3 Credits", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 24, "3", "Guidebook Table 5", true);
        addRule(eei5376, 5, "AND", "CA Level 4 Credits", PrerequisiteRule.RuleType.CA_CREDITS_AT_LEVEL, null, null, 15, "4", "Guidebook Table 5", true);

        Course mhj5282 = new Course("MHJ5282", "History of Technology", mh, catJ, 5, 3, 2, null, "6", false, true, true, "24 credits P at Level 3", null);
        courseRepository.save(mhj5282);
        addRule(mhj5282, 1, "AND", "Pass Level 3 Credits", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 24, "3", "Guidebook Table 5", true);

        // ==========================================
        // YEAR 4 – LEVEL 6
        // ==========================================
        Course eei6360 = new Course("EEI6360", "Software Project Management", ee, catI, 6, 4, 3, null, "7", true, true, true, "EEL3263(P), EEI3467(P), EEI5467(CA), pass in 45 credits at Levels 3 and 4, CA in 15 credits at Level 5", null);
        courseRepository.save(eei6360);
        addRule(eei6360, 1, "AND", "Communication Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEL3263", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6360, 2, "AND", "Software Eng Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3467", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6360, 3, "AND", "Software Testing CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI5467", "CA", null, null, "Guidebook Table 6", true);
        addRule(eei6360, 4, "AND", "Passed Levels 3 & 4 Credits", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 45, "3,4", "Guidebook Table 6", true);
        addRule(eei6360, 5, "AND", "CA Level 5 Credits", PrerequisiteRule.RuleType.CA_CREDITS_AT_LEVEL, null, null, 15, "5", "Guidebook Table 6", true);

        Course eei6373 = new Course("EEI6373", "Performance Modelling", ee, catI, 6, 4, 3, null, "7", true, true, true, "EEI3346(P), EEI3347(P), MHZ4377(P), EEI5486(CA), EEI5467(CA), CA in 15 credits at Level 5", null);
        courseRepository.save(eei6373);
        addRule(eei6373, 1, "AND", "Web App Dev Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3346", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6373, 2, "AND", "Web Tech Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3347", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6373, 3, "AND", "Statistics Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ4377", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6373, 4, "AND", "Advanced Database CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI5486", "CA", null, null, "Guidebook Table 6", true);
        addRule(eei6373, 5, "AND", "Software Testing CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI5467", "CA", null, null, "Guidebook Table 6", true);
        addRule(eei6373, 6, "AND", "CA Level 5 Credits", PrerequisiteRule.RuleType.CA_CREDITS_AT_LEVEL, null, null, 15, "5", "Guidebook Table 6", true);

        Course eem6202 = new Course("EEM6202", "Professional Practice", ee, catM, 6, 4, 2, null, "7", true, true, true, "EEL3263(P), pass in 45 credits at Levels 3 and 4, CA in 15 credits at Level 5", null);
        courseRepository.save(eem6202);
        addRule(eem6202, 1, "AND", "Communication Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEL3263", "P", null, null, "Guidebook Table 6", true);
        addRule(eem6202, 2, "AND", "Passed Levels 3 & 4 Credits", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 45, "3,4", "Guidebook Table 6", true);
        addRule(eem6202, 3, "AND", "CA Level 5 Credits", PrerequisiteRule.RuleType.CA_CREDITS_AT_LEVEL, null, null, 15, "5", "Guidebook Table 6", true);

        Course eei6567 = new Course("EEI6567", "Software Architecture and Design", ee, catI, 6, 4, 5, null, "7 & 8", true, true, true, "EEI3467(P), EEI3347(P), EEI4362(P), pass in 45 credits at Levels 3 and 4, CA in 15 credits at Level 5", null);
        courseRepository.save(eei6567);
        addRule(eei6567, 1, "AND", "Software Eng Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3467", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6567, 2, "AND", "Web Tech Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3347", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6567, 3, "AND", "Object Oriented Design Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI4362", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6567, 4, "AND", "Passed Levels 3 & 4 Credits", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 45, "3,4", "Guidebook Table 6", true);
        addRule(eei6567, 5, "AND", "CA Level 5 Credits", PrerequisiteRule.RuleType.CA_CREDITS_AT_LEVEL, null, null, 15, "5", "Guidebook Table 6", true);

        Course eer6689 = new Course("EER6689", "Final Project – Software Engineering", ee, catR, 6, 4, 6, null, "7 & 8", true, true, true, "LTE34SI(P), EER4189(P), EER6289(CA), EEI6360(CR), EEI6567(CR), pass in 75 credits", null);
        courseRepository.save(eer6689);
        addRule(eer6689, 1, "AND", "English Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "LTE34SI", "P", null, null, "Guidebook Table 6", true);
        addRule(eer6689, 2, "AND", "Group Design Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EER4189", "P", null, null, "Guidebook Table 6", true);
        addRule(eer6689, 3, "AND", "Research Methodology CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EER6289", "CA", null, null, "Guidebook Table 6", true);
        addRule(eer6689, 4, "AND", "Project Management CR", PrerequisiteRule.RuleType.CONCURRENT_REGISTRATION, "EEI6360", "CR", null, null, "Guidebook Table 6", true);
        addRule(eer6689, 5, "AND", "Software Architecture CR", PrerequisiteRule.RuleType.CONCURRENT_REGISTRATION, "EEI6567", "CR", null, null, "Guidebook Table 6", true);
        addRule(eer6689, 6, "AND", "Overall Passed Credits Threshold", PrerequisiteRule.RuleType.PASSED_CREDITS_TOTAL, null, null, 75, null, "Guidebook Table 6", true);

        Course eei6171 = new Course("EEI6171", "Emerging Technologies", ee, catI, 6, 4, 1, null, "8", true, true, true, "EEL3263(P), EEI4360(P), pass in 45 credits at Levels 3 and 4, CA in 15 credits at Level 5", null);
        courseRepository.save(eei6171);
        addRule(eei6171, 1, "AND", "Communication Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEL3263", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6171, 2, "AND", "Artificial Intelligence Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI4360", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6171, 3, "AND", "Passed Levels 3 & 4 Credits", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 45, "3,4", "Guidebook Table 6", true);
        addRule(eei6171, 4, "AND", "CA Level 5 Credits", PrerequisiteRule.RuleType.CA_CREDITS_AT_LEVEL, null, null, 15, "5", "Guidebook Table 6", true);

        // Year 4 Electives (At least 8 credits required)
        Course eei6279 = new Course("EEI6279", "Natural Language Processing", ee, catI, 6, 4, 2, null, "7", false, true, true, "EEI3467(P), EEI4365(P), MHZ5375(CA), pass in 50 credits at Levels 3 and 4", "Not offered in 2026/2027 academic year.");
        eei6279.setOffered(false);
        courseRepository.save(eei6279);
        addRule(eei6279, 1, "AND", "Software Eng Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3467", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6279, 2, "AND", "Data Structures Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI4365", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6279, 3, "AND", "Discrete Math CA", PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ5375", "CA", null, null, "Guidebook Table 6", true);
        addRule(eei6279, 4, "AND", "Passed Levels 3 & 4 Credits", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 50, "3,4", "Guidebook Table 6", true);

        Course eei6377 = new Course("EEI6377", "Data Mining", ee, catI, 6, 4, 3, null, "7", false, true, true, "MHZ4377(P), EEI5486(CA), EEI5373(P), pass in 50 credits at Levels 3 and 4", null);
        courseRepository.save(eei6377);
        addRule(eei6377, 1, "AND", "Statistics Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ4377", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6377, 2, "AND", "Advanced Database CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI5486", "CA", null, null, "Guidebook Table 6", true);
        addRule(eei6377, 3, "AND", "Data Science Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI5373", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6377, 4, "AND", "Passed Levels 3 & 4 Credits", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 50, "3,4", "Guidebook Table 6", true);

        Course eei6363 = new Course("EEI6363", "Compiler Construction", ee, catI, 6, 4, 3, null, "7 & 8", false, true, true, "EEI4365(P), EEI5263(CA), MHZ5375(CA), pass in 50 credits at Levels 3 and 4", null);
        courseRepository.save(eei6363);
        addRule(eei6363, 1, "AND", "Data Structures Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI4365", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6363, 2, "AND", "Comp Architecture CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI5263", "CA", null, null, "Guidebook Table 6", true);
        addRule(eei6363, 3, "AND", "Discrete Math CA", PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ5375", "CA", null, null, "Guidebook Table 6", true);
        addRule(eei6363, 4, "AND", "Passed Levels 3 & 4 Credits", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 50, "3,4", "Guidebook Table 6", true);

        Course eei6278 = new Course("EEI6278", "Neural Networks and Fuzzy Logic Applications", ee, catI, 6, 4, 2, null, "8", false, true, true, "EEI3372(P), MHZ3356(P), MHZ4359(CA), EEI4360(CA), pass in 24 credits at Level 3", null);
        courseRepository.save(eei6278);
        addRule(eei6278, 1, "AND", "Python Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3372", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6278, 2, "AND", "Math I Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ3356", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6278, 3, "AND", "Math II CA", PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ4359", "CA", null, null, "Guidebook Table 6", true);
        addRule(eei6278, 4, "AND", "AI CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI4360", "CA", null, null, "Guidebook Table 6", true);
        addRule(eei6278, 5, "AND", "Passed Level 3 Credits", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 24, "3", "Guidebook Table 6", true);

        Course eei6280 = new Course("EEI6280", "Creative Design", ee, catI, 6, 4, 2, null, "8", false, true, true, "EEI3346(P), EEI3347(P), EEI3269(P), EEI4360(P), pass in 50 credits at Levels 3 and 4", null);
        courseRepository.save(eei6280);
        addRule(eei6280, 1, "AND", "Web App Dev Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3346", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6280, 2, "AND", "Web Tech Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3347", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6280, 3, "AND", "Mobile Design Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3269", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6280, 4, "AND", "AI Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI4360", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6280, 5, "AND", "Passed Levels 3 & 4 Credits", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 50, "3,4", "Guidebook Table 6", true);

        Course eei6320 = new Course("EEI6320", "Intelligent Systems and Agent Technology", ee, catI, 6, 4, 3, null, "8", false, true, true, "EEI3467(P), EEI4360(P) [Flagged: Guidebook lists EE4360(P)], MHZ5375(CA), 45 credits P at Levels 3 and 4, 15 credits CA at Level 5", null);
        eei6320.setDiscrepancyFlag("Discrepancy Note: Guidebook row EEI6320 lists prerequisite EE4360(P). Must verify with department whether this maps to EEI4360(P).");
        courseRepository.save(eei6320);
        addRule(eei6320, 1, "AND", "Software Eng Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3467", "P", null, null, "Guidebook Table 6", true);
        // Note: Discrepancy rule with verified = false flag as required by prompt
        addRule(eei6320, 2, "AND", "Prerequisite Code Discrepancy", PrerequisiteRule.RuleType.ADMIN_VERIFICATION_REQUIRED, "EE4360", "P", null, null, "Guidebook EEI6320 row specifies EE4360(P). Official department verification required before enforcement.", false);
        addRule(eei6320, 3, "AND", "Discrete Math CA", PrerequisiteRule.RuleType.COURSE_RESULT, "MHZ5375", "CA", null, null, "Guidebook Table 6", true);
        addRule(eei6320, 4, "AND", "Passed Levels 3 & 4 Credits", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 45, "3,4", "Guidebook Table 6", true);
        addRule(eei6320, 5, "AND", "CA Level 5 Credits", PrerequisiteRule.RuleType.CA_CREDITS_AT_LEVEL, null, null, 15, "5", "Guidebook Table 6", true);

        Course eei6366 = new Course("EEI6366", "Big Data Technologies and Distributed Systems", ee, catI, 6, 4, 3, null, "8", false, true, true, "EEI3366(P), EEI5486(CA), pass in 50 credits at Levels 3 and 4", null);
        courseRepository.save(eei6366);
        addRule(eei6366, 1, "AND", "Database Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3366", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6366, 2, "AND", "Advanced Database CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI5486", "CA", null, null, "Guidebook Table 6", true);
        addRule(eei6366, 3, "AND", "Passed Levels 3 & 4 Credits", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 50, "3,4", "Guidebook Table 6", true);

        Course eei6369 = new Course("EEI6369", "Cloud Computing", ee, catI, 6, 4, 3, null, "8", false, true, true, "EEI3467(P), EEI3372(P), EEI3273(P), EEI4362(CA), pass in 50 credits at Levels 3 and 4", null);
        courseRepository.save(eei6369);
        addRule(eei6369, 1, "AND", "Software Eng Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3467", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6369, 2, "AND", "Python Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3372", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6369, 3, "AND", "Communication Tech Pass", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI3273", "P", null, null, "Guidebook Table 6", true);
        addRule(eei6369, 4, "AND", "Object Oriented Design CA", PrerequisiteRule.RuleType.COURSE_RESULT, "EEI4362", "CA", null, null, "Guidebook Table 6", true);
        addRule(eei6369, 5, "AND", "Passed Levels 3 & 4 Credits", PrerequisiteRule.RuleType.PASSED_CREDITS_AT_LEVEL, null, null, 50, "3,4", "Guidebook Table 6", true);

        // 7. Seed Demo Student
        Student demoStudent = new Student(
                "OUSL-SE-2026-001",
                "S92054321",
                "Nimal Perera",
                "nimal.perera@ousl.lk",
                bse,
                1,
                3
        );
        studentRepository.save(demoStudent);

        log.info("BSEHons catalogue seeded successfully with {} courses.", courseRepository.count());
    }

    private void addRule(Course course, int order, String op, String desc,
                         PrerequisiteRule.RuleType type, String prereqCourseCode, String reqStatus,
                         Integer minCredits, String targetLevels, String note, boolean verified) {
        PrerequisiteRuleGroup group = new PrerequisiteRuleGroup(course, order, op, desc);
        PrerequisiteRule rule = new PrerequisiteRule(group, type, prereqCourseCode, reqStatus, minCredits, targetLevels, note, verified, desc);
        group.addRule(rule);
        ruleGroupRepository.save(group);
    }
}


# UniEligible Database Schema Documentation

## 1. Overview
The **UniEligible** database is a normalized PostgreSQL relational schema designed to store the versioned BSEHons curriculum, academic departments, course categories, prerequisite rule trees, student academic records, verified exemptions, and concurrent registrations.

---

## 2. Entity-Relationship (ER) Diagram

```mermaid
erDiagram
    PROGRAMMES ||--o{ STUDENTS : enrolled_in
    PROGRAMMES ||--o{ GRADUATION_REQUIREMENTS : defines
    DEPARTMENTS ||--o{ COURSES : offers
    COURSE_CATEGORIES ||--o{ COURSES : classifies
    ACADEMIC_YEARS ||--o{ COURSES : contains

    COURSES ||--o{ PREREQUISITE_RULE_GROUPS : requires
    PREREQUISITE_RULE_GROUPS ||--o{ PREREQUISITE_RULES : contains

    STUDENTS ||--o{ STUDENT_ACADEMIC_RECORDS : achieves
    COURSES ||--o{ STUDENT_ACADEMIC_RECORDS : graded_in

    STUDENTS ||--o{ STUDENT_EXEMPTIONS : receives
    STUDENTS ||--o{ CONCURRENT_REGISTRATIONS : enrolls
    STUDENTS ||--o{ ELIGIBILITY_CHECKS : audits
    ELIGIBILITY_CHECKS ||--o{ ELIGIBILITY_CHECK_REASONS : details

    PROGRAMMES {
        string id PK "BSEHONS"
        string name
        string faculty
        int min_total_credits
        int max_annual_credits
        string curriculum_version
    }

    ACADEMIC_YEARS {
        int year_number PK "1, 2, 3, 4"
        int level_number "3, 4, 5, 6"
        string title
        text description
    }

    DEPARTMENTS {
        string code PK "EE, AG, CV, MH, DM, TA, LL, LT"
        string name
    }

    COURSE_CATEGORIES {
        string code PK "I, M, J, Z, R, W, L, E, S"
        string name
        int min_credits
        int max_credits
    }

    COURSES {
        string code PK "EEI3346"
        string name
        string department_code FK
        string category_code FK
        int course_level
        int academic_year
        int credits
        string credit_rating_note
        string semester
        boolean is_compulsory
        boolean is_degree_credit
        boolean is_gpa_contributing
        boolean is_virtual
        string prerequisite_display_text
        text remarks
        boolean is_offered
        string delivery_center
        text special_restriction
        text discrepancy_flag
    }

    PREREQUISITE_RULE_GROUPS {
        bigint id PK
        string course_code FK
        int group_order
        string logical_operator "AND / OR"
        string description
    }

    PREREQUISITE_RULES {
        bigint id PK
        bigint group_id FK
        string rule_type "COURSE_RESULT, CONCURRENT_REGISTRATION, etc."
        string prerequisite_course_code
        string required_status "P, CA, CR"
        int min_credits
        string target_levels "3, 4, 5, 6, 3,4"
        string rule_note
        boolean is_verified
        string description
    }

    STUDENTS {
        string id PK "OUSL-SE-2026-001"
        string registration_number UK
        string full_name
        string email
        string programme_id FK
        int current_academic_year
        int current_academic_level
        timestamp created_at
    }

    STUDENT_ACADEMIC_RECORDS {
        bigint id PK
        string student_id FK
        string course_code FK
        string result_status "PA, FA, CA, NOT_ATTEMPTED"
        string academic_year_taken
        timestamp updated_at
    }

    STUDENT_EXEMPTIONS {
        bigint id PK
        string student_id FK
        string course_code
        string course_name
        string category_code
        int course_level
        int credit_value
        string exemption_reference
        date granted_date
        boolean is_verified
    }

    CONCURRENT_REGISTRATIONS {
        bigint id PK
        string student_id FK
        string course_code
        string academic_session "2026/2027"
        boolean is_active
        timestamp registered_at
    }

    ELIGIBILITY_CHECKS {
        bigint id PK
        string student_id
        string course_code
        string status "ELIGIBLE, NOT_ELIGIBLE, etc."
        timestamp checked_at
    }

    ELIGIBILITY_CHECK_REASONS {
        bigint id PK
        bigint eligibility_check_id FK
        string rule_description
        string reason_message
        boolean is_satisfied
    }
```

---

## 3. Relational Table Specifications

### 3.1 `programmes`
Stores degree programme metadata and global graduation thresholds.
- `id` (VARCHAR(20), PK): Programme identifier (e.g. `BSEHONS`).
- `name` (VARCHAR(200), NOT NULL): Degree programme name.
- `faculty` (VARCHAR(100)): Faculty offering the programme.
- `min_total_credits` (INTEGER, Default: 125): Graduation credit target.
- `max_annual_credits` (INTEGER, Default: 38): Annual registration limit.
- `curriculum_version` (VARCHAR(20)): Syllabus version (`2026/2027`).

### 3.2 `courses`
Holds all course modules across Levels 2 through 6.
- `code` (VARCHAR(20), PK): Unique course code (e.g. `EEI3346`).
- `name` (VARCHAR(200), NOT NULL): Course title.
- `department_code` (VARCHAR(10), FK -> `departments.code`): Department offering the module.
- `category_code` (VARCHAR(5), FK -> `course_categories.code`): Official category code.
- `course_level` (INTEGER, NOT NULL): Academic level (`2`, `3`, `4`, `5`, `6`).
- `academic_year` (INTEGER, NOT NULL): Academic year (`1`, `2`, `3`, `4`).
- `credits` (INTEGER, NOT NULL): Credit value.
- `credit_rating_note` (VARCHAR(20)): Rating notes, such as `2*` for `MHZ2250`.
- `is_degree_credit` (BOOLEAN, NOT NULL): `false` for `MHZ2250` (does not contribute to graduation credits); `true` for standard courses.
- `is_compulsory` (BOOLEAN, NOT NULL): `true` for compulsory; `false` for electives.
- `is_offered` (BOOLEAN, NOT NULL): Offering status for 2026/2027 session (`false` for `EEI5369`, `EEI6279`).
- `delivery_center` (VARCHAR(100)): Venue constraints (`ALL` or `CRC only`).

### 3.3 `prerequisite_rule_groups` & `prerequisite_rules`
Implements modular composite prerequisite evaluation:
- `group_order` (INTEGER): Sequence of evaluation.
- `logical_operator` (VARCHAR(10)): `AND` or `OR` within the rule group.
- `rule_type` (VARCHAR(35)):
  - `COURSE_RESULT` (checks result status against `required_status`)
  - `CONCURRENT_REGISTRATION` (checks co-enrollment in current session)
  - `PASSED_CREDITS_TOTAL` (total passed credits threshold)
  - `PASSED_CREDITS_AT_LEVEL` (level-specific passed credits)
  - `CA_CREDITS_AT_LEVEL` (level-specific continuous assessment credits)
  - `ADMIN_VERIFICATION_REQUIRED` (unverified rule flag)
  - `SPECIAL_RESTRICTION` (advisory restriction check)

### 3.4 `student_academic_records`
Maintains individual student examination and assessment results.
- `student_id` (VARCHAR(50), FK -> `students.id`)
- `course_code` (VARCHAR(20), FK -> `courses.code`)
- `result_status` (VARCHAR(20), NOT NULL): `PA` (Passed), `CA` (Continuous Assessment Pass), `FA` (Failed), `NOT_ATTEMPTED`.
- Constraint: `UNIQUE(student_id, course_code)` guarantees idempotent result updates without duplicate rows.


# OUSL BSEHons Academic Regulations & Eligibility Rules Reference

## 1. Overview
This document specifies the academic regulations, prerequisite evaluation logic, and credit calculation rules implemented in **UniEligible**, grounded in the official **Open University of Sri Lanka (OUSL) Bachelor of Software Engineering Honours (BSEHons) Student Guidebook 2026/2027**.

---

## 2. Degree Programme Structure
The BSEHons degree spans four academic years corresponding directly to four academic levels:

| Academic Year | Course Level | Stage Focus |
|---|---|---|
| **Year 1** | Level 3 | Foundational Software Engineering, Programming, Databases, Mathematics & English |
| **Year 2** | Level 4 | Intermediate Systems, Data Structures & Algorithms, UX, AI, Security & Group Design |
| **Year 3** | Level 5 | Operating Systems, Testing & QA, Advanced DB, Research Methods & Industrial Training |
| **Year 4** | Level 6 | Final Year Project, Software Architecture & Design, SPM, Professional Practice & Electives |

---

## 3. Student Result Status System
For each subject in the curriculum, students record one mutually exclusive academic examination result:

- **PA (Passed):** Successfully passed the final examination and continuous assessment.
  - Satisfies `P` prerequisite conditions.
  - Satisfies `CA` prerequisite conditions (as passing the course requires satisfying continuous assessment).
  - Contributes to earned degree credit totals (if course is `isDegreeCredit = true`).
- **CA (Continuous Assessment Pass):** Obtained continuous assessment eligibility, but has not yet passed the final examination.
  - Satisfies `CA` prerequisite conditions.
  - Does **not** satisfy `P` prerequisite conditions.
  - Does **not** contribute to final earned degree credits. Tracked separately as pending CA credits.
- **FA (Failed):** Failed the continuous assessment and/or final examination.
  - Satisfies neither `P` nor `CA` prerequisites.
- **NOT_ATTEMPTED (Empty):** Student has not registered or attempted the subject.

### Concurrent Registration (CR) Tracking
- `CR` is **not** an examination result.
- Concurrent registration is tracked separately in the `concurrent_registrations` table.
- When a subject prerequisite specifies `(CR)`, the engine verifies whether the required course is actively enrolled in the current academic session or previously completed.

---

## 4. Prerequisite Evaluation Engine
The prerequisite engine models prerequisites as structured rule groups rather than fragile string matches:

1. **Logical Groups:**
   - A course contains one or more `PrerequisiteRuleGroup` entries.
   - Across groups, an **AND** relation is enforced (all groups must be satisfied).
   - Within each group, the group operator can be **AND** or **OR**.
2. **Rule Types Supported:**
   - `COURSE_RESULT`: Checks for specific course result (`P`, `CA`, or `CR`).
   - `CONCURRENT_REGISTRATION`: Checks active concurrent registration in `ConcurrentRegistration` table.
   - `PASSED_CREDITS_TOTAL`: Checks total earned + recognized credits across all levels.
   - `PASSED_CREDITS_AT_LEVEL`: Checks passed credits at specific target levels (e.g. `18` at Level 3, `45` at Levels 3 & 4, `50` at Levels 3 & 4).
   - `CA_CREDITS_AT_LEVEL`: Checks continuous assessment credits at specific levels (e.g. `15` CA credits at Level 4, `15` CA credits at Level 5).
   - `EXEMPTION_CONDITION`: Checks verified exemptions granted by the OUSL Senate.
   - `ADMIN_VERIFICATION_REQUIRED`: Used for flagged guidebook codes (e.g. `EE4360` in `EEI6320`) requiring official department verification.
   - `SPECIAL_RESTRICTION`: Restricts courses like `EER4489` requiring acceptable justification.

### Detailed Eligibility Outcomes
- **ELIGIBLE:** All prerequisite groups and credit thresholds are met.
- **NOT_ELIGIBLE:** One or more prerequisite conditions are unmet. The engine produces human-readable explanations detailing the exact missing courses, required statuses, and credit deficits.
- **PENDING_INFORMATION:** Unverified administrative flags exist.
- **CURRENTLY_NOT_OFFERED:** The course is not offered in the active academic session (e.g. `EEI5369`, `EEI6279` in 2026/2027).

---

## 5. Graduation Credit Requirements (Guidebook Table 1)
To qualify for the award of the Bachelor of Software Engineering Honours degree, a candidate must satisfy:

### Core Degree Targets:
1. **Minimum Total Degree Credits:** **125 credits**
2. **Levels 5 & 6 Minimum Credits:** **63 credits**
3. **Level 6 Minimum Credits:** **30 credits**
4. **Level 6 Elective Credits:** At least **8 credits** selected from Level 6 electives.
5. **Level 6 Project Requirement:** At least **6 project credits** must be at Level 6 (`EER6689` Final Project).
6. **Industrial Training Requirement:** Mandatory completion of `EEW5611` Industrial Training (6 credits).
7. **Maximum Annual Registration Limit:** **38 credits** per academic year.

### Course Category Bounds (Guidebook Table 1):
Degree minimum and maximum credit requirements are enforced strictly by **Course Category**, not department:

| Category Code | Category Name | Minimum Credits | Maximum Credits |
|---|---|---:|---:|
| **I** | Industrial | 67 | 78 |
| **M** | Management | 11 | 20 |
| **J** | General | 3 | 6 |
| **Z** | Mathematics | 12 | 12 |
| **R** | Projects | 9 | 13 |
| **W** | Industrial Training | 6 | 6 |
| **L / E** | Language / English | 6 | 6 |

### Additional Successful Completion Conditions (Section 4.3):
- At least **15 credits** completed at Level 6.
- At least **32 credits** completed at Levels 5 and 6.
- At least **63 credits** completed from Levels 3–6.

---

## 6. Special Academic Rules & Handled Guidebook Discrepancies

### MHZ2250 Credit Rating Exception
- `MHZ2250` (Elementary Mathematics for Computing) is a Level 2 foundational course with a `2*` rating.
- Under BSEHons degree regulations, its credits **do not contribute** towards graduation credit totals (`isDegreeCredit = false`).

### LTE34SI Non-GPA Exception
- Most non-GPA courses do not contribute to category degree credit totals.
- `LTE34SI` (English for Academic Purposes – Intermediate) is an explicit exception whose 4 credits count towards the official Language/English category credit totals.

### EEI6320 vs EE4360(P) Discrepancy Flag
- The guidebook row for `EEI6320` lists prerequisite code `EE4360(P)`.
- Rather than silently modifying the syllabus data, UniEligible preserves this rule with an administrative verification flag (`ADMIN_VERIFICATION_REQUIRED`) until confirmed with the Department of Electrical & Computer Engineering.

### FDE3024 vs FDE3023 Flag
- The guidebook refers to *Empowering for Independent Learning* as `FDE3024` in one table and `FDE3023` in another paragraph. This discrepancy is highlighted for administrator review.

### EER4489 Special Justification Restriction
- `EER4489` (Higher Diploma Project – Software Engineering, 4 credits) is offered only when the student provides an acceptable justification for not registering for Industrial Training (`EEW5611`).

### CRC Laboratory Venue Restriction
- Laboratory sessions for `EEI5364` (Data Communication) and `EEI5376` (Embedded Systems and IoT) are conducted exclusively at the Colombo Regional Centre (CRC).

### Course Availability in 2026/2027
- `EEI5369` (Mobile App Development with Android) is **not offered** in 2026/2027.
- `EEI6279` (Natural Language Processing) is **not offered** in 2026/2027.


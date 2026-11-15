# UniEligible REST API Documentation

## Base URL
- **Local Development:** `http://localhost:8080/api/v1`
- **Docker Compose:** `http://localhost:8080/api/v1` (or via Frontend reverse proxy `/api/v1`)
- **Production AWS EC2:** `http://<EC2-PUBLIC-IP>:8080/api/v1`

---

## 1. Programmes & Curriculum

### 1.1 List Degree Programmes
- **Method:** `GET /api/v1/programmes`
- **Description:** Returns all academic degree programmes.
- **Response (200 OK):**
```json
[
  {
    "id": "BSEHONS",
    "name": "Bachelor of Software Engineering Honours",
    "faculty": "Faculty of Engineering Technology",
    "minTotalCredits": 125,
    "maxAnnualCredits": 38,
    "curriculumVersion": "2026/2027"
  }
]
```

### 1.2 Get Academic Years
- **Method:** `GET /api/v1/academic-years`
- **Response (200 OK):**
```json
[
  {
    "yearNumber": 1,
    "levelNumber": 3,
    "title": "Year 1 – Level 3",
    "description": "Foundational software engineering, programming, databases, and mathematics."
  },
  {
    "yearNumber": 2,
    "levelNumber": 4,
    "title": "Year 2 – Level 4",
    "description": "Intermediate software engineering, data structures, UI/UX, AI, and economics."
  }
]
```

---

## 2. Course Catalogue

### 2.1 List Courses
- **Method:** `GET /api/v1/courses`
- **Query Parameters:**
  - `year` *(optional, integer)*: Filter courses by academic year (`1`, `2`, `3`, `4`).
- **Response (200 OK):**
```json
[
  {
    "code": "EEI3346",
    "name": "Web Application Development",
    "departmentCode": "EE",
    "departmentName": "Electrical and Computer Engineering",
    "categoryCode": "I",
    "categoryName": "Industrial",
    "level": 3,
    "academicYear": 1,
    "credits": 3,
    "creditRatingNote": null,
    "semester": "1",
    "compulsory": true,
    "degreeCredit": true,
    "gpaContributing": true,
    "virtual": false,
    "prerequisiteDisplayText": "None",
    "remarks": null,
    "offered": true,
    "deliveryCenter": "ALL",
    "specialRestriction": null,
    "discrepancyFlag": null
  }
]
```

### 2.2 Get Course by Code
- **Method:** `GET /api/v1/courses/{courseCode}`
- **Response (200 OK):** Detailed course information.
- **Response (404 Not Found):** If course code does not exist.

---

## 3. Student Profile & Results Management

### 3.1 Get Student Profile
- **Method:** `GET /api/v1/students/{studentId}`
- **Response (200 OK):**
```json
{
  "id": "OUSL-SE-2026-001",
  "registrationNumber": "S92054321",
  "fullName": "Nimal Perera",
  "email": "nimal.perera@ousl.lk",
  "programmeId": "BSEHONS",
  "programmeName": "Bachelor of Software Engineering Honours",
  "currentAcademicYear": 1,
  "currentAcademicLevel": 3
}
```

### 3.2 Create or Update Student Profile
- **Method:** `POST /api/v1/students`
- **Request Body:**
```json
{
  "id": "OUSL-SE-2026-001",
  "registrationNumber": "S92054321",
  "fullName": "Nimal Perera",
  "email": "nimal.perera@ousl.lk",
  "programmeId": "BSEHONS",
  "currentAcademicYear": 1,
  "currentAcademicLevel": 3
}
```

### 3.3 Get Student Results
- **Method:** `GET /api/v1/students/{studentId}/results`
- **Response (200 OK):**
```json
[
  {
    "courseCode": "EEI3346",
    "courseName": "Web Application Development",
    "resultStatus": "PA",
    "level": 3,
    "credits": 3,
    "academicYear": 1,
    "semester": "1",
    "compulsory": true,
    "academicYearTaken": "2026/2027",
    "updatedAt": "2026-11-13T22:50:00"
  }
]
```

### 3.4 Update Subject Result
- **Method:** `PUT /api/v1/students/{studentId}/results/{courseCode}`
- **Request Body:**
```json
{
  "resultStatus": "PA",
  "academicYearTaken": "2026/2027"
}
```
*Valid `resultStatus` values: `PA` (Pass), `CA` (Continuous Assessment Pass), `FA` (Fail), `NOT_ATTEMPTED` (Clear)*.

### 3.5 Batch Update Results
- **Method:** `POST /api/v1/students/{studentId}/results/batch`
- **Request Body:**
```json
{
  "results": [
    { "courseCode": "EEI3346", "resultStatus": "PA", "academicYearTaken": "2026/2027" },
    { "courseCode": "EEI3366", "resultStatus": "CA", "academicYearTaken": "2026/2027" }
  ]
}
```

---

## 4. Eligibility Engine

### 4.1 Evaluate All Subjects Eligibility
- **Method:** `GET /api/v1/students/{studentId}/eligibility`
- **Query Parameters:**
  - `year` *(optional, integer)*: Filter by academic year.
- **Response (200 OK):**
```json
[
  {
    "courseCode": "EEI6369",
    "courseName": "Cloud Computing",
    "academicYear": 4,
    "level": 6,
    "credits": 3,
    "semester": "8",
    "compulsory": false,
    "offered": true,
    "status": "NOT_ELIGIBLE",
    "eligible": false,
    "prerequisiteDisplayText": "EEI3467(P), EEI3372(P), EEI3273(P), EEI4362(CA), pass in 50 credits at Levels 3 and 4",
    "deliveryCenter": "ALL",
    "reasons": [
      "EEI3273 has not been attempted (requires P).",
      "EEI4362 has not obtained the required Continuous Assessment (CA) status.",
      "Student has only 42 passed credits at Level 3,4; 50 are required."
    ],
    "detailedReasons": [
      {
        "ruleDescription": "Software Eng Pass",
        "reasonMessage": "EEI3467(P) (Satisfied)",
        "satisfied": true
      },
      {
        "ruleDescription": "Communication Tech Pass",
        "reasonMessage": "EEI3273 has not been attempted (requires P).",
        "satisfied": false
      }
    ]
  }
]
```

### 4.2 Check Single Subject Eligibility
- **Method:** `POST /api/v1/students/{studentId}/eligibility/check?courseCode=EEI6369`

---

## 5. Credit Progress & Graduation Audit

### 5.1 Cumulative Credit Progress
- **Method:** `GET /api/v1/students/{studentId}/progress`
- **Response (200 OK):**
```json
{
  "totalEarnedCredits": 45,
  "totalExemptedCredits": 0,
  "totalRecognizedCredits": 45,
  "totalCACredits": 6,
  "targetTotalCredits": 125,
  "remainingDegreeCredits": 80,
  "compulsoryCredits": 45,
  "electiveCredits": 0,
  "level6ElectiveCredits": 0,
  "creditsByLevel": {
    "3": 31,
    "4": 14,
    "5": 0,
    "6": 0
  },
  "creditsByCategory": {
    "I": {
      "categoryCode": "I",
      "categoryName": "Industrial",
      "earnedCredits": 25,
      "exemptedCredits": 0,
      "totalRecognizedCredits": 25,
      "minRequired": 67,
      "maxAllowed": 78,
      "satisfied": false,
      "remainingCredits": 42
    }
  },
  "creditsByDepartment": {
    "EE": 39,
    "MH": 6
  },
  "unfulfilledRequirements": [
    "Total Credits: 45/125 required (needs 80 more)",
    "Levels 5 & 6 Credits: 0/63 required (needs 63 more)"
  ]
}
```

### 5.2 Complete Graduation Audit
- **Method:** `GET /api/v1/students/{studentId}/graduation-audit`
- **Response (200 OK):**
```json
{
  "studentId": "OUSL-SE-2026-001",
  "studentName": "Nimal Perera",
  "programmeName": "Bachelor of Software Engineering Honours",
  "targetTotalCredits": 125,
  "totalRecognizedCredits": 125,
  "graduationRequirementsSatisfied": true,
  "statusMessage": "Graduation Requirements Satisfied. The student has met all academic conditions for BSEHons.",
  "requirements": [ ... ],
  "categoryRequirements": [ ... ],
  "outstandingConditions": []
}
```

---

## 6. Health & Monitoring
- **Method:** `GET /actuator/health`
- **Response (200 OK):**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": { "status": "UP" },
    "ping": { "status": "UP" }
  }
}
```


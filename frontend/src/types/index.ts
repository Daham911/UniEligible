export type ResultStatus = 'PA' | 'FA' | 'CA' | 'NOT_ATTEMPTED';

export interface Programme {
  id: string;
  name: string;
  faculty: string;
  minTotalCredits: number;
  maxAnnualCredits: number;
  curriculumVersion: string;
}

export interface AcademicYear {
  yearNumber: number;
  levelNumber: number;
  title: string;
  description: string;
}

export interface Course {
  code: string;
  name: string;
  departmentCode: string;
  departmentName: string;
  categoryCode: string;
  categoryName: string;
  level: number;
  academicYear: number;
  credits: number;
  creditRatingNote?: string;
  semester: string;
  compulsory: boolean;
  degreeCredit: boolean;
  gpaContributing: boolean;
  virtual: boolean;
  prerequisiteDisplayText: string;
  remarks?: string;
  offered: boolean;
  deliveryCenter: string;
  specialRestriction?: string;
  discrepancyFlag?: string;
}

export interface StudentAcademicRecord {
  courseCode: string;
  courseName: string;
  resultStatus: ResultStatus;
  level: number;
  credits: number;
  academicYear: number;
  semester: string;
  compulsory: boolean;
  academicYearTaken?: string;
  updatedAt?: string;
}

export interface EligibilityReasonItem {
  ruleDescription: string;
  reasonMessage: string;
  satisfied: boolean;
}

export interface EligibilityResult {
  courseCode: string;
  courseName: string;
  academicYear: number;
  level: number;
  credits: number;
  semester: string;
  compulsory: boolean;
  offered: boolean;
  status: 'ELIGIBLE' | 'NOT_ELIGIBLE' | 'PENDING_INFORMATION' | 'CURRENTLY_NOT_OFFERED';
  eligible: boolean;
  prerequisiteDisplayText: string;
  deliveryCenter: string;
  specialRestriction?: string;
  discrepancyFlag?: string;
  reasons: string[];
  detailedReasons: EligibilityReasonItem[];
}

export interface CategoryCreditProgress {
  categoryCode: string;
  categoryName: string;
  earnedCredits: number;
  exemptedCredits: number;
  totalRecognizedCredits: number;
  minRequired: number;
  maxAllowed: number;
  satisfied: boolean;
  remainingCredits: number;
}

export interface CreditBreakdown {
  totalEarnedCredits: number;
  totalExemptedCredits: number;
  totalRecognizedCredits: number;
  totalCACredits: number;
  targetTotalCredits: number;
  remainingDegreeCredits: number;
  compulsoryCredits: number;
  electiveCredits: number;
  level6ElectiveCredits: number;
  creditsByLevel: Record<number, number>;
  creditsByCategory: Record<string, CategoryCreditProgress>;
  creditsByDepartment: Record<string, number>;
  unfulfilledRequirements: string[];
}

export interface GraduationRequirementCheck {
  code: string;
  title: string;
  description: string;
  targetValue: number;
  actualValue: number;
  satisfied: boolean;
  ruleReference: string;
}

export interface GraduationAudit {
  studentId: string;
  studentName: string;
  programmeName: string;
  targetTotalCredits: number;
  totalRecognizedCredits: number;
  graduationRequirementsSatisfied: boolean;
  statusMessage: string;
  requirements: GraduationRequirementCheck[];
  categoryRequirements: CategoryCreditProgress[];
  levelCredits: Record<string, number>;
  outstandingConditions: string[];
}

export interface StudentProfile {
  id: string;
  registrationNumber: string;
  fullName: string;
  email: string;
  programmeId: string;
  programmeName: string;
  currentAcademicYear: number;
  currentAcademicLevel: number;
}

export interface StudentExemption {
  id?: number;
  courseCode: string;
  courseName?: string;
  categoryCode?: string;
  level?: number;
  creditValue: number;
  exemptionReference?: string;
  grantedDate?: string;
  verified: boolean;
}

export interface ConcurrentRegistration {
  id?: number;
  courseCode: string;
  courseName?: string;
  academicSession: string;
  active: boolean;
  registeredAt?: string;
}


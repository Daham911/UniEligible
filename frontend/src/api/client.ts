import axios from 'axios';
import {
  Programme,
  AcademicYear,
  Course,
  StudentProfile,
  StudentAcademicRecord,
  EligibilityResult,
  CreditBreakdown,
  GraduationAudit,
  StudentExemption,
  ConcurrentRegistration,
  ResultStatus,
} from '../types';

const API_BASE_URL = import.meta.env.VITE_API_URL || '/api/v1';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const apiClient = {
  // Programmes
  getProgrammes: async (): Promise<Programme[]> => {
    const response = await api.get('/programmes');
    return response.data;
  },

  // Academic Years
  getAcademicYears: async (): Promise<AcademicYear[]> => {
    const response = await api.get('/academic-years');
    return response.data;
  },

  // Courses
  getCourses: async (year?: number): Promise<Course[]> => {
    const params = year ? { year } : {};
    const response = await api.get('/courses', { params });
    return response.data;
  },

  getCourseByCode: async (code: string): Promise<Course> => {
    const response = await api.get(`/courses/${code}`);
    return response.data;
  },

  // Students
  getStudents: async (): Promise<StudentProfile[]> => {
    const response = await api.get('/students');
    return response.data;
  },

  getStudentProfile: async (studentId: string): Promise<StudentProfile> => {
    const response = await api.get(`/students/${studentId}`);
    return response.data;
  },

  saveStudentProfile: async (profile: StudentProfile): Promise<StudentProfile> => {
    const response = await api.post('/students', profile);
    return response.data;
  },

  // Results
  getStudentResults: async (studentId: string): Promise<StudentAcademicRecord[]> => {
    const response = await api.get(`/students/${studentId}/results`);
    return response.data;
  },

  updateResult: async (
    studentId: string,
    courseCode: string,
    resultStatus: ResultStatus,
    academicYearTaken?: string
  ): Promise<StudentAcademicRecord> => {
    const response = await api.put(`/students/${studentId}/results/${courseCode}`, {
      resultStatus,
      academicYearTaken,
    });
    return response.data;
  },

  batchUpdateResults: async (
    studentId: string,
    results: Array<{ courseCode: string; resultStatus: string; academicYearTaken?: string }>
  ): Promise<StudentAcademicRecord[]> => {
    const response = await api.post(`/students/${studentId}/results/batch`, { results });
    return response.data;
  },

  // Eligibility
  getEligibility: async (studentId: string, year?: number): Promise<EligibilityResult[]> => {
    const params = year ? { year } : {};
    const response = await api.get(`/students/${studentId}/eligibility`, { params });
    return response.data;
  },

  checkSingleEligibility: async (studentId: string, courseCode: string): Promise<EligibilityResult> => {
    const response = await api.post(`/students/${studentId}/eligibility/check`, null, {
      params: { courseCode },
    });
    return response.data;
  },

  // Credit Progress & Graduation Audit
  getProgress: async (studentId: string): Promise<CreditBreakdown> => {
    const response = await api.get(`/students/${studentId}/progress`);
    return response.data;
  },

  getGraduationAudit: async (studentId: string): Promise<GraduationAudit> => {
    const response = await api.get(`/students/${studentId}/graduation-audit`);
    return response.data;
  },

  // Exemptions
  getExemptions: async (studentId: string): Promise<StudentExemption[]> => {
    const response = await api.get(`/students/${studentId}/exemptions`);
    return response.data;
  },

  addExemption: async (studentId: string, exemption: StudentExemption): Promise<StudentExemption> => {
    const response = await api.post(`/students/${studentId}/exemptions`, exemption);
    return response.data;
  },

  // Concurrent Registrations
  getConcurrentRegistrations: async (studentId: string): Promise<ConcurrentRegistration[]> => {
    const response = await api.get(`/students/${studentId}/concurrent-registrations`);
    return response.data;
  },

  addConcurrentRegistration: async (studentId: string, cr: ConcurrentRegistration): Promise<ConcurrentRegistration> => {
    const response = await api.post(`/students/${studentId}/concurrent-registrations`, cr);
    return response.data;
  },
};


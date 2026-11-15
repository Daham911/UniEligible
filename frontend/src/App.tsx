import React, { useState, useEffect, useCallback } from 'react';
import {
  Course,
  StudentProfile,
  StudentAcademicRecord,
  EligibilityResult,
  CreditBreakdown,
  GraduationAudit,
  StudentExemption,
  ConcurrentRegistration,
  ResultStatus,
} from './types';
import { apiClient } from './api/client';
import { Navbar } from './components/Navbar';
import { DashboardPage } from './pages/DashboardPage';
import { AcademicResultsPage } from './pages/AcademicResultsPage';
import { GraduationAuditPage } from './pages/GraduationAuditPage';
import { ProfileSettingsPage } from './pages/ProfileSettingsPage';
import { EligibilityDetailsModal } from './components/EligibilityDetailsModal';

export const App: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'dashboard' | 'academic' | 'graduation' | 'profile'>('dashboard');
  const [student, setStudent] = useState<StudentProfile | null>(null);
  const [courses, setCourses] = useState<Course[]>([]);
  const [records, setRecords] = useState<Record<string, StudentAcademicRecord>>({});
  const [eligibilityMap, setEligibilityMap] = useState<Record<string, EligibilityResult>>({});
  const [eligibilityList, setEligibilityList] = useState<EligibilityResult[]>([]);
  const [progress, setProgress] = useState<CreditBreakdown | null>(null);
  const [audit, setAudit] = useState<GraduationAudit | null>(null);
  const [exemptions, setExemptions] = useState<StudentExemption[]>([]);
  const [concurrentRegs, setConcurrentRegs] = useState<ConcurrentRegistration[]>([]);

  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [isRefreshing, setIsRefreshing] = useState<boolean>(false);
  const [isUpdatingCourse, setIsUpdatingCourse] = useState<string | null>(null);
  const [selectedDetails, setSelectedDetails] = useState<EligibilityResult | null>(null);

  // Initialize data
  const loadInitialData = useCallback(async () => {
    try {
      setIsLoading(true);

      // Load courses
      const allCourses = await apiClient.getCourses();
      setCourses(allCourses);

      // Load or create student
      let currentStudent: StudentProfile;
      try {
        const students = await apiClient.getStudents();
        if (students && students.length > 0) {
          currentStudent = students[0];
        } else {
          currentStudent = await apiClient.saveStudentProfile({
            id: 'OUSL-SE-2026-001',
            registrationNumber: 'S92054321',
            fullName: 'Nimal Perera',
            email: 'nimal.perera@ousl.lk',
            programmeId: 'BSEHONS',
            programmeName: 'Bachelor of Software Engineering Honours',
            currentAcademicYear: 1,
            currentAcademicLevel: 3,
          });
        }
      } catch (err) {
        // Fallback default student profile
        currentStudent = {
          id: 'OUSL-SE-2026-001',
          registrationNumber: 'S92054321',
          fullName: 'Nimal Perera',
          email: 'nimal.perera@ousl.lk',
          programmeId: 'BSEHONS',
          programmeName: 'Bachelor of Software Engineering Honours',
          currentAcademicYear: 1,
          currentAcademicLevel: 3,
        };
      }
      setStudent(currentStudent);

      await fetchStudentData(currentStudent.id);
    } catch (error) {
      console.error('Failed to load initial data:', error);
    } finally {
      setIsLoading(false);
    }
  }, []);

  const fetchStudentData = async (studentId: string) => {
    try {
      setIsRefreshing(true);

      const [resRecords, resEligibility, resProgress, resAudit, resExemptions, resConcurrent] =
        await Promise.all([
          apiClient.getStudentResults(studentId),
          apiClient.getEligibility(studentId),
          apiClient.getProgress(studentId),
          apiClient.getGraduationAudit(studentId),
          apiClient.getExemptions(studentId),
          apiClient.getConcurrentRegistrations(studentId),
        ]);

      // Map records by course code
      const recMap: Record<string, StudentAcademicRecord> = {};
      resRecords.forEach((r) => {
        recMap[r.courseCode.toUpperCase()] = r;
      });
      setRecords(recMap);

      // Map eligibility by course code
      const eligMap: Record<string, EligibilityResult> = {};
      resEligibility.forEach((e) => {
        eligMap[e.courseCode.toUpperCase()] = e;
      });
      setEligibilityMap(eligMap);
      setEligibilityList(resEligibility);

      setProgress(resProgress);
      setAudit(resAudit);
      setExemptions(resExemptions);
      setConcurrentRegs(resConcurrent);
    } catch (error) {
      console.error('Failed to fetch student data:', error);
    } finally {
      setIsRefreshing(false);
    }
  };

  useEffect(() => {
    loadInitialData();
  }, [loadInitialData]);

  // Handle student result radio button changes
  const handleResultChange = async (courseCode: string, status: ResultStatus) => {
    if (!student) return;

    try {
      setIsUpdatingCourse(courseCode);

      // Optimistic local update
      const existingRecord = records[courseCode.toUpperCase()];
      const updatedRecord: StudentAcademicRecord = {
        courseCode,
        courseName: existingRecord?.courseName || courseCode,
        resultStatus: status,
        level: existingRecord?.level || 3,
        credits: existingRecord?.credits || 3,
        academicYear: existingRecord?.academicYear || 1,
        semester: existingRecord?.semester || '1',
        compulsory: existingRecord?.compulsory ?? true,
        academicYearTaken: '2026/2027',
      };

      setRecords((prev) => ({
        ...prev,
        [courseCode.toUpperCase()]: updatedRecord,
      }));

      // Call Backend REST API
      await apiClient.updateResult(student.id, courseCode, status, '2026/2027');

      // Refresh eligibility and credit calculations
      await fetchStudentData(student.id);
    } catch (error) {
      console.error(`Failed to update result for ${courseCode}:`, error);
      // Revert if error
      if (student) fetchStudentData(student.id);
    } finally {
      setIsUpdatingCourse(null);
    }
  };

  const handleSaveProfile = async (updated: StudentProfile) => {
    try {
      const saved = await apiClient.saveStudentProfile(updated);
      setStudent(saved);
      await fetchStudentData(saved.id);
    } catch (error) {
      console.error('Failed to save profile:', error);
    }
  };

  const handleAddExemption = async (ex: StudentExemption) => {
    if (!student) return;
    try {
      await apiClient.addExemption(student.id, ex);
      await fetchStudentData(student.id);
    } catch (error) {
      console.error('Failed to add exemption:', error);
    }
  };

  const handleAddRegistration = async (cr: ConcurrentRegistration) => {
    if (!student) return;
    try {
      await apiClient.addConcurrentRegistration(student.id, cr);
      await fetchStudentData(student.id);
    } catch (error) {
      console.error('Failed to add concurrent registration:', error);
    }
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-slate-50 flex flex-col items-center justify-center p-4">
        <div className="w-12 h-12 border-4 border-ousl-blue border-t-ousl-gold rounded-full animate-spin mb-4" />
        <h2 className="text-lg font-bold text-slate-800">UniEligible Academic Engine</h2>
        <p className="text-xs text-slate-500 mt-1">Connecting to OUSL BSEHons 2026/2027 Catalogue...</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-100/70 flex flex-col font-sans">
      <Navbar
        student={student}
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        onRefresh={() => student && fetchStudentData(student.id)}
        isRefreshing={isRefreshing}
      />

      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8">
        {activeTab === 'dashboard' && (
          <DashboardPage
            student={student}
            progress={progress}
            eligibilityList={eligibilityList}
            onNavigateToResults={() => setActiveTab('academic')}
            onNavigateToGraduation={() => setActiveTab('graduation')}
            onViewDetails={(e) => setSelectedDetails(e)}
          />
        )}

        {activeTab === 'academic' && (
          <AcademicResultsPage
            courses={courses}
            records={records}
            eligibilityMap={eligibilityMap}
            progress={progress}
            onResultChange={handleResultChange}
            onViewDetails={(e) => setSelectedDetails(e)}
            isUpdatingCourse={isUpdatingCourse}
          />
        )}

        {activeTab === 'graduation' && (
          <GraduationAuditPage audit={audit} progress={progress} />
        )}

        {activeTab === 'profile' && (
          <ProfileSettingsPage
            student={student}
            exemptions={exemptions}
            concurrentRegs={concurrentRegs}
            onSaveProfile={handleSaveProfile}
            onAddExemption={handleAddExemption}
            onAddRegistration={handleAddRegistration}
          />
        )}
      </main>

      {/* Footer */}
      <footer className="bg-white border-t border-slate-200 py-6 text-slate-500 text-xs">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col sm:flex-row items-center justify-between gap-2 text-center sm:text-left">
          <div>
            <span className="font-bold text-slate-800">UniEligible</span> • Open University of Sri Lanka (OUSL)
            <div className="text-[11px] text-slate-400">
              Department of Electrical & Computer Engineering • Faculty of Engineering Technology
            </div>
          </div>
          <div className="text-[11px] text-slate-400">
            Advisory Academic Planning Tool • BSEHons Curriculum Version 2026/2027
          </div>
        </div>
      </footer>

      {/* Prerequisite & Eligibility Details Modal */}
      <EligibilityDetailsModal
        result={selectedDetails}
        onClose={() => setSelectedDetails(null)}
      />
    </div>
  );
};

export default App;


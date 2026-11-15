import React from 'react';
import { CreditBreakdown, EligibilityResult, StudentProfile } from '../types';
import { CreditStatsOverview } from '../components/CreditStatsOverview';
import { DisclaimerBanner } from '../components/DisclaimerBanner';
import { CheckCircle2, XCircle, ArrowRight, BookOpen, AlertTriangle, ShieldCheck, Sparkles } from 'lucide-react';

interface DashboardPageProps {
  student: StudentProfile | null;
  progress: CreditBreakdown | null;
  eligibilityList: EligibilityResult[];
  onNavigateToResults: () => void;
  onNavigateToGraduation: () => void;
  onViewDetails: (eligibility: EligibilityResult) => void;
}

export const DashboardPage: React.FC<DashboardPageProps> = ({
  student,
  progress,
  eligibilityList,
  onNavigateToResults,
  onNavigateToGraduation,
  onViewDetails,
}) => {
  const eligibleCourses = eligibilityList.filter((e) => e.eligible && e.academicYear > (student?.currentAcademicYear || 1));
  const ineligibleCourses = eligibilityList.filter(
    (e) => !e.eligible && e.status === 'NOT_ELIGIBLE' && e.academicYear > (student?.currentAcademicYear || 1)
  );

  return (
    <div className="space-y-6">
      <DisclaimerBanner />

      {/* Hero Welcome Banner */}
      <div className="bg-gradient-to-r from-ousl-blue via-ousl-navy to-slate-900 text-white rounded-2xl p-6 sm:p-8 shadow-md relative overflow-hidden">
        <div className="absolute right-0 top-0 bottom-0 w-1/3 bg-white/5 transform skew-x-12 pointer-events-none" />
        <div className="relative z-10 max-w-2xl">
          <div className="inline-flex items-center space-x-2 bg-ousl-gold/20 border border-ousl-gold/40 text-ousl-gold text-xs font-bold px-3 py-1 rounded-full mb-3">
            <Sparkles className="w-3.5 h-3.5" />
            <span>Academic Year 2026/2027 • OUSL BSEHons</span>
          </div>
          <h1 className="text-2xl sm:text-3xl font-black tracking-tight text-white">
            Welcome, {student ? student.fullName : 'Student'}!
          </h1>
          <p className="text-slate-300 text-xs sm:text-sm mt-2 leading-relaxed">
            Track your degree progress, enter past academic results (PA, CA, FA), and automatically calculate your eligibility for upcoming Bachelor of Software Engineering Honours subjects.
          </p>

          <div className="mt-5 flex flex-wrap gap-3">
            <button
              onClick={onNavigateToResults}
              className="px-4 py-2 bg-ousl-gold hover:bg-yellow-500 text-ousl-navy font-bold text-xs sm:text-sm rounded-xl shadow transition-colors flex items-center space-x-2"
            >
              <BookOpen className="w-4 h-4" />
              <span>Update Results & Check Eligibility</span>
              <ArrowRight className="w-4 h-4 ml-1" />
            </button>
            <button
              onClick={onNavigateToGraduation}
              className="px-4 py-2 bg-white/10 hover:bg-white/20 text-white font-medium text-xs sm:text-sm rounded-xl border border-white/20 transition-colors flex items-center space-x-2"
            >
              <ShieldCheck className="w-4 h-4 text-emerald-400" />
              <span>Graduation Compliance Audit</span>
            </button>
          </div>
        </div>
      </div>

      {/* Core Credit Statistics */}
      <CreditStatsOverview progress={progress} />

      {/* Two-Column Grid: Eligible vs Ineligible Upcoming Courses */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Eligible Upcoming Subjects */}
        <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center space-x-2">
              <CheckCircle2 className="w-5 h-5 text-emerald-600" />
              <h3 className="font-bold text-slate-900 text-base">Eligible Upcoming Subjects</h3>
            </div>
            <span className="text-xs font-bold bg-emerald-50 text-emerald-700 px-2.5 py-0.5 rounded-full border border-emerald-200">
              {eligibleCourses.length} Courses
            </span>
          </div>
          <p className="text-xs text-slate-500 mb-4">
            Subjects for subsequent academic years where you meet all prerequisite course and credit thresholds.
          </p>

          {eligibleCourses.length === 0 ? (
            <div className="text-center py-8 text-slate-400 text-xs bg-slate-50 rounded-xl border border-dashed border-slate-200">
              No higher-level subjects currently eligible. Complete prerequisite subjects in earlier years first.
            </div>
          ) : (
            <div className="space-y-2.5 max-h-96 overflow-y-auto custom-scrollbar pr-1">
              {eligibleCourses.slice(0, 8).map((course) => (
                <div
                  key={course.courseCode}
                  onClick={() => onViewDetails(course)}
                  className="p-3 rounded-xl border border-slate-200/90 hover:border-emerald-300 hover:bg-emerald-50/30 transition-all flex items-center justify-between cursor-pointer group"
                >
                  <div className="flex items-center space-x-3">
                    <span className="font-mono font-bold text-xs bg-slate-100 text-slate-800 px-2 py-1 rounded">
                      {course.courseCode}
                    </span>
                    <div>
                      <div className="font-semibold text-xs text-slate-800 group-hover:text-emerald-900">
                        {course.courseName}
                      </div>
                      <div className="text-[11px] text-slate-400">
                        Year {course.academicYear} • Level {course.level} • {course.credits} Credits
                      </div>
                    </div>
                  </div>
                  <span className="text-emerald-600 text-xs font-semibold flex items-center space-x-1">
                    <span>Eligible</span>
                    <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
                  </span>
                </div>
              ))}
              {eligibleCourses.length > 8 && (
                <button
                  onClick={onNavigateToResults}
                  className="w-full text-center py-2 text-xs font-semibold text-ousl-blue hover:underline"
                >
                  View all {eligibleCourses.length} eligible courses →
                </button>
              )}
            </div>
          )}
        </div>

        {/* Ineligible Subjects with Missing Prerequisites */}
        <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center space-x-2">
              <XCircle className="w-5 h-5 text-rose-600" />
              <h3 className="font-bold text-slate-900 text-base">Ineligible Subjects & Missing Prerequisites</h3>
            </div>
            <span className="text-xs font-bold bg-rose-50 text-rose-700 px-2.5 py-0.5 rounded-full border border-rose-200">
              {ineligibleCourses.length} Courses
            </span>
          </div>
          <p className="text-xs text-slate-500 mb-4">
            Click on any course to see the exact missing prerequisite subjects, CA status, or credit requirements.
          </p>

          {ineligibleCourses.length === 0 ? (
            <div className="text-center py-8 text-slate-400 text-xs bg-slate-50 rounded-xl border border-dashed border-slate-200">
              No ineligible subjects found!
            </div>
          ) : (
            <div className="space-y-2.5 max-h-96 overflow-y-auto custom-scrollbar pr-1">
              {ineligibleCourses.slice(0, 8).map((course) => (
                <div
                  key={course.courseCode}
                  onClick={() => onViewDetails(course)}
                  className="p-3 rounded-xl border border-slate-200/90 hover:border-rose-300 hover:bg-rose-50/30 transition-all flex items-start justify-between cursor-pointer group"
                >
                  <div className="flex items-start space-x-3">
                    <span className="font-mono font-bold text-xs bg-slate-100 text-slate-800 px-2 py-1 rounded mt-0.5">
                      {course.courseCode}
                    </span>
                    <div>
                      <div className="font-semibold text-xs text-slate-800 group-hover:text-rose-900">
                        {course.courseName}
                      </div>
                      <div className="text-[11px] text-rose-600 mt-0.5 line-clamp-1">
                        {course.reasons && course.reasons.length > 0 ? course.reasons[0] : 'Missing prerequisites'}
                      </div>
                    </div>
                  </div>
                  <span className="text-rose-600 text-xs font-semibold flex items-center space-x-1 flex-shrink-0 ml-2">
                    <span>Inspect</span>
                    <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
                  </span>
                </div>
              ))}
              {ineligibleCourses.length > 8 && (
                <button
                  onClick={onNavigateToResults}
                  className="w-full text-center py-2 text-xs font-semibold text-rose-600 hover:underline"
                >
                  View all {ineligibleCourses.length} ineligible courses →
                </button>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

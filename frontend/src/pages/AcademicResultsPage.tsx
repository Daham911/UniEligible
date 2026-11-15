import React from 'react';
import { Course, EligibilityResult, ResultStatus, StudentAcademicRecord, CreditBreakdown } from '../types';
import { YearTabs } from '../components/YearTabs';
import { CreditStatsOverview } from '../components/CreditStatsOverview';
import { DisclaimerBanner } from '../components/DisclaimerBanner';
import { Sparkles, HelpCircle } from 'lucide-react';

interface AcademicResultsPageProps {
  courses: Course[];
  records: Record<string, StudentAcademicRecord>;
  eligibilityMap: Record<string, EligibilityResult>;
  progress: CreditBreakdown | null;
  onResultChange: (courseCode: string, status: ResultStatus) => void;
  onViewDetails: (eligibility: EligibilityResult) => void;
  isUpdatingCourse?: string | null;
}

export const AcademicResultsPage: React.FC<AcademicResultsPageProps> = ({
  courses,
  records,
  eligibilityMap,
  progress,
  onResultChange,
  onViewDetails,
  isUpdatingCourse,
}) => {
  return (
    <div className="space-y-6">
      <DisclaimerBanner />

      {/* Guide Banner */}
      <div className="bg-white rounded-2xl border border-slate-200 p-5 sm:p-6 shadow-sm">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h1 className="text-xl sm:text-2xl font-black text-slate-900 tracking-tight">
              Academic Results & Subject Eligibility Engine
            </h1>
            <p className="text-xs sm:text-sm text-slate-500 mt-1 max-w-3xl">
              Select your academic results (<strong>PA</strong>: Passed exam, <strong>CA</strong>: Continuous Assessment Pass, <strong>FA</strong>: Failed) for each subject year by year. Eligibility for subsequent subjects and accumulated credit totals update automatically in real-time.
            </p>
          </div>

          <div className="flex items-center space-x-2 text-xs text-slate-500 bg-slate-50 px-3 py-2 rounded-xl border border-slate-200">
            <HelpCircle className="w-4 h-4 text-ousl-accent flex-shrink-0" />
            <span>Switching tabs never resets previously accumulated credits.</span>
          </div>
        </div>
      </div>

      {/* Credit Summary Stats */}
      <CreditStatsOverview progress={progress} />

      {/* Four Year Tabs */}
      <YearTabs
        courses={courses}
        records={records}
        eligibilityMap={eligibilityMap}
        onResultChange={onResultChange}
        onViewDetails={onViewDetails}
        isUpdatingCourse={isUpdatingCourse}
      />
    </div>
  );
};


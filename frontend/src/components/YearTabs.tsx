import React, { useState } from 'react';
import { Course, EligibilityResult, ResultStatus, StudentAcademicRecord } from '../types';
import { CourseTable } from './CourseTable';
import { Calendar, CheckCircle2, ChevronDown, ChevronUp, Layers, Sparkles } from 'lucide-react';

interface YearTabsProps {
  courses: Course[];
  records: Record<string, StudentAcademicRecord>;
  eligibilityMap: Record<string, EligibilityResult>;
  onResultChange: (courseCode: string, status: ResultStatus) => void;
  onViewDetails: (eligibility: EligibilityResult) => void;
  isUpdatingCourse?: string | null;
}

const YEAR_METADATA = [
  {
    yearNumber: 1,
    levelNumber: 3,
    title: 'Year 1 – Level 3',
    subtitle: 'Foundational Software Engineering & Computing',
    compulsoryCreditsTarget: 31,
  },
  {
    yearNumber: 2,
    levelNumber: 4,
    title: 'Year 2 – Level 4',
    subtitle: 'Data Structures, AI, Security & Architecture',
    compulsoryCreditsTarget: 31,
  },
  {
    yearNumber: 3,
    levelNumber: 5,
    title: 'Year 3 – Level 5',
    subtitle: 'Operating Systems, Testing, Advanced DB & Industrial Training',
    compulsoryCreditsTarget: 31,
  },
  {
    yearNumber: 4,
    levelNumber: 6,
    title: 'Year 4 – Level 6',
    subtitle: 'Final Project, SPM, Software Architecture & Electives (≥8 cr)',
    compulsoryCreditsTarget: 20,
  },
];

export const YearTabs: React.FC<YearTabsProps> = ({
  courses,
  records,
  eligibilityMap,
  onResultChange,
  onViewDetails,
  isUpdatingCourse,
}) => {
  const [activeYear, setActiveYear] = useState<number>(1);
  const [collapsedYears, setCollapsedYears] = useState<Record<number, boolean>>({});

  const toggleCollapse = (yearNum: number) => {
    setCollapsedYears((prev) => ({
      ...prev,
      [yearNum]: !prev[yearNum],
    }));
  };

  return (
    <div>
      {/* Year Selection Tabs */}
      <div className="flex border-b border-slate-200 bg-white rounded-t-2xl px-2 pt-2 gap-1 sm:gap-2 overflow-x-auto custom-scrollbar shadow-sm">
        {YEAR_METADATA.map((meta) => {
          const isActive = activeYear === meta.yearNumber;

          // Calculate completed credits in this year
          const yearCourses = courses.filter((c) => c.academicYear === meta.yearNumber);
          const earnedInYear = yearCourses.reduce((sum, c) => {
            const r = records[c.code.toUpperCase()];
            if (r && r.resultStatus === 'PA' && c.degreeCredit) {
              return sum + c.credits;
            }
            return sum;
          }, 0);

          return (
            <button
              key={meta.yearNumber}
              onClick={() => setActiveYear(meta.yearNumber)}
              className={`flex-1 min-w-[170px] py-3 sm:py-4 px-3 sm:px-4 text-left border-b-2 transition-all rounded-t-xl relative ${
                isActive
                  ? 'border-ousl-blue bg-slate-50 text-ousl-blue font-bold shadow-sm'
                  : 'border-transparent text-slate-600 hover:text-slate-900 hover:bg-slate-50/60 font-medium'
              }`}
            >
              <div className="flex items-center justify-between">
                <span className="text-xs uppercase font-extrabold tracking-wider text-slate-400">
                  Level {meta.levelNumber}
                </span>
                <span className="text-xs font-bold px-2 py-0.5 rounded-full bg-slate-100 text-slate-700">
                  {earnedInYear} cr
                </span>
              </div>
              <div className="text-sm sm:text-base font-bold mt-0.5 leading-snug">{meta.title}</div>
            </button>
          );
        })}
      </div>

      {/* Active Year Content */}
      {YEAR_METADATA.map((meta) => {
        if (activeYear !== meta.yearNumber) return null;

        const yearCourses = courses.filter((c) => c.academicYear === meta.yearNumber);
        const compulsoryCourses = yearCourses.filter((c) => c.compulsory);
        const electiveCourses = yearCourses.filter((c) => !c.compulsory);

        // Stats for current year
        const yearCompulsoryCredits = compulsoryCourses.reduce((acc, c) => acc + (c.degreeCredit ? c.credits : 0), 0);
        const yearPassedCredits = yearCourses.reduce((sum, c) => {
          const r = records[c.code.toUpperCase()];
          return r && r.resultStatus === 'PA' && c.degreeCredit ? sum + c.credits : sum;
        }, 0);
        const yearEligibleCount = yearCourses.filter((c) => eligibilityMap[c.code.toUpperCase()]?.eligible).length;

        const isCollapsed = collapsedYears[meta.yearNumber] || false;

        return (
          <div key={meta.yearNumber} className="bg-slate-50 p-4 sm:p-6 rounded-b-2xl border-x border-b border-slate-200">
            {/* Year Sub-Header Banner */}
            <div className="bg-white p-4 rounded-xl border border-slate-200 shadow-sm mb-6 flex flex-col md:flex-row md:items-center justify-between gap-4">
              <div>
                <div className="flex items-center space-x-2">
                  <h2 className="text-lg font-extrabold text-slate-900">{meta.title} Curriculum</h2>
                  <span className="bg-ousl-blue/10 text-ousl-blue text-xs font-bold px-2.5 py-0.5 rounded-full border border-ousl-blue/20">
                    Academic Year {meta.yearNumber}
                  </span>
                </div>
                <p className="text-xs sm:text-sm text-slate-500 mt-1">{meta.subtitle}</p>
              </div>

              {/* Badges */}
              <div className="flex items-center space-x-3 text-xs">
                <div className="bg-slate-50 px-3 py-2 rounded-lg border border-slate-200">
                  <span className="text-slate-500 block">Passed in Year:</span>
                  <span className="font-bold text-emerald-700 text-sm">
                    {yearPassedCredits} credits
                  </span>
                </div>

                <div className="bg-slate-50 px-3 py-2 rounded-lg border border-slate-200">
                  <span className="text-slate-500 block">Eligible to Register:</span>
                  <span className="font-bold text-ousl-blue text-sm">
                    {yearEligibleCount} / {yearCourses.length} courses
                  </span>
                </div>

                <button
                  onClick={() => toggleCollapse(meta.yearNumber)}
                  className="p-2 text-slate-500 hover:text-slate-800 rounded-lg hover:bg-slate-100 transition-colors"
                  title={isCollapsed ? 'Expand all tables' : 'Collapse all tables'}
                >
                  {isCollapsed ? <ChevronDown className="w-5 h-5" /> : <ChevronUp className="w-5 h-5" />}
                </button>
              </div>
            </div>

            {/* Tables Container */}
            {!isCollapsed && (
              <div className="space-y-6">
                {/* 1. Compulsory Courses */}
                <CourseTable
                  courses={compulsoryCourses}
                  records={records}
                  eligibilityMap={eligibilityMap}
                  onResultChange={onResultChange}
                  onViewDetails={onViewDetails}
                  title="1. Compulsory Courses"
                  badgeText={`Mandatory • ${yearCompulsoryCredits} Credits`}
                  isUpdatingCourse={isUpdatingCourse}
                />

                {/* 2. Elective Courses */}
                {electiveCourses.length > 0 && (
                  <CourseTable
                    courses={electiveCourses}
                    records={records}
                    eligibilityMap={eligibilityMap}
                    onResultChange={onResultChange}
                    onViewDetails={onViewDetails}
                    title="2. Elective Courses"
                    badgeText={
                      meta.yearNumber === 4
                        ? 'At least 8 credits required from Level 6 electives'
                        : 'Optional / Exit Pathways'
                    }
                    isUpdatingCourse={isUpdatingCourse}
                  />
                )}
              </div>
            )}
          </div>
        );
      })}
    </div>
  );
};


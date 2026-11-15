import React from 'react';
import { Course, EligibilityResult, ResultStatus, StudentAcademicRecord } from '../types';
import { EligibilityBadge } from './EligibilityBadge';
import { Check, X, BookOpen, AlertCircle, Info } from 'lucide-react';

interface CourseTableProps {
  courses: Course[];
  records: Record<string, StudentAcademicRecord>;
  eligibilityMap: Record<string, EligibilityResult>;
  onResultChange: (courseCode: string, status: ResultStatus) => void;
  onViewDetails: (eligibility: EligibilityResult) => void;
  title: string;
  badgeText?: string;
  isUpdatingCourse?: string | null;
}

export const CourseTable: React.FC<CourseTableProps> = ({
  courses,
  records,
  eligibilityMap,
  onResultChange,
  onViewDetails,
  title,
  badgeText,
  isUpdatingCourse,
}) => {
  if (courses.length === 0) {
    return (
      <div className="bg-white rounded-xl border border-slate-200 p-6 text-center text-slate-500 text-sm">
        No courses in this section.
      </div>
    );
  }

  return (
    <div className="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden mb-6">
      {/* Table Header Section */}
      <div className="px-5 py-4 border-b border-slate-200 bg-slate-50/80 flex items-center justify-between">
        <div className="flex items-center space-x-2">
          <h3 className="font-bold text-slate-800 text-sm sm:text-base tracking-tight">{title}</h3>
          {badgeText && (
            <span className="bg-slate-200/80 text-slate-700 text-xs font-semibold px-2.5 py-0.5 rounded-full">
              {badgeText}
            </span>
          )}
        </div>
        <div className="text-xs text-slate-500 font-medium">
          Total: <span className="font-bold text-slate-800">{courses.length}</span> courses
        </div>
      </div>

      {/* Responsive Table */}
      <div className="overflow-x-auto">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-slate-100/70 border-b border-slate-200 text-[11px] font-bold text-slate-600 uppercase tracking-wider">
              <th className="py-3 px-4">Code</th>
              <th className="py-3 px-4 min-w-[200px]">Course Name</th>
              <th className="py-3 px-3 text-center">Dept</th>
              <th className="py-3 px-3 text-center">Cat</th>
              <th className="py-3 px-3 text-center">Lvl</th>
              <th className="py-3 px-3 text-center">Cr</th>
              <th className="py-3 px-3 text-center">Sem</th>
              <th className="py-3 px-4 min-w-[180px]">Prerequisites</th>
              <th className="py-3 px-4 min-w-[240px] text-center">Student Result Status</th>
              <th className="py-3 px-4 text-center">Eligibility</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100 text-xs sm:text-sm">
            {courses.map((course) => {
              const record = records[course.code.toUpperCase()];
              const currentStatus: ResultStatus = record ? record.resultStatus : 'NOT_ATTEMPTED';
              const eligibility = eligibilityMap[course.code.toUpperCase()];
              const isUpdating = isUpdatingCourse === course.code;

              return (
                <tr
                  key={course.code}
                  className={`hover:bg-slate-50/80 transition-colors ${
                    isUpdating ? 'opacity-50 pointer-events-none' : ''
                  }`}
                >
                  {/* Code */}
                  <td className="py-3.5 px-4 font-mono font-bold text-slate-900 whitespace-nowrap">
                    {course.code}
                    {course.creditRatingNote && (
                      <span className="ml-1 text-[10px] text-amber-700 bg-amber-50 px-1 py-0.5 rounded border border-amber-200">
                        {course.creditRatingNote}
                      </span>
                    )}
                  </td>

                  {/* Course Name */}
                  <td className="py-3.5 px-4 font-medium text-slate-800">
                    <div className="flex flex-col">
                      <span>{course.name}</span>
                      {course.specialRestriction && (
                        <span className="text-[10px] text-amber-600 flex items-center space-x-1 mt-0.5">
                          <AlertCircle className="w-3 h-3 flex-shrink-0" />
                          <span>Special justification required</span>
                        </span>
                      )}
                      {course.deliveryCenter === 'CRC only' && (
                        <span className="text-[10px] text-blue-600 mt-0.5">
                          CRC Lab only
                        </span>
                      )}
                    </div>
                  </td>

                  {/* Department */}
                  <td className="py-3.5 px-3 text-center">
                    <span
                      className="px-2 py-0.5 rounded text-[11px] font-semibold bg-slate-100 text-slate-700"
                      title={course.departmentName}
                    >
                      {course.departmentCode}
                    </span>
                  </td>

                  {/* Category */}
                  <td className="py-3.5 px-3 text-center">
                    <span
                      className="px-2 py-0.5 rounded text-[11px] font-bold bg-indigo-50 text-indigo-700 border border-indigo-100"
                      title={course.categoryName}
                    >
                      {course.categoryCode}
                    </span>
                  </td>

                  {/* Level */}
                  <td className="py-3.5 px-3 text-center font-semibold text-slate-600">
                    {course.level}
                  </td>

                  {/* Credits */}
                  <td className="py-3.5 px-3 text-center font-bold text-slate-800">
                    {course.credits}
                    {!course.degreeCredit && (
                      <span className="text-[9px] text-red-500 font-normal block">non-deg</span>
                    )}
                  </td>

                  {/* Semester */}
                  <td className="py-3.5 px-3 text-center text-slate-600 whitespace-nowrap">
                    {course.semester}
                  </td>

                  {/* Prerequisites */}
                  <td className="py-3.5 px-4 font-mono text-[11px] text-slate-600">
                    {course.prerequisiteDisplayText && course.prerequisiteDisplayText !== 'None' ? (
                      <span className="bg-slate-100 px-2 py-1 rounded inline-block text-slate-700">
                        {course.prerequisiteDisplayText}
                      </span>
                    ) : (
                      <span className="text-slate-400">None</span>
                    )}
                  </td>

                  {/* Result Status Selector (Mutually Exclusive Radio Group) */}
                  <td className="py-3.5 px-4">
                    <div className="flex items-center justify-center space-x-1.5 sm:space-x-2 bg-slate-100/70 p-1.5 rounded-lg border border-slate-200/80">
                      {/* PA Radio */}
                      <label
                        className={`flex items-center space-x-1 px-2 py-1 rounded text-xs font-semibold cursor-pointer transition-all ${
                          currentStatus === 'PA'
                            ? 'bg-emerald-600 text-white shadow-sm ring-1 ring-emerald-700'
                            : 'bg-white text-slate-600 hover:bg-slate-50 border border-slate-200'
                        }`}
                        title="Passed examination"
                      >
                        <input
                          type="radio"
                          name={`result-${course.code}`}
                          value="PA"
                          checked={currentStatus === 'PA'}
                          onChange={() => onResultChange(course.code, 'PA')}
                          className="sr-only"
                        />
                        <Check className="w-3 h-3" />
                        <span>PA</span>
                      </label>

                      {/* CA Radio */}
                      <label
                        className={`flex items-center space-x-1 px-2 py-1 rounded text-xs font-semibold cursor-pointer transition-all ${
                          currentStatus === 'CA'
                            ? 'bg-amber-500 text-white shadow-sm ring-1 ring-amber-600'
                            : 'bg-white text-slate-600 hover:bg-slate-50 border border-slate-200'
                        }`}
                        title="Continuous Assessment Pass (Pending exam)"
                      >
                        <input
                          type="radio"
                          name={`result-${course.code}`}
                          value="CA"
                          checked={currentStatus === 'CA'}
                          onChange={() => onResultChange(course.code, 'CA')}
                          className="sr-only"
                        />
                        <span>CA</span>
                      </label>

                      {/* FA Radio */}
                      <label
                        className={`flex items-center space-x-1 px-2 py-1 rounded text-xs font-semibold cursor-pointer transition-all ${
                          currentStatus === 'FA'
                            ? 'bg-rose-600 text-white shadow-sm ring-1 ring-rose-700'
                            : 'bg-white text-slate-600 hover:bg-slate-50 border border-slate-200'
                        }`}
                        title="Failed"
                      >
                        <input
                          type="radio"
                          name={`result-${course.code}`}
                          value="FA"
                          checked={currentStatus === 'FA'}
                          onChange={() => onResultChange(course.code, 'FA')}
                          className="sr-only"
                        />
                        <X className="w-3 h-3" />
                        <span>FA</span>
                      </label>

                      {/* Clear / Not Attempted */}
                      {currentStatus !== 'NOT_ATTEMPTED' && (
                        <button
                          type="button"
                          onClick={() => onResultChange(course.code, 'NOT_ATTEMPTED')}
                          className="text-[10px] text-slate-400 hover:text-slate-700 px-1 hover:underline ml-1"
                          title="Reset to not attempted"
                        >
                          Clear
                        </button>
                      )}
                    </div>
                  </td>

                  {/* Eligibility Status Badge */}
                  <td className="py-3.5 px-4 text-center whitespace-nowrap">
                    <EligibilityBadge
                      eligibility={eligibility}
                      onClickDetails={onViewDetails}
                    />
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
};


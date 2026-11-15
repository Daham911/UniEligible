import React from 'react';
import { CreditBreakdown } from '../types';
import { Award, Layers, BookCheck, Clock, TrendingUp } from 'lucide-react';

interface CreditStatsOverviewProps {
  progress: CreditBreakdown | null;
}

export const CreditStatsOverview: React.FC<CreditStatsOverviewProps> = ({ progress }) => {
  if (!progress) return null;

  const targetTotal = progress.targetTotalCredits || 125;
  const recognized = progress.totalRecognizedCredits || 0;
  const percentage = Math.min(100, Math.round((recognized / targetTotal) * 100));

  const l3 = progress.creditsByLevel?.[3] || 0;
  const l4 = progress.creditsByLevel?.[4] || 0;
  const l5 = progress.creditsByLevel?.[5] || 0;
  const l6 = progress.creditsByLevel?.[6] || 0;

  return (
    <div className="bg-white rounded-2xl border border-slate-200 p-5 sm:p-6 shadow-sm mb-6">
      {/* Top Banner & Progress Bar */}
      <div className="mb-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2 mb-2">
          <div>
            <h2 className="text-lg font-bold text-slate-800 flex items-center space-x-2">
              <Award className="w-5 h-5 text-ousl-gold" />
              <span>Academic Degree Credit Progress</span>
            </h2>
            <p className="text-xs text-slate-500">
              Bachelor of Software Engineering Honours (BSEHons) • 125 Credits Target
            </p>
          </div>
          <div className="text-right">
            <span className="text-2xl font-black text-ousl-blue">{recognized}</span>
            <span className="text-sm font-semibold text-slate-400"> / {targetTotal} Credits</span>
            <span className="ml-2 text-xs font-bold text-emerald-600 bg-emerald-50 px-2 py-0.5 rounded-full border border-emerald-200">
              {percentage}%
            </span>
          </div>
        </div>

        {/* Progress Bar */}
        <div className="w-full bg-slate-100 rounded-full h-3 overflow-hidden border border-slate-200/60 flex">
          <div
            className="bg-gradient-to-r from-ousl-blue to-ousl-accent h-full rounded-full transition-all duration-500"
            style={{ width: `${percentage}%` }}
          />
        </div>
      </div>

      {/* Grid of Key Credit Metrics */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 sm:gap-4">
        {/* Total Earned */}
        <div className="bg-slate-50/80 p-3.5 rounded-xl border border-slate-200/80">
          <div className="text-slate-500 text-xs font-medium flex items-center space-x-1.5 mb-1">
            <BookCheck className="w-4 h-4 text-emerald-600" />
            <span>Passed Credits</span>
          </div>
          <div className="text-xl sm:text-2xl font-bold text-slate-900">
            {progress.totalEarnedCredits}
          </div>
          <div className="text-[11px] text-slate-500 mt-0.5">Examination passes (PA)</div>
        </div>

        {/* Recognized incl. Exemptions */}
        <div className="bg-slate-50/80 p-3.5 rounded-xl border border-slate-200/80">
          <div className="text-slate-500 text-xs font-medium flex items-center space-x-1.5 mb-1">
            <TrendingUp className="w-4 h-4 text-ousl-accent" />
            <span>Exempted Credits</span>
          </div>
          <div className="text-xl sm:text-2xl font-bold text-slate-900">
            {progress.totalExemptedCredits}
          </div>
          <div className="text-[11px] text-slate-500 mt-0.5">Senate-verified exemptions</div>
        </div>

        {/* CA Credits Pending Exam */}
        <div className="bg-slate-50/80 p-3.5 rounded-xl border border-slate-200/80">
          <div className="text-slate-500 text-xs font-medium flex items-center space-x-1.5 mb-1">
            <Clock className="w-4 h-4 text-amber-600" />
            <span>Continuous Assess (CA)</span>
          </div>
          <div className="text-xl sm:text-2xl font-bold text-slate-900">
            {progress.totalCACredits}
          </div>
          <div className="text-[11px] text-slate-500 mt-0.5">CA pass (Awaiting final exam)</div>
        </div>

        {/* Remaining to Graduation */}
        <div className="bg-slate-50/80 p-3.5 rounded-xl border border-slate-200/80">
          <div className="text-slate-500 text-xs font-medium flex items-center space-x-1.5 mb-1">
            <Award className="w-4 h-4 text-rose-500" />
            <span>Remaining Credits</span>
          </div>
          <div className="text-xl sm:text-2xl font-bold text-slate-900">
            {progress.remainingDegreeCredits}
          </div>
          <div className="text-[11px] text-slate-500 mt-0.5">Needed for graduation</div>
        </div>
      </div>

      {/* Level Breakdown Pill Bar */}
      <div className="mt-4 pt-4 border-t border-slate-100 flex flex-wrap items-center justify-between gap-3 text-xs">
        <div className="flex items-center space-x-1 text-slate-500 font-medium">
          <Layers className="w-4 h-4 text-slate-400" />
          <span>Credits by Academic Level:</span>
        </div>
        <div className="flex items-center space-x-2">
          <span className="bg-slate-100 text-slate-800 px-2.5 py-1 rounded-md font-semibold border border-slate-200">
            Level 3: <strong className="text-ousl-blue">{l3}</strong> cr
          </span>
          <span className="bg-slate-100 text-slate-800 px-2.5 py-1 rounded-md font-semibold border border-slate-200">
            Level 4: <strong className="text-ousl-blue">{l4}</strong> cr
          </span>
          <span className="bg-slate-100 text-slate-800 px-2.5 py-1 rounded-md font-semibold border border-slate-200">
            Level 5: <strong className="text-ousl-blue">{l5}</strong> cr
          </span>
          <span className="bg-slate-100 text-slate-800 px-2.5 py-1 rounded-md font-semibold border border-slate-200">
            Level 6: <strong className="text-ousl-blue">{l6}</strong> cr
          </span>
        </div>
      </div>
    </div>
  );
};


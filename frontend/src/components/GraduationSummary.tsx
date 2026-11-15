import React from 'react';
import { GraduationAudit, CreditBreakdown } from '../types';
import { Award, CheckCircle2, XCircle, AlertTriangle, Layers, BookOpen, Check, ShieldCheck } from 'lucide-react';

interface GraduationSummaryProps {
  audit: GraduationAudit | null;
  progress: CreditBreakdown | null;
}

export const GraduationSummary: React.FC<GraduationSummaryProps> = ({ audit, progress }) => {
  if (!audit) {
    return (
      <div className="bg-white p-8 rounded-2xl border border-slate-200 text-center text-slate-500">
        Loading graduation audit analysis...
      </div>
    );
  }

  const isSatisfied = audit.graduationRequirementsSatisfied;

  return (
    <div className="space-y-6">
      {/* Banner: Overall Status */}
      <div
        className={`p-6 rounded-2xl border shadow-sm flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 ${
          isSatisfied
            ? 'bg-gradient-to-r from-emerald-50 to-teal-50 border-emerald-300'
            : 'bg-gradient-to-r from-slate-50 to-amber-50/50 border-amber-300'
        }`}
      >
        <div className="flex items-start space-x-4">
          <div
            className={`p-3 rounded-2xl ${
              isSatisfied ? 'bg-emerald-600 text-white shadow-md' : 'bg-amber-500 text-white shadow-md'
            }`}
          >
            {isSatisfied ? <ShieldCheck className="w-8 h-8" /> : <Award className="w-8 h-8" />}
          </div>
          <div>
            <div className="flex items-center space-x-2">
              <h2 className="text-xl font-black text-slate-900">
                {isSatisfied ? 'Graduation Requirements Satisfied!' : 'Graduation Audit In Progress'}
              </h2>
              <span
                className={`text-xs font-extrabold uppercase px-2.5 py-0.5 rounded-full ${
                  isSatisfied
                    ? 'bg-emerald-200 text-emerald-900 border border-emerald-300'
                    : 'bg-amber-200 text-amber-900 border border-amber-300'
                }`}
              >
                {isSatisfied ? 'Ready for Graduation' : 'Requirements Remaining'}
              </span>
            </div>
            <p className="text-xs sm:text-sm text-slate-600 mt-1 max-w-2xl">{audit.statusMessage}</p>
          </div>
        </div>

        <div className="text-right sm:border-l sm:pl-6 border-slate-200">
          <div className="text-xs font-bold text-slate-500 uppercase">Total Recognized Credits</div>
          <div className="text-3xl font-black text-ousl-blue">
            {audit.totalRecognizedCredits}{' '}
            <span className="text-sm font-semibold text-slate-400">/ {audit.targetTotalCredits}</span>
          </div>
        </div>
      </div>

      {/* Grid: Academic Criteria Breakdown */}
      <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
        <h3 className="text-base font-bold text-slate-900 mb-4 flex items-center space-x-2">
          <BookOpen className="w-5 h-5 text-ousl-accent" />
          <span>Core Degree & Completion Requirements (OUSL Handbook 2026/2027)</span>
        </h3>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {audit.requirements.map((req) => (
            <div
              key={req.code}
              className={`p-4 rounded-xl border flex items-start space-x-3.5 transition-all ${
                req.satisfied
                  ? 'bg-emerald-50/50 border-emerald-200 text-emerald-950'
                  : 'bg-slate-50 border-slate-200 text-slate-800'
              }`}
            >
              <div className="mt-0.5">
                {req.satisfied ? (
                  <CheckCircle2 className="w-5 h-5 text-emerald-600" />
                ) : (
                  <XCircle className="w-5 h-5 text-slate-400" />
                )}
              </div>
              <div className="flex-1">
                <div className="flex items-center justify-between">
                  <h4 className="font-bold text-xs sm:text-sm text-slate-900">{req.title}</h4>
                  <span
                    className={`text-xs font-black px-2 py-0.5 rounded ${
                      req.satisfied ? 'bg-emerald-200 text-emerald-900' : 'bg-slate-200 text-slate-700'
                    }`}
                  >
                    {req.actualValue} / {req.targetValue} cr
                  </span>
                </div>
                <p className="text-xs text-slate-500 mt-1">{req.description}</p>
                <div className="text-[10px] text-slate-400 mt-1 italic">{req.ruleReference}</div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Table: Official Category Requirements (Table 1 of Guidebook) */}
      <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
        <div className="p-5 border-b border-slate-200 bg-slate-50/80 flex items-center justify-between">
          <div>
            <h3 className="font-bold text-slate-900 text-sm sm:text-base flex items-center space-x-2">
              <Layers className="w-5 h-5 text-ousl-gold" />
              <span>Official Course Category Requirements (Guidebook Table 1)</span>
            </h3>
            <p className="text-xs text-slate-500 mt-0.5">
              Degree minimums and maximums are enforced by course category, not department.
            </p>
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse text-xs sm:text-sm">
            <thead>
              <tr className="bg-slate-100 border-b border-slate-200 text-[11px] font-bold text-slate-600 uppercase tracking-wider">
                <th className="py-3 px-4">Code</th>
                <th className="py-3 px-4">Course Category</th>
                <th className="py-3 px-3 text-center">Min Required</th>
                <th className="py-3 px-3 text-center">Max Allowed</th>
                <th className="py-3 px-3 text-center">Earned (PA)</th>
                <th className="py-3 px-3 text-center">Exempted</th>
                <th className="py-3 px-3 text-center">Total Recognized</th>
                <th className="py-3 px-4 text-center">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {audit.categoryRequirements.map((cat) => (
                <tr key={cat.categoryCode} className="hover:bg-slate-50/80">
                  <td className="py-3 px-4 font-mono font-bold text-indigo-700">
                    <span className="bg-indigo-50 px-2 py-0.5 rounded border border-indigo-100">
                      {cat.categoryCode}
                    </span>
                  </td>
                  <td className="py-3 px-4 font-semibold text-slate-800">{cat.categoryName}</td>
                  <td className="py-3 px-3 text-center font-bold text-slate-700">{cat.minRequired}</td>
                  <td className="py-3 px-3 text-center text-slate-500">{cat.maxAllowed}</td>
                  <td className="py-3 px-3 text-center font-medium text-emerald-700">{cat.earnedCredits}</td>
                  <td className="py-3 px-3 text-center font-medium text-ousl-accent">{cat.exemptedCredits}</td>
                  <td className="py-3 px-3 text-center font-black text-slate-900">
                    {cat.totalRecognizedCredits}
                  </td>
                  <td className="py-3 px-4 text-center">
                    {cat.satisfied ? (
                      <span className="inline-flex items-center space-x-1 px-2.5 py-0.5 rounded-full text-xs font-semibold bg-emerald-100 text-emerald-800">
                        <Check className="w-3 h-3 text-emerald-600" />
                        <span>Satisfied</span>
                      </span>
                    ) : (
                      <span className="inline-flex items-center space-x-1 px-2.5 py-0.5 rounded-full text-xs font-semibold bg-amber-100 text-amber-800">
                        <span>Needs {cat.remainingCredits} cr</span>
                      </span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Informational Breakdown by Academic Department */}
      {progress && progress.creditsByDepartment && (
        <div className="bg-white rounded-2xl border border-slate-200 p-5 sm:p-6 shadow-sm">
          <h3 className="font-bold text-slate-900 text-sm sm:text-base mb-1">
            Department Credit Breakdown (Informational Only)
          </h3>
          <p className="text-xs text-slate-500 mb-4">
            Notice: The OUSL guidebook specifies degree requirements by course category, not department. This
            breakdown provides supplementary academic tracking.
          </p>

          <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
            {Object.entries(progress.creditsByDepartment).map(([dept, creds]) => (
              <div key={dept} className="bg-slate-50 p-3 rounded-xl border border-slate-200">
                <span className="text-xs font-bold text-slate-600 uppercase">Dept {dept}</span>
                <div className="text-lg font-black text-ousl-blue mt-0.5">{creds} Credits</div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Outstanding Requirements Checklist */}
      {audit.outstandingConditions && audit.outstandingConditions.length > 0 && (
        <div className="bg-amber-50/80 border border-amber-200 rounded-2xl p-5 sm:p-6">
          <h3 className="font-bold text-amber-900 text-sm sm:text-base flex items-center space-x-2 mb-3">
            <AlertTriangle className="w-5 h-5 text-amber-600" />
            <span>Outstanding Graduation Conditions ({audit.outstandingConditions.length})</span>
          </h3>
          <ul className="grid grid-cols-1 sm:grid-cols-2 gap-2 text-xs sm:text-sm text-amber-950">
            {audit.outstandingConditions.map((cond, i) => (
              <li key={i} className="flex items-start space-x-2 bg-white/70 p-2.5 rounded-lg border border-amber-200/60">
                <span className="w-1.5 h-1.5 rounded-full bg-amber-500 mt-2 flex-shrink-0" />
                <span>{cond}</span>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};


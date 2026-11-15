import React from 'react';
import { GraduationAudit, CreditBreakdown } from '../types';
import { GraduationSummary } from '../components/GraduationSummary';
import { DisclaimerBanner } from '../components/DisclaimerBanner';
import { Award, BookCheck, ShieldCheck } from 'lucide-react';

interface GraduationAuditPageProps {
  audit: GraduationAudit | null;
  progress: CreditBreakdown | null;
}

export const GraduationAuditPage: React.FC<GraduationAuditPageProps> = ({ audit, progress }) => {
  return (
    <div className="space-y-6">
      <DisclaimerBanner />

      <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm flex items-center justify-between">
        <div>
          <h1 className="text-xl sm:text-2xl font-black text-slate-900 tracking-tight flex items-center space-x-2">
            <Award className="w-6 h-6 text-ousl-gold" />
            <span>Graduation Compliance & Academic Audit</span>
          </h1>
          <p className="text-xs sm:text-sm text-slate-500 mt-1 max-w-3xl">
            Complete graduation audit verifying minimum 125 credits, category minimum/maximum bounds (Table 1), Level 5 & 6 thresholds, project requirements, and mandatory Industrial Training.
          </p>
        </div>
      </div>

      <GraduationSummary audit={audit} progress={progress} />
    </div>
  );
};


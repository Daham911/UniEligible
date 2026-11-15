import React, { useState } from 'react';
import { CheckCircle2, XCircle, AlertTriangle, HelpCircle, ChevronRight, Info } from 'lucide-react';
import { EligibilityResult } from '../types';

interface EligibilityBadgeProps {
  eligibility?: EligibilityResult;
  onClickDetails?: (result: EligibilityResult) => void;
}

export const EligibilityBadge: React.FC<EligibilityBadgeProps> = ({
  eligibility,
  onClickDetails,
}) => {
  if (!eligibility) {
    return (
      <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-slate-100 text-slate-600">
        Evaluating...
      </span>
    );
  }

  const { status, eligible, reasons } = eligibility;

  const handleClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (onClickDetails) {
      onClickDetails(eligibility);
    }
  };

  if (status === 'CURRENTLY_NOT_OFFERED') {
    return (
      <button
        onClick={handleClick}
        className="inline-flex items-center space-x-1 px-2.5 py-1 rounded-full text-xs font-semibold bg-amber-100 text-amber-800 border border-amber-300 hover:bg-amber-200 transition-colors cursor-pointer group"
        title="Course is not offered in 2026/2027"
      >
        <AlertTriangle className="w-3.5 h-3.5 text-amber-600" />
        <span>Not Offered</span>
        <Info className="w-3 h-3 text-amber-600 ml-0.5 opacity-60 group-hover:opacity-100" />
      </button>
    );
  }

  if (status === 'PENDING_INFORMATION') {
    return (
      <button
        onClick={handleClick}
        className="inline-flex items-center space-x-1 px-2.5 py-1 rounded-full text-xs font-semibold bg-purple-100 text-purple-800 border border-purple-300 hover:bg-purple-200 transition-colors cursor-pointer group"
        title="Pending administrative verification"
      >
        <HelpCircle className="w-3.5 h-3.5 text-purple-600" />
        <span>Pending Info</span>
        <ChevronRight className="w-3 h-3 text-purple-600 ml-0.5 opacity-60 group-hover:opacity-100" />
      </button>
    );
  }

  if (eligible) {
    return (
      <button
        onClick={handleClick}
        className="inline-flex items-center space-x-1 px-2.5 py-1 rounded-full text-xs font-semibold bg-emerald-100 text-emerald-800 border border-emerald-300 hover:bg-emerald-200 transition-colors cursor-pointer group"
        title="Eligible to register"
      >
        <CheckCircle2 className="w-3.5 h-3.5 text-emerald-600" />
        <span>Eligible</span>
        <ChevronRight className="w-3 h-3 text-emerald-600 ml-0.5 opacity-60 group-hover:opacity-100" />
      </button>
    );
  }

  // Not Eligible
  return (
    <button
      onClick={handleClick}
      className="inline-flex items-center space-x-1 px-2.5 py-1 rounded-full text-xs font-semibold bg-rose-100 text-rose-800 border border-rose-300 hover:bg-rose-200 transition-colors cursor-pointer group"
      title={reasons && reasons.length > 0 ? reasons[0] : 'Prerequisites unmet'}
    >
      <XCircle className="w-3.5 h-3.5 text-rose-600" />
      <span>Not Eligible</span>
      <ChevronRight className="w-3 h-3 text-rose-600 ml-0.5 opacity-60 group-hover:opacity-100" />
    </button>
  );
};


import React from 'react';
import { X, CheckCircle2, XCircle, AlertTriangle, HelpCircle, BookOpen, MapPin, AlertCircle } from 'lucide-react';
import { EligibilityResult } from '../types';

interface EligibilityDetailsModalProps {
  result: EligibilityResult | null;
  onClose: () => void;
}

export const EligibilityDetailsModal: React.FC<EligibilityDetailsModalProps> = ({
  result,
  onClose,
}) => {
  if (!result) return null;

  const isEligible = result.eligible;
  const isNotOffered = result.status === 'CURRENTLY_NOT_OFFERED';
  const isPending = result.status === 'PENDING_INFORMATION';

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto bg-slate-900/60 backdrop-blur-sm flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-2xl max-w-xl w-full border border-slate-200 overflow-hidden transform transition-all animate-in fade-in zoom-in-95 duration-200">
        {/* Header */}
        <div
          className={`px-6 py-5 flex items-start justify-between border-b ${
            isNotOffered
              ? 'bg-amber-50 border-amber-200'
              : isEligible
              ? 'bg-emerald-50 border-emerald-200'
              : isPending
              ? 'bg-purple-50 border-purple-200'
              : 'bg-rose-50 border-rose-200'
          }`}
        >
          <div className="flex items-start space-x-3">
            <div className="mt-1">
              {isNotOffered ? (
                <AlertTriangle className="w-6 h-6 text-amber-600" />
              ) : isEligible ? (
                <CheckCircle2 className="w-6 h-6 text-emerald-600" />
              ) : isPending ? (
                <HelpCircle className="w-6 h-6 text-purple-600" />
              ) : (
                <XCircle className="w-6 h-6 text-rose-600" />
              )}
            </div>
            <div>
              <div className="flex items-center space-x-2">
                <span className="font-mono font-bold text-lg text-slate-900">{result.courseCode}</span>
                <span
                  className={`px-2.5 py-0.5 rounded-full text-xs font-bold uppercase tracking-wider ${
                    isNotOffered
                      ? 'bg-amber-200 text-amber-900'
                      : isEligible
                      ? 'bg-emerald-200 text-emerald-900'
                      : isPending
                      ? 'bg-purple-200 text-purple-900'
                      : 'bg-rose-200 text-rose-900'
                  }`}
                >
                  {result.status.replace(/_/g, ' ')}
                </span>
              </div>
              <h3 className="font-semibold text-slate-800 text-base mt-0.5">{result.courseName}</h3>
              <p className="text-xs text-slate-500 mt-0.5">
                Level {result.level} • {result.credits} Credits • Semester {result.semester} •{' '}
                {result.compulsory ? 'Compulsory' : 'Elective'}
              </p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="text-slate-400 hover:text-slate-600 p-1.5 rounded-lg hover:bg-black/5 transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Modal Body */}
        <div className="px-6 py-5 space-y-4 max-h-[70vh] overflow-y-auto custom-scrollbar">
          {/* Prerequisite Text from Handbook */}
          <div className="bg-slate-50 p-3.5 rounded-xl border border-slate-200/80">
            <div className="text-xs font-semibold uppercase tracking-wider text-slate-500 flex items-center space-x-1.5 mb-1">
              <BookOpen className="w-3.5 h-3.5 text-ousl-accent" />
              <span>Handbook Prerequisite Requirement</span>
            </div>
            <div className="font-mono text-xs sm:text-sm text-slate-800 font-medium bg-white px-2.5 py-1.5 rounded border border-slate-200">
              {result.prerequisiteDisplayText || 'None'}
            </div>
          </div>

          {/* Special Notes & Discrepancy Flags */}
          {result.deliveryCenter === 'CRC only' && (
            <div className="bg-blue-50 border border-blue-200 text-blue-900 text-xs p-3 rounded-xl flex items-start space-x-2">
              <MapPin className="w-4 h-4 text-blue-600 flex-shrink-0 mt-0.5" />
              <div>
                <span className="font-semibold">Laboratory Notice:</span> Practical laboratory sessions for this course
                are held at the Colombo Regional Centre (CRC) only.
              </div>
            </div>
          )}

          {result.specialRestriction && (
            <div className="bg-amber-50 border border-amber-200 text-amber-900 text-xs p-3 rounded-xl flex items-start space-x-2">
              <AlertCircle className="w-4 h-4 text-amber-600 flex-shrink-0 mt-0.5" />
              <div>
                <span className="font-semibold">Special Academic Restriction:</span> {result.specialRestriction}
              </div>
            </div>
          )}

          {result.discrepancyFlag && (
            <div className="bg-purple-50 border border-purple-200 text-purple-900 text-xs p-3 rounded-xl flex items-start space-x-2">
              <HelpCircle className="w-4 h-4 text-purple-600 flex-shrink-0 mt-0.5" />
              <div>
                <span className="font-semibold">Administrative Verification Flag:</span> {result.discrepancyFlag}
              </div>
            </div>
          )}

          {/* Reasons / Evaluation Breakdown */}
          <div>
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-600 mb-2">
              Prerequisite & Credit Evaluation Breakdown
            </h4>

            {result.detailedReasons && result.detailedReasons.length > 0 ? (
              <div className="space-y-2">
                {result.detailedReasons.map((item, idx) => (
                  <div
                    key={idx}
                    className={`p-3 rounded-xl border flex items-start space-x-3 text-xs sm:text-sm ${
                      item.satisfied
                        ? 'bg-emerald-50/60 border-emerald-200 text-emerald-950'
                        : 'bg-rose-50/70 border-rose-200 text-rose-950'
                    }`}
                  >
                    <div className="mt-0.5 flex-shrink-0">
                      {item.satisfied ? (
                        <CheckCircle2 className="w-4 h-4 text-emerald-600" />
                      ) : (
                        <XCircle className="w-4 h-4 text-rose-600" />
                      )}
                    </div>
                    <div className="flex-1">
                      <div className="font-semibold text-xs text-slate-700">{item.ruleDescription}</div>
                      <div className="mt-0.5">{item.reasonMessage}</div>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="p-3 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-600">
                {result.reasons && result.reasons.length > 0
                  ? result.reasons.join(' ')
                  : 'No specific prerequisite evaluation details available.'}
              </div>
            )}
          </div>
        </div>

        {/* Modal Footer */}
        <div className="bg-slate-50 px-6 py-4 border-t border-slate-200 flex justify-end">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-slate-800 hover:bg-slate-900 text-white font-medium text-xs sm:text-sm rounded-lg transition-colors"
          >
            Close Details
          </button>
        </div>
      </div>
    </div>
  );
};


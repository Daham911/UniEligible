import React from 'react';
import { AlertCircle } from 'lucide-react';

export const DisclaimerBanner: React.FC = () => {
  return (
    <div className="bg-amber-50 border-l-4 border-amber-500 p-3 sm:p-4 rounded-r-lg shadow-sm mb-6 text-amber-900 text-xs sm:text-sm flex items-start space-x-3">
      <AlertCircle className="w-5 h-5 text-amber-600 flex-shrink-0 mt-0.5" />
      <div>
        <span className="font-semibold text-amber-800">Unofficial Student Advisory Tool:</span>{' '}
        This application provides advisory calculations based on the OUSL Bachelor of Software Engineering Honours (BSEHons) Student Guidebook 2026/2027. Official course registration, prerequisite waivers, and final degree awards remain strictly subject to Open University of Sri Lanka (OUSL) Senate approval.
      </div>
    </div>
  );
};


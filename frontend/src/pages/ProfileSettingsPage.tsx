import React, { useState } from 'react';
import { StudentProfile, StudentExemption, ConcurrentRegistration } from '../types';
import { ExemptionManager } from '../components/ExemptionManager';
import { ConcurrentRegistrationManager } from '../components/ConcurrentRegistrationManager';
import { DisclaimerBanner } from '../components/DisclaimerBanner';
import { User, ShieldAlert, BookOpen, Save, CheckCircle2 } from 'lucide-react';

interface ProfileSettingsPageProps {
  student: StudentProfile | null;
  exemptions: StudentExemption[];
  concurrentRegs: ConcurrentRegistration[];
  onSaveProfile: (profile: StudentProfile) => Promise<void>;
  onAddExemption: (exemption: StudentExemption) => Promise<void>;
  onAddRegistration: (cr: ConcurrentRegistration) => Promise<void>;
}

export const ProfileSettingsPage: React.FC<ProfileSettingsPageProps> = ({
  student,
  exemptions,
  concurrentRegs,
  onSaveProfile,
  onAddExemption,
  onAddRegistration,
}) => {
  const [fullName, setFullName] = useState(student?.fullName || 'Nimal Perera');
  const [regNo, setRegNo] = useState(student?.registrationNumber || 'S92054321');
  const [email, setEmail] = useState(student?.email || 'nimal.perera@ousl.lk');
  const [currentYear, setCurrentYear] = useState(student?.currentAcademicYear || 1);
  const [currentLevel, setCurrentLevel] = useState(student?.currentAcademicLevel || 3);
  const [isSaved, setIsSaved] = useState(false);

  const handleProfileSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!student) return;

    await onSaveProfile({
      ...student,
      fullName,
      registrationNumber: regNo,
      email,
      currentAcademicYear: currentYear,
      currentAcademicLevel: currentLevel,
    });
    setIsSaved(true);
    setTimeout(() => setIsSaved(false), 3000);
  };

  return (
    <div className="space-y-6">
      <DisclaimerBanner />

      {/* Student Profile Card */}
      <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
        <h2 className="text-base sm:text-lg font-bold text-slate-900 mb-4 flex items-center space-x-2">
          <User className="w-5 h-5 text-ousl-blue" />
          <span>Student Profile & Academic Settings</span>
        </h2>

        <form onSubmit={handleProfileSubmit} className="space-y-4">
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <div>
              <label className="block text-xs font-semibold text-slate-600 mb-1">Student Full Name</label>
              <input
                type="text"
                required
                value={fullName}
                onChange={(e) => setFullName(e.target.value)}
                className="w-full px-3 py-2 bg-slate-50 border border-slate-300 rounded-lg text-xs sm:text-sm focus:ring-1 focus:ring-ousl-blue"
              />
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-600 mb-1">Registration Number</label>
              <input
                type="text"
                required
                value={regNo}
                onChange={(e) => setRegNo(e.target.value)}
                className="w-full px-3 py-2 bg-slate-50 border border-slate-300 rounded-lg text-xs sm:text-sm font-mono uppercase focus:ring-1 focus:ring-ousl-blue"
              />
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-600 mb-1">Student Email</label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full px-3 py-2 bg-slate-50 border border-slate-300 rounded-lg text-xs sm:text-sm focus:ring-1 focus:ring-ousl-blue"
              />
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-600 mb-1">Degree Programme</label>
              <input
                type="text"
                disabled
                value="Bachelor of Software Engineering Honours (BSEHons)"
                className="w-full px-3 py-2 bg-slate-100 border border-slate-200 rounded-lg text-xs sm:text-sm text-slate-600 font-medium"
              />
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-600 mb-1">Current Academic Year</label>
              <select
                value={currentYear}
                onChange={(e) => {
                  const y = Number(e.target.value);
                  setCurrentYear(y);
                  setCurrentLevel(y + 2);
                }}
                className="w-full px-3 py-2 bg-slate-50 border border-slate-300 rounded-lg text-xs sm:text-sm focus:ring-1 focus:ring-ousl-blue"
              >
                <option value={1}>Year 1</option>
                <option value={2}>Year 2</option>
                <option value={3}>Year 3</option>
                <option value={4}>Year 4</option>
              </select>
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-600 mb-1">Current Academic Level</label>
              <input
                type="text"
                disabled
                value={`Level ${currentLevel}`}
                className="w-full px-3 py-2 bg-slate-100 border border-slate-200 rounded-lg text-xs sm:text-sm text-slate-600 font-medium"
              />
            </div>
          </div>

          <div className="flex items-center justify-between pt-2">
            {isSaved ? (
              <span className="text-xs text-emerald-600 font-semibold flex items-center space-x-1">
                <CheckCircle2 className="w-4 h-4" />
                <span>Profile updated successfully!</span>
              </span>
            ) : <span />}
            <button
              type="submit"
              className="px-4 py-2 bg-ousl-blue hover:bg-ousl-navy text-white text-xs sm:text-sm font-semibold rounded-lg flex items-center space-x-1.5 transition-colors shadow-sm"
            >
              <Save className="w-4 h-4" />
              <span>Save Profile</span>
            </button>
          </div>
        </form>
      </div>

      {/* Exemptions Management */}
      <ExemptionManager exemptions={exemptions} onAddExemption={onAddExemption} />

      {/* Concurrent Registrations Management */}
      <ConcurrentRegistrationManager
        concurrentRegs={concurrentRegs}
        onAddRegistration={onAddRegistration}
      />

      {/* Academic Handbook Reference Notes & Discrepancies */}
      <div className="bg-slate-50 rounded-2xl border border-slate-200 p-6">
        <h3 className="font-bold text-slate-900 text-sm sm:text-base flex items-center space-x-2 mb-3">
          <BookOpen className="w-5 h-5 text-ousl-accent" />
          <span>Guidebook 2026/2027 Academic Discrepancies & Policies Handled</span>
        </h3>
        <div className="space-y-3 text-xs text-slate-600">
          <div className="bg-white p-3 rounded-xl border border-slate-200">
            <span className="font-bold text-slate-800">MHZ2250 Credit Rating:</span> Elementary Mathematics for Computing is rated as a 2* course at Level 2. Under BSEHons academic regulations, its credits are excluded from graduation credit calculations.
          </div>
          <div className="bg-white p-3 rounded-xl border border-slate-200">
            <span className="font-bold text-slate-800">LTE34SI Non-GPA Exception:</span> LTE34SI (English for Academic Purposes – Intermediate) is an exception among non-GPA courses; its 4 credits contribute to the official Language/English category credit totals.
          </div>
          <div className="bg-white p-3 rounded-xl border border-slate-200">
            <span className="font-bold text-slate-800">EE4360(P) Discrepancy Flag:</span> The guidebook row for EEI6320 (Intelligent Systems and Agent Technology) lists prerequisite code EE4360(P). UniEligible flags this for administrative verification rather than silently modifying it.
          </div>
          <div className="bg-white p-3 rounded-xl border border-slate-200">
            <span className="font-bold text-slate-800">EER4489 Special Restriction:</span> Higher Diploma Project in Software Engineering (4 credits) is restricted and offered only when an acceptable justification is provided for not registering for Industrial Training (EEW5611).
          </div>
        </div>
      </div>
    </div>
  );
};


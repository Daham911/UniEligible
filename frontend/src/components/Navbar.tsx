import React from 'react';
import { GraduationCap, BookOpen, Award, User, RefreshCw } from 'lucide-react';
import { StudentProfile } from '../types';

interface NavbarProps {
  student: StudentProfile | null;
  activeTab: 'dashboard' | 'academic' | 'graduation' | 'profile';
  setActiveTab: (tab: 'dashboard' | 'academic' | 'graduation' | 'profile') => void;
  onRefresh?: () => void;
  isRefreshing?: boolean;
}

export const Navbar: React.FC<NavbarProps> = ({
  student,
  activeTab,
  setActiveTab,
  onRefresh,
  isRefreshing,
}) => {
  return (
    <header className="bg-ousl-blue text-white shadow-md sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16 sm:h-20">
          {/* Logo & University Title */}
          <div className="flex items-center space-x-3 cursor-pointer" onClick={() => setActiveTab('dashboard')}>
            <div className="bg-ousl-gold text-ousl-navy p-2 sm:p-2.5 rounded-xl shadow-inner font-black text-xl flex items-center justify-center">
              <GraduationCap className="w-6 h-6 text-ousl-navy" />
            </div>
            <div>
              <div className="flex items-center space-x-2">
                <span className="font-bold text-lg sm:text-xl tracking-tight text-white">UniEligible</span>
                <span className="bg-ousl-gold/20 text-ousl-gold border border-ousl-gold/40 text-[10px] font-semibold uppercase px-2 py-0.5 rounded-full">
                  OUSL BSEHons
                </span>
              </div>
              <p className="text-xs text-slate-300 hidden sm:block">
                Open University of Sri Lanka • Academic Progress & Eligibility Calculator
              </p>
            </div>
          </div>

          {/* Navigation Items */}
          <nav className="hidden md:flex items-center space-x-1">
            <button
              onClick={() => setActiveTab('dashboard')}
              className={`px-3 py-2 rounded-lg text-sm font-medium transition-colors flex items-center space-x-1.5 ${
                activeTab === 'dashboard'
                  ? 'bg-white/15 text-white font-semibold'
                  : 'text-slate-300 hover:text-white hover:bg-white/10'
              }`}
            >
              <span>Dashboard</span>
            </button>
            <button
              onClick={() => setActiveTab('academic')}
              className={`px-3 py-2 rounded-lg text-sm font-medium transition-colors flex items-center space-x-1.5 ${
                activeTab === 'academic'
                  ? 'bg-white/15 text-white font-semibold'
                  : 'text-slate-300 hover:text-white hover:bg-white/10'
              }`}
            >
              <BookOpen className="w-4 h-4 text-ousl-gold" />
              <span>Results & Eligibility</span>
            </button>
            <button
              onClick={() => setActiveTab('graduation')}
              className={`px-3 py-2 rounded-lg text-sm font-medium transition-colors flex items-center space-x-1.5 ${
                activeTab === 'graduation'
                  ? 'bg-white/15 text-white font-semibold'
                  : 'text-slate-300 hover:text-white hover:bg-white/10'
              }`}
            >
              <Award className="w-4 h-4 text-emerald-400" />
              <span>Graduation Audit</span>
            </button>
            <button
              onClick={() => setActiveTab('profile')}
              className={`px-3 py-2 rounded-lg text-sm font-medium transition-colors flex items-center space-x-1.5 ${
                activeTab === 'profile'
                  ? 'bg-white/15 text-white font-semibold'
                  : 'text-slate-300 hover:text-white hover:bg-white/10'
              }`}
            >
              <User className="w-4 h-4 text-sky-400" />
              <span>Settings</span>
            </button>
          </nav>

          {/* Student Profile Pill & Refresh Button */}
          <div className="flex items-center space-x-2 sm:space-x-3">
            {onRefresh && (
              <button
                onClick={onRefresh}
                disabled={isRefreshing}
                title="Recalculate eligibility & refresh"
                className="p-2 rounded-lg bg-white/10 hover:bg-white/20 text-slate-200 transition-all active:scale-95 disabled:opacity-50"
              >
                <RefreshCw className={`w-4 h-4 ${isRefreshing ? 'animate-spin text-ousl-gold' : ''}`} />
              </button>
            )}

            {student && (
              <div
                onClick={() => setActiveTab('profile')}
                className="flex items-center space-x-2 bg-ousl-navy/80 hover:bg-ousl-navy px-3 py-1.5 rounded-lg border border-slate-700/60 cursor-pointer transition-colors"
              >
                <div className="w-7 h-7 rounded-full bg-ousl-gold text-ousl-navy flex items-center justify-center font-bold text-xs">
                  {student.fullName ? student.fullName.charAt(0) : 'S'}
                </div>
                <div className="text-left hidden lg:block">
                  <div className="text-xs font-semibold text-white leading-tight">{student.fullName}</div>
                  <div className="text-[10px] text-slate-400 leading-tight">
                    {student.registrationNumber} • Y{student.currentAcademicYear} (L{student.currentAcademicLevel})
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>

        {/* Mobile Navigation Row */}
        <div className="md:hidden flex items-center justify-around py-2 border-t border-slate-800 text-xs">
          <button
            onClick={() => setActiveTab('dashboard')}
            className={`py-1 px-2 rounded ${activeTab === 'dashboard' ? 'text-ousl-gold font-bold' : 'text-slate-300'}`}
          >
            Dashboard
          </button>
          <button
            onClick={() => setActiveTab('academic')}
            className={`py-1 px-2 rounded ${activeTab === 'academic' ? 'text-ousl-gold font-bold' : 'text-slate-300'}`}
          >
            Results
          </button>
          <button
            onClick={() => setActiveTab('graduation')}
            className={`py-1 px-2 rounded ${activeTab === 'graduation' ? 'text-ousl-gold font-bold' : 'text-slate-300'}`}
          >
            Graduation
          </button>
          <button
            onClick={() => setActiveTab('profile')}
            className={`py-1 px-2 rounded ${activeTab === 'profile' ? 'text-ousl-gold font-bold' : 'text-slate-300'}`}
          >
            Settings
          </button>
        </div>
      </div>
    </header>
  );
};


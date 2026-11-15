import React, { useState } from 'react';
import { ConcurrentRegistration } from '../types';
import { Calendar, Plus, CheckCircle2, BookmarkCheck } from 'lucide-react';

interface ConcurrentRegistrationManagerProps {
  concurrentRegs: ConcurrentRegistration[];
  onAddRegistration: (cr: ConcurrentRegistration) => Promise<void>;
  isSubmitting?: boolean;
}

export const ConcurrentRegistrationManager: React.FC<ConcurrentRegistrationManagerProps> = ({
  concurrentRegs,
  onAddRegistration,
  isSubmitting,
}) => {
  const [courseCode, setCourseCode] = useState('');
  const [academicSession, setAcademicSession] = useState('2026/2027');
  const [showForm, setShowForm] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!courseCode.trim()) return;

    await onAddRegistration({
      courseCode: courseCode.trim().toUpperCase(),
      academicSession,
      active: true,
    });

    setCourseCode('');
    setShowForm(false);
  };

  return (
    <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm mb-6">
      <div className="flex items-center justify-between mb-4">
        <div>
          <h3 className="font-bold text-slate-900 text-base flex items-center space-x-2">
            <BookmarkCheck className="w-5 h-5 text-ousl-accent" />
            <span>Active Concurrent Registrations (CR)</span>
          </h3>
          <p className="text-xs text-slate-500 mt-0.5">
            Modules currently co-registered in this academic session. CR prerequisites are satisfied through this tracking.
          </p>
        </div>
        <button
          onClick={() => setShowForm(!showForm)}
          className="px-3 py-1.5 bg-ousl-blue hover:bg-ousl-navy text-white text-xs font-semibold rounded-lg flex items-center space-x-1.5 transition-colors"
        >
          <Plus className="w-4 h-4" />
          <span>{showForm ? 'Cancel' : 'Add CR Course'}</span>
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="bg-slate-50 p-4 rounded-xl border border-slate-200 mb-6 text-xs sm:text-sm">
          <h4 className="font-bold text-slate-800 mb-3">Add Concurrent Course Registration</h4>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 mb-3">
            <div>
              <label className="block text-slate-600 font-medium mb-1">Course Code (CR Required Module) *</label>
              <input
                type="text"
                required
                placeholder="e.g. MHZ2250, EEI3346, EEI3467, MHZ4359"
                value={courseCode}
                onChange={(e) => setCourseCode(e.target.value)}
                className="w-full px-3 py-2 bg-white border border-slate-300 rounded-lg text-xs font-mono uppercase focus:ring-1 focus:ring-ousl-blue"
              />
            </div>
            <div>
              <label className="block text-slate-600 font-medium mb-1">Academic Session</label>
              <input
                type="text"
                value={academicSession}
                onChange={(e) => setAcademicSession(e.target.value)}
                className="w-full px-3 py-2 bg-white border border-slate-300 rounded-lg text-xs focus:ring-1 focus:ring-ousl-blue"
              />
            </div>
          </div>
          <div className="flex justify-end space-x-2">
            <button
              type="button"
              onClick={() => setShowForm(false)}
              className="px-3 py-1.5 border border-slate-300 text-slate-700 rounded-lg text-xs font-semibold hover:bg-slate-100"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={isSubmitting}
              className="px-4 py-1.5 bg-ousl-blue hover:bg-ousl-navy text-white rounded-lg text-xs font-semibold"
            >
              Save Concurrent Registration
            </button>
          </div>
        </form>
      )}

      {concurrentRegs.length === 0 ? (
        <div className="text-center py-6 border-2 border-dashed border-slate-200 rounded-xl text-xs text-slate-400">
          No active concurrent registrations recorded. Add courses that you are concurrently enrolled in.
        </div>
      ) : (
        <div className="flex flex-wrap gap-2">
          {concurrentRegs.map((cr, i) => (
            <div
              key={i}
              className="bg-blue-50 border border-blue-200 text-blue-900 px-3 py-2 rounded-xl flex items-center space-x-2 text-xs"
            >
              <CheckCircle2 className="w-4 h-4 text-blue-600" />
              <div>
                <span className="font-mono font-bold">{cr.courseCode}</span>
                <span className="text-slate-500 ml-1.5">({cr.academicSession})</span>
                {cr.courseName && <span className="text-blue-800 ml-1">• {cr.courseName}</span>}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};


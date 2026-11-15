import React, { useState } from 'react';
import { StudentExemption } from '../types';
import { Award, Plus, CheckCircle2, ShieldCheck } from 'lucide-react';

interface ExemptionManagerProps {
  exemptions: StudentExemption[];
  onAddExemption: (exemption: StudentExemption) => Promise<void>;
  isSubmitting?: boolean;
}

export const ExemptionManager: React.FC<ExemptionManagerProps> = ({
  exemptions,
  onAddExemption,
  isSubmitting,
}) => {
  const [courseCode, setCourseCode] = useState('');
  const [courseName, setCourseName] = useState('');
  const [categoryCode, setCategoryCode] = useState('I');
  const [level, setLevel] = useState(3);
  const [creditValue, setCreditValue] = useState(3);
  const [exemptionReference, setExemptionReference] = useState('');
  const [showForm, setShowForm] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!courseCode.trim() || creditValue <= 0) return;

    await onAddExemption({
      courseCode: courseCode.trim().toUpperCase(),
      courseName: courseName.trim() || courseCode.trim().toUpperCase(),
      categoryCode,
      level,
      creditValue,
      exemptionReference: exemptionReference.trim() || 'Official Senate Exemption Approval',
      verified: true,
    });

    setCourseCode('');
    setCourseName('');
    setExemptionReference('');
    setShowForm(false);
  };

  return (
    <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm mb-6">
      <div className="flex items-center justify-between mb-4">
        <div>
          <h3 className="font-bold text-slate-900 text-base flex items-center space-x-2">
            <Award className="w-5 h-5 text-ousl-gold" />
            <span>Officially Recognized Course Exemptions</span>
          </h3>
          <p className="text-xs text-slate-500 mt-0.5">
            Exemptions granted by the OUSL Senate contribute to recognized category and level credit totals.
          </p>
        </div>
        <button
          onClick={() => setShowForm(!showForm)}
          className="px-3 py-1.5 bg-ousl-blue hover:bg-ousl-navy text-white text-xs font-semibold rounded-lg flex items-center space-x-1.5 transition-colors"
        >
          <Plus className="w-4 h-4" />
          <span>{showForm ? 'Cancel' : 'Add Exemption'}</span>
        </button>
      </div>

      {/* Exemption Form */}
      {showForm && (
        <form onSubmit={handleSubmit} className="bg-slate-50 p-4 rounded-xl border border-slate-200 mb-6 text-xs sm:text-sm">
          <h4 className="font-bold text-slate-800 mb-3">Record Verified Academic Exemption</h4>
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-3 mb-3">
            <div>
              <label className="block text-slate-600 font-medium mb-1">Course Code / VT Code *</label>
              <input
                type="text"
                required
                placeholder="e.g. EEI3346 or VT3001"
                value={courseCode}
                onChange={(e) => setCourseCode(e.target.value)}
                className="w-full px-3 py-2 bg-white border border-slate-300 rounded-lg text-xs font-mono uppercase focus:ring-1 focus:ring-ousl-blue"
              />
            </div>
            <div>
              <label className="block text-slate-600 font-medium mb-1">Course Title</label>
              <input
                type="text"
                placeholder="e.g. Prior Web Development Exemption"
                value={courseName}
                onChange={(e) => setCourseName(e.target.value)}
                className="w-full px-3 py-2 bg-white border border-slate-300 rounded-lg text-xs focus:ring-1 focus:ring-ousl-blue"
              />
            </div>
            <div>
              <label className="block text-slate-600 font-medium mb-1">Category Code</label>
              <select
                value={categoryCode}
                onChange={(e) => setCategoryCode(e.target.value)}
                className="w-full px-3 py-2 bg-white border border-slate-300 rounded-lg text-xs focus:ring-1 focus:ring-ousl-blue"
              >
                <option value="I">Industrial (I)</option>
                <option value="M">Management (M)</option>
                <option value="Z">Mathematics (Z)</option>
                <option value="J">General (J)</option>
                <option value="R">Projects (R)</option>
                <option value="W">Industrial Training (W)</option>
                <option value="L">Language (L)</option>
                <option value="E">English (E)</option>
              </select>
            </div>
            <div>
              <label className="block text-slate-600 font-medium mb-1">Level</label>
              <select
                value={level}
                onChange={(e) => setLevel(Number(e.target.value))}
                className="w-full px-3 py-2 bg-white border border-slate-300 rounded-lg text-xs focus:ring-1 focus:ring-ousl-blue"
              >
                <option value={3}>Level 3</option>
                <option value={4}>Level 4</option>
                <option value={5}>Level 5</option>
                <option value={6}>Level 6</option>
              </select>
            </div>
            <div>
              <label className="block text-slate-600 font-medium mb-1">Credit Value</label>
              <input
                type="number"
                min="1"
                max="10"
                required
                value={creditValue}
                onChange={(e) => setCreditValue(Number(e.target.value))}
                className="w-full px-3 py-2 bg-white border border-slate-300 rounded-lg text-xs focus:ring-1 focus:ring-ousl-blue"
              />
            </div>
            <div>
              <label className="block text-slate-600 font-medium mb-1">Senate Approval Reference</label>
              <input
                type="text"
                placeholder="e.g. OUSL/SEN/2026/EX/104"
                value={exemptionReference}
                onChange={(e) => setExemptionReference(e.target.value)}
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
              className="px-4 py-1.5 bg-emerald-600 hover:bg-emerald-700 text-white rounded-lg text-xs font-semibold flex items-center space-x-1"
            >
              <ShieldCheck className="w-4 h-4" />
              <span>Record Exemption</span>
            </button>
          </div>
        </form>
      )}

      {/* Exemptions List */}
      {exemptions.length === 0 ? (
        <div className="text-center py-6 border-2 border-dashed border-slate-200 rounded-xl text-xs text-slate-400">
          No exemptions currently recorded for this student.
        </div>
      ) : (
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs sm:text-sm">
            <thead>
              <tr className="bg-slate-50 text-[11px] font-bold text-slate-500 uppercase border-b border-slate-200">
                <th className="py-2.5 px-3">Course Code</th>
                <th className="py-2.5 px-3">Course Name</th>
                <th className="py-2.5 px-3 text-center">Category</th>
                <th className="py-2.5 px-3 text-center">Level</th>
                <th className="py-2.5 px-3 text-center">Credits</th>
                <th className="py-2.5 px-3">Senate Reference</th>
                <th className="py-2.5 px-3 text-center">Verification</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {exemptions.map((ex, i) => (
                <tr key={i} className="hover:bg-slate-50/60">
                  <td className="py-2.5 px-3 font-mono font-bold text-slate-800">{ex.courseCode}</td>
                  <td className="py-2.5 px-3 text-slate-700 font-medium">{ex.courseName || ex.courseCode}</td>
                  <td className="py-2.5 px-3 text-center">
                    <span className="bg-indigo-50 text-indigo-700 px-2 py-0.5 rounded text-[11px] font-bold">
                      {ex.categoryCode}
                    </span>
                  </td>
                  <td className="py-2.5 px-3 text-center font-semibold text-slate-600">{ex.level}</td>
                  <td className="py-2.5 px-3 text-center font-bold text-emerald-700">{ex.creditValue} cr</td>
                  <td className="py-2.5 px-3 text-slate-500 text-xs">{ex.exemptionReference}</td>
                  <td className="py-2.5 px-3 text-center">
                    <span className="inline-flex items-center space-x-1 px-2 py-0.5 rounded-full text-[10px] font-semibold bg-emerald-100 text-emerald-800">
                      <CheckCircle2 className="w-3 h-3 text-emerald-600" />
                      <span>Verified</span>
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};


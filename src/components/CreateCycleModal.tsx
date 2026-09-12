import React, { useState } from 'react';
import { X, Plus, Calendar } from 'lucide-react';
import { getLogicalTodayDate } from '../engine/dateUtils';

interface CreateCycleModalProps {
  onDismiss: () => void;
  onCreateCycle: (
    title: string,
    startDate: string,
    targetTheme: string,
    inheritedStreak: number,
    rules: string[]
  ) => void;
}

export const CreateCycleModal: React.FC<CreateCycleModalProps> = ({
  onDismiss,
  onCreateCycle
}) => {
  const [title, setTitle] = useState('');
  const [startDate, setStartDate] = useState(getLogicalTodayDate());
  const [targetTheme, setTargetTheme] = useState('');
  const [inheritedStreak, setInheritedStreak] = useState(0);
  const [rule1, setRule1] = useState('سحرخیزی بی‌چون‌وچرا سر ساعت مقرر');
  const [rule2, setRule2] = useState('ورزش روزانه بدون لغو به بهانه خستگی');
  const [rule3, setRule3] = useState('ثبت و تیک روزانه قبل از خواب');
  const [rule4, setRule4] = useState('کالبدشکافی فوری در صورت هرگونه افت');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const effectiveTitle = title.trim() || 'چرخه جدید ۹۰ روزه';
    const rulesList = [rule1, rule2, rule3, rule4].filter(Boolean);
    onCreateCycle(effectiveTitle, startDate, targetTheme, inheritedStreak, rulesList);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-3 bg-black/80 backdrop-blur-sm">
      <div className="w-full max-w-lg max-h-[92vh] flex flex-col bg-[#121215] border border-[#27272a] rounded-2xl shadow-2xl overflow-hidden">
        {/* Header */}
        <div className="p-4 border-b border-[#27272a] flex items-center justify-between">
          <button
            onClick={onDismiss}
            className="p-1.5 rounded-lg bg-[#18181b] border border-[#27272a] hover:border-[#3f3f46] text-[#a1a1aa] hover:text-white transition-colors"
          >
            <X className="w-4 h-4" />
          </button>

          <div className="text-right">
            <div className="text-sm font-bold text-white">
              تاسیس چرخه ۹۰ روزه جدید
            </div>
            <div className="text-xs text-[#a1a1aa]">
              ۹۰ روز انضباط آهنین و تسلط بر خود
            </div>
          </div>
        </div>

        {/* Form Body */}
        <form onSubmit={handleSubmit} className="flex-1 overflow-y-auto p-4 flex flex-col gap-4 text-right">
          {/* Title */}
          <div className="flex flex-col gap-1">
            <label className="text-xs font-bold text-zinc-300">عنوان چرخه:</label>
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="مثال: چرخه ۲ — تسلط بر تمرکز عمیق و سحرخیزی"
              className="w-full bg-[#18181b] border border-[#27272a] rounded-lg p-2.5 text-xs text-white placeholder:text-zinc-600 focus:outline-none focus:border-rose-500"
            />
          </div>

          {/* Start Date */}
          <div className="flex flex-col gap-1">
            <label className="text-xs font-bold text-zinc-300">تاریخ شروع (YYYY-MM-DD):</label>
            <input
              type="date"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
              className="w-full bg-[#18181b] border border-[#27272a] rounded-lg p-2.5 text-xs text-white focus:outline-none focus:border-rose-500"
            />
          </div>

          {/* Target Theme */}
          <div className="flex flex-col gap-1">
            <label className="text-xs font-bold text-zinc-300">آرمان و تم راهبردی چرخه:</label>
            <input
              type="text"
              value={targetTheme}
              onChange={(e) => setTargetTheme(e.target.value)}
              placeholder="مثال: تثبیت روتین صبحگاهی و تکمیل پروژه اصلی"
              className="w-full bg-[#18181b] border border-[#27272a] rounded-lg p-2.5 text-xs text-white placeholder:text-zinc-600 focus:outline-none focus:border-rose-500"
            />
          </div>

          {/* Inherited Streak */}
          <div className="flex flex-col gap-1">
            <label className="text-xs font-bold text-zinc-300">استریک موروثی از دوره قبل (اختیاری):</label>
            <input
              type="number"
              min="0"
              value={inheritedStreak}
              onChange={(e) => setInheritedStreak(parseInt(e.target.value, 10) || 0)}
              className="w-full bg-[#18181b] border border-[#27272a] rounded-lg p-2.5 text-xs text-white focus:outline-none focus:border-rose-500"
            />
          </div>

          {/* 4 Rules */}
          <div className="flex flex-col gap-2">
            <label className="text-xs font-bold text-zinc-300">قوانین و مرام‌نامه چرخه (۴ اصل):</label>
            <input
              type="text"
              value={rule1}
              onChange={(e) => setRule1(e.target.value)}
              className="w-full bg-[#18181b] border border-[#27272a] rounded-lg p-2 text-xs text-white focus:outline-none focus:border-amber-500"
            />
            <input
              type="text"
              value={rule2}
              onChange={(e) => setRule2(e.target.value)}
              className="w-full bg-[#18181b] border border-[#27272a] rounded-lg p-2 text-xs text-white focus:outline-none focus:border-amber-500"
            />
            <input
              type="text"
              value={rule3}
              onChange={(e) => setRule3(e.target.value)}
              className="w-full bg-[#18181b] border border-[#27272a] rounded-lg p-2 text-xs text-white focus:outline-none focus:border-amber-500"
            />
            <input
              type="text"
              value={rule4}
              onChange={(e) => setRule4(e.target.value)}
              className="w-full bg-[#18181b] border border-[#27272a] rounded-lg p-2 text-xs text-white focus:outline-none focus:border-amber-500"
            />
          </div>

          <div className="p-2 border-t border-[#27272a] flex items-center gap-2 mt-2">
            <button
              type="button"
              onClick={onDismiss}
              className="flex-1 py-2.5 rounded-xl border border-[#27272a] text-xs font-bold text-zinc-300 hover:bg-[#18181b] transition-colors"
            >
              انصراف
            </button>
            <button
              type="submit"
              className="flex-1 py-2.5 rounded-xl bg-rose-600 hover:bg-rose-500 text-xs font-bold text-white transition-transform active:scale-95 shadow-md"
            >
              تاسیس رسمی چرخه و آغاز تعهد
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

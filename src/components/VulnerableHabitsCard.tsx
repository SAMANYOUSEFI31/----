import React from 'react';
import { AlertTriangle, Clock, Target } from 'lucide-react';
import { VulnerableHabit } from '../types';
import { toPersianDigits } from '../engine/numberUtils';

interface VulnerableHabitsCardProps {
  vulnerableHabits: VulnerableHabit[];
  dominantFailureReason: string;
  dominantFailureTime: string;
}

export const VulnerableHabitsCard: React.FC<VulnerableHabitsCardProps> = ({
  vulnerableHabits,
  dominantFailureReason,
  dominantFailureTime
}) => {
  return (
    <div className="w-full rounded-2xl bg-[#121215] border border-[#27272a] p-4 flex flex-col gap-3 shadow-lg">
      <div className="flex items-center justify-between">
        <span className="text-xs text-rose-400 font-bold">زیر ۷۰٪ پایبندی</span>
        <div className="flex items-center gap-1.5 text-white text-sm font-bold">
          <span>ماتریس آسیب‌پذیری و تحلیل افت</span>
          <AlertTriangle className="w-4 h-4 text-rose-400" />
        </div>
      </div>

      {/* Vulnerable Habits List */}
      <div className="flex flex-col gap-2">
        {vulnerableHabits.length > 0 ? (
          vulnerableHabits.map((v) => (
            <div
              key={v.key}
              className="w-full bg-[#18181b] border border-rose-500/20 rounded-xl p-2.5 flex items-center justify-between"
            >
              <div className="text-xs font-black text-rose-400">
                {toPersianDigits(v.ratePct)}٪
              </div>
              <div className="text-xs font-bold text-zinc-200">
                عادت «{v.titleFa}»
              </div>
            </div>
          ))
        ) : (
          <div className="text-xs text-emerald-400 text-center py-2 bg-emerald-500/10 border border-emerald-500/20 rounded-xl">
            تمامی پایه‌ها در وضعیت ایمن و بالای ۷۰٪ قرار دارند.
          </div>
        )}
      </div>

      {/* Dominant Reason & Time */}
      <div className="grid grid-cols-2 gap-2 pt-2 border-t border-[#27272a]/60 text-right">
        <div className="bg-[#18181b] p-2.5 rounded-xl border border-[#27272a]">
          <div className="flex items-center justify-end gap-1 text-[10px] text-zinc-500 mb-1">
            <span>شایع‌ترین علت افت</span>
            <Target className="w-3 h-3" />
          </div>
          <div className="text-xs font-bold text-zinc-200 truncate">
            {dominantFailureReason}
          </div>
        </div>

        <div className="bg-[#18181b] p-2.5 rounded-xl border border-[#27272a]">
          <div className="flex items-center justify-end gap-1 text-[10px] text-zinc-500 mb-1">
            <span>ساعت بحرانی آسیب‌پذیری</span>
            <Clock className="w-3 h-3" />
          </div>
          <div className="text-xs font-bold text-zinc-200 truncate">
            {dominantFailureTime}
          </div>
        </div>
      </div>
    </div>
  );
};

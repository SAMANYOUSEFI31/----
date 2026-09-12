import React from 'react';
import { Sparkles, Check } from 'lucide-react';

interface SpecialMissionCardProps {
  isCompleted: boolean;
  onToggle: () => void;
  disabled?: boolean;
}

export const SpecialMissionCard: React.FC<SpecialMissionCardProps> = ({
  isCompleted,
  onToggle,
  disabled
}) => {
  return (
    <div
      onClick={() => !disabled && onToggle()}
      className={`w-full rounded-xl p-3.5 flex items-center justify-between transition-all cursor-pointer select-none border ${
        isCompleted
          ? 'bg-amber-500/10 border-amber-500/40 shadow-sm'
          : 'bg-[#121215] border-[#27272a] hover:border-amber-500/30'
      } ${disabled ? 'opacity-50 pointer-events-none' : 'active:scale-[0.99]'}`}
    >
      {/* Checkbox Trigger (Left in RTL) */}
      <div
        className={`w-7 h-7 rounded-lg flex items-center justify-center transition-all ${
          isCompleted
            ? 'bg-amber-400 text-black shadow-md'
            : 'bg-[#18181b] border border-[#3f3f46]'
        }`}
      >
        {isCompleted && <Check className="w-4 h-4 stroke-[3]" />}
      </div>

      {/* Title & Subtitle (Center in RTL) */}
      <div className="flex-1 px-3 text-right">
        <div className="flex items-center justify-end gap-2">
          <span className="text-[10px] font-bold px-1.5 py-0.5 rounded bg-amber-500/15 border border-amber-500/30 text-amber-300">
            ۲+ امتیاز تسلط
          </span>
          <span
            className={`text-sm font-bold transition-colors ${
              isCompleted ? 'text-amber-300' : 'text-zinc-100'
            }`}
          >
            ماموریت ویژه روز
          </span>
        </div>
        <div className="text-xs text-zinc-400 leading-tight mt-0.5">
          یک گام فراتر از استانداردهای عادی جهت تحقق کمال دیسیپلین
        </div>
      </div>

      {/* Special Mission Icon */}
      <div className="w-10 h-10 rounded-xl bg-amber-500/15 border border-amber-500/30 flex items-center justify-center shrink-0">
        <Sparkles className="w-5 h-5 text-amber-400" />
      </div>
    </div>
  );
};

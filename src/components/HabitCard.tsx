import React from 'react';
import { Sun, Dumbbell, BookOpen, PenTool, Briefcase, Check } from 'lucide-react';
import { HabitDef, HabitKey } from '../types';

interface HabitCardProps {
  habit: HabitDef;
  isCompleted: boolean;
  onToggle: () => void;
  disabled?: boolean;
}

export const HabitCard: React.FC<HabitCardProps> = ({
  habit,
  isCompleted,
  onToggle,
  disabled
}) => {
  const renderIcon = (name: string) => {
    switch (name) {
      case 'Sun':
        return <Sun className="w-5 h-5 text-amber-400" />;
      case 'Dumbbell':
        return <Dumbbell className="w-5 h-5 text-emerald-400" />;
      case 'BookOpen':
        return <BookOpen className="w-5 h-5 text-blue-400" />;
      case 'PenTool':
        return <PenTool className="w-5 h-5 text-purple-400" />;
      case 'Briefcase':
        return <Briefcase className="w-5 h-5 text-rose-400" />;
      default:
        return <Sun className="w-5 h-5 text-zinc-400" />;
    }
  };

  return (
    <div
      onClick={() => !disabled && onToggle()}
      className={`w-full rounded-xl p-3.5 flex items-center justify-between transition-all cursor-pointer select-none border ${
        isCompleted
          ? 'bg-[#18181b] border-emerald-500/40 shadow-sm'
          : 'bg-[#121215] border-[#27272a] hover:border-[#3f3f46]'
      } ${disabled ? 'opacity-50 pointer-events-none' : 'active:scale-[0.99]'}`}
    >
      {/* Checkbox Trigger (Left in RTL) */}
      <div
        className={`w-7 h-7 rounded-lg flex items-center justify-center transition-all ${
          isCompleted
            ? 'bg-emerald-500 text-black shadow-md'
            : 'bg-[#18181b] border border-[#3f3f46]'
        }`}
      >
        {isCompleted && <Check className="w-4 h-4 stroke-[3]" />}
      </div>

      {/* Title & Subtitle (Center in RTL) */}
      <div className="flex-1 px-3 text-right">
        <div
          className={`text-sm font-bold transition-colors ${
            isCompleted ? 'text-emerald-300' : 'text-zinc-100'
          }`}
        >
          {habit.titleFa}
        </div>
        <div className="text-xs text-zinc-400 leading-tight mt-0.5">
          {habit.subtitleFa}
        </div>
      </div>

      {/* Habit Icon (Right in RTL) */}
      <div
        className="w-10 h-10 rounded-xl flex items-center justify-center shrink-0 border"
        style={{
          backgroundColor: `${habit.colorHex}12`,
          borderColor: `${habit.colorHex}30`
        }}
      >
        {renderIcon(habit.iconName)}
      </div>
    </div>
  );
};

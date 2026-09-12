import React from 'react';
import {
  ChevronLeft,
  ChevronRight,
  Shield,
  ShieldAlert,
  Sparkles,
  Lock,
  Edit,
  Edit3,
  FileText,
  Save
} from 'lucide-react';
import { DailyLog, DailyComputed, HabitKey } from '../types';
import { FOUNDATION_HABITS } from '../engine/bushidoCalculations';
import { getDayLabelFa } from '../engine/dateUtils';
import { toPersianDigits } from '../engine/numberUtils';
import { DailyScoreWell } from './DailyScoreWell';
import { HabitCard } from './HabitCard';
import { SpecialMissionCard } from './SpecialMissionCard';

interface BattlefieldViewProps {
  selectedDate: string;
  logicalToday: string;
  log: DailyLog;
  computed: DailyComputed;
  onPrevDay: () => void;
  onNextDay: () => void;
  onJumpToToday: () => void;
  onToggleHabit: (key: HabitKey) => void;
  onToggleSpecialMission: () => void;
  onUpdateNotes: (notes: string) => void;
  onOpenAutopsy: () => void;
}

export const BattlefieldView: React.FC<BattlefieldViewProps> = ({
  selectedDate,
  logicalToday,
  log,
  computed,
  onPrevDay,
  onNextDay,
  onJumpToToday,
  onToggleHabit,
  onToggleSpecialMission,
  onUpdateNotes,
  onOpenAutopsy
}) => {
  const isToday = selectedDate === logicalToday;
  const isLocked = computed.isLockedDueToPastDebt;

  return (
    <div className="w-full max-w-2xl mx-auto flex flex-col gap-4 pb-20">
      {/* Date Switcher (RTL Layout: Next on Left, Prev on Right, Jump to Today) */}
      <div className="w-full bg-[#121215] border border-[#27272a] rounded-2xl p-2.5 flex items-center justify-between shadow-md">
        {/* Next Day (Left in RTL) */}
        <button
          onClick={onNextDay}
          title="روز بعد"
          className="p-2 rounded-xl bg-[#18181b] border border-[#27272a] hover:border-[#3f3f46] text-zinc-300 hover:text-white transition-colors cursor-pointer"
        >
          <ChevronLeft className="w-4 h-4" />
        </button>

        {/* Date Center */}
        <div className="flex flex-col items-center">
          <div className="text-sm font-bold text-white flex items-center gap-1.5">
            {DateLabelWithBadge(selectedDate, logicalToday)}
          </div>
          <div className="text-[11px] text-zinc-500 font-mono">
            {toPersianDigits(selectedDate)}
          </div>
        </div>

        {/* Prev Day & Jump to Today (Right in RTL) */}
        <div className="flex items-center gap-1.5">
          {!isToday && (
            <button
              onClick={onJumpToToday}
              className="px-2 py-1 rounded-lg bg-amber-500/15 border border-amber-500/30 text-amber-300 text-xs font-bold hover:bg-amber-500/25 transition-colors cursor-pointer"
            >
              امروز
            </button>
          )}
          <button
            onClick={onPrevDay}
            title="روز قبل"
            className="p-2 rounded-xl bg-[#18181b] border border-[#27272a] hover:border-[#3f3f46] text-zinc-300 hover:text-white transition-colors cursor-pointer"
          >
            <ChevronRight className="w-4 h-4" />
          </button>
        </div>
      </div>

      {/* Locked System Warning Banner */}
      {isLocked && (
        <div className="w-full bg-red-500/10 border border-red-500/40 rounded-2xl p-3.5 flex items-center gap-3 text-right">
          <div className="flex-1">
            <div className="text-xs font-bold text-red-400 mb-0.5">
              سیستم قفل شده است!
            </div>
            <div className="text-xs text-zinc-300 leading-relaxed">
              برای ثبت روز جاری، باید پرونده بدهی کالبدشکافی نشده روزهای قبل را تسویه کنید.
            </div>
          </div>
          <div className="w-10 h-10 rounded-xl bg-red-500/20 border border-red-500/40 flex items-center justify-center text-red-400 shrink-0">
            <Lock className="w-5 h-5" />
          </div>
        </div>
      )}

      {/* Daily Score Well */}
      <DailyScoreWell computed={computed} />

      {/* 5 Foundation Habits Section Header */}
      <div className="flex items-center justify-between px-1">
        <span className="text-xs font-bold text-zinc-400">
          {toPersianDigits(computed.habitsCount)} از ۵ تکمیل شده
        </span>
        <div className="flex items-center gap-1.5 text-sm font-bold text-white">
          <span>۵ رکن فونداسیون روزانه</span>
          <Shield className="w-4 h-4 text-zinc-400" />
        </div>
      </div>

      {/* Habit Cards */}
      <div className="flex flex-col gap-2.5">
        {FOUNDATION_HABITS.map((h) => (
          <HabitCard
            key={h.key}
            habit={h}
            isCompleted={!!log[h.key]}
            onToggle={() => onToggleHabit(h.key)}
            disabled={isLocked}
          />
        ))}
      </div>

      {/* Special Mission Card */}
      <SpecialMissionCard
        isCompleted={log.specialMission}
        onToggle={onToggleSpecialMission}
        disabled={isLocked}
      />

      {/* Autopsy Trigger Button */}
      {!computed.isStandard && selectedDate <= logicalToday && (
        <div
          onClick={onOpenAutopsy}
          className={`w-full rounded-2xl p-3.5 flex items-center justify-between cursor-pointer transition-all border ${
            computed.statusType === 'BURNED_RESOLVED'
              ? 'bg-purple-500/10 border-purple-500/30 text-purple-300'
              : 'bg-red-500/10 border-red-500/30 text-red-400 hover:border-red-500/50'
          }`}
        >
          <Edit className="w-4 h-4 text-zinc-400" />
          <div className="text-right flex-1 px-3">
            <div className="text-xs font-bold">
              {computed.statusType === 'BURNED_RESOLVED'
                ? 'ویرایش کالبدشکافی ثبت‌شده'
                : 'کالبدشکافی افت و ثبت علت شکست'}
            </div>
            <div className="text-[11px] text-zinc-400">
              {computed.statusType === 'BURNED_RESOLVED'
                ? 'پرونده بسته است — برای بازبینی کلیک کنید'
                : 'ثبت علت عدم تکمیل ۵ پایه و قانون پیشگیرانه'}
            </div>
          </div>
          <div
            className={`w-9 h-9 rounded-xl flex items-center justify-center shrink-0 border ${
              computed.statusType === 'BURNED_RESOLVED'
                ? 'bg-purple-500/20 border-purple-500/40 text-purple-300'
                : 'bg-red-500/20 border-red-500/40 text-red-400'
            }`}
          >
            <ShieldAlert className="w-5 h-5" />
          </div>
        </div>
      )}

      {/* Daily Reflection Notes */}
      <div className="w-full bg-[#121215] border border-[#27272a] rounded-2xl p-4 flex flex-col gap-2.5 shadow-md">
        <div className="flex items-center justify-between">
          <span className="text-xs text-zinc-500">بینش‌ها و روایت نبرد</span>
          <div className="flex items-center gap-1.5 text-sm font-bold text-white">
            <span>یادداشت و روایت روزانه</span>
            <FileText className="w-4 h-4 text-zinc-400" />
          </div>
        </div>

        <textarea
          value={log.notes || ''}
          onChange={(e) => onUpdateNotes(e.target.value)}
          placeholder="ثبت بینش‌ها، چالش‌ها و دستاوردهای امروز..."
          rows={3}
          className="w-full bg-[#18181b] border border-[#27272a] rounded-xl p-3 text-xs text-white placeholder:text-zinc-600 focus:outline-none focus:border-rose-500 resize-none text-right"
        />
      </div>
    </div>
  );
};

function DateLabelWithBadge(selectedDate: string, logicalToday: string) {
  const label = getDayLabelFa(selectedDate, logicalToday);
  const isToday = selectedDate === logicalToday;
  return (
    <span className={isToday ? 'text-amber-400 font-black' : 'text-zinc-200'}>
      {label}
    </span>
  );
}

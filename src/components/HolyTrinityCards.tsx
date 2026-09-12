import React from 'react';
import { Flame, CheckCircle2, Award } from 'lucide-react';
import { toPersianDigits } from '../engine/numberUtils';

interface HolyTrinityCardsProps {
  pureStreak: number;
  standardDays: number;
  totalScore: number;
}

export const HolyTrinityCards: React.FC<HolyTrinityCardsProps> = ({
  pureStreak,
  standardDays,
  totalScore
}) => {
  return (
    <div className="grid grid-cols-3 gap-2 sm:gap-3 w-full">
      {/* 1. Pure Streak */}
      <div className="rounded-xl bg-[#121215] border border-rose-500/20 p-3 flex flex-col items-center justify-center gap-1 text-center shadow-md">
        <div className="w-8 h-8 rounded-lg bg-rose-500/10 border border-rose-500/20 flex items-center justify-center text-rose-400">
          <Flame className="w-4 h-4 fill-rose-400" />
        </div>
        <div className="text-lg font-black text-rose-400 mt-1">
          {toPersianDigits(pureStreak)}
        </div>
        <div className="text-[11px] font-bold text-zinc-300">
          استریک مداوم
        </div>
        <div className="text-[10px] text-zinc-500">
          روزهای ۵ از ۵ متوالی
        </div>
      </div>

      {/* 2. Standard Days */}
      <div className="rounded-xl bg-[#121215] border border-emerald-500/20 p-3 flex flex-col items-center justify-center gap-1 text-center shadow-md">
        <div className="w-8 h-8 rounded-lg bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-400">
          <CheckCircle2 className="w-4 h-4" />
        </div>
        <div className="text-lg font-black text-emerald-400 mt-1">
          {toPersianDigits(standardDays)}
        </div>
        <div className="text-[11px] font-bold text-zinc-300">
          روزهای استاندارد
        </div>
        <div className="text-[10px] text-zinc-500">
          ۵ پایه فونداسیون
        </div>
      </div>

      {/* 3. Total Score */}
      <div className="rounded-xl bg-[#121215] border border-amber-500/20 p-3 flex flex-col items-center justify-center gap-1 text-center shadow-md">
        <div className="w-8 h-8 rounded-lg bg-amber-500/10 border border-amber-500/20 flex items-center justify-center text-amber-400">
          <Award className="w-4 h-4" />
        </div>
        <div className="text-lg font-black text-amber-400 mt-1">
          {toPersianDigits(totalScore)}
        </div>
        <div className="text-[11px] font-bold text-zinc-300">
          مجموع امتیاز
        </div>
        <div className="text-[10px] text-zinc-500">
          حاصل انضباط چرخه
        </div>
      </div>
    </div>
  );
};

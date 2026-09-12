import React from 'react';
import { Flame, ShieldAlert, Sparkles, Plus, ChevronDown } from 'lucide-react';
import { Cycle } from '../types';
import { toPersianDigits } from '../engine/numberUtils';

interface TopHubBarProps {
  activeCycle: Cycle;
  allCycles: Cycle[];
  pureStreak: number;
  unresolvedDebtCount: number;
  onSelectCycle: (cycleId: string) => void;
  onOpenVip: () => void;
  onOpenCreateCycle: () => void;
}

export const TopHubBar: React.FC<TopHubBarProps> = ({
  activeCycle,
  allCycles,
  pureStreak,
  unresolvedDebtCount,
  onSelectCycle,
  onOpenVip,
  onOpenCreateCycle
}) => {
  return (
    <header className="sticky top-0 z-40 w-full bg-[#0c0c0e]/95 backdrop-blur-md border-b border-[#27272a] px-3 sm:px-4 py-2 flex items-center justify-between gap-2">
      {/* Brand & Cycle Selector (RTL Right) */}
      <div className="flex items-center gap-2">
        <div className="w-8 h-8 rounded-lg bg-[#e11d48] flex items-center justify-center font-black text-white text-sm shadow-md">
          武
        </div>

        <div className="relative group">
          <select
            value={activeCycle?.id || ''}
            onChange={(e) => onSelectCycle(e.target.value)}
            className="appearance-none bg-[#18181b] border border-[#27272a] hover:border-[#3f3f46] text-white text-xs font-bold rounded-lg pl-6 pr-2 py-1.5 focus:outline-none focus:border-[#e11d48] cursor-pointer transition-colors max-w-[140px] sm:max-w-[200px] truncate"
          >
            {allCycles.map((c) => (
              <option key={c.id} value={c.id}>
                {c.title}
              </option>
            ))}
          </select>
          <ChevronDown className="w-3 h-3 text-[#a1a1aa] absolute left-2 top-1/2 -translate-y-1/2 pointer-events-none" />
        </div>

        <button
          onClick={onOpenCreateCycle}
          title="تاسیس چرخه جدید"
          className="p-1.5 rounded-lg bg-[#18181b] border border-[#27272a] hover:border-[#e11d48] text-[#a1a1aa] hover:text-white transition-colors cursor-pointer"
        >
          <Plus className="w-3.5 h-3.5" />
        </button>
      </div>

      {/* Metrics & VIP CTA (RTL Left) */}
      <div className="flex items-center gap-2">
        {/* Pure Streak Flame */}
        <div className="flex items-center gap-1 px-2.5 py-1 rounded-lg bg-rose-500/10 border border-rose-500/20 text-[#fb7185] text-xs font-bold">
          <Flame className="w-3.5 h-3.5 text-[#fb7185] fill-[#fb7185]" />
          <span>{toPersianDigits(pureStreak)}</span>
        </div>

        {/* Unresolved Debt Alert */}
        {unresolvedDebtCount > 0 && (
          <div className="flex items-center gap-1 px-2 py-1 rounded-lg bg-red-500/10 border border-red-500/30 text-red-400 text-xs font-bold animate-pulse">
            <ShieldAlert className="w-3.5 h-3.5 text-red-400" />
            <span>{toPersianDigits(unresolvedDebtCount)} بدهی</span>
          </div>
        )}

        {/* VIP Upgrade CTA */}
        <button
          onClick={onOpenVip}
          className="flex items-center gap-1 px-2.5 py-1 rounded-lg bg-[#fbbf24] hover:bg-[#f59e0b] text-[#09090b] text-xs font-bold transition-transform active:scale-95 cursor-pointer shadow-sm"
        >
          <Sparkles className="w-3 h-3 text-[#09090b]" />
          <span className="hidden xs:inline">VIP سامورایی</span>
          <span className="xs:hidden">VIP</span>
        </button>
      </div>
    </header>
  );
};

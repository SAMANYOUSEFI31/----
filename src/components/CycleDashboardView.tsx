import React from 'react';
import { Shield, Sparkles, MessageSquare } from 'lucide-react';
import { CycleMetrics, DailyLog } from '../types';
import { toPersianDigits } from '../engine/numberUtils';
import { HolyTrinityCards } from './HolyTrinityCards';
import { PhantomDisciplineCard } from './PhantomDisciplineCard';
import { TacticalHeatmap90 } from './TacticalHeatmap90';
import { VulnerableHabitsCard } from './VulnerableHabitsCard';

interface CycleDashboardViewProps {
  metrics: CycleMetrics;
  logs: DailyLog[];
  selectedDate: string;
  onSelectDate: (date: string) => void;
}

export const CycleDashboardView: React.FC<CycleDashboardViewProps> = ({
  metrics,
  logs,
  selectedDate,
  onSelectDate
}) => {
  const cycle = metrics.cycle;

  return (
    <div className="w-full max-w-2xl mx-auto flex flex-col gap-4 pb-20">
      {/* Master Overview Banner */}
      <div className="w-full bg-[#121215] border border-[#27272a] rounded-2xl p-4 flex flex-col gap-2.5 shadow-lg text-right">
        <div className="flex items-center justify-between">
          <span className="px-2 py-0.5 rounded-md bg-rose-500/15 border border-rose-500/30 text-rose-400 text-xs font-bold">
            {metrics.statusLabelFa}
          </span>
          <div className="text-base font-bold text-white">
            {cycle.title}
          </div>
        </div>

        {cycle.targetTheme && (
          <div className="text-xs text-zinc-400">
            آرمان چرخه: {cycle.targetTheme}
          </div>
        )}

        <div className="flex items-center justify-between text-xs text-zinc-500 pt-1 border-t border-[#27272a]/60">
          <span>پایان: {toPersianDigits(cycle.endDate)}</span>
          <span>شروع: {toPersianDigits(cycle.startDate)}</span>
        </div>
      </div>

      {/* Holy Trinity Cards */}
      <HolyTrinityCards
        pureStreak={metrics.pureStreak}
        standardDays={metrics.standardDaysCount}
        totalScore={metrics.totalScore}
      />

      {/* Phantom Discipline Score Card */}
      <PhantomDisciplineCard metrics={metrics} />

      {/* 90-Day Tactical Heatmap Grid */}
      <TacticalHeatmap90
        cycle={cycle}
        logs={logs}
        selectedDate={selectedDate}
        onSelectDate={onSelectDate}
      />

      {/* Vulnerable Habits Matrix */}
      <VulnerableHabitsCard
        vulnerableHabits={metrics.vulnerableHabits}
        dominantFailureReason={metrics.dominantFailureReason}
        dominantFailureTime={metrics.dominantFailureTime}
      />

      {/* Sensei Strategic Coach Banner */}
      <div className="w-full bg-[#121215] border border-[#27272a] rounded-2xl p-4 flex items-center gap-3 text-right shadow-md">
        <div className="flex-1">
          <div className="text-xs font-bold text-amber-400 mb-1">
            پیام راهبردی سنسی دیسیپلین
          </div>
          <div className="text-xs text-zinc-200 leading-relaxed">
            {metrics.coachMessage}
          </div>
        </div>
        <div className="w-10 h-10 rounded-xl bg-amber-500/15 border border-amber-500/40 flex items-center justify-center text-amber-400 shrink-0">
          <MessageSquare className="w-5 h-5" />
        </div>
      </div>
    </div>
  );
};

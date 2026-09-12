import React from 'react';
import { Gavel, Award, Shield, FileText, CheckCircle2, AlertCircle } from 'lucide-react';
import { CycleMetrics, Cycle } from '../types';
import { getDeterministicCourtVerdict } from '../engine/deterministicSensei';
import { toPersianDigits } from '../engine/numberUtils';

interface BushidoCourtViewProps {
  metrics: CycleMetrics;
  allCycles: Cycle[];
  onSelectCycle: (cycleId: string) => void;
}

export const BushidoCourtView: React.FC<BushidoCourtViewProps> = ({
  metrics,
  allCycles,
  onSelectCycle
}) => {
  const cycle = metrics.cycle;

  const courtResult = getDeterministicCourtVerdict(
    cycle.title,
    metrics.standardDaysCount,
    metrics.elapsedDays,
    metrics.maxPureStreak,
    metrics.disciplinePercentage,
    metrics.vulnerableHabits
  );

  const gradeColor =
    courtResult.grade === 'A+'
      ? 'text-amber-400 border-amber-400 bg-amber-400/15'
      : courtResult.grade === 'A'
      ? 'text-emerald-400 border-emerald-400 bg-emerald-400/15'
      : courtResult.grade === 'B'
      ? 'text-blue-400 border-blue-400 bg-blue-400/15'
      : 'text-rose-400 border-rose-400 bg-rose-400/15';

  return (
    <div className="w-full max-w-2xl mx-auto flex flex-col gap-4 pb-20">
      {/* Court Seal Banner */}
      <div className="w-full bg-[#121215] border border-amber-500/30 rounded-2xl p-5 flex flex-col items-center text-center gap-3 shadow-xl">
        <div className="w-14 h-14 rounded-full bg-amber-500/15 border-2 border-amber-500/40 flex items-center justify-center text-amber-400">
          <Gavel className="w-7 h-7" />
        </div>

        <div>
          <div className="text-base font-bold text-white mb-0.5">
            دیوان عالی بوشیدو و داوری ۹۰ روزه
          </div>
          <div className="text-xs text-zinc-400">
            حکم رسمی ارزیابی چرخه «{cycle.title}»
          </div>
        </div>

        {/* Grade Stamp */}
        <div className={`px-4 py-2 rounded-2xl border-2 flex items-center gap-2 ${gradeColor}`}>
          <span className="text-xs font-bold">رتبه نهایی دیسیپلین:</span>
          <span className="text-2xl font-black">{courtResult.grade}</span>
        </div>
      </div>

      {/* Official Verdict Copy */}
      <div className="w-full bg-[#121215] border border-[#27272a] rounded-2xl p-4 flex flex-col gap-2.5 shadow-md text-right">
        <div className="flex items-center justify-end gap-1.5 text-sm font-bold text-white">
          <span>متن رای و قضاوت دیوان</span>
          <FileText className="w-4 h-4 text-zinc-400" />
        </div>
        <div className="text-xs text-zinc-200 leading-relaxed">
          {courtResult.verdict}
        </div>
        <div className="text-xs text-zinc-400 leading-relaxed pt-2 border-t border-[#27272a]/60">
          {courtResult.senseiNotes}
        </div>
      </div>

      {/* Strengths & Weaknesses */}
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 text-right">
        {/* Strengths */}
        <div className="bg-[#121215] border border-[#27272a] rounded-2xl p-3.5 flex flex-col gap-2">
          <div className="flex items-center justify-end gap-1.5 text-xs font-bold text-emerald-400">
            <span>مواضع قدرت</span>
            <CheckCircle2 className="w-3.5 h-3.5" />
          </div>
          {courtResult.strengths.map((s, i) => (
            <div key={i} className="text-xs text-zinc-300">
              • {s}
            </div>
          ))}
        </div>

        {/* Weaknesses */}
        <div className="bg-[#121215] border border-[#27272a] rounded-2xl p-3.5 flex flex-col gap-2">
          <div className="flex items-center justify-end gap-1.5 text-xs font-bold text-rose-400">
            <span>نقاط آسیب‌پذیر</span>
            <AlertCircle className="w-3.5 h-3.5" />
          </div>
          {courtResult.weaknesses.map((w, i) => (
            <div key={i} className="text-xs text-zinc-300">
              • {w}
            </div>
          ))}
        </div>
      </div>

      {/* Tactical Plan for Next Cycle */}
      <div className="w-full bg-[#121215] border border-[#27272a] rounded-2xl p-4 flex flex-col gap-1.5 shadow-md text-right">
        <div className="text-xs font-bold text-amber-400">
          دستور کار تاکتیکی برای چرخه بعدی:
        </div>
        <div className="text-xs text-zinc-200 leading-relaxed">
          {courtResult.tacticalPlanForNextCycle}
        </div>
      </div>

      {/* Archive of All Cycles */}
      <div className="flex flex-col gap-2 text-right">
        <div className="text-xs font-bold text-zinc-300 px-1">
          آرشیو چرخه‌های ۹۰ روزه:
        </div>
        <div className="flex flex-col gap-2">
          {allCycles.map((c) => {
            const isSelected = c.id === cycle.id;
            return (
              <div
                key={c.id}
                onClick={() => onSelectCycle(c.id)}
                className={`w-full p-3.5 rounded-2xl border flex items-center justify-between cursor-pointer transition-all ${
                  isSelected
                    ? 'bg-rose-500/10 border-rose-500/40'
                    : 'bg-[#121215] border-[#27272a] hover:border-[#3f3f46]'
                }`}
              >
                {isSelected ? (
                  <span className="px-2 py-0.5 rounded bg-rose-500 text-white text-[10px] font-bold">
                    فعال
                  </span>
                ) : (
                  <span className="text-xs text-zinc-500">مشاهده</span>
                )}
                <div className="text-right">
                  <div className="text-xs font-bold text-white">{c.title}</div>
                  <div className="text-[10px] text-zinc-500">
                    {toPersianDigits(c.startDate)} تا {toPersianDigits(c.endDate)}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

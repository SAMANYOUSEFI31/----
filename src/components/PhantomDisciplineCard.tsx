import React from 'react';
import { Shield, TrendingUp } from 'lucide-react';
import { CycleMetrics } from '../types';
import { toPersianDigits } from '../engine/numberUtils';

interface PhantomDisciplineCardProps {
  metrics: CycleMetrics;
}

export const PhantomDisciplineCard: React.FC<PhantomDisciplineCardProps> = ({ metrics }) => {
  const pct = metrics.disciplinePercentage;
  const level = metrics.disciplineLevel;

  const levelColor =
    level === 'IRON'
      ? '#fbbf24'
      : level === 'STABLE'
      ? '#34d399'
      : level === 'UNSTABLE'
      ? '#fb7185'
      : '#f43f5e';

  const levelLabelFa =
    level === 'IRON'
      ? 'انضباط آهنین (Iron)'
      : level === 'STABLE'
      ? 'پایدار (Stable)'
      : level === 'UNSTABLE'
      ? 'ناپایدار (Unstable)'
      : 'بحران دیسیپلین (Crisis)';

  return (
    <div className="w-full rounded-2xl bg-[#121215] border border-[#27272a] p-4 flex flex-col gap-3 shadow-lg">
      <div className="flex items-center justify-between">
        <div
          className="px-2.5 py-1 rounded-md text-xs font-bold border flex items-center gap-1"
          style={{
            backgroundColor: `${levelColor}15`,
            borderColor: `${levelColor}40`,
            color: levelColor
          }}
        >
          <Shield className="w-3.5 h-3.5" />
          <span>{levelLabelFa}</span>
        </div>

        <div className="text-right">
          <div className="text-xs text-zinc-400">فرمول مخرج فانتوم</div>
          <div className="text-sm font-bold text-white">شاخص تسلط و دیسیپلین بوشیدو</div>
        </div>
      </div>

      {/* Percentage Bar */}
      <div className="flex items-center justify-between gap-3">
        <div className="text-2xl font-black text-white">
          {toPersianDigits(pct)}٪
        </div>

        <div className="flex-1 h-3 bg-[#18181b] rounded-full overflow-hidden border border-[#27272a]">
          <div
            className="h-full rounded-full transition-all duration-500"
            style={{
              width: `${pct}%`,
              backgroundColor: levelColor
            }}
          />
        </div>
      </div>

      {/* Breakdown Metrics */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-2 pt-2 border-t border-[#27272a]/60 text-right">
        <div>
          <div className="text-[10px] text-zinc-500">روزهای سپری شده</div>
          <div className="text-xs font-bold text-zinc-200">
            {toPersianDigits(metrics.elapsedDays)} از ۹۰ روز
          </div>
        </div>

        <div>
          <div className="text-[10px] text-zinc-500">روزهای باقیمانده</div>
          <div className="text-xs font-bold text-zinc-200">
            {toPersianDigits(metrics.remainingDays)} روز
          </div>
        </div>

        <div>
          <div className="text-[10px] text-zinc-500">بدهی کالبدشکافی نشده</div>
          <div
            className={`text-xs font-bold ${
              metrics.unresolvedDebtCount > 0 ? 'text-red-400' : 'text-emerald-400'
            }`}
          >
            {toPersianDigits(metrics.unresolvedDebtCount)} روز
          </div>
        </div>

        <div>
          <div className="text-[10px] text-zinc-500">حداکثر استریک چرخه</div>
          <div className="text-xs font-bold text-rose-400">
            {toPersianDigits(metrics.maxPureStreak)} روز
          </div>
        </div>
      </div>
    </div>
  );
};

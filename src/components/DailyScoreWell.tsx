import React from 'react';
import { Shield, Sparkles, AlertCircle } from 'lucide-react';
import { DailyComputed } from '../types';
import { toPersianDigits } from '../engine/numberUtils';

interface DailyScoreWellProps {
  computed: DailyComputed;
}

export const DailyScoreWell: React.FC<DailyScoreWellProps> = ({ computed }) => {
  const isMastery = computed.score === 10;
  const isStandard = computed.isStandard;
  const isFrozen = computed.statusType === 'PERSONAL_FROZEN';

  const accentColor = isMastery
    ? '#fbbf24'
    : isStandard
    ? '#34d399'
    : isFrozen
    ? '#60a5fa'
    : computed.score > 0
    ? '#fb7185'
    : '#71717a';

  return (
    <div className="w-full rounded-2xl bg-[#121215] border border-[#27272a] p-4 flex flex-col gap-3 shadow-lg">
      <div className="flex items-center justify-between">
        <div
          className="px-2.5 py-1 rounded-md text-xs font-bold border flex items-center gap-1.5"
          style={{
            backgroundColor: `${accentColor}15`,
            borderColor: `${accentColor}40`,
            color: accentColor
          }}
        >
          {isMastery ? (
            <>
              <Sparkles className="w-3.5 h-3.5" />
              <span>کمال استادی ۱۰/۱۰</span>
            </>
          ) : isStandard ? (
            <>
              <Shield className="w-3.5 h-3.5" />
              <span>روز استاندارد بوشیدو</span>
            </>
          ) : isFrozen ? (
            <span>توقف اضطراری موجه</span>
          ) : (
            <>
              <AlertCircle className="w-3.5 h-3.5" />
              <span>روز نیازمند اقدام</span>
            </>
          )}
        </div>

        <div className="text-right">
          <div className="text-xs text-[#a1a1aa]">ارزش انضباط امروز</div>
          <div className="text-sm font-bold text-white">
            {toPersianDigits(computed.score)} از ۱۰ امتیاز
          </div>
        </div>
      </div>

      {/* 10-Segment Gauge */}
      <div className="grid grid-cols-10 gap-1 sm:gap-1.5 h-2.5 w-full">
        {Array.from({ length: 10 }).map((_, index) => {
          const isFilled = index < computed.score;
          const isMissionSlot = index >= 8;
          let segmentColor = '#27272a';

          if (isFilled) {
            if (isMastery) segmentColor = '#fbbf24';
            else if (isMissionSlot) segmentColor = '#fbbf24';
            else if (isStandard) segmentColor = '#34d399';
            else segmentColor = '#fb7185';
          }

          return (
            <div
              key={index}
              className="h-full rounded-sm transition-colors duration-200"
              style={{
                backgroundColor: segmentColor,
                border: isFilled ? 'none' : '1px solid #27272a'
              }}
            />
          );
        })}
      </div>

      {/* Coach Microcopy */}
      <div className="text-xs text-[#a1a1aa] text-right leading-relaxed">
        {computed.coachStatusLabel}
      </div>
    </div>
  );
};

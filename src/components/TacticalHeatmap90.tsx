import React from 'react';
import { Calendar } from 'lucide-react';
import { Cycle, DailyLog } from '../types';
import { addDaysToDate, getLogicalTodayDate } from '../engine/dateUtils';
import { computeDailyProperties } from '../engine/bushidoCalculations';
import { toPersianDigits } from '../engine/numberUtils';

interface TacticalHeatmap90Props {
  cycle: Cycle;
  logs: DailyLog[];
  selectedDate: string;
  onSelectDate: (date: string) => void;
}

export const TacticalHeatmap90: React.FC<TacticalHeatmap90Props> = ({
  cycle,
  logs,
  selectedDate,
  onSelectDate
}) => {
  const logicalToday = getLogicalTodayDate();
  const startDate = cycle.startDate;

  const days = Array.from({ length: 90 }).map((_, i) => {
    const dStr = addDaysToDate(startDate, i);
    const existingLog = logs.find((l) => l.date === dStr);
    const synthLog: DailyLog = existingLog || {
      id: `virtual-${dStr}`,
      cycleId: cycle.id,
      date: dStr,
      isVirtual: true,
      wakeUp: false,
      workout: false,
      study: false,
      journal: false,
      hardTask: false,
      specialMission: false,
      failureReason: '',
      failureTime: '',
      autopsyNotes: '',
      countermeasure: '',
      aiFeedback: '',
      notes: ''
    };

    const computed = computeDailyProperties(synthLog, logs, logicalToday, startDate);
    return {
      dayIndex: i + 1,
      date: dStr,
      isToday: dStr === logicalToday,
      isFuture: dStr > logicalToday,
      isSelected: dStr === selectedDate,
      computed
    };
  });

  return (
    <div className="w-full rounded-2xl bg-[#121215] border border-[#27272a] p-4 flex flex-col gap-3 shadow-lg">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2 text-[11px] text-zinc-400">
          <div className="flex items-center gap-1">
            <div className="w-2.5 h-2.5 rounded-sm bg-emerald-500" />
            <span>استاندارد</span>
          </div>
          <div className="flex items-center gap-1">
            <div className="w-2.5 h-2.5 rounded-sm bg-blue-500" />
            <span>فریز</span>
          </div>
          <div className="flex items-center gap-1">
            <div className="w-2.5 h-2.5 rounded-sm bg-purple-500" />
            <span>بسته</span>
          </div>
          <div className="flex items-center gap-1">
            <div className="w-2.5 h-2.5 rounded-sm bg-red-500" />
            <span>بدهی</span>
          </div>
        </div>

        <div className="flex items-center gap-1.5 text-white text-sm font-bold">
          <span>نقشه حرارتی نبرد ۹۰ روزه</span>
          <Calendar className="w-4 h-4 text-zinc-400" />
        </div>
      </div>

      {/* 90-Cell Grid: 10 columns x 9 rows */}
      <div className="grid grid-cols-10 gap-1.5 sm:gap-2">
        {days.map((item) => {
          let bgColor = '#18181b';
          let borderColor = '#27272a';

          if (item.isFuture) {
            bgColor = '#141416';
            borderColor = '#202023';
          } else if (item.computed.isStandard) {
            bgColor = item.computed.score === 10 ? '#fbbf24' : '#10b981';
            borderColor = item.computed.score === 10 ? '#f59e0b' : '#059669';
          } else if (item.computed.statusType === 'PERSONAL_FROZEN') {
            bgColor = '#3b82f6';
            borderColor = '#2563eb';
          } else if (item.computed.statusType === 'BURNED_RESOLVED') {
            bgColor = '#8b5cf6';
            borderColor = '#7c3aed';
          } else if (item.date < logicalToday) {
            bgColor = '#ef4444';
            borderColor = '#dc2626';
          } else {
            // Today incomplete
            bgColor = '#27272a';
            borderColor = '#3f3f46';
          }

          return (
            <button
              key={item.date}
              onClick={() => onSelectDate(item.date)}
              title={`${toPersianDigits(item.dayIndex)}: ${item.date}`}
              className={`aspect-square rounded-md flex flex-col items-center justify-center transition-all cursor-pointer relative ${
                item.isSelected ? 'ring-2 ring-white scale-105 z-10' : ''
              } ${item.isToday ? 'border-2 border-amber-400 font-black' : 'border'}`}
              style={{
                backgroundColor: bgColor,
                borderColor: item.isToday ? '#fbbf24' : borderColor
              }}
            >
              <span
                className={`text-[9px] sm:text-[10px] font-bold ${
                  item.computed.isStandard || item.computed.statusType !== 'BURNED_UNRESOLVED'
                    ? 'text-black'
                    : 'text-zinc-400'
                }`}
              >
                {toPersianDigits(item.dayIndex)}
              </span>
            </button>
          );
        })}
      </div>
    </div>
  );
};

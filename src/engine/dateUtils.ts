import { toPersianDigits } from './numberUtils';

export function getLogicalTodayDate(nightOwlCutoffHour = 4): string {
  const now = new Date();
  if (now.getHours() < nightOwlCutoffHour) {
    now.setDate(now.getDate() - 1);
  }
  return now.toISOString().split('T')[0];
}

export function addDaysToDate(dateStr: string, days: number): string {
  const d = new Date(dateStr + 'T00:00:00');
  d.setDate(d.getDate() + days);
  return d.toISOString().split('T')[0];
}

export function daysBetween(startDateStr: string, endDateStr: string): number {
  const s = new Date(startDateStr + 'T00:00:00');
  const e = new Date(endDateStr + 'T00:00:00');
  const diffMs = e.getTime() - s.getTime();
  return Math.round(diffMs / (1000 * 60 * 60 * 24));
}

export function getDayLabelFa(dateStr: string, logicalToday = getLogicalTodayDate()): string {
  if (dateStr === logicalToday) {
    return 'روز جاری نبرد';
  }
  const diff = daysBetween(logicalToday, dateStr);
  if (diff === 1) return 'فردا (۱ روز بعد)';
  if (diff > 1) return `${toPersianDigits(diff)} روز بعد`;
  if (diff === -1) return 'دیروز (۱ روز قبل)';
  return `${toPersianDigits(Math.abs(diff))} روز قبل`;
}

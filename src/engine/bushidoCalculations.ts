import {
  Cycle,
  DailyLog,
  DailyComputed,
  CycleMetrics,
  HabitDef,
  VulnerableHabit,
  DisciplineLevel,
  DayStatusType,
  CycleStatusType
} from '../types';
import { addDaysToDate, daysBetween, getLogicalTodayDate } from './dateUtils';
import { toPersianDigits } from './numberUtils';

export const FOUNDATION_HABITS: HabitDef[] = [
  {
    key: 'wakeUp',
    title: 'Early Rise',
    titleFa: 'سحرخیزی',
    subtitleFa: 'بیدارباش سر ساعت بدون بهانه',
    iconName: 'Sun',
    colorHex: '#FBBF24'
  },
  {
    key: 'workout',
    title: 'Workout',
    titleFa: 'ورزش و تحرک',
    subtitleFa: 'فعالیت بدنی هدفمند و منظم',
    iconName: 'Dumbbell',
    colorHex: '#34D399'
  },
  {
    key: 'study',
    title: 'Study & Reading',
    titleFa: 'مطالعه تخصصی',
    subtitleFa: 'تغذیه ذهن و یادگیری عمیق',
    iconName: 'BookOpen',
    colorHex: '#60A5FA'
  },
  {
    key: 'journal',
    title: 'Journaling',
    titleFa: 'ژورنال‌نویسی',
    subtitleFa: 'ثبت روزانه، تحلیل ذهن و شفافیت',
    iconName: 'PenTool',
    colorHex: '#C084FC'
  },
  {
    key: 'hardTask',
    title: 'Deep Hard Task',
    titleFa: 'کار سخت روز',
    subtitleFa: 'سنگین‌ترین قورباغه روزکاری',
    iconName: 'Briefcase',
    colorHex: '#FB7185'
  }
];

export function computeDailyProperties(
  log: DailyLog,
  allCycleLogs: DailyLog[],
  logicalToday: string = getLogicalTodayDate(),
  cycleStartDate?: string
): DailyComputed {
  let habitsCount = 0;
  if (log.wakeUp) habitsCount++;
  if (log.workout) habitsCount++;
  if (log.study) habitsCount++;
  if (log.journal) habitsCount++;
  if (log.hardTask) habitsCount++;

  const isStandard = habitsCount === 5;
  const missionScore = log.specialMission ? 2 : 0;
  const bonusScore = isStandard ? 3 : 0;
  const score = Math.min(10, habitsCount + missionScore + bonusScore);

  let statusType: DayStatusType = 'BURNED_UNRESOLVED';
  if (isStandard) {
    statusType = 'STANDARD';
  } else if (log.failureReason === 'دلایل شخصی') {
    statusType = 'PERSONAL_FROZEN';
  } else if (log.failureReason && log.failureTime) {
    statusType = 'BURNED_RESOLVED';
  }

  const needsAutopsy = log.date < logicalToday && !isStandard && statusType === 'BURNED_UNRESOLVED';

  let pastUnresolvedCount = 0;
  if (cycleStartDate && log.date > cycleStartDate) {
    let checkDate = cycleStartDate;
    while (checkDate < log.date && checkDate < logicalToday) {
      const existing = allCycleLogs.find(
        (l) => l.date === checkDate && (!log.cycleId || !l.cycleId || l.cycleId === log.cycleId)
      );
      if (!existing) {
        pastUnresolvedCount++;
      } else {
        let lCount = 0;
        if (existing.wakeUp) lCount++;
        if (existing.workout) lCount++;
        if (existing.study) lCount++;
        if (existing.journal) lCount++;
        if (existing.hardTask) lCount++;
        const lStandard = lCount === 5;
        if (!lStandard && existing.failureReason !== 'دلایل شخصی') {
          if (!existing.failureReason || !existing.failureTime) {
            pastUnresolvedCount++;
          }
        }
      }
      checkDate = addDaysToDate(checkDate, 1);
    }
  }

  const isLockedDueToPastDebt = log.date === logicalToday && pastUnresolvedCount > 0;

  const smTag = log.specialMission ? ' + ماموریت ویژه' : '';
  let coachStatusLabel = '';
  if (log.date > logicalToday) {
    const diff = daysBetween(logicalToday, log.date);
    coachStatusLabel = diff === 1 ? 'فردا هنوز فرا نرسیده است. تمرکز بر فتح روز جاری است.' : `${toPersianDigits(diff)} روز تا این تاریخ باقی است. تمرکز بر روز جاری است.`;
  } else if (log.date < logicalToday) {
    if (statusType === 'PERSONAL_FROZEN') {
      coachStatusLabel = 'توقف اضطراری؛ زنجیره بدون آسیب حفظ شد.';
    } else if (isStandard) {
      coachStatusLabel = `روز استاندارد تثبیت شد${smTag}`;
    } else if (statusType === 'BURNED_UNRESOLVED') {
      coachStatusLabel = 'نیازمند کالبدشکافی؛ ثبت علت افت الزامی است.';
    } else {
      coachStatusLabel = 'پرونده شکست با ثبت کالبدشکافی بسته شد.';
    }
  } else {
    if (isStandard) {
      coachStatusLabel = log.specialMission ? 'کمال تعهد با ۵ پایه و ماموریت ویژه ثبت شد.' : 'روز استاندارد با ۵ پایه فونداسیون تثبیت شد.';
    } else if (statusType === 'PERSONAL_FROZEN') {
      coachStatusLabel = 'توقف اضطراری؛ زنجیره در امان است.';
    } else if (habitsCount === 4) {
      coachStatusLabel = `فقط ۱ پایه تا استانداردسازی روز فاصله دارید.${smTag}`;
    } else if (habitsCount >= 1) {
      coachStatusLabel = `${toPersianDigits(5 - habitsCount)} پایه تا استانداردسازی روز باقی مانده است.${smTag}`;
    } else {
      coachStatusLabel = 'روز در جریان است؛ ثبت پایه‌های فونداسیون را آغاز کنید.';
    }
  }

  const displayScore = score === 10 ? 'کمال تعهد: ۱۰ از ۱۰' : `امتیاز ارزش روز: ${toPersianDigits(score)} از ۱۰`;
  const displayProgressBlocks = '■ '.repeat(score) + '□ '.repeat(Math.max(0, 10 - score));

  return {
    isStandard,
    habitsCount,
    score,
    statusType,
    needsAutopsy,
    isLockedDueToPastDebt,
    coachStatusLabel,
    displayScore,
    displayProgressBlocks
  };
}

export function computeCycleMetrics(
  cycle: Cycle,
  logs: DailyLog[],
  allCycles: Cycle[] = [],
  logicalToday: string = getLogicalTodayDate()
): CycleMetrics {
  const cycleLogs = logs
    .filter((l) => l.cycleId === cycle.id || (!l.cycleId && l.date >= cycle.startDate && l.date <= cycle.endDate))
    .sort((a, b) => a.date.localeCompare(b.date));

  const cycleStartDate = cycle.startDate;
  const cycleEndDate = cycle.endDate || addDaysToDate(cycle.startDate, 89);

  let status: CycleStatusType = 'ACTIVE';
  let statusLabelFa = 'چرخه فعال';

  const otherCycles = allCycles.filter((c) => c.id !== cycle.id);
  const hasOverlap = otherCycles.some((oc) => {
    const ocEnd = oc.endDate || addDaysToDate(oc.startDate, 89);
    return !(cycleEndDate < oc.startDate || cycleStartDate > ocEnd);
  });

  if (hasOverlap) {
    status = 'OVERLAP_ERROR';
    statusLabelFa = 'تداخل تقویمی چرخه';
  } else if (cycle.isArchived) {
    status = 'ARCHIVED';
    statusLabelFa = 'بایگانی شده';
  } else if (logicalToday < cycleStartDate) {
    status = 'UPCOMING';
    statusLabelFa = 'چرخه آینده';
  } else if (logicalToday > cycleEndDate) {
    if (cycle.reportRead) {
      status = 'ARCHIVED';
      statusLabelFa = 'بایگانی شده';
    } else {
      status = 'READY_FOR_COURT';
      statusLabelFa = 'آماده برای قرارگاه بوشیدو';
    }
  }

  const elapsedDays =
    logicalToday < cycleStartDate ? 0 : logicalToday > cycleEndDate ? 90 : Math.min(90, Math.max(1, daysBetween(cycleStartDate, logicalToday) + 1));
  const remainingDays = Math.max(0, 90 - elapsedDays);

  const effectiveEnd = logicalToday > cycleEndDate ? cycleEndDate : logicalToday;
  const synthesizedList: { log: DailyLog; computed: DailyComputed }[] = [];

  if (status !== 'UPCOMING') {
    let curr = cycleStartDate;
    while (curr <= effectiveEnd) {
      let existingLog = cycleLogs.find((l) => l.date === curr);
      if (!existingLog) {
        existingLog = {
          id: `virtual-${curr}`,
          cycleId: cycle.id,
          date: curr,
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
      }
      synthesizedList.push({
        log: existingLog,
        computed: computeDailyProperties(existingLog, cycleLogs, logicalToday, cycleStartDate)
      });
      curr = addDaysToDate(curr, 1);
    }
  }

  const computedList = synthesizedList;
  const standardDaysCount = computedList.filter((c) => c.computed.isStandard).length;
  const unresolvedDebtCount = computedList.filter(
    (c) => c.log.date < logicalToday && c.computed.statusType === 'BURNED_UNRESOLVED'
  ).length;
  const resolvedDebtCount = computedList.filter((c) => c.computed.statusType === 'BURNED_RESOLVED').length;
  const burnedDaysCount = unresolvedDebtCount + resolvedDebtCount;
  const frozenDaysCount = computedList.filter((c) => c.computed.statusType === 'PERSONAL_FROZEN').length;
  const inactiveDaysCount = computedList.filter((c) => c.computed.statusType !== 'PERSONAL_FROZEN' && c.computed.habitsCount === 0 && !c.computed.isStandard).length;
  const incompleteDaysCount = computedList.filter((c) => c.computed.statusType !== 'PERSONAL_FROZEN' && c.computed.habitsCount > 0 && !c.computed.isStandard).length;
  const totalScore = computedList.reduce((acc, c) => acc + c.computed.score, 0);

  // Phantom Denominator Calculation
  const todayLog = computedList.find((c) => c.log.date === logicalToday);
  const isTodayCompleted = todayLog ? todayLog.computed.isStandard || !!todayLog.log.failureReason : false;

  const validLogs = computedList.filter(
    (c) => c.log.date <= logicalToday && (c.log.date < logicalToday || isTodayCompleted)
  );

  const nFrozen = validLogs.filter((c) => c.computed.statusType === 'PERSONAL_FROZEN').length;
  const rawElapsed = status === 'ACTIVE' ? elapsedDays - 1 + (isTodayCompleted ? 1 : 0) : elapsedDays;
  const evaluatedDays = Math.max(0, rawElapsed - nFrozen);

  const n1 = validLogs.filter((c) => c.computed.statusType === 'STANDARD').length;
  const n3 = validLogs.filter((c) => c.computed.statusType === 'BURNED_RESOLVED' && c.computed.habitsCount === 0).length;
  const n4 = validLogs.filter((c) => c.computed.statusType === 'BURNED_UNRESOLVED' && c.computed.habitsCount > 0).length;
  const n5 = validLogs.filter((c) => c.computed.statusType === 'BURNED_UNRESOLVED' && c.computed.habitsCount === 0).length;

  const activeValidCount = validLogs.filter((c) => c.computed.statusType !== 'PERSONAL_FROZEN').length;
  const missingDays = Math.max(0, evaluatedDays - activeValidCount);

  const phantomDenominator = evaluatedDays + (0.2 * n3) + (1.5 * n4) + (3.0 * (n5 + missingDays));

  let disciplineScore = 1.0;
  if (evaluatedDays > 0 && phantomDenominator > 0) {
    disciplineScore = Math.max(0, Math.min(1, n1 / phantomDenominator));
  } else if (evaluatedDays === 0) {
    disciplineScore = 1.0;
  }
  const disciplinePercentage = Math.round(disciplineScore * 100);

  let disciplineLevel: DisciplineLevel = 'IRON';
  if (status === 'UPCOMING') disciplineLevel = 'UPCOMING';
  else if (status === 'OVERLAP_ERROR') disciplineLevel = 'STRUCTURE_ERROR';
  else if (disciplineScore >= 0.8) disciplineLevel = 'IRON';
  else if (disciplineScore >= 0.6) disciplineLevel = 'STABLE';
  else if (disciplineScore >= 0.4) disciplineLevel = 'UNSTABLE';
  else disciplineLevel = 'CRISIS';

  // Pure Streak Engine
  let currentPureStreak = 0;
  let maxPureStreak = 0;
  let runningStreak = 0;

  const pastAndTodayLogs = computedList
    .filter((c) => c.log.date <= logicalToday)
    .sort((a, b) => a.log.date.localeCompare(b.log.date));

  for (let i = 0; i < pastAndTodayLogs.length; i++) {
    const item = pastAndTodayLogs[i];
    const isToday = item.log.date === logicalToday;

    if (i > 0) {
      const prev = pastAndTodayLogs[i - 1];
      const gap = daysBetween(prev.log.date, item.log.date) - 1;
      if (gap > 0) {
        runningStreak = 0;
      }
    }

    if (item.computed.isStandard) {
      runningStreak++;
      if (runningStreak > maxPureStreak) maxPureStreak = runningStreak;
    } else if (item.computed.statusType === 'PERSONAL_FROZEN') {
      // pass through
    } else if (isToday && !item.log.failureReason) {
      // in progress
    } else {
      runningStreak = 0;
    }
  }
  currentPureStreak = runningStreak;

  const inheritedStreak = cycle.inheritedStreak || 0;
  const globalLiveStreak = inheritedStreak + currentPureStreak;
  const maxGlobalStreak = Math.max(inheritedStreak + maxPureStreak, globalLiveStreak);

  // Failure distributions
  const reasonCounts: Record<string, number> = {};
  const timeCounts: Record<string, number> = {};

  cycleLogs.forEach((l) => {
    if (l.failureReason && l.failureReason !== 'دلایل شخصی') {
      reasonCounts[l.failureReason] = (reasonCounts[l.failureReason] || 0) + 1;
    }
    if (l.failureTime) {
      timeCounts[l.failureTime] = (timeCounts[l.failureTime] || 0) + 1;
    }
  });

  let dominantFailureReason = 'بدون شکست';
  let maxR = 0;
  Object.entries(reasonCounts).forEach(([r, c]) => {
    if (c > maxR) {
      maxR = c;
      dominantFailureReason = r;
    }
  });

  let dominantFailureTime = 'بدون شکست';
  let maxT = 0;
  Object.entries(timeCounts).forEach(([t, c]) => {
    if (c > maxT) {
      maxT = c;
      dominantFailureTime = t;
    }
  });

  // Vulnerable Habits
  const activeBase = Math.max(1, cycleLogs.length - frozenDaysCount);
  const vulnerableHabits: VulnerableHabit[] = [];

  FOUNDATION_HABITS.forEach((h) => {
    const successCount = cycleLogs.filter((l) => (l as any)[h.key]).length;
    const ratePct = Math.round((successCount / activeBase) * 100);
    if (cycleLogs.length > 0 && ratePct < 70) {
      vulnerableHabits.push({
        key: h.key,
        titleFa: h.titleFa,
        ratePct,
        successCount,
        totalEvaluated: activeBase,
        icon: h.iconName
      });
    }
  });

  const needsIntervention = status === 'ACTIVE' && (disciplineLevel === 'CRISIS' || unresolvedDebtCount >= 2);
  const isCourtReady = remainingDays === 0 && unresolvedDebtCount === 0 && cycleLogs.length >= 85;

  let coachMessage = 'انضباط آهنین │ این سطح از تعهد و دیسیپلین را تا پایان چرخه ۹۰ روزه حفظ کن.';
  if (status === 'OVERLAP_ERROR') coachMessage = 'تداخل تقویمی │ این چرخه با یکی از چرخه‌های دیگر تداخل زمانی دارد.';
  else if (status === 'UPCOMING') coachMessage = 'در انتظار آغاز │ این دوره هنوز فعال نشده است.';
  else if (isCourtReady) coachMessage = 'گزارش میدان نبرد آماده است │ برای دریافت حکم نهایی به دادگاه بوشیدو مراجعه کنید.';
  else if (disciplineLevel === 'CRISIS') coachMessage = 'بحران دیسیپلین │ امتیاز انضباط به پایین‌ترین سطح رسیده است. سریعاً به میدان نبرد برگردید!';
  else if (unresolvedDebtCount >= 2) coachMessage = `نیازمند مداخله فوری │ ${toPersianDigits(unresolvedDebtCount)} روز بدهی کالبدشکافی نشده وجود دارد. سیستم قفل شده است!`;
  else if (unresolvedDebtCount === 1) coachMessage = 'روز بررسی نشده │ ۱ روز بررسی نشده از قبل دارید. قبل از بحرانی شدن پرونده‌اش را ببندید.';

  return {
    cycle,
    status,
    statusLabelFa,
    elapsedDays,
    remainingDays,
    logsCount: cycleLogs.length,
    standardDaysCount,
    burnedDaysCount,
    unresolvedDebtCount,
    resolvedDebtCount,
    frozenDaysCount,
    inactiveDaysCount,
    incompleteDaysCount,
    totalScore,
    disciplineScore,
    disciplinePercentage,
    disciplineLevel,
    pureStreak: currentPureStreak,
    maxPureStreak,
    inheritedStreak,
    globalLiveStreak,
    maxGlobalStreak,
    maxFailureRun: 0,
    maxInactiveRun: 0,
    maxIncompleteRun: 0,
    dominantFailureReason,
    dominantFailureTime,
    vulnerableHabits,
    needsIntervention,
    isCourtReady,
    coachMessage
  };
}

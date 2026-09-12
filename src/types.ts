export type HabitKey = 'wakeUp' | 'workout' | 'study' | 'journal' | 'hardTask';

export interface HabitDef {
  key: HabitKey;
  title: string;
  titleFa: string;
  subtitleFa: string;
  iconName: string;
  colorHex: string;
}

export type DayStatusType =
  | 'STANDARD'
  | 'PERSONAL_FROZEN'
  | 'BURNED_UNRESOLVED'
  | 'BURNED_RESOLVED';

export type CycleStatusType =
  | 'ACTIVE'
  | 'UPCOMING'
  | 'READY_FOR_COURT'
  | 'ARCHIVED'
  | 'OVERLAP_ERROR';

export type DisciplineLevel =
  | 'IRON'
  | 'STABLE'
  | 'UNSTABLE'
  | 'CRISIS'
  | 'UPCOMING'
  | 'STRUCTURE_ERROR';

export interface Cycle {
  id: string;
  title: string;
  startDate: string; // YYYY-MM-DD
  endDate: string;   // YYYY-MM-DD
  targetTheme: string;
  rules: string[];
  inheritedStreak: number;
  isArchived: boolean;
  reportRead: boolean;
  revision: number;
}

export interface DailyLog {
  id: string;
  cycleId: string;
  date: string; // YYYY-MM-DD
  createdAt?: string;
  revision?: number;
  isVirtual?: boolean;

  // 5 Foundation Habits
  wakeUp: boolean;
  workout: boolean;
  study: boolean;
  journal: boolean;
  hardTask: boolean;

  // Special Mission
  specialMission: boolean;

  // Failure & Autopsy
  failureReason: string;
  failureTime: string;
  autopsyNotes: string;
  countermeasure: string;
  aiFeedback: string;

  // Reflection
  notes: string;
}

export interface UserProfile {
  id: string;
  name: string;
  email: string;
  phoneNumber: string;
  tier: string;
  isVip: boolean;
  isAdmin: boolean;
  nightOwlCutoffHour: number;
}

export interface VulnerableHabit {
  key: HabitKey;
  titleFa: string;
  ratePct: number;
  successCount: number;
  totalEvaluated: number;
  icon: string;
}

export interface DailyComputed {
  isStandard: boolean;
  habitsCount: number;
  score: number;
  statusType: DayStatusType;
  needsAutopsy: boolean;
  isLockedDueToPastDebt: boolean;
  coachStatusLabel: string;
  displayScore: string;
  displayProgressBlocks: string;
}

export interface CycleMetrics {
  cycle: Cycle;
  status: CycleStatusType;
  statusLabelFa: string;
  elapsedDays: number;
  remainingDays: number;
  logsCount: number;
  standardDaysCount: number;
  burnedDaysCount: number;
  unresolvedDebtCount: number;
  resolvedDebtCount: number;
  frozenDaysCount: number;
  inactiveDaysCount: number;
  incompleteDaysCount: number;
  totalScore: number;
  disciplineScore: number;
  disciplinePercentage: number;
  disciplineLevel: DisciplineLevel;

  pureStreak: number;
  maxPureStreak: number;
  inheritedStreak: number;
  globalLiveStreak: number;
  maxGlobalStreak: number;

  maxFailureRun: number;
  maxInactiveRun: number;
  maxIncompleteRun: number;

  dominantFailureReason: string;
  dominantFailureTime: string;
  vulnerableHabits: VulnerableHabit[];
  needsIntervention: boolean;
  isCourtReady: boolean;
  coachMessage: string;
}

export interface DeterministicAutopsyResult {
  analysis: string;
  psychologicalTrap: string;
  countermeasure: string;
  tacticalActionTomorrow: string;
}

export interface DeterministicSenseiResult {
  coachVerdict: string;
  keyAdvice: string;
  strategicWarning: string;
  bushidoQuote: string;
}

export interface DeterministicCourtResult {
  verdict: string;
  grade: string;
  senseiNotes: string;
  strengths: string[];
  weaknesses: string[];
  tacticalPlanForNextCycle: string;
}

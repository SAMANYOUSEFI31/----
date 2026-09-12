import React, { useState, useEffect } from 'react';
import {
  Shield,
  BarChart2,
  Brain,
  Gavel,
  Settings as SettingsIcon,
  Flame,
  Award,
  CheckCircle2
} from 'lucide-react';
import {
  Cycle,
  DailyLog,
  UserProfile,
  HabitKey,
  DailyComputed,
  CycleMetrics
} from './types';
import {
  getLogicalTodayDate,
  addDaysToDate,
  daysBetween
} from './engine/dateUtils';
import {
  computeDailyProperties,
  computeCycleMetrics
} from './engine/bushidoCalculations';
import { TopHubBar } from './components/TopHubBar';
import { BattlefieldView } from './components/BattlefieldView';
import { CycleDashboardView } from './components/CycleDashboardView';
import { SenseiView } from './components/SenseiView';
import { BushidoCourtView } from './components/BushidoCourtView';
import { SettingsView } from './components/SettingsView';
import { AutopsyModal } from './components/AutopsyModal';
import { CreateCycleModal } from './components/CreateCycleModal';
import { VipSubscriptionModal } from './components/VipSubscriptionModal';
import { DisciplineRulesModal } from './components/DisciplineRulesModal';
import { ResetConfirmationModal } from './components/ResetConfirmationModal';

const INITIAL_PROFILE: UserProfile = {
  id: 'usr-1',
  name: 'سامورایی دیسیپلین',
  email: 'samurai@bushido.os',
  phoneNumber: '۰۹۱۲۳۴۵۶۷۸۹',
  tier: 'VIP',
  isVip: true,
  isAdmin: false,
  nightOwlCutoffHour: 4
};

function generateInitialData(): { cycles: Cycle[]; logs: DailyLog[] } {
  const today = getLogicalTodayDate(4);
  const cycleStart = addDaysToDate(today, -20);
  const cycleEnd = addDaysToDate(cycleStart, 89);

  const initialCycle: Cycle = {
    id: 'cycle-1',
    title: 'چرخه ۱ — بیداری سامورایی و انضباط بنیادین',
    startDate: cycleStart,
    endDate: cycleEnd,
    targetTheme: 'سحرخیزی بی‌چون‌وچرا سر ساعت ۵، تمرکز عمیق روزانه و تسلط بر ورزش',
    rules: [
      'سحرخیزی راس ساعت ۵:۰۰ بدون دکمه اسنوز',
      'حداقل ۴۵ دقیقه ورزش روزانه بدون بهانه',
      'ثبت و تیک روزانه قبل از خواب',
      'کالبدشکافی فوری در صورت هرگونه افت'
    ],
    inheritedStreak: 12,
    isArchived: false,
    reportRead: false,
    revision: 1
  };

  const logs: DailyLog[] = [];
  for (let i = -20; i <= 0; i++) {
    const d = addDaysToDate(today, i);
    const isToday = i === 0;
    const isFrozen = i === -12;
    const isBurned = i === -8 || i === -4;
    const isMastery = i === -2 || i === -6 || i === -14;

    logs.push({
      id: `log-${d}`,
      cycleId: initialCycle.id,
      date: d,
      wakeUp: isFrozen ? false : isBurned ? false : true,
      workout: isFrozen ? false : true,
      study: isFrozen ? false : true,
      journal: isFrozen ? false : true,
      hardTask: isFrozen ? false : isBurned ? false : true,
      specialMission: isMastery && !isFrozen && !isBurned,
      failureReason: isFrozen ? 'دلایل شخصی' : isBurned ? 'کم‌خوابی و خستگی مفرط' : '',
      failureTime: isBurned ? 'صبح زود (۵ الی ۹)' : '',
      autopsyNotes: isBurned ? 'خستگی ناشی از بیداری دیرهنگام در شب قبل.' : '',
      countermeasure: isBurned ? 'خواب ساعت ۲۲:۳۰ و پرهیز از کار شبانه.' : '',
      aiFeedback: '',
      notes: isToday ? 'انرژی صبحگاهی عالی بود و کار سخت روز به موقع انجام شد.' : ''
    });
  }

  return { cycles: [initialCycle], logs };
}

function safeLoadStorage<T>(key: string, fallback: T): T {
  try {
    const saved = localStorage.getItem(key);
    if (!saved) return fallback;
    const parsed = JSON.parse(saved);
    if (Array.isArray(fallback)) {
      if (!Array.isArray(parsed) || parsed.length === 0) {
        return fallback;
      }
    }
    return parsed;
  } catch (err) {
    console.error(`Failed to load ${key} from localStorage`, err);
    return fallback;
  }
}

type TabType = 'battlefield' | 'dashboard' | 'sensei' | 'court' | 'settings';

export const App: React.FC = () => {
  const initialData = generateInitialData();

  const [profile, setProfile] = useState<UserProfile>(() => {
    return safeLoadStorage('bushido_profile', INITIAL_PROFILE);
  });

  const [cycles, setCycles] = useState<Cycle[]>(() => {
    return safeLoadStorage('bushido_cycles', initialData.cycles);
  });

  const [logs, setLogs] = useState<DailyLog[]>(() => {
    return safeLoadStorage('bushido_logs', initialData.logs);
  });

  const [activeCycleId, setActiveCycleId] = useState<string>(() => {
    return (cycles && cycles[0]?.id) || initialData.cycles[0].id;
  });

  const logicalToday = getLogicalTodayDate(profile.nightOwlCutoffHour);
  const [selectedDate, setSelectedDate] = useState<string>(logicalToday);
  const [currentTab, setCurrentTab] = useState<TabType>('battlefield');

  // Modals
  const [isAutopsyOpen, setIsAutopsyOpen] = useState(false);
  const [isCreateCycleOpen, setIsCreateCycleOpen] = useState(false);
  const [isVipOpen, setIsVipOpen] = useState(false);
  const [isRulesOpen, setIsRulesOpen] = useState(false);
  const [isResetOpen, setIsResetOpen] = useState(false);

  // Persistence
  useEffect(() => {
    try {
      localStorage.setItem('bushido_profile', JSON.stringify(profile));
    } catch (e) {
      console.error(e);
    }
  }, [profile]);

  useEffect(() => {
    try {
      localStorage.setItem('bushido_cycles', JSON.stringify(cycles));
    } catch (e) {
      console.error(e);
    }
  }, [cycles]);

  useEffect(() => {
    try {
      localStorage.setItem('bushido_logs', JSON.stringify(logs));
    } catch (e) {
      console.error(e);
    }
  }, [logs]);

  const fallbackCycle = initialData.cycles[0];
  const activeCycle = (cycles && cycles.find((c) => c.id === activeCycleId)) || cycles?.[0] || fallbackCycle;
  const activeCycleMetrics = computeCycleMetrics(activeCycle, logs, cycles || [fallbackCycle], logicalToday);

  // Active Daily Log
  const existingLog = (logs || []).find((l) => l.date === selectedDate && l.cycleId === activeCycle.id);
  const currentLog: DailyLog = existingLog || {
    id: `log-${selectedDate}`,
    cycleId: activeCycle.id,
    date: selectedDate,
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

  const selectedComputed = computeDailyProperties(
    currentLog,
    logs || [],
    logicalToday,
    activeCycle.startDate
  );

  // Handlers
  const handleToggleHabit = (key: HabitKey) => {
    const updated: DailyLog = {
      ...currentLog,
      [key]: !currentLog[key]
    };
    saveLog(updated);
  };

  const handleToggleSpecialMission = () => {
    const updated: DailyLog = {
      ...currentLog,
      specialMission: !currentLog.specialMission
    };
    saveLog(updated);
  };

  const handleUpdateNotes = (notes: string) => {
    const updated: DailyLog = {
      ...currentLog,
      notes
    };
    saveLog(updated);
  };

  const handleSubmitAutopsy = (
    reason: string,
    time: string,
    notes: string,
    countermeasure: string
  ) => {
    const updated: DailyLog = {
      ...currentLog,
      failureReason: reason,
      failureTime: time,
      autopsyNotes: notes,
      countermeasure
    };
    saveLog(updated);
    setIsAutopsyOpen(false);
  };

  const handleApplyFreeze = (notes: string) => {
    const updated: DailyLog = {
      ...currentLog,
      wakeUp: false,
      workout: false,
      study: false,
      journal: false,
      hardTask: false,
      specialMission: false,
      failureReason: 'دلایل شخصی',
      failureTime: 'تمام روز',
      autopsyNotes: notes
    };
    saveLog(updated);
    setIsAutopsyOpen(false);
  };

  const saveLog = (logToSave: DailyLog) => {
    setLogs((prev) => {
      const idx = prev.findIndex((l) => l.date === logToSave.date && l.cycleId === logToSave.cycleId);
      if (idx >= 0) {
        const copy = [...prev];
        copy[idx] = logToSave;
        return copy;
      }
      return [...prev, logToSave];
    });
  };

  const handleCreateCycle = (
    title: string,
    startDate: string,
    targetTheme: string,
    inheritedStreak: number,
    rules: string[]
  ) => {
    const endDate = addDaysToDate(startDate, 89);
    const newCycle: Cycle = {
      id: `cycle-${Date.now()}`,
      title,
      startDate,
      endDate,
      targetTheme,
      inheritedStreak,
      rules,
      isArchived: false,
      reportRead: false,
      revision: 1
    };
    setCycles((prev) => [...prev, newCycle]);
    setActiveCycleId(newCycle.id);
    setSelectedDate(startDate);
    setIsCreateCycleOpen(false);
  };

  const handleResetDatabase = () => {
    const { cycles: newCycles, logs: newLogs } = generateInitialData();
    setCycles(newCycles);
    setLogs(newLogs);
    setActiveCycleId(newCycles[0].id);
    setSelectedDate(logicalToday);
    setIsResetOpen(false);
  };

  return (
    <div className="min-h-screen bg-[#09090b] text-[#f4f4f5] flex flex-col selection:bg-rose-500/20 selection:text-rose-300">
      {/* Top Hub Bar */}
      <TopHubBar
        activeCycle={activeCycle}
        allCycles={cycles}
        pureStreak={activeCycleMetrics.pureStreak}
        unresolvedDebtCount={activeCycleMetrics.unresolvedDebtCount}
        onSelectCycle={(id) => setActiveCycleId(id)}
        onOpenVip={() => setIsVipOpen(true)}
        onOpenCreateCycle={() => setIsCreateCycleOpen(true)}
      />

      {/* Main Viewport Container */}
      <main className="flex-1 p-3 sm:p-4 max-w-3xl mx-auto w-full">
        {currentTab === 'battlefield' && (
          <BattlefieldView
            selectedDate={selectedDate}
            logicalToday={logicalToday}
            log={currentLog}
            computed={selectedComputed}
            onPrevDay={() => setSelectedDate((d) => addDaysToDate(d, -1))}
            onNextDay={() => setSelectedDate((d) => addDaysToDate(d, 1))}
            onJumpToToday={() => setSelectedDate(logicalToday)}
            onToggleHabit={handleToggleHabit}
            onToggleSpecialMission={handleToggleSpecialMission}
            onUpdateNotes={handleUpdateNotes}
            onOpenAutopsy={() => setIsAutopsyOpen(true)}
          />
        )}

        {currentTab === 'dashboard' && (
          <CycleDashboardView
            metrics={activeCycleMetrics}
            logs={logs}
            selectedDate={selectedDate}
            onSelectDate={(d) => {
              setSelectedDate(d);
              setCurrentTab('battlefield');
            }}
          />
        )}

        {currentTab === 'sensei' && (
          <SenseiView metrics={activeCycleMetrics} />
        )}

        {currentTab === 'court' && (
          <BushidoCourtView
            metrics={activeCycleMetrics}
            allCycles={cycles}
            onSelectCycle={(id) => setActiveCycleId(id)}
          />
        )}

        {currentTab === 'settings' && (
          <SettingsView
            profile={profile}
            onOpenRules={() => setIsRulesOpen(true)}
            onOpenReset={() => setIsResetOpen(true)}
            onUpdateCutoffHour={(hour) =>
              setProfile((p) => ({ ...p, nightOwlCutoffHour: hour }))
            }
          />
        )}
      </main>

      {/* Bottom Navigation Dock */}
      <nav className="fixed bottom-0 inset-x-0 z-40 bg-[#0c0c0e]/95 backdrop-blur-md border-t border-[#27272a] px-2 py-1.5 flex items-center justify-around max-w-lg mx-auto sm:rounded-t-2xl">
        <button
          onClick={() => setCurrentTab('battlefield')}
          className={`flex flex-col items-center gap-1 py-1 px-3 rounded-xl transition-all cursor-pointer ${
            currentTab === 'battlefield'
              ? 'text-rose-500 font-bold'
              : 'text-zinc-400 hover:text-zinc-200'
          }`}
        >
          <Shield className="w-5 h-5" />
          <span className="text-[10px]">میدان نبرد</span>
        </button>

        <button
          onClick={() => setCurrentTab('dashboard')}
          className={`flex flex-col items-center gap-1 py-1 px-3 rounded-xl transition-all cursor-pointer ${
            currentTab === 'dashboard'
              ? 'text-rose-500 font-bold'
              : 'text-zinc-400 hover:text-zinc-200'
          }`}
        >
          <BarChart2 className="w-5 h-5" />
          <span className="text-[10px]">چرخه</span>
        </button>

        <button
          onClick={() => setCurrentTab('sensei')}
          className={`flex flex-col items-center gap-1 py-1 px-3 rounded-xl transition-all cursor-pointer ${
            currentTab === 'sensei'
              ? 'text-rose-500 font-bold'
              : 'text-zinc-400 hover:text-zinc-200'
          }`}
        >
          <Brain className="w-5 h-5" />
          <span className="text-[10px]">سنسی</span>
        </button>

        <button
          onClick={() => setCurrentTab('court')}
          className={`flex flex-col items-center gap-1 py-1 px-3 rounded-xl transition-all cursor-pointer ${
            currentTab === 'court'
              ? 'text-rose-500 font-bold'
              : 'text-zinc-400 hover:text-zinc-200'
          }`}
        >
          <Gavel className="w-5 h-5" />
          <span className="text-[10px]">دیوان</span>
        </button>

        <button
          onClick={() => setCurrentTab('settings')}
          className={`flex flex-col items-center gap-1 py-1 px-3 rounded-xl transition-all cursor-pointer ${
            currentTab === 'settings'
              ? 'text-rose-500 font-bold'
              : 'text-zinc-400 hover:text-zinc-200'
          }`}
        >
          <SettingsIcon className="w-5 h-5" />
          <span className="text-[10px]">تنظیمات</span>
        </button>
      </nav>

      {/* Modal Dialogs */}
      {isAutopsyOpen && (
        <AutopsyModal
          log={currentLog}
          onDismiss={() => setIsAutopsyOpen(false)}
          onSubmitAutopsy={handleSubmitAutopsy}
          onApplyFreeze={handleApplyFreeze}
        />
      )}

      {isCreateCycleOpen && (
        <CreateCycleModal
          onDismiss={() => setIsCreateCycleOpen(false)}
          onCreateCycle={handleCreateCycle}
        />
      )}

      {isVipOpen && (
        <VipSubscriptionModal
          onDismiss={() => setIsVipOpen(false)}
        />
      )}

      {isRulesOpen && (
        <DisciplineRulesModal
          onDismiss={() => setIsRulesOpen(false)}
        />
      )}

      {isResetOpen && (
        <ResetConfirmationModal
          onDismiss={() => setIsResetOpen(false)}
          onConfirmReset={handleResetDatabase}
        />
      )}
    </div>
  );
};

import React, { useState } from 'react';
import { MessageSquare, Sparkles, HelpCircle, Send, Quote, AlertCircle, CheckCircle } from 'lucide-react';
import { CycleMetrics, DeterministicSenseiResult } from '../types';
import { getDeterministicSenseiAdvice } from '../engine/deterministicSensei';

interface SenseiViewProps {
  metrics: CycleMetrics;
}

export const SenseiView: React.FC<SenseiViewProps> = ({ metrics }) => {
  const [query, setQuery] = useState('');
  const [customAdvice, setCustomAdvice] = useState<DeterministicSenseiResult | null>(null);

  const defaultAdvice = getDeterministicSenseiAdvice(
    metrics.cycle.title,
    metrics.elapsedDays,
    metrics.remainingDays,
    metrics.disciplinePercentage,
    metrics.disciplineLevel,
    metrics.pureStreak,
    metrics.vulnerableHabits,
    metrics.dominantFailureReason,
    metrics.dominantFailureTime
  );

  const activeAdvice = customAdvice || defaultAdvice;

  const quickQuestions = [
    { q: 'چگونه در سحرخیزی ثبات داشته باشم؟', keyword: 'سحرخیزی' },
    { q: 'در مواجهه با خستگی بدنی چگونه ورزش کنم؟', keyword: 'ورزش' },
    { q: 'برای غلبه بر تنبلی در کار سخت روز چه کنم؟', keyword: 'کار سخت' }
  ];

  const handleAsk = (text: string) => {
    setQuery(text);
    const newAdvice = getDeterministicSenseiAdvice(
      metrics.cycle.title,
      metrics.elapsedDays,
      metrics.remainingDays,
      metrics.disciplinePercentage,
      metrics.disciplineLevel,
      metrics.pureStreak,
      metrics.vulnerableHabits,
      metrics.dominantFailureReason,
      metrics.dominantFailureTime
    );
    setCustomAdvice(newAdvice);
  };

  return (
    <div className="w-full max-w-2xl mx-auto flex flex-col gap-4 pb-20">
      {/* Sensei Banner */}
      <div className="w-full bg-[#121215] border border-amber-500/30 rounded-2xl p-4 flex items-center justify-between shadow-lg">
        <div className="w-12 h-12 rounded-2xl bg-amber-500/15 border border-amber-500/40 flex items-center justify-center text-amber-400 font-black text-xl">
          道
        </div>
        <div className="text-right flex-1 pr-3">
          <div className="text-base font-bold text-amber-400">
            سنسی هوشمند بوشیدو
          </div>
          <div className="text-xs text-zinc-400">
            راهنمای راهبردی دیسیپلین و غلبه بر تله‌های شناختی
          </div>
        </div>
      </div>

      {/* Real-time Tactical Diagnosis Card */}
      <div className="w-full bg-[#121215] border border-[#27272a] rounded-2xl p-4 flex flex-col gap-3 shadow-md text-right">
        <div className="flex items-center justify-between">
          <span className="px-2 py-0.5 rounded-md bg-amber-500/15 border border-amber-500/30 text-amber-300 text-xs font-bold">
            تحلیل وضعیت نبرد
          </span>
          <div className="text-sm font-bold text-white">حکم سنسی بر عملکرد جاری</div>
        </div>

        <div className="text-xs text-zinc-200 leading-relaxed">
          {activeAdvice.coachVerdict}
        </div>

        {/* Operational Advice */}
        <div className="bg-[#18181b] border border-emerald-500/30 rounded-xl p-3 flex flex-col gap-1">
          <div className="flex items-center justify-end gap-1.5 text-xs font-bold text-emerald-400">
            <span>دستور عملیاتی سنسی:</span>
            <CheckCircle className="w-3.5 h-3.5" />
          </div>
          <div className="text-xs text-zinc-300 leading-relaxed">
            {activeAdvice.keyAdvice}
          </div>
        </div>

        {/* Strategic Warning */}
        <div className="bg-[#18181b] border border-rose-500/30 rounded-xl p-3 flex flex-col gap-1">
          <div className="flex items-center justify-end gap-1.5 text-xs font-bold text-rose-400">
            <span>هشدار راهبردی:</span>
            <AlertCircle className="w-3.5 h-3.5" />
          </div>
          <div className="text-xs text-zinc-300 leading-relaxed">
            {activeAdvice.strategicWarning}
          </div>
        </div>
      </div>

      {/* Quick Consultation Topics */}
      <div className="flex flex-col gap-2 text-right">
        <div className="text-xs font-bold text-zinc-300 px-1">
          پرسش‌های راهبردی متداول از سنسی:
        </div>
        <div className="flex flex-col gap-1.5">
          {quickQuestions.map((item) => (
            <button
              key={item.q}
              onClick={() => handleAsk(item.q)}
              className="w-full bg-[#121215] border border-[#27272a] hover:border-amber-500/40 p-3 rounded-xl flex items-center justify-between text-xs text-zinc-300 hover:text-white transition-colors cursor-pointer text-right"
            >
              <HelpCircle className="w-4 h-4 text-amber-400 shrink-0" />
              <span>{item.q}</span>
            </button>
          ))}
        </div>
      </div>

      {/* Custom Question Input */}
      <div className="w-full bg-[#121215] border border-[#27272a] rounded-2xl p-4 flex flex-col gap-3 shadow-md text-right">
        <div className="text-xs font-bold text-zinc-300">
          مشاوره اختصاصی با سنسی:
        </div>
        <textarea
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="چالش یا سوال خود را مطرح کنید (مثلاً: چطور بعدازظهرها تمرکز کنم؟)..."
          rows={2}
          className="w-full bg-[#18181b] border border-[#27272a] rounded-xl p-3 text-xs text-white placeholder:text-zinc-600 focus:outline-none focus:border-amber-500 resize-none text-right"
        />
        <button
          onClick={() => query && handleAsk(query)}
          className="w-full py-2.5 rounded-xl bg-amber-400 hover:bg-amber-300 text-black text-xs font-bold transition-transform active:scale-95 shadow-md flex items-center justify-center gap-1.5 cursor-pointer"
        >
          <Send className="w-3.5 h-3.5" />
          <span>دریافت پاسخ راهبردی از سنسی</span>
        </button>
      </div>

      {/* Bushido Golden Quote Card */}
      <div className="w-full bg-[#121215] border border-[#27272a] rounded-2xl p-5 flex flex-col items-center text-center gap-2 shadow-md">
        <Quote className="w-6 h-6 text-amber-400/60" />
        <div className="text-xs font-bold text-zinc-200 leading-relaxed">
          {activeAdvice.bushidoQuote}
        </div>
        <div className="text-[10px] text-zinc-500">
          — حکمت جاودان مرام‌نامه بوشیدو
        </div>
      </div>
    </div>
  );
};

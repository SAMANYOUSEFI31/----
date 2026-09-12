import React, { useState } from 'react';
import { X, ShieldAlert, Snowflake, Check, ShieldCheck } from 'lucide-react';
import { DailyLog } from '../types';
import { getDeterministicAutopsy } from '../engine/deterministicSensei';

interface AutopsyModalProps {
  log: DailyLog;
  onDismiss: () => void;
  onSubmitAutopsy: (
    reason: string,
    time: string,
    notes: string,
    countermeasure: string
  ) => void;
  onApplyFreeze: (notes: string) => void;
}

const FAILURE_REASONS = [
  'کم‌خوابی و خستگی مفرط',
  'افتادن در تله گوشی و فضای مجازی',
  'اهمال‌کاری و تاخیر در شروع',
  'جلسات و کارهای غیرمترقبه بیرونی',
  'افت انگیزه و فرسودگی روانی',
  'سایر موانع اجرایی'
];

const FAILURE_TIMES = [
  'صبح زود (۵ الی ۹)',
  'قبل از ظهر (۹ الی ۱۳)',
  'بعدازظهر (۱۳ الی ۱۷)',
  'غروب (۱۷ الی ۲۱)',
  'آخر شب (۲۱ الی ۱ بامداد)'
];

export const AutopsyModal: React.FC<AutopsyModalProps> = ({
  log,
  onDismiss,
  onSubmitAutopsy,
  onApplyFreeze
}) => {
  const [selectedReason, setSelectedReason] = useState(log.failureReason || FAILURE_REASONS[0]);
  const [selectedTime, setSelectedTime] = useState(log.failureTime || FAILURE_TIMES[0]);
  const [autopsyNotes, setAutopsyNotes] = useState(log.autopsyNotes || '');
  const [countermeasure, setCountermeasure] = useState(log.countermeasure || '');
  const [isFreezeMode, setIsFreezeMode] = useState(log.failureReason === 'دلایل شخصی');

  const diagnosis = getDeterministicAutopsy(selectedReason, selectedTime, autopsyNotes, countermeasure);

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-3 bg-black/80 backdrop-blur-sm">
      <div className="w-full max-w-lg max-h-[92vh] flex flex-col bg-[#121215] border border-[#27272a] rounded-2xl shadow-2xl overflow-hidden">
        {/* Header */}
        <div className="p-4 border-b border-[#27272a] flex items-center justify-between">
          <button
            onClick={onDismiss}
            className="p-1.5 rounded-lg bg-[#18181b] border border-[#27272a] hover:border-[#3f3f46] text-[#a1a1aa] hover:text-white transition-colors"
          >
            <X className="w-4 h-4" />
          </button>

          <div className="flex items-center gap-2 text-right">
            <div>
              <div className="text-sm font-bold text-white">
                کالبدشکافی روز و تسویه بدهی انضباطی
              </div>
              <div className="text-xs text-[#a1a1aa]">
                تاریخ: {log.date}
              </div>
            </div>
            <div className="w-9 h-9 rounded-xl bg-red-500/10 border border-red-500/30 flex items-center justify-center text-red-400">
              <ShieldAlert className="w-5 h-5" />
            </div>
          </div>
        </div>

        {/* Scrollable Body */}
        <div className="flex-1 overflow-y-auto p-4 flex flex-col gap-4 text-right">
          {/* Toggle Mode: Failure Autopsy vs Personal Freeze */}
          <div className="grid grid-cols-2 gap-2 bg-[#18181b] p-1 rounded-xl border border-[#27272a]">
            <button
              onClick={() => setIsFreezeMode(false)}
              className={`py-2 text-xs font-bold rounded-lg transition-all ${
                !isFreezeMode
                  ? 'bg-red-500/15 border border-red-500/40 text-red-400 shadow-sm'
                  : 'text-zinc-400 hover:text-white'
              }`}
            >
              ثبت کالبدشکافی افت
            </button>
            <button
              onClick={() => setIsFreezeMode(true)}
              className={`py-2 text-xs font-bold rounded-lg transition-all flex items-center justify-center gap-1.5 ${
                isFreezeMode
                  ? 'bg-blue-500/15 border border-blue-500/40 text-blue-400 shadow-sm'
                  : 'text-zinc-400 hover:text-white'
              }`}
            >
              <Snowflake className="w-3.5 h-3.5" />
              <span>فریز شخصی (شرایط اضطراری)</span>
            </button>
          </div>

          {isFreezeMode ? (
            <div className="flex flex-col gap-3 bg-[#18181b] p-3.5 rounded-xl border border-blue-500/30">
              <div className="text-xs font-bold text-blue-300">
                قانون فریز شخصی بوشیدو:
              </div>
              <div className="text-xs text-zinc-300 leading-relaxed">
                استفاده از فریز شخصی تنها در موارد بیماری، سفر ضروری یا بحران‌های غیرمترقبه مجاز است. این روز از محاسبه دیسیپلین مستثنی شده و استریک شما را نمی‌شکند.
              </div>
              <textarea
                value={autopsyNotes}
                onChange={(e) => setAutopsyNotes(e.target.value)}
                placeholder="توضیح شرایط اضطراری (اختیاری)..."
                rows={3}
                className="w-full bg-[#121215] border border-[#27272a] rounded-lg p-2.5 text-xs text-white placeholder:text-zinc-600 focus:outline-none focus:border-blue-500 resize-none"
              />
            </div>
          ) : (
            <>
              {/* Failure Reasons */}
              <div className="flex flex-col gap-2">
                <label className="text-xs font-bold text-zinc-300">
                  علت اصلی عدم تکمیل ۵ پایه:
                </label>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-1.5">
                  {FAILURE_REASONS.map((r) => (
                    <button
                      key={r}
                      onClick={() => setSelectedReason(r)}
                      className={`p-2 rounded-lg text-xs text-right border transition-all ${
                        selectedReason === r
                          ? 'bg-red-500/15 border-red-500/40 text-red-300 font-bold'
                          : 'bg-[#18181b] border-[#27272a] text-zinc-400 hover:border-[#3f3f46]'
                      }`}
                    >
                      {r}
                    </button>
                  ))}
                </div>
              </div>

              {/* Failure Time */}
              <div className="flex flex-col gap-2">
                <label className="text-xs font-bold text-zinc-300">
                  بازه زمانی وقوع ریزش:
                </label>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-1.5">
                  {FAILURE_TIMES.map((t) => (
                    <button
                      key={t}
                      onClick={() => setSelectedTime(t)}
                      className={`p-2 rounded-lg text-xs text-right border transition-all ${
                        selectedTime === t
                          ? 'bg-red-500/15 border-red-500/40 text-red-300 font-bold'
                          : 'bg-[#18181b] border-[#27272a] text-zinc-400 hover:border-[#3f3f46]'
                      }`}
                    >
                      {t}
                    </button>
                  ))}
                </div>
              </div>

              {/* Countermeasure */}
              <div className="flex flex-col gap-1.5">
                <label className="text-xs font-bold text-zinc-300">
                  قانون پیشگیرانه برای فردا:
                </label>
                <input
                  type="text"
                  value={countermeasure}
                  onChange={(e) => setCountermeasure(e.target.value)}
                  placeholder="مثال: گوشی را قبل از شروع کار در کشوی میز قرار می‌دهم"
                  className="w-full bg-[#18181b] border border-[#27272a] rounded-lg p-2.5 text-xs text-white placeholder:text-zinc-600 focus:outline-none focus:border-red-500"
                />
              </div>

              {/* Deterministic Diagnosis Card */}
              <div className="bg-[#18181b] border border-purple-500/30 rounded-xl p-3 flex flex-col gap-2">
                <div className="flex items-center justify-end gap-1.5 text-xs font-bold text-purple-300">
                  <span>تحلیل ساختاری سنسی:</span>
                  <ShieldCheck className="w-4 h-4" />
                </div>
                <div className="text-xs text-zinc-300 leading-relaxed">
                  {diagnosis.analysis}
                </div>
                <div className="text-[11px] text-zinc-400">
                  <strong className="text-purple-300">دام شناختی:</strong> {diagnosis.psychologicalTrap}
                </div>
              </div>
            </>
          )}
        </div>

        {/* Footer Actions */}
        <div className="p-4 border-t border-[#27272a] flex items-center gap-2">
          <button
            onClick={onDismiss}
            className="flex-1 py-2.5 rounded-xl border border-[#27272a] text-xs font-bold text-zinc-300 hover:bg-[#18181b] transition-colors"
          >
            انصراف
          </button>

          <button
            onClick={() => {
              if (isFreezeMode) {
                onApplyFreeze(autopsyNotes);
              } else {
                onSubmitAutopsy(selectedReason, selectedTime, autopsyNotes, countermeasure);
              }
            }}
            className={`flex-1 py-2.5 rounded-xl text-xs font-bold text-white transition-transform active:scale-95 shadow-md ${
              isFreezeMode
                ? 'bg-blue-600 hover:bg-blue-500'
                : 'bg-red-600 hover:bg-red-500'
            }`}
          >
            {isFreezeMode ? 'ثبت فریز و حفظ استریک' : 'تایید کالبدشکافی و بستن پرونده'}
          </button>
        </div>
      </div>
    </div>
  );
};

import React from 'react';
import { User, Shield, Moon, Trash2, ChevronLeft, Gavel, Sparkles } from 'lucide-react';
import { UserProfile } from '../types';

interface SettingsViewProps {
  profile: UserProfile;
  onOpenRules: () => void;
  onOpenReset: () => void;
  onUpdateCutoffHour: (hour: number) => void;
}

export const SettingsView: React.FC<SettingsViewProps> = ({
  profile,
  onOpenRules,
  onOpenReset,
  onUpdateCutoffHour
}) => {
  const cutoffOptions = [
    { hour: 0, label: '۱۲ نیمه‌شب (استاندارد تقویمی)' },
    { hour: 2, label: '۲ بامداد (شب‌زنده‌دار متوسط)' },
    { hour: 4, label: '۴ بامداد (پیش‌فرض بوشیدو)' },
    { hour: 5, label: '۵ بامداد (جنگجوی شب)' }
  ];

  return (
    <div className="w-full max-w-2xl mx-auto flex flex-col gap-4 pb-20">
      {/* User Identity Card */}
      <div className="w-full bg-[#121215] border border-[#27272a] rounded-2xl p-4 flex items-center justify-between shadow-lg">
        <span className="px-2.5 py-1 rounded-lg bg-amber-500/15 border border-amber-500/40 text-amber-300 text-xs font-bold">
          عضو VIP بوشیدو
        </span>

        <div className="flex items-center gap-3 text-right">
          <div>
            <div className="text-sm font-bold text-white">{profile.name}</div>
            <div className="text-xs text-zinc-400">{profile.phoneNumber}</div>
          </div>
          <div className="w-11 h-11 rounded-full bg-rose-600 border border-rose-400/40 flex items-center justify-center font-black text-white text-lg">
            武
          </div>
        </div>
      </div>

      {/* Bushido Doctrine Button */}
      <div
        onClick={onOpenRules}
        className="w-full bg-[#121215] border border-[#27272a] hover:border-[#3f3f46] rounded-2xl p-4 flex items-center justify-between cursor-pointer transition-colors shadow-md"
      >
        <ChevronLeft className="w-4 h-4 text-zinc-400" />
        <div className="flex items-center gap-2 text-right">
          <span className="text-xs font-bold text-white">
            مرام‌نامه و ۴ اصل بنیادین دیسیپلین
          </span>
          <Gavel className="w-4 h-4 text-zinc-400" />
        </div>
      </div>

      {/* Night Owl Cutoff Selector */}
      <div className="w-full bg-[#121215] border border-[#27272a] rounded-2xl p-4 flex flex-col gap-3 shadow-md text-right">
        <div className="flex items-center justify-end gap-1.5 text-sm font-bold text-white">
          <span>ساعت کات‌آف شبانه (پایان روز منطقی)</span>
          <Moon className="w-4 h-4 text-zinc-400" />
        </div>
        <div className="text-xs text-zinc-400 leading-relaxed">
          تا قبل از فرا رسیدن ساعت کات‌آف، ثبت تیک‌ها به حساب روز قبل منظور می‌گردد.
        </div>

        <div className="flex flex-col gap-2">
          {cutoffOptions.map((opt) => {
            const isSelected = profile.nightOwlCutoffHour === opt.hour;
            return (
              <div
                key={opt.hour}
                onClick={() => onUpdateCutoffHour(opt.hour)}
                className={`p-3 rounded-xl border flex items-center justify-between cursor-pointer transition-all ${
                  isSelected
                    ? 'bg-rose-500/10 border-rose-500/40 text-white font-bold'
                    : 'bg-[#18181b] border-[#27272a] hover:border-[#3f3f46] text-zinc-400'
                }`}
              >
                <div
                  className={`w-4 h-4 rounded-full border flex items-center justify-center ${
                    isSelected ? 'border-rose-500 bg-rose-500' : 'border-zinc-600 bg-transparent'
                  }`}
                />
                <span className="text-xs">{opt.label}</span>
              </div>
            );
          })}
        </div>
      </div>

      {/* Danger Zone: Reset Database */}
      <div className="w-full bg-[#121215] border border-rose-500/30 rounded-2xl p-4 flex flex-col gap-2.5 shadow-md text-right">
        <div className="flex items-center justify-end gap-1.5 text-sm font-bold text-rose-400">
          <span>منطقه پرخطر: پاکسازی داده‌ها</span>
          <Trash2 className="w-4 h-4 text-rose-400" />
        </div>
        <div className="text-xs text-zinc-400 leading-relaxed">
          بازنشانی کلیه چرخه‌ها، گزارش‌های روزانه و ثبت مجدد داده‌های نمونه اولیه.
        </div>
        <button
          onClick={onOpenReset}
          className="w-full py-2.5 rounded-xl border border-rose-500/40 text-rose-400 text-xs font-bold hover:bg-rose-500/10 transition-colors cursor-pointer mt-1"
        >
          بازنشانی پایگاه داده به حالت کارخانه
        </button>
      </div>

      {/* System Metadata */}
      <div className="text-center text-zinc-600 text-[11px] py-2 flex flex-col gap-0.5">
        <span className="font-bold text-zinc-500">Bushido Discipline OS v1.0.0</span>
        <span>سیستم‌عامل انضباط رزمی، تسلط بر اراده و خودفرمانی</span>
      </div>
    </div>
  );
};

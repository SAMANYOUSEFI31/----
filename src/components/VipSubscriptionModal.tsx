import React, { useState } from 'react';
import { X, Sparkles, Check, Crown } from 'lucide-react';

interface VipSubscriptionModalProps {
  onDismiss: () => void;
}

export const VipSubscriptionModal: React.FC<VipSubscriptionModalProps> = ({ onDismiss }) => {
  const [selectedPlan, setSelectedPlan] = useState('6m');

  const plans = [
    { id: '3m', title: 'اشتراک سه‌ماهه سامورایی (۱ چرخه)', price: '۱۹۹,۰۰۰ تومان' },
    { id: '6m', title: 'اشتراک شش‌ماهه فرماندهی (۲ چرخه)', price: '۳۴۹,۰۰۰ تومان' },
    { id: '12m', title: 'اشتراک سالانه استادی دیسیپلین (۴ چرخه)', price: '۵۹۰,۰۰۰ تومان' }
  ];

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-3 bg-black/80 backdrop-blur-sm">
      <div className="w-full max-w-lg max-h-[92vh] flex flex-col bg-[#121215] border border-amber-500/30 rounded-2xl shadow-2xl overflow-hidden">
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
              <div className="text-sm font-bold text-amber-400">
                عضویت ویژه VIP سامورایی
              </div>
              <div className="text-xs text-[#a1a1aa]">
                دسترسی نامحدود به سنسی هوشمند و آرشیو دیوان
              </div>
            </div>
            <div className="w-9 h-9 rounded-xl bg-amber-500/15 border border-amber-500/40 flex items-center justify-center text-amber-400">
              <Crown className="w-5 h-5" />
            </div>
          </div>
        </div>

        {/* Content Body */}
        <div className="flex-1 overflow-y-auto p-4 flex flex-col gap-4 text-right">
          {/* Plan Selector */}
          <div className="flex flex-col gap-2">
            {plans.map((p) => {
              const isSelected = selectedPlan === p.id;
              return (
                <div
                  key={p.id}
                  onClick={() => setSelectedPlan(p.id)}
                  className={`p-3.5 rounded-xl border flex items-center justify-between cursor-pointer transition-all ${
                    isSelected
                      ? 'bg-amber-500/10 border-amber-400 text-white'
                      : 'bg-[#18181b] border-[#27272a] hover:border-[#3f3f46] text-zinc-300'
                  }`}
                >
                  <span className="text-xs font-bold text-amber-300">{p.price}</span>
                  <span className="text-xs font-bold">{p.title}</span>
                </div>
              );
            })}
          </div>

          {/* Perks */}
          <div className="bg-[#18181b] p-3.5 rounded-xl border border-[#27272a] flex flex-col gap-2.5">
            <div className="text-xs font-bold text-amber-400">
              مزایای اختصاصی حساب VIP سامورایی:
            </div>
            <div className="flex items-center justify-end gap-2 text-xs text-zinc-300">
              <span>مشاوره‌های تخصصی نامحدود از سنسی دیسیپلین</span>
              <Check className="w-3.5 h-3.5 text-amber-400 shrink-0" />
            </div>
            <div className="flex items-center justify-end gap-2 text-xs text-zinc-300">
              <span>ارزیابی و احکام رسمی دادگاه عالی بوشیدو پس از هر دوره</span>
              <Check className="w-3.5 h-3.5 text-amber-400 shrink-0" />
            </div>
            <div className="flex items-center justify-end gap-2 text-xs text-zinc-300">
              <span>آرشیو بدون محدودیت چرخه‌ها و نقشه‌های حرارتی ۹۰ روزه</span>
              <Check className="w-3.5 h-3.5 text-amber-400 shrink-0" />
            </div>
            <div className="flex items-center justify-end gap-2 text-xs text-zinc-300">
              <span>همگام‌سازی ابری امن و حفظ استریک موروثی بین دوره‌ها</span>
              <Check className="w-3.5 h-3.5 text-amber-400 shrink-0" />
            </div>
          </div>
        </div>

        {/* Action */}
        <div className="p-4 border-t border-[#27272a]">
          <button
            onClick={onDismiss}
            className="w-full py-3 rounded-xl bg-amber-400 hover:bg-amber-300 text-black text-xs font-bold transition-transform active:scale-95 shadow-lg flex items-center justify-center gap-1.5"
          >
            <Sparkles className="w-4 h-4" />
            <span>فعال‌سازی آنی دسترسی VIP</span>
          </button>
        </div>
      </div>
    </div>
  );
};

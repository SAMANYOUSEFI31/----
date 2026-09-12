import React from 'react';
import { X, Gavel } from 'lucide-react';

interface DisciplineRulesModalProps {
  onDismiss: () => void;
}

export const DisciplineRulesModal: React.FC<DisciplineRulesModalProps> = ({ onDismiss }) => {
  const rules = [
    {
      num: '۱',
      title: 'اصل صداقت بی‌رحمانه در ثبت داده‌ها',
      desc: 'یک روز سوخته با ثبت واقعی، شرف دارد به روزی که با تیک‌های دروغین و توهمی پر شده باشد. بوشیدو بر مبنای حقیقت بنا شده است.'
    },
    {
      num: '۲',
      title: 'اصل ۵ از ۵: روز استاندارد یا سوخته',
      desc: 'فونداسیون یک کل تفکیک‌ناپذیر است. ثبت ۴ پایه از ۵ پایه همچنان روز سوخته است تا ذهن به کم‌کاری جزئی خو نگیرد و برای کمال بجنگد.'
    },
    {
      num: '۳',
      title: 'اصل قفل سیستم در برابر بدهی‌های گذشته',
      desc: 'سیستم اجازه ثبت روز جاری را بدون کالبدشکافی و بستن پرونده افت‌های گذشته نمی‌دهد. فرار از بازبینی گذشته در مرام سامورایی جایی ندارد.'
    },
    {
      num: '۴',
      title: 'اصل فریز شخصی در شرایط اضطرار واقعی',
      desc: 'اگر شرایط غیرمترقبه و اضطراری رخ داد، فریز شخصی زنجیره شما را محافظت می‌کند تا بدون اضطراب بی‌مورد دوباره به خط بازگردید.'
    }
  ];

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-3 bg-black/80 backdrop-blur-sm">
      <div className="w-full max-w-lg max-h-[90vh] flex flex-col bg-[#121215] border border-[#27272a] rounded-2xl shadow-2xl overflow-hidden">
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
                مرام‌نامه و ۴ اصل بنیادین بوشیدو
              </div>
              <div className="text-xs text-[#a1a1aa]">
                اصول حاکم بر میدان نبرد ۹۰ روزه
              </div>
            </div>
            <div className="w-9 h-9 rounded-xl bg-[#18181b] border border-[#27272a] flex items-center justify-center text-zinc-300">
              <Gavel className="w-5 h-5" />
            </div>
          </div>
        </div>

        {/* Rules Content */}
        <div className="flex-1 overflow-y-auto p-4 flex flex-col gap-3 text-right">
          {rules.map((r) => (
            <div key={r.num} className="bg-[#18181b] p-3.5 rounded-xl border border-[#27272a] flex flex-col gap-1.5">
              <div className="flex items-center justify-end gap-2">
                <span className="text-xs font-bold text-white">{r.title}</span>
                <span className="w-5 h-5 rounded bg-rose-500/20 border border-rose-500/40 text-rose-400 text-[11px] font-black flex items-center justify-center">
                  {r.num}
                </span>
              </div>
              <div className="text-xs text-zinc-400 leading-relaxed">
                {r.desc}
              </div>
            </div>
          ))}
        </div>

        {/* Action */}
        <div className="p-4 border-t border-[#27272a]">
          <button
            onClick={onDismiss}
            className="w-full py-2.5 rounded-xl bg-rose-600 hover:bg-rose-500 text-white text-xs font-bold transition-colors shadow-md"
          >
            متوجه شدم و متعهدم
          </button>
        </div>
      </div>
    </div>
  );
};

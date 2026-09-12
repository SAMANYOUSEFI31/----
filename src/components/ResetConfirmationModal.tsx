import React from 'react';
import { AlertTriangle } from 'lucide-react';

interface ResetConfirmationModalProps {
  onDismiss: () => void;
  onConfirmReset: () => void;
}

export const ResetConfirmationModal: React.FC<ResetConfirmationModalProps> = ({
  onDismiss,
  onConfirmReset
}) => {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-3 bg-black/80 backdrop-blur-sm">
      <div className="w-full max-w-md bg-[#121215] border border-rose-500/40 rounded-2xl p-5 flex flex-col items-center gap-4 text-center shadow-2xl">
        <div className="w-12 h-12 rounded-2xl bg-rose-500/15 border border-rose-500/40 flex items-center justify-center text-rose-400">
          <AlertTriangle className="w-6 h-6" />
        </div>

        <div>
          <div className="text-sm font-bold text-white mb-1">
            بازنشانی کامل پایگاه داده بوشیدو
          </div>
          <div className="text-xs text-zinc-400 leading-relaxed">
            آیا اطمینان دارید؟ تمام چرخه‌ها، لاگ‌های روزانه و پرونده‌های کالبدشکافی حذف شده و داده‌های نمونه اولیه مجدداً بارگذاری می‌شوند.
          </div>
        </div>

        <div className="flex items-center gap-2 w-full pt-2">
          <button
            onClick={onDismiss}
            className="flex-1 py-2.5 rounded-xl border border-[#27272a] text-xs font-bold text-zinc-300 hover:bg-[#18181b] transition-colors"
          >
            انصراف
          </button>
          <button
            onClick={onConfirmReset}
            className="flex-1 py-2.5 rounded-xl bg-rose-600 hover:bg-rose-500 text-xs font-bold text-white transition-colors shadow-md"
          >
            تایید و بازنشانی
          </button>
        </div>
      </div>
    </div>
  );
};

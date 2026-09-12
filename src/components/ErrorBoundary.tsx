import React, { Component, ErrorInfo, ReactNode } from 'react';
import { AlertTriangle, RefreshCw } from 'lucide-react';

interface Props {
  children: ReactNode;
}

interface State {
  hasError: boolean;
  error: Error | null;
}

export class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false,
    error: null
  };

  public static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error };
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error('Uncaught error in Bushido Discipline OS:', error, errorInfo);
  }

  private handleReset = () => {
    localStorage.removeItem('bushido_cycles');
    localStorage.removeItem('bushido_logs');
    localStorage.removeItem('bushido_profile');
    window.location.reload();
  };

  public render() {
    if (this.state.hasError) {
      return (
        <div className="min-h-screen bg-[#09090b] text-[#f4f4f5] flex items-center justify-center p-4 selection:bg-rose-500/20 selection:text-rose-300">
          <div className="w-full max-w-md bg-[#121215] border border-rose-500/30 rounded-2xl p-6 flex flex-col items-center gap-4 text-center shadow-2xl">
            <div className="w-14 h-14 rounded-2xl bg-rose-500/15 border border-rose-500/40 flex items-center justify-center text-rose-400">
              <AlertTriangle className="w-7 h-7" />
            </div>

            <div>
              <h2 className="text-base font-bold text-white mb-1">
                توقف موقت در اجرای سیستم دیسیپلین
              </h2>
              <p className="text-xs text-zinc-400 leading-relaxed">
                خطایی در پردازش داده‌ها رخ داد. با فشردن دکمه زیر، داده‌های سیستم بازنشانی شده و اپلیکیشن مجدداً بارگذاری می‌شود.
              </p>
            </div>

            {this.state.error && (
              <div className="w-full p-2.5 bg-[#18181b] border border-[#27272a] rounded-xl text-[11px] text-zinc-500 font-mono text-left overflow-x-auto max-h-24">
                {this.state.error.message}
              </div>
            )}

            <button
              onClick={this.handleReset}
              className="w-full py-3 rounded-xl bg-rose-600 hover:bg-rose-500 text-white text-xs font-bold transition-all flex items-center justify-center gap-2 shadow-lg cursor-pointer"
            >
              <RefreshCw className="w-4 h-4" />
              <span>بازنشانی داده‌ها و راه‌اندازی مجدد</span>
            </button>
          </div>
        </div>
      );
    }

    return this.props.children;
  }
}

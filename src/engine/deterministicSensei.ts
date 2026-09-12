import {
  DeterministicAutopsyResult,
  DeterministicSenseiResult,
  DeterministicCourtResult,
  VulnerableHabit
} from '../types';
import { toPersianDigits } from './numberUtils';

export function getDeterministicAutopsy(
  reason: string,
  time: string,
  notes: string,
  countermeasure: string
): DeterministicAutopsyResult {
  let analysis = 'بررسی کالبدشکافی نشان‌دهنده افت در مرام انضباطی است.';
  let psychologicalTrap = 'خطای تسلیم در برابر لذت لحظه‌ای و اهمال‌کاری.';
  let cm = countermeasure || 'اجرای قانون ۵ ثانیه و حذف محرک‌های حواس‌پرتی.';
  let tacticalActionTomorrow = 'فردا در همان ساعت وقوع افت، تدابیر پیشگیرانه اتخاذ شود.';

  if (reason.includes('خستگی') || reason.includes('کم‌خوابی')) {
    analysis = `علت افت در بازه زمانی ${time}، کاهش سطح انرژی زیستی و ضعف در ریکاوری بوده است.`;
    psychologicalTrap = 'تله فرسودگی شناختی و تصمیم‌گیری در وضعیت خستگی شدید.';
    cm = countermeasure || 'خواب زودتر در شب قبل و کاهش کار بی‌مورد شبانه.';
    tacticalActionTomorrow = 'ساعت ۲۲ خاموشی کامل و حذف نمایشگرها برای بازسازی قوا.';
  } else if (reason.includes('گوشی') || reason.includes('فضای مجازی')) {
    analysis = `افت در ساعت ${time} بر اثر نشت تمرکز در فضای مجازی رخ داده است.`;
    psychologicalTrap = 'دام ترشح دوپامین ارزان‌قیمت از شبکه‌های اجتماعی.';
    cm = countermeasure || 'گذاشتن گوشی در اتاق دیگر هنگام اجرای کار عمیق.';
    tacticalActionTomorrow = 'فعال‌سازی حالت فوکوس و قرار دادن گوشی دور از دسترس.';
  } else if (reason.includes('اهمال‌کاری') || reason.includes('شروع نکردن')) {
    analysis = `تاخیر در آغاز فعالیت در ساعت ${time} منجر به ریزش دیسیپلین روز شد.`;
    psychologicalTrap = 'سندروم ترس از شروع و توهم کامل‌گرایی بی‌جا.';
    cm = countermeasure || 'شکستن کار سخت به تکه‌های کوچک ۵ دقیقه‌ای.';
    tacticalActionTomorrow = 'آغاز مستقیم کار سخت در همان دقیقه اول ورود به میز.';
  }

  return {
    analysis,
    psychologicalTrap,
    countermeasure: cm,
    tacticalActionTomorrow
  };
}

export function getDeterministicSenseiAdvice(
  cycleTitle: string,
  elapsedDays: number,
  remainingDays: number,
  disciplinePercentage: number,
  disciplineLevel: string,
  pureStreak: number,
  vulnerableHabits: VulnerableHabit[],
  dominantFailureReason: string,
  dominantFailureTime: string
): DeterministicSenseiResult {
  let coachVerdict = `در روز ${toPersianDigits(elapsedDays)} از ۹۰ روز، با انضباط ${toPersianDigits(disciplinePercentage)}٪ در وضعیت ${disciplineLevel} هستید.`;
  let keyAdvice = 'بر تثبیت پایه‌های ۵ گانه در ساعات اولیه روز تمرکز کنید.';
  let strategicWarning = 'اجازه ندهید خستگی عصرگاهی زنجیره را بشکند.';
  let bushidoQuote = '«پیروزی نهایی نه از شدت ضربات، بلکه از ثبات روزمره در سنگر حاصل می‌شود.»';

  if (disciplinePercentage >= 80) {
    coachVerdict = `عملکرد درخشان! تسلط ${toPersianDigits(disciplinePercentage)}٪ و استریک مداوم ${toPersianDigits(pureStreak)} روز، نشان‌دهنده روحیه‌ای شکست‌ناپذیر است.`;
    keyAdvice = 'از غرور پرهیز کنید؛ بزرگ‌ترین دشمن سامورایی، آسودگی پس از موفقیت‌های پیاپی است.';
    strategicWarning = 'به استانداردهای روزانه پایبند بمانید و برای روزهای سخت آماده باشید.';
    bushidoQuote = '«شمشیر صیقل‌خورده هر روز نیازمند مراقبت است؛ انضباط پایانی ندارد.»';
  } else if (disciplinePercentage < 50) {
    coachVerdict = `هشدار بحران دیسیپلین! انضباط به ${toPersianDigits(disciplinePercentage)}٪ افت کرده است. برای نجات چرخه باید فوراً اقدام کنید.`;
    keyAdvice = 'پایه‌های فرعی را رها کنید و روی ۱ کار اصلی و سحرخیزی تمرکز کنید.';
    strategicWarning = 'اگر پرونده‌های بدهی را تسویه نکنید، توهم شکست چرخه را متوقف خواهد کرد.';
    bushidoQuote = '«افتادن هفت بار، برخاستن هشت بار. راه جنگجو تسلیم‌ناپذیری است.»';
  }

  return {
    coachVerdict,
    keyAdvice,
    strategicWarning,
    bushidoQuote
  };
}

export function getDeterministicCourtVerdict(
  cycleTitle: string,
  standardDays: number,
  totalDays: number,
  maxStreak: number,
  disciplinePercentage: number,
  vulnerableHabits: VulnerableHabit[]
): DeterministicCourtResult {
  let grade = 'B';
  if (disciplinePercentage >= 90) grade = 'A+';
  else if (disciplinePercentage >= 75) grade = 'A';
  else if (disciplinePercentage >= 60) grade = 'B';
  else if (disciplinePercentage >= 40) grade = 'C';
  else grade = 'F';

  const verdict = `دیوان عالی بوشیدو پس از بررسی داده‌های چرخه «${cycleTitle}» با ${toPersianDigits(standardDays)} روز استاندارد از ${toPersianDigits(totalDays)} روز نبرد، رتبه ${grade} را به شما اعطا می‌نماید.`;
  const senseiNotes = `حداکثر زنجیره متوالی ثبت‌شده ${toPersianDigits(maxStreak)} روز بوده و انضباط فانتوم ${toPersianDigits(disciplinePercentage)}٪ محاسبه شده است.`;

  const strengths = [
    'تثبیت نسبی سحرخیزی و تعهد به اهداف',
    'پایبندی به بازبینی روزانه و ثبت لاگ‌ها',
    'غلبه بر موانع اولیه شروع در هفته‌های ابتدایی'
  ];

  const weaknesses = vulnerableHabits.length > 0
    ? vulnerableHabits.map((v) => `ضعف نسبی در عادت «${v.titleFa}» با نرخ موفقیت ${toPersianDigits(v.ratePct)}٪`)
    : ['افت انرژی در بازه‌های عصرگاهی'];

  const tacticalPlanForNextCycle =
    'در چرخه بعدی، ماموریت اصلی تقویت ضعیف‌ترین پایه و رساندن استریک موروثی به بالای ۳۰ روز است.';

  return {
    verdict,
    grade,
    senseiNotes,
    strengths,
    weaknesses,
    tacticalPlanForNextCycle
  };
}

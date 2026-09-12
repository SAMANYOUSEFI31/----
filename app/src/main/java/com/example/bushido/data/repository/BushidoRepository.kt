package com.example.bushido.data.repository

import com.example.bushido.data.local.BushidoDatabase
import com.example.bushido.data.model.Cycle
import com.example.bushido.data.model.DailyLog
import com.example.bushido.data.model.UserProfile
import com.example.bushido.engine.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class BushidoRepository(private val db: BushidoDatabase) {
    val allCycles: Flow<List<Cycle>> = db.cycleDao().getAllCycles()
    val allLogs: Flow<List<DailyLog>> = db.dailyLogDao().getAllLogs()
    val userProfile: Flow<UserProfile?> = db.userProfileDao().getUserProfile()

    fun getLogsForCycle(cycleId: String): Flow<List<DailyLog>> = db.dailyLogDao().getLogsForCycle(cycleId)

    suspend fun initializeSeedDataIfNeeded() = withContext(Dispatchers.IO) {
        val existingCycles = db.cycleDao().getAllCycles().firstOrNull()
        if (existingCycles.isNullOrEmpty()) {
            val todayStr = DateUtils.getLogicalTodayDate()
            val cycle1StartDate = DateUtils.addDaysToDate(todayStr, -24)
            val cycle1EndDate = DateUtils.addDaysToDate(cycle1StartDate, 89)

            val sampleCycle = Cycle(
                id = "cycle-1",
                title = "چرخه ۱ (نمونه) — فونداسیون اراده و دیسیپلین آهنین",
                startDate = cycle1StartDate,
                endDate = cycle1EndDate,
                targetTheme = "تسلط بر سحرخیزی، ۱۰۰ ساعت کار عمیق و ثبات در ورزش روزانه",
                inheritedStreak = 0,
                rules = listOf(
                    "ساعت بیدارباش ۵:۳۰ صبح بدون استفاده از اسنوز",
                    "هیچ روزی بدون حداقل ۳۰ دقیقه ورزش و تحرک سپری نمی‌شود",
                    "ثبت روزانه بلافاصله قبل از خواب در میدان نبرد",
                    "کالبدشکافی بدون تعارف در صورت هرگونه افت"
                ),
                isArchived = false,
                reportRead = false,
                revision = 1
            )

            db.cycleDao().insertCycle(sampleCycle)

            val logs = mutableListOf<DailyLog>()
            for (i in 0..24) {
                val dayDate = DateUtils.addDaysToDate(cycle1StartDate, i)
                val isToday = dayDate == todayStr

                if (isToday) {
                    logs.add(
                        DailyLog(
                            id = "log-$dayDate",
                            cycleId = sampleCycle.id,
                            date = dayDate,
                            wakeUp = true,
                            workout = true,
                            study = true,
                            journal = false,
                            hardTask = true,
                            specialMission = true,
                            notes = "تمرکز بالا روی پروژه و شروع عالی صبح"
                        )
                    )
                } else if (i == 18) {
                    logs.add(
                        DailyLog(
                            id = "log-$dayDate",
                            cycleId = sampleCycle.id,
                            date = dayDate,
                            wakeUp = true,
                            workout = false,
                            study = false,
                            journal = true,
                            hardTask = false,
                            specialMission = false,
                            failureReason = "دلایل شخصی",
                            failureTime = "وسط روز",
                            autopsyNotes = "سفر کاری اضطراری و عدم دسترسی به امکانات عادی. ریتم فریز شد.",
                            countermeasure = "حفظ استانداردهای ذهنی و ژورنال‌نویسی شبانه در شرایط بحران."
                        )
                    )
                } else if (i == 11) {
                    logs.add(
                        DailyLog(
                            id = "log-$dayDate",
                            cycleId = sampleCycle.id,
                            date = dayDate,
                            wakeUp = false,
                            workout = true,
                            study = true,
                            journal = true,
                            hardTask = false,
                            specialMission = false,
                            failureReason = "وقتم رو به خوبی مدیریت نکردم",
                            failureTime = "آخر روز",
                            autopsyNotes = "اتلاف وقت در شبکه‌های اجتماعی در ساعات اولیه صبح باعث به تعویق افتادن کار سخت شد.",
                            countermeasure = "قانون صفر دسترسی: گوشی قبل از ساعت ۹ صبح در اتاق دیگر قفل می‌شود.",
                            aiFeedback = "افت اصلی ناشی از تصمیم‌گیری واکنشی به جای کنشگرانه بوده است. اعمال قانون صفر گوشی بهترین اقدام تثبیت‌کننده است."
                        )
                    )
                } else if (i == 5) {
                    logs.add(
                        DailyLog(
                            id = "log-$dayDate",
                            cycleId = sampleCycle.id,
                            date = dayDate,
                            wakeUp = true,
                            workout = false,
                            study = true,
                            journal = true,
                            hardTask = true,
                            specialMission = false,
                            failureReason = "نیمه‌کاره رها کردم",
                            failureTime = "وسط روز",
                            autopsyNotes = "به علت خستگی عصرانه تمرین ورزشی را نیمه‌کاره رها کردم.",
                            countermeasure = "ورزش به ساعات بلافاصله پس از بیداری (۶:۳۰ صبح) منتقل شد."
                        )
                    )
                } else {
                    logs.add(
                        DailyLog(
                            id = "log-$dayDate",
                            cycleId = sampleCycle.id,
                            date = dayDate,
                            wakeUp = true,
                            workout = true,
                            study = true,
                            journal = true,
                            hardTask = true,
                            specialMission = i % 3 == 0,
                            notes = if (i % 4 == 0) "انرژی و تمرکز فوق‌العاده. تسلط کامل بر زمان." else ""
                        )
                    )
                }
            }

            db.dailyLogDao().insertLogs(logs)

            val profile = UserProfile(
                id = "admin-master-001",
                name = "فرمانده ارشد سامورایی",
                email = "admin@bushido.app",
                phoneNumber = "09375454050",
                tier = "vip_samurai",
                isVip = true,
                isAdmin = true,
                nightOwlCutoffHour = 4
            )
            db.userProfileDao().insertProfile(profile)
        }
    }

    suspend fun saveDailyLog(log: DailyLog) = withContext(Dispatchers.IO) {
        db.dailyLogDao().insertLog(log)
    }

    suspend fun saveCycle(cycle: Cycle) = withContext(Dispatchers.IO) {
        db.cycleDao().insertCycle(cycle)
    }

    suspend fun deleteCycle(cycleId: String) = withContext(Dispatchers.IO) {
        db.dailyLogDao().deleteLogsForCycle(cycleId)
        db.cycleDao().deleteCycleById(cycleId)
    }

    suspend fun saveUserProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        db.userProfileDao().insertProfile(profile)
    }

    suspend fun resetAllData() = withContext(Dispatchers.IO) {
        db.dailyLogDao().clearAll()
        db.cycleDao().clearAll()
        db.userProfileDao().clearAll()
        initializeSeedDataIfNeeded()
    }
}

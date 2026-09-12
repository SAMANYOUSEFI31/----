package com.example.bushido.engine

import com.example.bushido.data.model.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

object BushidoCalculations {

    val FOUNDATION_HABITS = listOf(
        HabitDef(
            key = HabitKeys.WAKE_UP,
            title = "Early Rise",
            titleFa = "سحرخیزی",
            subtitleFa = "بیدارباش سر ساعت بدون بهانه",
            iconName = "Sun",
            colorHex = "#FBBF24"
        ),
        HabitDef(
            key = HabitKeys.WORKOUT,
            title = "Workout",
            titleFa = "ورزش و تحرک",
            subtitleFa = "فعالیت بدنی هدفمند و منظم",
            iconName = "Dumbbell",
            colorHex = "#34D399"
        ),
        HabitDef(
            key = HabitKeys.STUDY,
            title = "Study & Reading",
            titleFa = "مطالعه تخصصی",
            subtitleFa = "تغذیه ذهن و یادگیری عمیق",
            iconName = "BookOpen",
            colorHex = "#60A5FA"
        ),
        HabitDef(
            key = HabitKeys.JOURNAL,
            title = "Journaling",
            titleFa = "ژورنال‌نویسی",
            subtitleFa = "ثبت روزانه، تحلیل ذهن و شفافیت",
            iconName = "PenTool",
            colorHex = "#C084FC"
        ),
        HabitDef(
            key = HabitKeys.HARD_TASK,
            title = "Deep Hard Task",
            titleFa = "کار سخت روز",
            subtitleFa = "سنگین‌ترین قورباغه روزکاری",
            iconName = "Briefcase",
            colorHex = "#FB7185"
        )
    )

    fun createEmptyCycleMetrics(cycle: Cycle? = null): CycleMetrics {
        val targetCycle = cycle ?: Cycle(
            id = "default",
            title = "بدون چرخه فعال",
            startDate = DateUtils.getLogicalTodayDate(),
            endDate = DateUtils.addDaysToDate(DateUtils.getLogicalTodayDate(), 89),
            targetTheme = "تسلط بر سحرخیزی و دیسیپلین آهنین",
            rules = listOf("سحرخیزی بی‌چون‌وچرا", "ورزش روزانه", "ثبت قبل از خواب"),
            inheritedStreak = 0
        )
        return CycleMetrics(
            cycle = targetCycle,
            status = CycleStatusType.ACTIVE,
            statusLabelFa = "بدون چرخه فعال",
            elapsedDays = 0,
            remainingDays = 90,
            logsCount = 0,
            standardDaysCount = 0,
            burnedDaysCount = 0,
            unresolvedDebtCount = 0,
            resolvedDebtCount = 0,
            frozenDaysCount = 0,
            inactiveDaysCount = 0,
            incompleteDaysCount = 0,
            totalScore = 0,
            disciplineScore = 0.0,
            disciplinePercentage = 0,
            disciplineLevel = DisciplineLevel.IRON,
            pureStreak = 0,
            maxPureStreak = 0,
            inheritedStreak = 0,
            globalLiveStreak = 0,
            maxGlobalStreak = 0,
            maxFailureRun = 0,
            maxInactiveRun = 0,
            maxIncompleteRun = 0,
            dominantFailureReason = "بدون شکست",
            dominantFailureTime = "بدون شکست",
            vulnerableHabits = emptyList(),
            needsIntervention = false,
            isCourtReady = false,
            coachMessage = "برای شروع، نخستین چرخه ۹۰ روزه خود را ایجاد و ثبت روزانه را آغاز کنید"
        )
    }

    fun computeDailyProperties(
        log: DailyLog,
        allCycleLogs: List<DailyLog>,
        logicalToday: String = DateUtils.getLogicalTodayDate(),
        cycleStartDate: String? = null
    ): DailyComputed {
        var habitsCount = 0
        if (log.wakeUp) habitsCount++
        if (log.workout) habitsCount++
        if (log.study) habitsCount++
        if (log.journal) habitsCount++
        if (log.hardTask) habitsCount++

        val isStandard = habitsCount == 5
        val missionScore = if (log.specialMission) 2 else 0
        val bonusScore = if (isStandard) 3 else 0
        val score = min(10, habitsCount + missionScore + bonusScore)

        val statusType = when {
            isStandard -> DayStatusType.STANDARD
            log.failureReason == "دلایل شخصی" -> DayStatusType.PERSONAL_FROZEN
            log.failureReason.isNotBlank() && log.failureTime.isNotBlank() -> DayStatusType.BURNED_RESOLVED
            else -> DayStatusType.BURNED_UNRESOLVED
        }

        val needsAutopsy = log.date < logicalToday && !isStandard && statusType == DayStatusType.BURNED_UNRESOLVED

        // Check if previous days strictly before this date (within the cycle) have unresolved debts
        var pastUnresolvedCount = 0
        if (!cycleStartDate.isNullOrBlank() && log.date > cycleStartDate) {
            var checkDate = cycleStartDate
            while (checkDate < log.date && checkDate < logicalToday) {
                val existing = allCycleLogs.find { it.date == checkDate && (log.cycleId.isBlank() || it.cycleId.isBlank() || it.cycleId == log.cycleId) }
                if (existing == null) {
                    pastUnresolvedCount += 1
                } else {
                    var lCount = 0
                    if (existing.wakeUp) lCount++
                    if (existing.workout) lCount++
                    if (existing.study) lCount++
                    if (existing.journal) lCount++
                    if (existing.hardTask) lCount++
                    val lStandard = lCount == 5
                    if (!lStandard && existing.failureReason != "دلایل شخصی") {
                        if (existing.failureReason.isBlank() || existing.failureTime.isBlank()) {
                            pastUnresolvedCount += 1
                        }
                    }
                }
                checkDate = DateUtils.addDaysToDate(checkDate, 1)
            }
        } else {
            pastUnresolvedCount = allCycleLogs.count { l ->
                if (log.cycleId.isNotBlank() && l.cycleId.isNotBlank() && l.cycleId != log.cycleId) return@count false
                if (l.date >= log.date || l.date >= logicalToday) return@count false
                var lCount = 0
                if (l.wakeUp) lCount++
                if (l.workout) lCount++
                if (l.study) lCount++
                if (l.journal) lCount++
                if (l.hardTask) lCount++
                val lStandard = lCount == 5
                if (lStandard || l.failureReason == "دلایل شخصی") return@count false
                l.failureReason.isBlank() || l.failureTime.isBlank()
            }
        }

        val isLockedDueToPastDebt = log.date == logicalToday && pastUnresolvedCount > 0

        val smTag = if (log.specialMission) " + ماموریت ویژه" else ""
        val coachStatusLabel = when {
            log.date > logicalToday -> {
                val diff = DateUtils.daysBetween(logicalToday, log.date)
                if (diff == 1) {
                    "فردا هنوز فرا نرسیده است. تمرکز بر فتح روز جاری است."
                } else {
                    "${NumberUtils.toPersianDigits(diff)} روز تا این تاریخ باقی است. تمرکز بر روز جاری است."
                }
            }
            log.date < logicalToday -> {
                when {
                    statusType == DayStatusType.PERSONAL_FROZEN -> "توقف اضطراری؛ زنجیره بدون آسیب حفظ شد."
                    isStandard -> "روز استاندارد تثبیت شد$smTag"
                    statusType == DayStatusType.BURNED_UNRESOLVED -> "نیازمند کالبدشکافی؛ ثبت علت افت الزامی است."
                    else -> "پرونده شکست با ثبت کالبدشکافی بسته شد."
                }
            }
            else -> {
                // Today
                when {
                    isStandard -> if (log.specialMission) "کمال تعهد با ۵ پایه و ماموریت ویژه ثبت شد." else "روز استاندارد با ۵ پایه فونداسیون تثبیت شد."
                    statusType == DayStatusType.PERSONAL_FROZEN -> "توقف اضطراری؛ زنجیره در امان است."
                    habitsCount == 4 -> "فقط ۱ پایه تا استانداردسازی روز فاصله دارید.$smTag"
                    habitsCount in 1..3 -> "${NumberUtils.toPersianDigits(5 - habitsCount)} پایه تا استانداردسازی روز باقی مانده است.$smTag"
                    else -> "روز در جریان است؛ ثبت پایه‌های فونداسیون را آغاز کنید."
                }
            }
        }

        val displayScore = if (score == 10) "کمال تعهد: ۱۰ از ۱۰" else "امتیاز ارزش روز: ${NumberUtils.toPersianDigits(score)} از ۱۰"
        val displayProgressBlocks = "■ ".repeat(score) + "□ ".repeat(max(0, 10 - score))

        return DailyComputed(
            isStandard = isStandard,
            habitsCount = habitsCount,
            score = score,
            statusType = statusType,
            needsAutopsy = needsAutopsy,
            isLockedDueToPastDebt = isLockedDueToPastDebt,
            coachStatusLabel = coachStatusLabel,
            displayScore = displayScore,
            displayProgressBlocks = displayProgressBlocks
        )
    }

    fun computeCycleMetrics(
        cycle: Cycle,
        logs: List<DailyLog>,
        allCycles: List<Cycle> = emptyList(),
        logicalToday: String = DateUtils.getLogicalTodayDate()
    ): CycleMetrics {
        val cycleLogs = logs
            .filter { it.cycleId == cycle.id || (it.cycleId.isBlank() && it.date >= cycle.startDate && it.date <= cycle.endDate) }
            .sortedBy { it.date }

        val cycleStartDate = cycle.startDate
        val cycleEndDate = if (cycle.endDate.isNotBlank()) cycle.endDate else DateUtils.addDaysToDate(cycle.startDate, 89)

        var status = CycleStatusType.ACTIVE
        var statusLabelFa = "چرخه فعال"

        val otherCycles = allCycles.filter { it.id != cycle.id }
        val hasOverlap = otherCycles.any { oc ->
            val ocEnd = if (oc.endDate.isNotBlank()) oc.endDate else DateUtils.addDaysToDate(oc.startDate, 89)
            !(cycleEndDate < oc.startDate || cycleStartDate > ocEnd)
        }

        when {
            hasOverlap -> {
                status = CycleStatusType.OVERLAP_ERROR
                statusLabelFa = "تداخل تقویمی چرخه"
            }
            cycle.isArchived -> {
                status = CycleStatusType.ARCHIVED
                statusLabelFa = "بایگانی شده"
            }
            logicalToday < cycleStartDate -> {
                status = CycleStatusType.UPCOMING
                statusLabelFa = "چرخه آینده"
            }
            logicalToday > cycleEndDate -> {
                if (cycle.reportRead) {
                    status = CycleStatusType.ARCHIVED
                    statusLabelFa = "بایگانی شده"
                } else {
                    status = CycleStatusType.READY_FOR_COURT
                    statusLabelFa = "آماده برای قرارگاه بوشیدو"
                }
            }
            else -> {
                status = CycleStatusType.ACTIVE
                statusLabelFa = "چرخه فعال"
            }
        }

        val elapsedDays = when {
            logicalToday < cycleStartDate -> 0
            logicalToday > cycleEndDate -> 90
            else -> min(90, max(1, DateUtils.daysBetween(cycleStartDate, logicalToday) + 1))
        }
        val remainingDays = max(0, 90 - elapsedDays)

        // Synthesize timeline up to effectiveEnd
        val effectiveEnd = if (logicalToday > cycleEndDate) cycleEndDate else logicalToday
        val synthesizedList = mutableListOf<Pair<DailyLog, DailyComputed>>()

        if (status != CycleStatusType.UPCOMING) {
            var curr = cycleStartDate
            while (curr <= effectiveEnd) {
                var existingLog = cycleLogs.find { it.date == curr }
                if (existingLog == null) {
                    existingLog = DailyLog(
                        id = "virtual-$curr",
                        cycleId = cycle.id,
                        date = curr,
                        isVirtual = true
                    )
                }
                synthesizedList.add(existingLog to computeDailyProperties(existingLog, cycleLogs, logicalToday, cycleStartDate))
                curr = DateUtils.addDaysToDate(curr, 1)
            }
        }

        // Include any future modified logs
        cycleLogs.forEach { l ->
            if (l.date > effectiveEnd) {
                synthesizedList.add(l to computeDailyProperties(l, cycleLogs, logicalToday, cycleStartDate))
            }
        }

        val computedList = synthesizedList
        val logsCount = cycleLogs.size
        val standardDaysCount = computedList.count { it.second.isStandard }
        val unresolvedDebtCount = computedList.count { it.first.date < logicalToday && it.second.statusType == DayStatusType.BURNED_UNRESOLVED }
        val resolvedDebtCount = computedList.count { it.second.statusType == DayStatusType.BURNED_RESOLVED }
        val burnedDaysCount = unresolvedDebtCount + resolvedDebtCount
        val frozenDaysCount = computedList.count { it.second.statusType == DayStatusType.PERSONAL_FROZEN }
        val inactiveDaysCount = computedList.count { it.second.statusType != DayStatusType.PERSONAL_FROZEN && it.second.habitsCount == 0 && !it.second.isStandard }
        val incompleteDaysCount = computedList.count { it.second.statusType != DayStatusType.PERSONAL_FROZEN && it.second.habitsCount > 0 && !it.second.isStandard }
        val totalScore = computedList.sumOf { it.second.score }

        // Phantom Denominator & Discipline Score
        val todayLog = computedList.find { it.first.date == logicalToday }
        val isTodayCompleted = todayLog?.let { it.second.isStandard || it.first.failureReason.isNotBlank() } ?: false

        val validLogs = computedList.filter {
            it.first.date <= logicalToday && (it.first.date < logicalToday || isTodayCompleted)
        }

        val nFrozen = validLogs.count { it.second.statusType == DayStatusType.PERSONAL_FROZEN }
        val rawElapsed = if (status == CycleStatusType.ACTIVE) (elapsedDays - 1 + if (isTodayCompleted) 1 else 0) else elapsedDays
        val evaluatedDays = max(0, rawElapsed - nFrozen)

        val n1 = validLogs.count { it.second.statusType == DayStatusType.STANDARD }
        val n2 = validLogs.count { it.second.statusType == DayStatusType.BURNED_RESOLVED && it.second.habitsCount > 0 }
        val n3 = validLogs.count { it.second.statusType == DayStatusType.BURNED_RESOLVED && it.second.habitsCount == 0 }
        val n4 = validLogs.count { it.second.statusType == DayStatusType.BURNED_UNRESOLVED && it.second.habitsCount > 0 }
        val n5 = validLogs.count { it.second.statusType == DayStatusType.BURNED_UNRESOLVED && it.second.habitsCount == 0 }

        val activeValidCount = validLogs.count { it.second.statusType != DayStatusType.PERSONAL_FROZEN }
        val missingDays = max(0, evaluatedDays - activeValidCount)

        val phantomDenominator = evaluatedDays + (0.2 * n3) + (1.5 * n4) + (3.0 * (n5 + missingDays))

        var disciplineScore = 1.0
        if (evaluatedDays > 0 && phantomDenominator > 0) {
            disciplineScore = max(0.0, min(1.0, n1.toDouble() / phantomDenominator))
        } else if (evaluatedDays == 0) {
            disciplineScore = 1.0
        }
        val disciplinePercentage = (disciplineScore * 100).roundToInt()

        val disciplineLevel = when {
            status == CycleStatusType.UPCOMING -> DisciplineLevel.UPCOMING
            status == CycleStatusType.OVERLAP_ERROR -> DisciplineLevel.STRUCTURE_ERROR
            disciplineScore >= 0.8 -> DisciplineLevel.IRON
            disciplineScore >= 0.6 -> DisciplineLevel.STABLE
            disciplineScore >= 0.4 -> DisciplineLevel.UNSTABLE
            else -> DisciplineLevel.CRISIS
        }

        // Pure Streak & Max Pure Streak
        var currentPureStreak = 0
        var maxPureStreak = 0
        var runningStreak = 0

        val pastAndTodayLogs = computedList
            .filter { it.first.date <= logicalToday }
            .sortedBy { it.first.date }

        for (i in pastAndTodayLogs.indices) {
            val item = pastAndTodayLogs[i]
            val isToday = item.first.date == logicalToday

            if (i > 0) {
                val prev = pastAndTodayLogs[i - 1]
                val gap = DateUtils.daysBetween(prev.first.date, item.first.date) - 1
                if (gap > 0) {
                    runningStreak = 0
                }
            }

            if (item.second.isStandard) {
                runningStreak += 1
                if (runningStreak > maxPureStreak) maxPureStreak = runningStreak
            } else if (item.second.statusType == DayStatusType.PERSONAL_FROZEN) {
                // Pass-through
            } else if (isToday && item.first.failureReason.isBlank()) {
                // In progress today
            } else {
                runningStreak = 0
            }
        }
        currentPureStreak = runningStreak

        val inheritedStreak = cycle.inheritedStreak
        val globalLiveStreak = inheritedStreak + currentPureStreak
        val maxGlobalStreak = max(inheritedStreak + maxPureStreak, globalLiveStreak)

        // Failure Runs
        var maxFailureRun = 0
        var currFailureRun = 0
        var maxInactiveRun = 0
        var currInactiveRun = 0
        var maxIncompleteRun = 0
        var currIncompleteRun = 0

        pastAndTodayLogs.forEach { item ->
            if (item.second.statusType == DayStatusType.BURNED_UNRESOLVED || item.second.statusType == DayStatusType.BURNED_RESOLVED) {
                currFailureRun += 1
                if (currFailureRun > maxFailureRun) maxFailureRun = currFailureRun
            } else {
                currFailureRun = 0
            }

            if (item.second.statusType != DayStatusType.PERSONAL_FROZEN && item.second.habitsCount == 0 && !item.second.isStandard) {
                currInactiveRun += 1
                if (currInactiveRun > maxInactiveRun) maxInactiveRun = currInactiveRun
            } else {
                currInactiveRun = 0
            }

            if (item.second.statusType != DayStatusType.PERSONAL_FROZEN && item.second.habitsCount > 0 && !item.second.isStandard) {
                currIncompleteRun += 1
                if (currIncompleteRun > maxIncompleteRun) maxIncompleteRun = currIncompleteRun
            } else {
                currIncompleteRun = 0
            }
        }

        // Dominant reasons & times
        val reasonCounts = mutableMapOf<String, Int>()
        val timeCounts = mutableMapOf<String, Int>()

        cycleLogs.forEach { l ->
            if (l.failureReason.isNotBlank() && l.failureReason != "دلایل شخصی") {
                reasonCounts[l.failureReason] = (reasonCounts[l.failureReason] ?: 0) + 1
            }
            if (l.failureTime.isNotBlank()) {
                timeCounts[l.failureTime] = (timeCounts[l.failureTime] ?: 0) + 1
            }
        }

        var dominantFailureReason = "بدون شکست"
        var maxRCount = 0
        reasonCounts.forEach { (r, c) ->
            if (c > maxRCount) {
                maxRCount = c
                dominantFailureReason = r
            }
        }

        var dominantFailureTime = "بدون شکست"
        var maxTCount = 0
        timeCounts.forEach { (t, c) ->
            if (c > maxTCount) {
                maxTCount = c
                dominantFailureTime = t
            }
        }

        // Vulnerable Habits (< 70% threshold)
        val activeBase = max(1, logsCount - frozenDaysCount)
        val vulnerableHabits = mutableListOf<VulnerableHabit>()

        FOUNDATION_HABITS.forEach { h ->
            val successCount = cycleLogs.count { l ->
                when (h.key) {
                    HabitKeys.WAKE_UP -> l.wakeUp
                    HabitKeys.WORKOUT -> l.workout
                    HabitKeys.STUDY -> l.study
                    HabitKeys.JOURNAL -> l.journal
                    HabitKeys.HARD_TASK -> l.hardTask
                    else -> false
                }
            }
            val ratePct = ((successCount.toDouble() / activeBase) * 100).roundToInt()
            if (logsCount > 0 && ratePct < 70) {
                vulnerableHabits.add(
                    VulnerableHabit(
                        key = h.key,
                        titleFa = h.titleFa,
                        ratePct = ratePct,
                        successCount = successCount,
                        totalEvaluated = activeBase,
                        icon = h.iconName
                    )
                )
            }
        }

        val needsIntervention = status == CycleStatusType.ACTIVE && (
            disciplineLevel == DisciplineLevel.CRISIS || unresolvedDebtCount >= 2
        )

        val isCourtReady = remainingDays == 0 && unresolvedDebtCount == 0 && logsCount >= 85

        val coachMessage = when {
            status == CycleStatusType.OVERLAP_ERROR -> "تداخل تقویمی │ این چرخه با یکی از چرخه‌های دیگر تداخل زمانی دارد."
            status == CycleStatusType.UPCOMING -> "در انتظار آغاز │ این دوره هنوز فعال نشده است."
            isCourtReady -> "گزارش میدان نبرد آماده است │ برای دریافت حکم نهایی به دادگاه بوشیدو مراجعه کنید."
            remainingDays == 0 -> "چرخه منقضی شده │ برای دریافت گزارش نهایی، بررسی‌های باقیمانده را انجام دهید."
            disciplineLevel == DisciplineLevel.CRISIS -> "بحران دیسیپلین │ امتیاز انضباط به پایین‌ترین سطح رسیده است. سریعاً به میدان نبرد برگردید!"
            unresolvedDebtCount >= 2 -> "نیازمند مداخله فوری │ ${NumberUtils.toPersianDigits(unresolvedDebtCount)} روز بدهی کالبدشکافی نشده وجود دارد. سیستم قفل شده است!"
            unresolvedDebtCount == 1 -> "روز بررسی نشده │ ۱ روز بررسی نشده از قبل دارید. قبل از بحرانی شدن پرونده‌اش را ببندید."
            burnedDaysCount > 0 && unresolvedDebtCount == 0 && disciplineLevel != DisciplineLevel.IRON -> "دارای پتانسیل جهش │ با تمرکز بیشتر می‌توانید به انضباط آهنین برسید."
            else -> "انضباط آهنین │ این سطح از تعهد و دیسیپلین را تا پایان چرخه ۹۰ روزه حفظ کن."
        }

        return CycleMetrics(
            cycle = cycle,
            status = status,
            statusLabelFa = statusLabelFa,
            elapsedDays = elapsedDays,
            remainingDays = remainingDays,
            logsCount = logsCount,
            standardDaysCount = standardDaysCount,
            burnedDaysCount = burnedDaysCount,
            unresolvedDebtCount = unresolvedDebtCount,
            resolvedDebtCount = resolvedDebtCount,
            frozenDaysCount = frozenDaysCount,
            inactiveDaysCount = inactiveDaysCount,
            incompleteDaysCount = incompleteDaysCount,
            totalScore = totalScore,
            disciplineScore = disciplineScore,
            disciplinePercentage = disciplinePercentage,
            disciplineLevel = disciplineLevel,
            pureStreak = currentPureStreak,
            maxPureStreak = maxPureStreak,
            inheritedStreak = inheritedStreak,
            globalLiveStreak = globalLiveStreak,
            maxGlobalStreak = maxGlobalStreak,
            maxFailureRun = maxFailureRun,
            maxInactiveRun = maxInactiveRun,
            maxIncompleteRun = maxIncompleteRun,
            dominantFailureReason = dominantFailureReason,
            dominantFailureTime = dominantFailureTime,
            vulnerableHabits = vulnerableHabits,
            needsIntervention = needsIntervention,
            isCourtReady = isCourtReady,
            coachMessage = coachMessage
        )
    }
}

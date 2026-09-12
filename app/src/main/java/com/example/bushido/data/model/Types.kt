package com.example.bushido.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

typealias HabitKey = String

object HabitKeys {
    const val WAKE_UP = "wakeUp"
    const val WORKOUT = "workout"
    const val STUDY = "study"
    const val JOURNAL = "journal"
    const val HARD_TASK = "hardTask"
}

data class HabitDef(
    val key: String,
    val title: String,
    val titleFa: String,
    val subtitleFa: String,
    val iconName: String,
    val colorHex: String
)

enum class DayStatusType {
    STANDARD,          // تعهد کامل | پایه‌ها اجرا شد 🟢
    PERSONAL_FROZEN,   // توقف اضطراری | ریتم فریز شد ❄️
    BURNED_UNRESOLVED, // کالبدشکافی نشده | بدهی باز ⚠️
    BURNED_RESOLVED    // پرونده این شکست بسته شد 🔴
}

enum class CycleStatusType {
    ACTIVE,
    UPCOMING,
    READY_FOR_COURT,
    ARCHIVED,
    OVERLAP_ERROR
}

enum class DisciplineLevel(val labelFa: String) {
    IRON("انضباط آهنین"),
    STABLE("انضباط پایدار"),
    UNSTABLE("انضباط ناپایدار"),
    CRISIS("بحران تعهد"),
    UPCOMING("آینده"),
    STRUCTURE_ERROR("خطای ساختار")
}

@Entity(tableName = "cycles")
@Serializable
data class Cycle(
    @PrimaryKey val id: String,
    val title: String,
    val startDate: String, // YYYY-MM-DD
    val endDate: String,   // YYYY-MM-DD
    val targetTheme: String,
    val rules: List<String> = emptyList(),
    val inheritedStreak: Int = 0,
    val isArchived: Boolean = false,
    val reportRead: Boolean = false,
    val revision: Int = 1
)

@Entity(tableName = "daily_logs")
@Serializable
data class DailyLog(
    @PrimaryKey val id: String,
    val cycleId: String,
    val date: String, // YYYY-MM-DD
    val createdAt: String = "",
    val revision: Int = 1,
    val isVirtual: Boolean = false,

    // Foundation 5 Core Habits
    val wakeUp: Boolean = false,
    val workout: Boolean = false,
    val study: Boolean = false,
    val journal: Boolean = false,
    val hardTask: Boolean = false,

    // Bonus Gamification
    val specialMission: Boolean = false,

    // Failure Layer (کالبدشکافی شکست)
    val failureReason: String = "",
    val failureTime: String = "",
    val autopsyNotes: String = "",
    val countermeasure: String = "",
    val aiFeedback: String = "",

    // Daily reflection notes
    val notes: String = ""
)

@Entity(tableName = "user_profile")
@Serializable
data class UserProfile(
    @PrimaryKey val id: String = "admin-master-001",
    val name: String = "فرمانده ارشد سامورایی",
    val email: String = "admin@bushido.app",
    val phoneNumber: String = "09375454050",
    val tier: String = "vip_samurai", // free or vip_samurai
    val isVip: Boolean = true,
    val isAdmin: Boolean = true,
    val vipSince: String = "",
    val vipExpiresAt: String = "",
    val paymentRefId: String = "REF-78942150",
    val activeCycleLimit: Int = 99,
    val nightOwlCutoffHour: Int = 4
)

data class VulnerableHabit(
    val key: String,
    val titleFa: String,
    val ratePct: Int,
    val successCount: Int,
    val totalEvaluated: Int,
    val icon: String
)

data class DailyComputed(
    val isStandard: Boolean,
    val habitsCount: Int,
    val score: Int,
    val statusType: DayStatusType,
    val needsAutopsy: Boolean,
    val isLockedDueToPastDebt: Boolean,
    val coachStatusLabel: String,
    val displayScore: String,
    val displayProgressBlocks: String
)

data class CycleMetrics(
    val cycle: Cycle,
    val status: CycleStatusType,
    val statusLabelFa: String,
    val elapsedDays: Int,
    val remainingDays: Int,
    val logsCount: Int,
    val standardDaysCount: Int,
    val burnedDaysCount: Int,
    val unresolvedDebtCount: Int,
    val resolvedDebtCount: Int,
    val frozenDaysCount: Int,
    val inactiveDaysCount: Int,
    val incompleteDaysCount: Int,
    val totalScore: Int,
    val disciplineScore: Double,
    val disciplinePercentage: Int,
    val disciplineLevel: DisciplineLevel,

    // Streaks
    val pureStreak: Int,
    val maxPureStreak: Int,
    val inheritedStreak: Int,
    val globalLiveStreak: Int,
    val maxGlobalStreak: Int,

    // Runs
    val maxFailureRun: Int,
    val maxInactiveRun: Int,
    val maxIncompleteRun: Int,

    // Analysis
    val dominantFailureReason: String,
    val dominantFailureTime: String,
    val vulnerableHabits: List<VulnerableHabit>,
    val needsIntervention: Boolean,
    val isCourtReady: Boolean,
    val coachMessage: String
)

data class DeterministicAutopsyResult(
    val analysis: String,
    val psychologicalTrap: String,
    val countermeasure: String,
    val tacticalActionTomorrow: String
)

data class DeterministicSenseiResult(
    val coachVerdict: String,
    val keyAdvice: String,
    val strategicWarning: String,
    val bushidoQuote: String
)

data class DeterministicCourtResult(
    val verdict: String,
    val grade: String, // A+, A, B, C, F
    val senseiNotes: String,
    val strengths: List<String>,
    val weaknesses: List<String>,
    val tacticalPlanForNextCycle: String
)

data class SubscriptionPlan(
    val id: String,
    val titleFa: String,
    val priceToman: Long,
    val formattedPrice: String,
    val durationMonths: Int,
    val badgeFa: String,
    val features: List<String>,
    val isPopular: Boolean = false
)

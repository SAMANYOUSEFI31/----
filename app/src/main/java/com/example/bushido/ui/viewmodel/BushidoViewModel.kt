package com.example.bushido.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bushido.data.model.*
import com.example.bushido.data.repository.BushidoRepository
import com.example.bushido.engine.BushidoCalculations
import com.example.bushido.engine.DateUtils
import com.example.bushido.engine.DeterministicSensei
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class BushidoUiState(
    val cycles: List<Cycle> = emptyList(),
    val logs: List<DailyLog> = emptyList(),
    val userProfile: UserProfile = UserProfile(),
    val activeCycleId: String = "",
    val selectedDate: String = DateUtils.getLogicalTodayDate(),
    val activeCycleMetrics: CycleMetrics = BushidoCalculations.createEmptyCycleMetrics(),
    val selectedLog: DailyLog = DailyLog(id = "init", cycleId = "", date = DateUtils.getLogicalTodayDate()),
    val selectedComputed: DailyComputed = DailyComputed(
        isStandard = false,
        habitsCount = 0,
        score = 0,
        statusType = DayStatusType.BURNED_UNRESOLVED,
        needsAutopsy = false,
        isLockedDueToPastDebt = false,
        coachStatusLabel = "",
        displayScore = "",
        displayProgressBlocks = ""
    ),
    val unresolvedDebtsCount: Int = 0,
    val senseiCustomAdvice: DeterministicSenseiResult? = null,
    val isAutopsyDialogOpen: Boolean = false,
    val isCreateCycleDialogOpen: Boolean = false,
    val isVipDialogOpen: Boolean = false,
    val isRulesDialogOpen: Boolean = false,
    val isResetDialogOpen: Boolean = false
)

class BushidoViewModel(private val repository: BushidoRepository) : ViewModel() {

    private val _selectedDate = MutableStateFlow(DateUtils.getLogicalTodayDate())
    private val _activeCycleId = MutableStateFlow("")
    private val _senseiCustomAdvice = MutableStateFlow<DeterministicSenseiResult?>(null)
    private val _isAutopsyOpen = MutableStateFlow(false)
    private val _isCreateCycleOpen = MutableStateFlow(false)
    private val _isVipOpen = MutableStateFlow(false)
    private val _isRulesOpen = MutableStateFlow(false)
    private val _isResetOpen = MutableStateFlow(false)

    val uiState: StateFlow<BushidoUiState> = combine(
        repository.allCycles,
        repository.allLogs,
        repository.userProfile,
        _selectedDate,
        _activeCycleId,
        _senseiCustomAdvice,
        _isAutopsyOpen,
        _isCreateCycleOpen,
        _isVipOpen,
        _isRulesOpen,
        _isResetOpen
    ) { cycles, logs, profile, selectedDate, activeId, senseiAdv, isAutopsy, isCreate, isVip, isRules, isReset ->
        val effectiveProfile = profile ?: UserProfile()
        val logicalToday = DateUtils.getLogicalTodayDate(effectiveProfile.nightOwlCutoffHour)

        val targetCycle = if (activeId.isNotBlank()) {
            cycles.find { it.id == activeId }
        } else {
            cycles.find { !it.isArchived && logicalToday >= it.startDate && logicalToday <= it.endDate }
                ?: cycles.firstOrNull { !it.isArchived }
                ?: cycles.firstOrNull()
        } ?: Cycle(
            id = "default",
            title = "بدون چرخه فعال",
            startDate = logicalToday,
            endDate = DateUtils.addDaysToDate(logicalToday, 89),
            targetTheme = "تسلط بر سحرخیزی و دیسیپلین آهنین",
            rules = listOf("سحرخیزی بی‌چون‌وچرا", "ورزش روزانه", "ثبت قبل از خواب")
        )

        val cycleMetrics = BushidoCalculations.computeCycleMetrics(
            cycle = targetCycle,
            logs = logs,
            allCycles = cycles,
            logicalToday = logicalToday
        )

        val existingLog = logs.find { it.date == selectedDate && (it.cycleId == targetCycle.id || it.cycleId.isBlank()) }
            ?: DailyLog(
                id = "log-$selectedDate",
                cycleId = targetCycle.id,
                date = selectedDate
            )

        val computed = BushidoCalculations.computeDailyProperties(
            log = existingLog,
            allCycleLogs = logs.filter { it.cycleId == targetCycle.id },
            logicalToday = logicalToday,
            cycleStartDate = targetCycle.startDate
        )

        BushidoUiState(
            cycles = cycles,
            logs = logs,
            userProfile = effectiveProfile,
            activeCycleId = targetCycle.id,
            selectedDate = selectedDate,
            activeCycleMetrics = cycleMetrics,
            selectedLog = existingLog,
            selectedComputed = computed,
            unresolvedDebtsCount = cycleMetrics.unresolvedDebtCount,
            senseiCustomAdvice = senseiAdv,
            isAutopsyDialogOpen = isAutopsy,
            isCreateCycleDialogOpen = isCreate,
            isVipDialogOpen = isVip,
            isRulesDialogOpen = isRules,
            isResetDialogOpen = isReset
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BushidoUiState()
    )

    init {
        viewModelScope.launch {
            repository.initializeSeedDataIfNeeded()
        }
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    fun selectCycle(cycleId: String) {
        _activeCycleId.value = cycleId
    }

    fun jumpToToday() {
        val cutoff = uiState.value.userProfile.nightOwlCutoffHour
        _selectedDate.value = DateUtils.getLogicalTodayDate(cutoff)
    }

    fun nextDay() {
        val next = DateUtils.addDaysToDate(_selectedDate.value, 1)
        _selectedDate.value = next
    }

    fun prevDay() {
        val prev = DateUtils.addDaysToDate(_selectedDate.value, -1)
        _selectedDate.value = prev
    }

    fun toggleHabit(habitKey: String) {
        val currentLog = uiState.value.selectedLog
        val updated = when (habitKey) {
            HabitKeys.WAKE_UP -> currentLog.copy(wakeUp = !currentLog.wakeUp)
            HabitKeys.WORKOUT -> currentLog.copy(workout = !currentLog.workout)
            HabitKeys.STUDY -> currentLog.copy(study = !currentLog.study)
            HabitKeys.JOURNAL -> currentLog.copy(journal = !currentLog.journal)
            HabitKeys.HARD_TASK -> currentLog.copy(hardTask = !currentLog.hardTask)
            else -> currentLog
        }
        viewModelScope.launch {
            repository.saveDailyLog(updated)
        }
    }

    fun toggleSpecialMission() {
        val currentLog = uiState.value.selectedLog
        val updated = currentLog.copy(specialMission = !currentLog.specialMission)
        viewModelScope.launch {
            repository.saveDailyLog(updated)
        }
    }

    fun updateNotes(notes: String) {
        val currentLog = uiState.value.selectedLog
        val updated = currentLog.copy(notes = notes)
        viewModelScope.launch {
            repository.saveDailyLog(updated)
        }
    }

    fun submitAutopsy(
        failureReason: String,
        failureTime: String,
        notes: String,
        countermeasure: String
    ) {
        val currentLog = uiState.value.selectedLog
        val missedHabits = mutableListOf<String>()
        if (!currentLog.wakeUp) missedHabits.add("سحرخیزی")
        if (!currentLog.workout) missedHabits.add("ورزش")
        if (!currentLog.study) missedHabits.add("مطالعه")
        if (!currentLog.journal) missedHabits.add("ژورنال")
        if (!currentLog.hardTask) missedHabits.add("کار سخت")

        val aiResult = DeterministicSensei.getDeterministicAutopsy(
            missedHabits = missedHabits,
            failureReason = failureReason,
            failureTime = failureTime,
            userNotes = notes
        )

        val updated = currentLog.copy(
            failureReason = failureReason,
            failureTime = failureTime,
            autopsyNotes = notes,
            countermeasure = if (countermeasure.isNotBlank()) countermeasure else aiResult.countermeasure,
            aiFeedback = aiResult.analysis
        )

        viewModelScope.launch {
            repository.saveDailyLog(updated)
            _isAutopsyOpen.value = false
        }
    }

    fun applyPersonalFreeze(notes: String) {
        val currentLog = uiState.value.selectedLog
        val updated = currentLog.copy(
            failureReason = "دلایل شخصی",
            failureTime = "وسط روز",
            autopsyNotes = if (notes.isNotBlank()) notes else "توقف اضطراری با حفظ زنجیره",
            countermeasure = "حفظ آرامش و بازگشت پرقدرت به ریتم از فردا"
        )
        viewModelScope.launch {
            repository.saveDailyLog(updated)
            _isAutopsyOpen.value = false
        }
    }

    fun createCycle(
        title: String,
        startDate: String,
        targetTheme: String,
        inheritedStreak: Int,
        rules: List<String>
    ) {
        val endDate = DateUtils.addDaysToDate(startDate, 89)
        val newCycle = Cycle(
            id = "cycle-${System.currentTimeMillis()}",
            title = title,
            startDate = startDate,
            endDate = endDate,
            targetTheme = targetTheme,
            inheritedStreak = inheritedStreak,
            rules = rules,
            isArchived = false,
            reportRead = false
        )
        viewModelScope.launch {
            repository.saveCycle(newCycle)
            _activeCycleId.value = newCycle.id
            _isCreateCycleOpen.value = false
        }
    }

    fun deleteCycle(cycleId: String) {
        viewModelScope.launch {
            repository.deleteCycle(cycleId)
        }
    }

    fun archiveCycle(cycle: Cycle) {
        viewModelScope.launch {
            repository.saveCycle(cycle.copy(isArchived = true, reportRead = true))
        }
    }

    fun updateNightOwlCutoff(hour: Int) {
        val currentProfile = uiState.value.userProfile
        viewModelScope.launch {
            repository.saveUserProfile(currentProfile.copy(nightOwlCutoffHour = hour))
        }
    }

    fun askSensei(query: String) {
        val metrics = uiState.value.activeCycleMetrics
        val advice = DeterministicSensei.getDeterministicSenseiAdvice(
            cycleTitle = metrics.cycle.title,
            elapsedDays = metrics.elapsedDays,
            remainingDays = metrics.remainingDays,
            disciplinePercentage = metrics.disciplinePercentage,
            disciplineLevel = metrics.disciplineLevel.labelFa,
            pureStreak = metrics.pureStreak,
            vulnerableHabits = metrics.vulnerableHabits,
            dominantFailureReason = metrics.dominantFailureReason,
            dominantFailureTime = metrics.dominantFailureTime,
            userQuery = query
        )
        _senseiCustomAdvice.value = advice
    }

    fun resetDatabase() {
        viewModelScope.launch {
            repository.resetAllData()
            _isResetOpen.value = false
        }
    }

    fun setAutopsyDialogOpen(open: Boolean) { _isAutopsyOpen.value = open }
    fun setCreateCycleDialogOpen(open: Boolean) { _isCreateCycleOpen.value = open }
    fun setVipDialogOpen(open: Boolean) { _isVipOpen.value = open }
    fun setRulesDialogOpen(open: Boolean) { _isRulesOpen.value = open }
    fun setResetDialogOpen(open: Boolean) { _isResetOpen.value = open }
}

class BushidoViewModelFactory(private val repository: BushidoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BushidoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BushidoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package com.example.bushido.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bushido.data.model.DayStatusType
import com.example.bushido.data.model.HabitKeys
import com.example.bushido.engine.BushidoCalculations
import com.example.bushido.engine.DateUtils
import com.example.bushido.engine.NumberUtils
import com.example.bushido.ui.components.DailyScoreWell
import com.example.bushido.ui.components.HabitCard
import com.example.bushido.ui.components.SpecialMissionCard
import com.example.bushido.ui.theme.*
import com.example.bushido.ui.viewmodel.BushidoUiState
import com.example.bushido.ui.viewmodel.BushidoViewModel

@Composable
fun BattlefieldScreen(
    state: BushidoUiState,
    viewModel: BushidoViewModel,
    modifier: Modifier = Modifier
) {
    val selectedDate = state.selectedDate
    val logicalToday = DateUtils.getLogicalTodayDate(state.userProfile.nightOwlCutoffHour)
    val isToday = selectedDate == logicalToday
    val log = state.selectedLog
    val computed = state.selectedComputed
    val isLocked = computed.isLockedDueToPastDebt

    var notesText by remember(log.id, log.notes) { mutableStateOf(log.notes) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasRoot)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Temporal Date Switcher (RTL rule: Jump to today on right, Date center, Silent navigation)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardElevated,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Next Day Button
                IconButton(
                    onClick = { viewModel.nextDay() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Next day in Persian RTL
                        contentDescription = "Next Day",
                        tint = TextPrimary
                    )
                }

                // Date Label & Relative Position
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = DateUtils.getDayLabelFa(selectedDate, logicalToday),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isToday) SamuraiAmber else TextPrimary
                    )
                    Text(
                        text = NumberUtils.toPersianDigits(selectedDate),
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                // Prev Day Button & Jump to Today
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (!isToday) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { viewModel.jumpToToday() },
                            color = SamuraiAmber.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SamuraiAmber.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "امروز",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SamuraiAmber,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = { viewModel.prevDay() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward, // Prev day in Persian RTL
                            contentDescription = "Previous Day",
                            tint = TextPrimary
                        )
                    }
                }
            }
        }

        // Locked System Alert (if past unresolved debts exist)
        if (isLocked) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = HonorCrimson.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, HonorCrimson.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = HonorCrimson,
                        modifier = Modifier.size(24.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "سیستم قفل شده است!",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = HonorCrimson,
                            textAlign = TextAlign.Right
                        )
                        Text(
                            text = "برای باز شدن امکان ثبت روز جاری، باید پرونده بدهی کالبدشکافی نشده روزهای قبل را تسویه کنید.",
                            fontSize = 11.sp,
                            color = TextPrimary,
                            textAlign = TextAlign.Right
                        )
                    }
                }
            }
        }

        // Daily Score Well Gauge
        DailyScoreWell(computed = computed)

        // 5 Foundation Habits Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${NumberUtils.toPersianDigits(computed.habitsCount)} از ۵ تکمیل شده",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (computed.isStandard) VitalityEmerald else TextSecondary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "۵ رکن فونداسیون روزانه",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Icon(
                    imageVector = Icons.Default.Checklist,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Habit Cards
        BushidoCalculations.FOUNDATION_HABITS.forEach { habit ->
            val isDone = when (habit.key) {
                HabitKeys.WAKE_UP -> log.wakeUp
                HabitKeys.WORKOUT -> log.workout
                HabitKeys.STUDY -> log.study
                HabitKeys.JOURNAL -> log.journal
                HabitKeys.HARD_TASK -> log.hardTask
                else -> false
            }
            HabitCard(
                habit = habit,
                isCompleted = isDone,
                onToggle = {
                    if (!isLocked) {
                        viewModel.toggleHabit(habit.key)
                    }
                }
            )
        }

        // Special Mission Card
        SpecialMissionCard(
            isCompleted = log.specialMission,
            onToggle = {
                if (!isLocked) {
                    viewModel.toggleSpecialMission()
                }
            }
        )

        // Autopsy / Failure Trigger Button
        if (!computed.isStandard && log.date <= logicalToday) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { viewModel.setAutopsyDialogOpen(true) },
                color = if (computed.statusType == DayStatusType.BURNED_RESOLVED) BushidoViolet.copy(alpha = 0.12f) else HonorCrimson.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (computed.statusType == DayStatusType.BURNED_RESOLVED) BushidoViolet.copy(alpha = 0.4f) else HonorCrimson.copy(alpha = 0.4f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = if (computed.statusType == DayStatusType.BURNED_RESOLVED) BushidoViolet else HonorCrimson,
                        modifier = Modifier.size(18.dp)
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (computed.statusType == DayStatusType.BURNED_RESOLVED) "ویرایش کالبدشکافی ثبت‌شده" else "کالبدشکافی افت و ثبت علت شکست",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (computed.statusType == DayStatusType.BURNED_RESOLVED) BushidoViolet else HonorCrimson
                        )
                        Text(
                            text = if (computed.statusType == DayStatusType.BURNED_RESOLVED) "پرونده بسته است — برای بازبینی کلیک کنید" else "ثبت علت عدم تکمیل ۵ پایه و قانون مقابله",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = if (computed.statusType == DayStatusType.BURNED_RESOLVED) BushidoViolet else HonorCrimson,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Daily Reflection Notes
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardElevated,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (notesText != log.notes) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { viewModel.updateNotes(notesText) },
                            color = VitalityEmerald,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "ذخیره یادداشت",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "یادداشت و روایت نبرد روزانه",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = notesText,
                    onValueChange = {
                        notesText = it
                        viewModel.updateNotes(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4,
                    placeholder = {
                        Text(
                            text = "ثبت بینش‌ها، چالش‌ها و دستاوردهای امروز...",
                            fontSize = 12.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Right
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DisciplineCrimson,
                        unfocusedBorderColor = BorderStandard,
                        focusedContainerColor = CardInner,
                        unfocusedContainerColor = CardInner,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

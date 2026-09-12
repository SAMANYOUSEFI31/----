package com.example.bushido.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bushido.engine.NumberUtils
import com.example.bushido.ui.components.HolyTrinityCards
import com.example.bushido.ui.components.PhantomDisciplineCard
import com.example.bushido.ui.components.TacticalHeatmap90
import com.example.bushido.ui.components.VulnerableHabitsCard
import com.example.bushido.ui.theme.*
import com.example.bushido.ui.viewmodel.BushidoUiState
import com.example.bushido.ui.viewmodel.BushidoViewModel

@Composable
fun CycleDashboardScreen(
    state: BushidoUiState,
    viewModel: BushidoViewModel,
    modifier: Modifier = Modifier
) {
    val metrics = state.activeCycleMetrics
    val cycle = metrics.cycle

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasRoot)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Cycle Overview Master Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardElevated,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = DisciplineCrimson.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DisciplineCrimson.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = metrics.statusLabelFa,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DisciplineCrimson,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Text(
                        text = cycle.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Right
                    )
                }

                if (cycle.targetTheme.isNotBlank()) {
                    Text(
                        text = "آرمان چرخه: ${cycle.targetTheme}",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "پایان: ${NumberUtils.toPersianDigits(cycle.endDate)}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "شروع: ${NumberUtils.toPersianDigits(cycle.startDate)}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Holy Trinity Cards (Pure streak 🔥, Standard days 🟢, Total score 🏆)
        HolyTrinityCards(
            pureStreak = metrics.pureStreak,
            standardDays = metrics.standardDaysCount,
            totalScore = metrics.totalScore
        )

        // Phantom Discipline Score Card
        PhantomDisciplineCard(metrics = metrics)

        // 90-Day Tactical Heatmap Grid
        TacticalHeatmap90(
            cycle = cycle,
            logs = state.logs,
            selectedDate = state.selectedDate,
            onSelectDate = { date ->
                viewModel.selectDate(date)
            }
        )

        // Vulnerable Habits Matrix & Failure Analysis
        VulnerableHabitsCard(
            vulnerableHabits = metrics.vulnerableHabits,
            dominantFailureReason = metrics.dominantFailureReason,
            dominantFailureTime = metrics.dominantFailureTime
        )

        // Sensei Coach Strategic Advice Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardElevated,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "پیام راهبردی سنسی دیسیپلین",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SamuraiAmber,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = metrics.coachMessage,
                        fontSize = 12.sp,
                        color = TextPrimary,
                        textAlign = TextAlign.Right,
                        lineHeight = 18.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(SamuraiAmber.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
                        .border(1.dp, SamuraiAmber.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = SamuraiAmber,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

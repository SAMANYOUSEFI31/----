package com.example.bushido.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bushido.engine.DeterministicSensei
import com.example.bushido.engine.NumberUtils
import com.example.bushido.ui.theme.*
import com.example.bushido.ui.viewmodel.BushidoUiState
import com.example.bushido.ui.viewmodel.BushidoViewModel

@Composable
fun BushidoCourtScreen(
    state: BushidoUiState,
    viewModel: BushidoViewModel,
    modifier: Modifier = Modifier
) {
    val metrics = state.activeCycleMetrics
    val cycle = metrics.cycle

    val courtResult = remember(metrics) {
        DeterministicSensei.getDeterministicCourtVerdict(
            cycleTitle = cycle.title,
            standardDays = metrics.standardDaysCount,
            totalDays = metrics.elapsedDays,
            maxStreak = metrics.maxPureStreak,
            disciplinePercentage = metrics.disciplinePercentage,
            vulnerableHabits = metrics.vulnerableHabits
        )
    }

    val gradeColor = when (courtResult.grade) {
        "A+" -> SamuraiAmber
        "A" -> VitalityEmerald
        "B" -> FrostBlue
        else -> HonorCrimson
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasRoot)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Court Seal Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardElevated,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SamuraiAmber.copy(alpha = 0.35f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Imperial Court Crest
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(SamuraiAmber.copy(alpha = 0.15f), CircleShape)
                        .border(1.5.dp, SamuraiAmber.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = null,
                        tint = SamuraiAmber,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = "دیوان عالی بوشیدو و داوری ۹۰ روزه",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "حکم رسمی ارزیابی چرخه «${cycle.title}»",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                // Grade Stamp
                Surface(
                    color = gradeColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, gradeColor.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "رتبه نهایی دیسیپلین:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = courtResult.grade,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = gradeColor
                        )
                    }
                }
            }
        }

        // Official Verdict Copy
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
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "متن رای و قضاوت دیوان",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = courtResult.verdict,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Right,
                    lineHeight = 22.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = courtResult.senseiNotes,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Right,
                    lineHeight = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Strengths & Weaknesses
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Weaknesses
            Surface(
                modifier = Modifier.weight(1f),
                color = CardElevated,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "نقاط آسیب‌پذیر",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = FieryRose,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                    courtResult.weaknesses.forEach { w ->
                        Text(
                            text = "• $w",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Strengths
            Surface(
                modifier = Modifier.weight(1f),
                color = CardElevated,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "مواضع قدرت",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = VitalityEmerald,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                    courtResult.strengths.forEach { s ->
                        Text(
                            text = "• $s",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Tactical Plan for Next Cycle
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
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "دستور کار تاکتیکی برای چرخه بعدی:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SamuraiAmber,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = courtResult.tacticalPlanForNextCycle,
                    fontSize = 12.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Right,
                    lineHeight = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Archive of All Cycles
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${NumberUtils.toPersianDigits(state.cycles.size)} چرخه ثبت‌شده",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Text(
                    text = "آرشیو چرخه‌های ۹۰ روزه",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            state.cycles.forEach { c ->
                val isSelected = c.id == cycle.id
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { viewModel.selectCycle(c.id) },
                    color = if (isSelected) DisciplineCrimson.copy(alpha = 0.12f) else CardElevated,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) DisciplineCrimson else BorderStandard
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSelected) {
                            Surface(
                                color = DisciplineCrimson,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "فعال",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        } else {
                            Text(
                                text = "مشاهده",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = c.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${NumberUtils.toPersianDigits(c.startDate)} تا ${NumberUtils.toPersianDigits(c.endDate)}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

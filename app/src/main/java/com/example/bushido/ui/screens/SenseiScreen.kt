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
import com.example.bushido.engine.DeterministicSensei
import com.example.bushido.engine.NumberUtils
import com.example.bushido.ui.theme.*
import com.example.bushido.ui.viewmodel.BushidoUiState
import com.example.bushido.ui.viewmodel.BushidoViewModel

@Composable
fun SenseiScreen(
    state: BushidoUiState,
    viewModel: BushidoViewModel,
    modifier: Modifier = Modifier
) {
    val metrics = state.activeCycleMetrics
    var customQuery by remember { mutableStateOf("") }

    val defaultAdvice = remember(metrics) {
        DeterministicSensei.getDeterministicSenseiAdvice(
            cycleTitle = metrics.cycle.title,
            elapsedDays = metrics.elapsedDays,
            remainingDays = metrics.remainingDays,
            disciplinePercentage = metrics.disciplinePercentage,
            disciplineLevel = metrics.disciplineLevel.labelFa,
            pureStreak = metrics.pureStreak,
            vulnerableHabits = metrics.vulnerableHabits,
            dominantFailureReason = metrics.dominantFailureReason,
            dominantFailureTime = metrics.dominantFailureTime
        )
    }

    val activeAdvice = state.senseiCustomAdvice ?: defaultAdvice

    val quickQuestions = listOf(
        "چگونه در سحرخیزی ثبات داشته باشم؟" to "سحرخیزی",
        "در مواجهه با خستگی بدنی چگونه ورزش کنم؟" to "ورزش",
        "برای غلبه بر تنبلی در کار سخت روز چه کنم؟" to "کار سخت"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasRoot)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Sensei Master Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardElevated,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SamuraiAmber.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "سنسی هوشمند بوشیدو",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = SamuraiAmber
                    )
                    Text(
                        text = "راهنمای راهبردی دیسیپلین، غلبه بر تله‌های شناختی و تسلط بر میدان نبرد",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Right,
                        lineHeight = 18.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(SamuraiAmber.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .border(1.dp, SamuraiAmber.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "道",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = SamuraiAmber
                    )
                }
            }
        }

        // Live Tactical Diagnosis Card
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = SamuraiAmber.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SamuraiAmber.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "تحلیل وضعیت نبرد",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SamuraiAmber,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = "حکم سنسی بر عملکرد جاری",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Text(
                    text = activeAdvice.coachVerdict,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Right,
                    lineHeight = 22.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                // Key Tactical Advice
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CardInner,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, VitalityEmerald.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "دستور عملیاتی سنسی:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = VitalityEmerald,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = activeAdvice.keyAdvice,
                            fontSize = 12.sp,
                            color = TextPrimary,
                            textAlign = TextAlign.Right,
                            lineHeight = 18.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Strategic Warning
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CardInner,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FieryRose.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "هشدار راهبردی:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = FieryRose,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = activeAdvice.strategicWarning,
                            fontSize = 12.sp,
                            color = TextPrimary,
                            textAlign = TextAlign.Right,
                            lineHeight = 18.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Quick Consultation Inquiries
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "پرسش‌های راهبردی متداول از سنسی:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )

            quickQuestions.forEach { (question, keyword) ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            customQuery = question
                            viewModel.askSensei(keyword)
                        },
                    color = CardElevated,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = null,
                            tint = SamuraiAmber,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = question,
                            fontSize = 12.sp,
                            color = TextPrimary,
                            textAlign = TextAlign.Right
                        )
                    }
                }
            }
        }

        // Custom Question Input
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
                Text(
                    text = "مشاوره اختصاصی با سنسی:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = customQuery,
                    onValueChange = { customQuery = it },
                    placeholder = {
                        Text(
                            text = "چالش یا سوال خود را مطرح کنید (مثلاً: چطور بعدازظهرها تمرکز کنم؟)...",
                            fontSize = 12.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Right
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SamuraiAmber,
                        unfocusedBorderColor = BorderStandard,
                        focusedContainerColor = CardInner,
                        unfocusedContainerColor = CardInner,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Button(
                    onClick = {
                        if (customQuery.isNotBlank()) {
                            viewModel.askSensei(customQuery)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SamuraiAmber,
                        contentColor = CanvasRoot
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "دریافت پاسخ راهبردی از سنسی",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Bushido Golden Quote Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardInner,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FormatQuote,
                    contentDescription = null,
                    tint = SamuraiAmber,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "«${activeAdvice.bushidoQuote}»",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
                Text(
                    text = "— حکمت جاودان مرام‌نامه بوشیدو",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

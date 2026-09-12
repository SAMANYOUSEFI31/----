package com.example.bushido.ui.components

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
import androidx.compose.ui.window.Dialog
import com.example.bushido.data.model.DailyLog
import com.example.bushido.engine.DateUtils
import com.example.bushido.engine.DeterministicSensei
import com.example.bushido.engine.NumberUtils
import com.example.bushido.ui.theme.*

@Composable
fun AutopsyDialog(
    log: DailyLog,
    onDismiss: () -> Unit,
    onSubmitAutopsy: (reason: String, time: String, notes: String, countermeasure: String) -> Unit,
    onApplyFreeze: (notes: String) -> Unit
) {
    var selectedReason by remember { mutableStateOf(if (log.failureReason.isNotBlank()) log.failureReason else "وقتم رو به خوبی مدیریت نکردم") }
    var selectedTime by remember { mutableStateOf(if (log.failureTime.isNotBlank()) log.failureTime else "وسط روز") }
    var notesText by remember { mutableStateOf(log.autopsyNotes) }
    var countermeasureText by remember { mutableStateOf(log.countermeasure) }

    val failureReasons = listOf(
        "وقتم رو به خوبی مدیریت نکردم",
        "نیمه‌کاره رها کردم",
        "بی‌برنامه بودم"
    )

    val failureTimes = listOf(
        "اول روز" to "صبحگاه و شروع",
        "وسط روز" to "میانه و بعدازظهر",
        "آخر روز" to "شامگاه و ساعات پایانی"
    )

    val missedHabits = remember(log) {
        mutableListOf<String>().apply {
            if (!log.wakeUp) add("سحرخیزی")
            if (!log.workout) add("ورزش")
            if (!log.study) add("مطالعه")
            if (!log.journal) add("ژورنال")
            if (!log.hardTask) add("کار سخت")
        }
    }

    val deterministicAnalysis = remember(selectedReason, selectedTime, notesText) {
        DeterministicSensei.getDeterministicAutopsy(
            missedHabits = missedHabits,
            failureReason = selectedReason,
            failureTime = selectedTime,
            userNotes = notesText
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f),
            shape = RoundedCornerShape(20.dp),
            color = CardFloating,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "بستن",
                            tint = TextSecondary
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "کالبدشکافی شکست و بازیابی تعهد",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "تاریخ: ${DateUtils.getDayLabelFa(log.date)} (${NumberUtils.toPersianDigits(log.date)})",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                HorizontalDivider(color = BorderStandard, modifier = Modifier.padding(vertical = 10.dp))

                // Scrollable Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Failure Reason Selector (Neutral & Crimson Tokens, no Amber/Emerald)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "علت اصلی افت و ناتمامی پایه‌ها:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )

                        failureReasons.forEach { reason ->
                            val isSelected = selectedReason == reason
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedReason = reason },
                                color = if (isSelected) HonorCrimson.copy(alpha = 0.15f) else CardInner,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) HonorCrimson.copy(alpha = 0.6f) else BorderStandard
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Radio pill (geometric stability 16x16dp)
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) HonorCrimson else CardFloating)
                                            .border(1.dp, if (isSelected) HonorCrimson else BorderStandard, RoundedCornerShape(8.dp))
                                    )
                                    Text(
                                        text = reason,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) HonorCrimson else TextPrimary
                                    )
                                }
                            }
                        }
                    }

                    // 2. Failure Time Selector
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "در کدام مقطع زمانی کنترل ریتم از دست رفت؟",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            failureTimes.forEach { (timeVal, timeDesc) ->
                                val isSelected = selectedTime == timeVal
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { selectedTime = timeVal },
                                    color = if (isSelected) HonorCrimson.copy(alpha = 0.15f) else CardInner,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) HonorCrimson.copy(alpha = 0.6f) else BorderStandard
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp, horizontal = 6.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = timeVal,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) HonorCrimson else TextPrimary
                                        )
                                        Text(
                                            text = timeDesc,
                                            fontSize = 9.sp,
                                            color = TextSecondary,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 3. Deterministic AI Diagnosis
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = CardInner,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BushidoViolet.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "تشخیص هوشمند تله شناختی",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BushidoViolet
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = BushidoViolet,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = deterministicAnalysis.psychologicalTrap,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                                textAlign = TextAlign.Right,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = deterministicAnalysis.analysis,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Right,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // 4. Notes Input
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "یادداشت و روایت شکست (چه عاملی مانع شد؟)",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = notesText,
                            onValueChange = { notesText = it },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 4,
                            placeholder = { Text("شرح مختصر اصطکاک رخ‌داده...", fontSize = 12.sp, color = TextMuted) },
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

                    // 5. Countermeasure Action
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "قانون مقابله برای فردا:",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = if (countermeasureText.isNotBlank()) countermeasureText else deterministicAnalysis.countermeasure,
                            onValueChange = { countermeasureText = it },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VitalityEmerald,
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

                HorizontalDivider(color = BorderStandard, modifier = Modifier.padding(vertical = 10.dp))

                // Footer Actions: Submit Autopsy & Freeze Button
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val cm = if (countermeasureText.isNotBlank()) countermeasureText else deterministicAnalysis.countermeasure
                            onSubmitAutopsy(selectedReason, selectedTime, notesText, cm)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DisciplineCrimson,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "ثبت و بستن پرونده کالبدشکافی",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = { onApplyFreeze(notesText) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = FrostBlue
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FrostBlue.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AcUnit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = FrostBlue
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "اعمال توقف اضطراری (فریز شخصی با حفظ زنجیره)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FrostBlue
                        )
                    }
                }
            }
        }
    }
}

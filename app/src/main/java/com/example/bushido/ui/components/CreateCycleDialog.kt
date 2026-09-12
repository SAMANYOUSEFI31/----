package com.example.bushido.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.example.bushido.engine.DateUtils
import com.example.bushido.engine.NumberUtils
import com.example.bushido.ui.theme.*

@Composable
fun CreateCycleDialog(
    onDismiss: () -> Unit,
    onCreateCycle: (title: String, startDate: String, targetTheme: String, inheritedStreak: Int, rules: List<String>) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(DateUtils.getLogicalTodayDate()) }
    var targetTheme by remember { mutableStateOf("") }
    var inheritedStreakText by remember { mutableStateOf("0") }
    var rule1 by remember { mutableStateOf("سحرخیزی بی‌چون‌وچرا سر ساعت مقرر") }
    var rule2 by remember { mutableStateOf("ورزش روزانه بدون لغو به بهانه خستگی") }
    var rule3 by remember { mutableStateOf("ثبت و تیک روزانه قبل از خواب") }
    var rule4 by remember { mutableStateOf("کالبدشکافی فوری در صورت هرگونه افت") }

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
                            text = "تاسیس چرخه ۹۰ روزه جدید",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "۹۰ روز انضباط آهنین و تسلط بر خود",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                HorizontalDivider(color = BorderStandard, modifier = Modifier.padding(vertical = 8.dp))

                // Form Scrollable
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Title
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "عنوان چرخه:", fontSize = 12.sp, color = TextSecondary, textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = { Text("مثال: چرخه ۲ — تسلط بر تمرکز عمیق و سحرخیزی", fontSize = 12.sp, color = TextMuted) },
                            modifier = Modifier.fillMaxWidth(),
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

                    // Start Date
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "تاریخ شروع (YYYY-MM-DD):", fontSize = 12.sp, color = TextSecondary, textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(
                            value = startDate,
                            onValueChange = { startDate = it },
                            modifier = Modifier.fillMaxWidth(),
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

                    // Target Theme
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "آرمان و تم راهبردی چرخه:", fontSize = 12.sp, color = TextSecondary, textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(
                            value = targetTheme,
                            onValueChange = { targetTheme = it },
                            placeholder = { Text("مثال: تکمیل پروژه اصلی و تثبیت روتین صبحگاهی", fontSize = 12.sp, color = TextMuted) },
                            modifier = Modifier.fillMaxWidth(),
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

                    // Inherited Streak
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "استریک موروثی از چرخه قبل (اختیاری):", fontSize = 12.sp, color = TextSecondary, textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(
                            value = inheritedStreakText,
                            onValueChange = { inheritedStreakText = it.filter { ch -> ch.isDigit() } },
                            modifier = Modifier.fillMaxWidth(),
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

                    // 4 Custom Rules
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "قوانین و مرام‌نامه چرخه (۴ قانون بوشیدو):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary, textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(
                            value = rule1,
                            onValueChange = { rule1 = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("قانون ۱", fontSize = 10.sp) },
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SamuraiAmber, unfocusedBorderColor = BorderStandard, focusedContainerColor = CardInner, unfocusedContainerColor = CardInner, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = rule2,
                            onValueChange = { rule2 = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("قانون ۲", fontSize = 10.sp) },
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SamuraiAmber, unfocusedBorderColor = BorderStandard, focusedContainerColor = CardInner, unfocusedContainerColor = CardInner, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = rule3,
                            onValueChange = { rule3 = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("قانون ۳", fontSize = 10.sp) },
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SamuraiAmber, unfocusedBorderColor = BorderStandard, focusedContainerColor = CardInner, unfocusedContainerColor = CardInner, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = rule4,
                            onValueChange = { rule4 = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("قانون ۴", fontSize = 10.sp) },
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SamuraiAmber, unfocusedBorderColor = BorderStandard, focusedContainerColor = CardInner, unfocusedContainerColor = CardInner, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }

                HorizontalDivider(color = BorderStandard, modifier = Modifier.padding(vertical = 8.dp))

                // Create Action
                Button(
                    onClick = {
                        val effectiveTitle = if (title.isNotBlank()) title else "چرخه جدید ۹۰ روزه"
                        val effectiveStreak = inheritedStreakText.toIntOrNull() ?: 0
                        val rulesList = listOf(rule1, rule2, rule3, rule4).filter { it.isNotBlank() }
                        onCreateCycle(effectiveTitle, startDate, targetTheme, effectiveStreak, rulesList)
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
                        text = "تاسیس رسمی چرخه و آغاز تعهد",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bushido.engine.NumberUtils
import com.example.bushido.ui.theme.*
import com.example.bushido.ui.viewmodel.BushidoUiState
import com.example.bushido.ui.viewmodel.BushidoViewModel

@Composable
fun SettingsScreen(
    state: BushidoUiState,
    viewModel: BushidoViewModel,
    modifier: Modifier = Modifier
) {
    val profile = state.userProfile
    val cutoffHour = profile.nightOwlCutoffHour

    val cutoffOptions = listOf(
        0 to "۱۲ نیمه‌شب (استاندارد تقویمی)",
        2 to "۲ بامداد (شب‌زنده‌دار متوسط)",
        4 to "۴ بامداد (پیش‌فرض بوشیدو)",
        5 to "۵ بامداد (جنگجوی شب)"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasRoot)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // User Identity Master Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardElevated,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = SamuraiAmber.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SamuraiAmber.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "عضو VIP بوشیدو",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SamuraiAmber,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = profile.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = profile.phoneNumber,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(DisciplineCrimson, CircleShape)
                            .border(1.5.dp, DisciplineCrimson.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "武",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Bushido Doctrine & Rules Button
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .clickable { viewModel.setRulesDialogOpen(true) },
            color = CardElevated,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "مرام‌نامه و ۴ اصل بنیادین دیسیپلین",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Night Owl Cutoff Hour Config
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
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ساعت کات‌آف شبانه (پایان روز منطقی)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Bedtime,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = "تا قبل از فرا رسیدن ساعت کات‌آف، ثبت تیک‌ها به حساب روز قبل منظور می‌گردد.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )

                cutoffOptions.forEach { (hour, label) ->
                    val isSelected = cutoffHour == hour
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { viewModel.updateNightOwlCutoff(hour) },
                        color = if (isSelected) DisciplineCrimson.copy(alpha = 0.12f) else CardInner,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) DisciplineCrimson else BorderStandard
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) DisciplineCrimson else CardFloating)
                                    .border(1.dp, if (isSelected) DisciplineCrimson else BorderStandard, RoundedCornerShape(8.dp))
                            )
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) DisciplineCrimson else TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Danger Zone: Reset Database
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardElevated,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, HonorCrimson.copy(alpha = 0.35f))
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
                        text = "منطقه پرخطر: پاکسازی داده‌ها",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = HonorCrimson
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = HonorCrimson,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = "بازنشانی کلیه چرخه‌ها، گزارش‌های روزانه و ثبت مجدد داده‌های نمونه اولیه.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedButton(
                    onClick = { viewModel.setResetDialogOpen(true) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = HonorCrimson
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HonorCrimson.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "بازنشانی پایگاه داده به حالت کارخانه",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // System Metadata
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "Bushido Discipline OS v1.0.0",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )
            Text(
                text = "سیستم‌عامل انضباط رزمی، تسلط بر اراده و خودفرمانی",
                fontSize = 10.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

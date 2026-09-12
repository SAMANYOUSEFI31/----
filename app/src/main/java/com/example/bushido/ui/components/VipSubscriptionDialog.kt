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
import com.example.bushido.engine.NumberUtils
import com.example.bushido.ui.theme.*

@Composable
fun VipSubscriptionDialog(
    onDismiss: () -> Unit
) {
    var selectedPlanId by remember { mutableStateOf("plan-6m") }

    val plans = listOf(
        Triple("plan-3m", "اشتراک سه‌ماهه سامورایی (۱ چرخه)", "۱۹۹,۰۰۰ تومان"),
        Triple("plan-6m", "اشتراک شش‌ماهه فرماندهی (۲ چرخه)", "۳۴۹,۰۰۰ تومان"),
        Triple("plan-12m", "اشتراک سالانه استادی دیسیپلین (۴ چرخه)", "۵۹۰,۰۰۰ تومان")
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(20.dp),
            color = CardFloating,
            border = androidx.compose.foundation.BorderStroke(1.dp, SamuraiAmber.copy(alpha = 0.3f))
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

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "عضویت ویژه VIP سامورایی",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = SamuraiAmber
                            )
                            Text(
                                text = "دسترسی نامحدود به سنسی هوشمند و آرشیو دیوان",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(SamuraiAmber.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                .border(1.dp, SamuraiAmber.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = SamuraiAmber,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(color = BorderStandard, modifier = Modifier.padding(vertical = 8.dp))

                // Plans List
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    plans.forEach { (id, title, price) ->
                        val isSelected = selectedPlanId == id
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedPlanId = id },
                            color = if (isSelected) SamuraiAmber.copy(alpha = 0.12f) else CardInner,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) SamuraiAmber else BorderStandard
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = price,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) SamuraiAmber else TextPrimary
                                )
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier.weight(1f).padding(horizontal = 10.dp)
                                ) {
                                    Text(
                                        text = title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        textAlign = TextAlign.Right
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) SamuraiAmber else CardFloating)
                                        .border(1.dp, if (isSelected) SamuraiAmber else BorderStandard, RoundedCornerShape(8.dp))
                                )
                            }
                        }
                    }

                    // VIP Perks Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = CardInner,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "مزایای اختصاصی حساب VIP سامورایی:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SamuraiAmber,
                                textAlign = TextAlign.Right,
                                modifier = Modifier.fillMaxWidth()
                            )
                            PerkRow("مشاوره‌های تخصصی نامحدود از سنسی دیسیپلین")
                            PerkRow("ارزیابی و احکام رسمی دادگاه عالی بوشیدو پس از هر دوره")
                            PerkRow("آرشیو بدون محدودیت چرخه‌ها و نقشه‌های حرارتی ۹۰ روزه")
                            PerkRow("همگام‌سازی ابری امن و حفظ استریک موروثی بین دوره‌ها")
                        }
                    }
                }

                HorizontalDivider(color = BorderStandard, modifier = Modifier.padding(vertical = 8.dp))

                // Action Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SamuraiAmber,
                        contentColor = CanvasRoot
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "فعال‌سازی آنی دسترسی VIP",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun PerkRow(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            color = TextPrimary,
            textAlign = TextAlign.Right
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = SamuraiAmber,
            modifier = Modifier.size(14.dp)
        )
    }
}

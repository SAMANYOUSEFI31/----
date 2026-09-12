package com.example.bushido.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bushido.data.model.CycleMetrics
import com.example.bushido.data.model.DisciplineLevel
import com.example.bushido.engine.NumberUtils
import com.example.bushido.ui.theme.*

@Composable
fun PhantomDisciplineCard(
    metrics: CycleMetrics,
    modifier: Modifier = Modifier
) {
    val pct = metrics.disciplinePercentage
    val level = metrics.disciplineLevel

    val (levelColor, levelBg, levelLabel) = when (level) {
        DisciplineLevel.IRON -> Triple(VitalityEmerald, VitalityEmerald.copy(alpha = 0.15f), "انضباط آهنین (≥۸۰٪)")
        DisciplineLevel.STABLE -> Triple(SamuraiAmber, SamuraiAmber.copy(alpha = 0.15f), "انضباط پایدار (۶۰-۷۹٪)")
        DisciplineLevel.UNSTABLE -> Triple(FieryRose, FieryRose.copy(alpha = 0.15f), "انضباط ناپایدار (۴۰-۵۹٪)")
        DisciplineLevel.CRISIS -> Triple(HonorCrimson, HonorCrimson.copy(alpha = 0.15f), "بحران تعهد (<۴۰٪)")
        else -> Triple(TextSecondary, CardInner, "در انتظار ارزیابی")
    }

    val animatedProgress by animateFloatAsState(
        targetValue = (pct / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 600),
        label = "disciplineGauge"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = CardElevated,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = levelBg,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, levelColor.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = levelLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = levelColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "شاخص دیسیپلین",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Icon(
                        imageVector = Icons.Default.Assessment,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Big Percentage Number
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "ارزیابی بر مبنای مخرج فانتوم",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Text(
                    text = "${NumberUtils.toPersianDigits(pct)}٪",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = levelColor
                )
            }

            // Linear Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(CardInner)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(levelColor)
                )
            }

            // 4-Column Sub-Metrics Grid
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CardInner,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    SubMetricItem(
                        label = "سپری شده",
                        value = "${NumberUtils.toPersianDigits(metrics.elapsedDays)} روز"
                    )
                    SubMetricItem(
                        label = "باقیمانده",
                        value = "${NumberUtils.toPersianDigits(metrics.remainingDays)} روز"
                    )
                    SubMetricItem(
                        label = "بدهی باز",
                        value = "${NumberUtils.toPersianDigits(metrics.unresolvedDebtCount)} روز",
                        color = if (metrics.unresolvedDebtCount > 0) HonorCrimson else TextPrimary
                    )
                    SubMetricItem(
                        label = "فریز شده",
                        value = "${NumberUtils.toPersianDigits(metrics.frozenDaysCount)} روز",
                        color = FrostBlue
                    )
                }
            }
        }
    }
}

@Composable
private fun SubMetricItem(
    label: String,
    value: String,
    color: Color = TextPrimary
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = TextSecondary
        )
    }
}

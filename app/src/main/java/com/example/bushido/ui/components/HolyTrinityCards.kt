package com.example.bushido.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bushido.engine.NumberUtils
import com.example.bushido.ui.theme.*

@Composable
fun HolyTrinityCards(
    pureStreak: Int,
    standardDays: Int,
    totalScore: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. Pure Continuous Streak (Flame - Fiery Rose)
        DisciplineStatCard(
            title = "استریک متوالی",
            value = "${NumberUtils.toPersianDigits(pureStreak)} روز",
            subLabel = "بدون شکست",
            icon = Icons.Default.LocalFireDepartment,
            accentColor = FieryRose,
            modifier = Modifier.weight(1f)
        )

        // 2. Standard Days (CheckCircle - Vitality Emerald)
        DisciplineStatCard(
            title = "روزهای استاندارد",
            value = "${NumberUtils.toPersianDigits(standardDays)} روز",
            subLabel = "تعهد ۵ از ۵",
            icon = Icons.Default.CheckCircle,
            accentColor = VitalityEmerald,
            modifier = Modifier.weight(1f)
        )

        // 3. Cumulative Total Score (Award/Trophy - Samurai Amber)
        DisciplineStatCard(
            title = "امتیاز انباشته",
            value = "${NumberUtils.toPersianDigits(totalScore)}",
            subLabel = "ارزش کل",
            icon = Icons.Default.EmojiEvents,
            accentColor = SamuraiAmber,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun DisciplineStatCard(
    title: String,
    value: String,
    subLabel: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = CardElevated,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Level 4 Icon Container (w-8 h-8 shrink-0 container with w-4 h-4 icon)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(accentColor.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                    .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary
            )

            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
        }
    }
}

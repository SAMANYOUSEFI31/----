package com.example.bushido.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
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
import com.example.bushido.data.model.DailyComputed
import com.example.bushido.data.model.DayStatusType
import com.example.bushido.engine.NumberUtils
import com.example.bushido.ui.theme.*

@Composable
fun DailyScoreWell(
    computed: DailyComputed,
    modifier: Modifier = Modifier
) {
    val score = computed.score
    val isTen = score == 10
    val isStandard = computed.isStandard

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row: Score & Status Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status Pill
                val (statusBg, statusBorder, statusText, statusIcon, statusLabel) = when {
                    isTen -> Tuple5(
                        SamuraiAmber.copy(alpha = 0.15f),
                        SamuraiAmber.copy(alpha = 0.4f),
                        SamuraiAmber,
                        Icons.Default.EmojiEvents,
                        "کمال تعهد ۱۰/۱۰"
                    )
                    isStandard -> Tuple5(
                        VitalityEmerald.copy(alpha = 0.15f),
                        VitalityEmerald.copy(alpha = 0.4f),
                        VitalityEmerald,
                        Icons.Default.CheckCircle,
                        "روز استاندارد ۵/۵"
                    )
                    computed.statusType == DayStatusType.PERSONAL_FROZEN -> Tuple5(
                        FrostBlue.copy(alpha = 0.15f),
                        FrostBlue.copy(alpha = 0.4f),
                        FrostBlue,
                        Icons.Default.AcUnit,
                        "توقف اضطراری (فریز)"
                    )
                    computed.statusType == DayStatusType.BURNED_RESOLVED -> Tuple5(
                        BushidoViolet.copy(alpha = 0.15f),
                        BushidoViolet.copy(alpha = 0.4f),
                        BushidoViolet,
                        Icons.Default.Shield,
                        "کالبدشکافی شده"
                    )
                    else -> Tuple5(
                        HonorCrimson.copy(alpha = 0.15f),
                        HonorCrimson.copy(alpha = 0.4f),
                        HonorCrimson,
                        Icons.Default.Warning,
                        "روز سوخته / کالبدشکافی نشده"
                    )
                }

                Surface(
                    color = statusBg,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, statusBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = statusText
                        )
                        Text(
                            text = statusLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusText
                        )
                    }
                }

                // Numeric Score Counter
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "از ۱۰",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 4.sp.value.dp)
                    )
                    Text(
                        text = NumberUtils.toPersianDigits(score),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isTen) SamuraiAmber else if (isStandard) VitalityEmerald else TextPrimary
                    )
                }
            }

            // 10-Segment Gauge Bar (Geometric Uniformity Rule)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (i in 1..10) {
                    val isActive = i <= score
                    val targetColor = when {
                        !isActive -> CardInner
                        isTen -> SamuraiAmber
                        isStandard -> VitalityEmerald
                        else -> DisciplineCrimson
                    }
                    val animatedColor by animateColorAsState(
                        targetValue = targetColor,
                        animationSpec = tween(durationMillis = 250),
                        label = "segmentColor"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(animatedColor)
                            .border(
                                1.dp,
                                if (isActive) Color.Transparent else BorderStandard,
                                RoundedCornerShape(3.dp)
                            )
                    )
                }
            }

            // Coach Guidance Feedback Label
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CardInner,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TextSecondary
                    )
                    Text(
                        text = computed.coachStatusLabel,
                        fontSize = 12.sp,
                        color = TextPrimary,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

private data class Tuple5<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)

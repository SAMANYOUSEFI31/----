package com.example.bushido.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bushido.data.model.HabitDef
import com.example.bushido.ui.theme.*

@Composable
fun HabitCard(
    habit: HabitDef,
    isCompleted: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val checkBgColor by animateColorAsState(
        targetValue = if (isCompleted) VitalityEmerald else CardInner,
        animationSpec = tween(200),
        label = "checkBgColor"
    )

    val iconVector: ImageVector = when (habit.iconName) {
        "Sun" -> Icons.Default.WbSunny
        "Dumbbell" -> Icons.Default.FitnessCenter
        "BookOpen" -> Icons.Default.MenuBook
        "PenTool" -> Icons.Default.Create
        "Briefcase" -> Icons.Default.Work
        else -> Icons.Default.Check
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onToggle() },
        color = CardElevated,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isCompleted) VitalityEmerald.copy(alpha = 0.4f) else BorderStandard
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Checkmark Toggle Button (Min 48dp touch target)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(checkBgColor)
                    .border(
                        1.dp,
                        if (isCompleted) VitalityEmerald else BorderStandard,
                        RoundedCornerShape(10.dp)
                    )
                    .clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Text Content (Persian Right-Aligned)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = habit.titleFa,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCompleted) TextPrimary else TextPrimary.copy(alpha = 0.9f),
                    textAlign = TextAlign.Right
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = habit.subtitleFa,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Right
                )
            }

            // Level 3 Icon Container (w-10 h-10 with w-5 h-5 icon)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(CardInner)
                    .border(1.dp, BorderStandard, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = habit.titleFa,
                    tint = if (isCompleted) VitalityEmerald else TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun SpecialMissionCard(
    isCompleted: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val checkBgColor by animateColorAsState(
        targetValue = if (isCompleted) SamuraiAmber else CardInner,
        animationSpec = tween(200),
        label = "specialMissionCheckBg"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onToggle() },
        color = CardElevated,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isCompleted) SamuraiAmber.copy(alpha = 0.5f) else SamuraiAmber.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Checkmark Toggle Button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(checkBgColor)
                    .border(
                        1.dp,
                        if (isCompleted) SamuraiAmber else SamuraiAmber.copy(alpha = 0.4f),
                        RoundedCornerShape(10.dp)
                    )
                    .clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Mission Completed",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Persian Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = SamuraiAmber.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SamuraiAmber.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "+۲ امتیاز کمال",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SamuraiAmber,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "ماموریت ویژه روز",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCompleted) SamuraiAmber else TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "تکمیل ۵ پایه + تسک ویژه = فتح روز ۱۰ از ۱۰",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Right
                )
            }

            // Trophy Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SamuraiAmber.copy(alpha = 0.1f))
                    .border(1.dp, SamuraiAmber.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = SamuraiAmber,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

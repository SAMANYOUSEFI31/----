package com.example.bushido.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bushido.data.model.Cycle
import com.example.bushido.data.model.DailyLog
import com.example.bushido.data.model.DayStatusType
import com.example.bushido.engine.BushidoCalculations
import com.example.bushido.engine.DateUtils
import com.example.bushido.engine.NumberUtils
import com.example.bushido.ui.theme.*

@Composable
fun TacticalHeatmap90(
    cycle: Cycle,
    logs: List<DailyLog>,
    selectedDate: String,
    onSelectDate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val logicalToday = DateUtils.getLogicalTodayDate()
    val cycleStartDate = cycle.startDate

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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "۹۰ خانه نبرد",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "نقشه تاکتیکی ۹۰ روزه",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Icon(
                        imageVector = Icons.Default.GridOn,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // 10 columns x 9 rows grid
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (row in 0 until 9) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        for (col in 0 until 10) {
                            val dayIndex = (row * 10) + col
                            val dayDate = DateUtils.addDaysToDate(cycleStartDate, dayIndex)
                            val isSelected = dayDate == selectedDate
                            val isToday = dayDate == logicalToday
                            val isFuture = dayDate > logicalToday

                            val existingLog = logs.find { it.date == dayDate && it.cycleId == cycle.id }
                            val computed = existingLog?.let {
                                BushidoCalculations.computeDailyProperties(it, logs, logicalToday, cycleStartDate)
                            }

                            val cellColor = when {
                                isFuture -> CardInner
                                computed == null -> CardInner
                                computed.isStandard -> VitalityEmerald
                                computed.statusType == DayStatusType.PERSONAL_FROZEN -> FrostBlue
                                computed.statusType == DayStatusType.BURNED_RESOLVED -> BushidoViolet
                                computed.statusType == DayStatusType.BURNED_UNRESOLVED -> HonorCrimson
                                else -> CardInner
                            }

                            val cellBorderColor = when {
                                isSelected -> SamuraiAmber
                                isToday -> Color.White
                                else -> if (isFuture) BorderStandard else cellColor.copy(alpha = 0.5f)
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(cellColor)
                                    .border(
                                        width = if (isSelected || isToday) 1.5.dp else 0.5.dp,
                                        color = cellBorderColor,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .clickable { onSelectDate(dayDate) },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .background(Color.White, RoundedCornerShape(2.dp))
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Legend Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(color = VitalityEmerald, label = "استاندارد")
                LegendItem(color = FrostBlue, label = "فریز")
                LegendItem(color = BushidoViolet, label = "کالبدشکافی")
                LegendItem(color = HonorCrimson, label = "بدهی باز")
                LegendItem(color = CardInner, label = "آینده", borderColor = BorderStandard)
            }
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    borderColor: Color = Color.Transparent
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(9.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
                .border(0.5.dp, borderColor, RoundedCornerShape(2.dp))
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = TextSecondary
        )
    }
}

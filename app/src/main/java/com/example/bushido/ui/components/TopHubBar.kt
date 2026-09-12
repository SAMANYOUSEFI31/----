package com.example.bushido.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bushido.data.model.Cycle
import com.example.bushido.engine.NumberUtils
import com.example.bushido.ui.theme.*

@Composable
fun TopHubBar(
    activeCycle: Cycle?,
    allCycles: List<Cycle>,
    pureStreak: Int,
    unresolvedDebtCount: Int,
    onSelectCycle: (String) -> Unit,
    onOpenVip: () -> Unit,
    onOpenCreateCycle: () -> Unit
) {
    var expandedCycleMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = CanvasRoot,
        border = null
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: VIP CTA & Streak Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // VIP Badge / Button
                    Button(
                        onClick = onOpenVip,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SamuraiAmber.copy(alpha = 0.15f),
                            contentColor = SamuraiAmber
                        ),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SamuraiAmber.copy(alpha = 0.3f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = SamuraiAmber
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "VIP سامورایی",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SamuraiAmber
                        )
                    }

                    // Pure Streak Badge
                    Surface(
                        modifier = Modifier.height(34.dp),
                        color = FieryRose.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FieryRose.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Live Pure Streak",
                                modifier = Modifier.size(16.dp),
                                tint = FieryRose
                            )
                            Text(
                                text = "${NumberUtils.toPersianDigits(pureStreak)} روز",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = FieryRose
                            )
                        }
                    }

                    // Debt Alert Pill (if any debt)
                    if (unresolvedDebtCount > 0) {
                        Surface(
                            modifier = Modifier.height(34.dp),
                            color = HonorCrimson.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, HonorCrimson.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .background(HonorCrimson, CircleShape)
                                )
                                Text(
                                    text = "${NumberUtils.toPersianDigits(unresolvedDebtCount)} بدهی",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HonorCrimson
                                )
                            }
                        }
                    }
                }

                // Right: Cycle Dropdown & Bushido Kanji Brand Mark
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Cycle Selector Dropdown
                    Box {
                        Surface(
                            modifier = Modifier
                                .height(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { expandedCycleMenu = true },
                            color = CardElevated,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select Cycle",
                                    modifier = Modifier.size(16.dp),
                                    tint = TextSecondary
                                )
                                Text(
                                    text = activeCycle?.title?.take(16) ?: "انتخاب چرخه",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Icon(
                                    imageVector = Icons.Default.Layers,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = TextSecondary
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = expandedCycleMenu,
                            onDismissRequest = { expandedCycleMenu = false },
                            modifier = Modifier.background(CardFloating)
                        ) {
                            allCycles.forEach { cycle ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = cycle.title,
                                            fontSize = 12.sp,
                                            color = if (cycle.id == activeCycle?.id) DisciplineCrimson else TextPrimary
                                        )
                                    },
                                    onClick = {
                                        onSelectCycle(cycle.id)
                                        expandedCycleMenu = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = if (cycle.id == activeCycle?.id) Icons.Default.Check else Icons.Default.Layers,
                                            contentDescription = null,
                                            tint = if (cycle.id == activeCycle?.id) DisciplineCrimson else TextSecondary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                )
                            }
                            HorizontalDivider(color = BorderStandard)
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "+ چرخه ۹۰ روزه جدید",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = VitalityEmerald
                                    )
                                },
                                onClick = {
                                    expandedCycleMenu = false
                                    onOpenCreateCycle()
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = VitalityEmerald,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }
                    }

                    // Bushido Brand Kanji Seal (武)
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(DisciplineCrimson, RoundedCornerShape(8.dp))
                            .border(1.dp, DisciplineCrimson.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "武",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }

            HorizontalDivider(
                color = BorderStandard,
                thickness = 1.dp
            )
        }
    }
}

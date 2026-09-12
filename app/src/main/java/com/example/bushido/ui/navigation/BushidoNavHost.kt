package com.example.bushido.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bushido.ui.components.*
import com.example.bushido.ui.screens.*
import com.example.bushido.ui.theme.*
import com.example.bushido.ui.viewmodel.BushidoViewModel

enum class BushidoTab(val titleFa: String, val icon: ImageVector) {
    BATTLEFIELD("میدان نبرد", Icons.Default.Shield),
    DASHBOARD("چرخه", Icons.Default.Assessment),
    SENSEI("سنسی", Icons.Default.Psychology),
    COURT("دیوان", Icons.Default.Gavel),
    SETTINGS("تنظیمات", Icons.Default.Settings)
}

@Composable
fun BushidoApp(
    viewModel: BushidoViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var currentTab by remember { mutableStateOf(BushidoTab.BATTLEFIELD) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CanvasRoot,
        topBar = {
            TopHubBar(
                activeCycle = state.activeCycleMetrics.cycle,
                allCycles = state.cycles,
                pureStreak = state.activeCycleMetrics.pureStreak,
                unresolvedDebtCount = state.unresolvedDebtsCount,
                onSelectCycle = { viewModel.selectCycle(it) },
                onOpenVip = { viewModel.setVipDialogOpen(true) },
                onOpenCreateCycle = { viewModel.setCreateCycleDialogOpen(true) }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = ShellChrome,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    modifier = Modifier.height(60.dp)
                ) {
                    BushidoTab.entries.forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentTab = tab },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.titleFa,
                                    tint = if (isSelected) DisciplineCrimson else TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = tab.titleFa,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) DisciplineCrimson else TextSecondary
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DisciplineCrimson,
                                selectedTextColor = DisciplineCrimson,
                                indicatorColor = DisciplineCrimson.copy(alpha = 0.12f),
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                BushidoTab.BATTLEFIELD -> BattlefieldScreen(state = state, viewModel = viewModel)
                BushidoTab.DASHBOARD -> CycleDashboardScreen(state = state, viewModel = viewModel)
                BushidoTab.SENSEI -> SenseiScreen(state = state, viewModel = viewModel)
                BushidoTab.COURT -> BushidoCourtScreen(state = state, viewModel = viewModel)
                BushidoTab.SETTINGS -> SettingsScreen(state = state, viewModel = viewModel)
            }
        }
    }

    // Modal Dialogs
    if (state.isAutopsyDialogOpen) {
        AutopsyDialog(
            log = state.selectedLog,
            onDismiss = { viewModel.setAutopsyDialogOpen(false) },
            onSubmitAutopsy = { reason, time, notes, cm ->
                viewModel.submitAutopsy(reason, time, notes, cm)
            },
            onApplyFreeze = { notes ->
                viewModel.applyPersonalFreeze(notes)
            }
        )
    }

    if (state.isCreateCycleDialogOpen) {
        CreateCycleDialog(
            onDismiss = { viewModel.setCreateCycleDialogOpen(false) },
            onCreateCycle = { title, startDate, targetTheme, inheritedStreak, rules ->
                viewModel.createCycle(title, startDate, targetTheme, inheritedStreak, rules)
            }
        )
    }

    if (state.isVipDialogOpen) {
        VipSubscriptionDialog(
            onDismiss = { viewModel.setVipDialogOpen(false) }
        )
    }

    if (state.isRulesDialogOpen) {
        DisciplineRulesDialog(
            onDismiss = { viewModel.setRulesDialogOpen(false) }
        )
    }

    if (state.isResetDialogOpen) {
        ResetConfirmationDialog(
            onDismiss = { viewModel.setResetDialogOpen(false) },
            onConfirmReset = { viewModel.resetDatabase() }
        )
    }
}

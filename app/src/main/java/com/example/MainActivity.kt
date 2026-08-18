package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SpeedMathViewModel
import com.example.ui.screens.analytics.AnalyticsScreen
import com.example.ui.screens.battle.BattleScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.learn.LearnDetailScreen
import com.example.ui.screens.learn.LearnScreen
import com.example.ui.screens.practice.PracticeCatalogScreen
import com.example.ui.screens.practice.PracticeSessionScreen
import com.example.ui.screens.practice.PracticeSummaryScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.vault.MistakeVaultScreen
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Emerald700
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate900

class MainActivity : ComponentActivity() {

    private val viewModel: SpeedMathViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val userProfile by viewModel.userProfile.collectAsState()
            val isDarkTheme = userProfile?.themeMode == "dark"

            MyApplicationTheme(darkTheme = isDarkTheme) {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: SpeedMathViewModel) {
    val userProfile by viewModel.userProfile.collectAsState()
    val allMistakes by viewModel.allMistakes.collectAsState()
    val allHistory by viewModel.allHistory.collectAsState()
    val practiceState by viewModel.practiceState.collectAsState()
    val battleState by viewModel.battleState.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val selectedLearnModule by viewModel.selectedLearnModule.collectAsState()

    val language = userProfile?.language ?: "en"

    // 1. Full Screen Overlay: Active Practice Session
    if (practiceState.isActive) {
        BackHandler { viewModel.exitPracticeSession() }
        Box(modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
            PracticeSessionScreen(
                state = practiceState,
                language = language,
                onDigit = { viewModel.onKeypadDigit(it) },
                onBackspace = { viewModel.onKeypadBackspace() },
                onClear = { viewModel.onKeypadClear() },
                onSubmit = { viewModel.submitAnswer() },
                onExit = { viewModel.exitPracticeSession() }
            )
        }
        return
    }

    // 2. Full Screen Overlay: Practice Summary Screen
    if (practiceState.isSummary && practiceState.summary != null) {
        BackHandler { viewModel.exitPracticeSession() }
        Box(modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
            PracticeSummaryScreen(
                summary = practiceState.summary!!,
                language = language,
                onPracticeAgain = {
                    val topicId = practiceState.summary?.topicId ?: "squares_1_30"
                    viewModel.startPracticeSession(topicId)
                },
                onOpenVault = {
                    viewModel.exitPracticeSession()
                    viewModel.setTab("vault")
                },
                onReturnHome = { viewModel.exitPracticeSession() }
            )
        }
        return
    }

    // 3. Full Screen Overlay: Battle Mode
    if (battleState.isBattleActive) {
        BackHandler { viewModel.exitBattle() }
        Box(modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
            BattleScreen(
                battleState = battleState,
                language = language,
                onStartBattle = { isGhost -> viewModel.startBattle(isGhost) },
                onP1Digit = { viewModel.onP1Digit(it) },
                onP1Backspace = { viewModel.onP1Backspace() },
                onP2Digit = { viewModel.onP2Digit(it) },
                onP2Backspace = { viewModel.onP2Backspace() },
                onExit = { viewModel.exitBattle() }
            )
        }
        return
    }

    // 4. Learn Detail Screen Overlay
    if (selectedLearnModule != null) {
        BackHandler { viewModel.selectLearnModule(null) }
        Box(modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
            LearnDetailScreen(
                module = selectedLearnModule!!,
                language = language,
                onBack = { viewModel.selectLearnModule(null) },
                onStartPractice = { modeId ->
                    viewModel.selectLearnModule(null)
                    viewModel.startPracticeSession(modeId)
                }
            )
        }
        return
    }

    // 5. Main Scaffold with Clean Minimalism Bottom Navigation
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        bottomBar = {
            CleanMinimalBottomNav(
                currentTab = currentTab,
                onSelectTab = { viewModel.setTab(it) },
                mistakeCount = allMistakes.size,
                language = language
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        AnimatedContent(
            targetState = currentTab,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            label = "TabContentAnimation"
        ) { tab ->
            when (tab) {
                "home" -> {
                    HomeScreen(
                        userProfile = userProfile,
                        mistakeCount = allMistakes.size,
                        onStartPractice = { topicId -> viewModel.startPracticeSession(topicId) },
                        onNavigateTab = { viewModel.setTab(it) },
                        onOpenBattle = { viewModel.startBattle(isGhost = false) },
                        onOpenVault = { viewModel.setTab("vault") }
                    )
                }
                "learn" -> {
                    LearnScreen(
                        language = language,
                        onSelectModule = { module -> viewModel.selectLearnModule(module) },
                        onSwitchToPractice = { viewModel.setTab("practice") }
                    )
                }
                "practice" -> {
                    PracticeCatalogScreen(
                        language = language,
                        onStartPractice = { modeId -> viewModel.startPracticeSession(modeId) },
                        onSwitchToLearn = { viewModel.setTab("learn") }
                    )
                }
                "vault" -> {
                    MistakeVaultScreen(
                        mistakes = allMistakes,
                        language = language,
                        onPracticeVault = { mistakes -> viewModel.startVaultPracticeSession(mistakes) },
                        onDeleteMistake = { id -> viewModel.deleteMistake(id) },
                        onClearAll = { viewModel.clearAllMistakes() }
                    )
                }
                "analytics" -> {
                    AnalyticsScreen(
                        userProfile = userProfile,
                        sessionHistory = allHistory,
                        language = language
                    )
                }
                "settings" -> {
                    SettingsScreen(
                        userProfile = userProfile,
                        onUpdateLanguage = { viewModel.updateLanguage(it) },
                        onUpdateTimer = { viewModel.updateTimerSetting(it) },
                        onUpdateDailyGoal = { viewModel.updateDailyGoal(it) },
                        onUpdateAutoSubmit = { viewModel.updateAutoSubmit(it) },
                        onSaveName = { viewModel.saveUserName(it) },
                        onResetAllProgress = { viewModel.resetAllProgress() },
                        onClearMistakes = { viewModel.clearAllMistakes() }
                    )
                }
                else -> {
                    HomeScreen(
                        userProfile = userProfile,
                        mistakeCount = allMistakes.size,
                        onStartPractice = { topicId -> viewModel.startPracticeSession(topicId) },
                        onNavigateTab = { viewModel.setTab(it) },
                        onOpenBattle = { viewModel.startBattle(isGhost = false) },
                        onOpenVault = { viewModel.setTab("vault") }
                    )
                }
            }
        }
    }
}

@Composable
private fun CleanMinimalBottomNav(
    currentTab: String,
    onSelectTab: (String) -> Unit,
    mistakeCount: Int,
    language: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        ),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val navItems = listOf(
                Triple("home", if (language == "hi") "होम" else "Home", "🏠"),
                Triple("learn", if (language == "hi") "सीखें" else "Learn", "📖"),
                Triple("practice", if (language == "hi") "अभ्यास" else "Practice", "✏️"),
                Triple("vault", if (language == "hi") "तिजोरी" else "Vault", "🗝️"),
                Triple("analytics", if (language == "hi") "आंकड़े" else "Analytics", "📊"),
                Triple("settings", if (language == "hi") "सेटिंग्स" else "Settings", "⚙️")
            )

            navItems.forEach { (tabId, label, emoji) ->
                val isSelected = currentTab == tabId

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectTab(tabId) }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .testTag("nav_tab_$tabId"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Box(contentAlignment = Alignment.TopEnd) {
                        Text(
                            text = emoji,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(2.dp)
                        )
                        if (tabId == "vault" && mistakeCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.error)
                            )
                        }
                    }

                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Slate400
                    )
                }
            }
        }
    }
}

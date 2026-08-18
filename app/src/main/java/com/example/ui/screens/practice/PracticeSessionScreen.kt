package com.example.ui.screens.practice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.FlashFeedback
import com.example.ui.PracticeSessionUiState
import com.example.ui.components.CustomKeypad
import com.example.ui.components.TimerRing
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500
import com.example.ui.theme.MistakeRed
import com.example.ui.theme.MistakeRedLight
import com.example.ui.theme.SuccessGreen

@Composable
fun PracticeSessionScreen(
    state: PracticeSessionUiState,
    language: String,
    onDigit: (String) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit,
    onSubmit: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalQuestions = state.questions.size
    val currentQ = state.questions.getOrNull(state.currentIndex)

    // Animated screen border / card flash based on answer outcome
    val flashBgColor by animateColorAsState(
        targetValue = when (state.flashState) {
            FlashFeedback.CORRECT -> SuccessGreen.copy(alpha = 0.12f)
            FlashFeedback.WRONG -> MistakeRed.copy(alpha = 0.15f)
            FlashFeedback.NONE -> Color.Transparent
        },
        animationSpec = tween(durationMillis = 150),
        label = "flash_bg"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(flashBgColor)
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 1. Top Bar: Exit button, Topic Title, Timer Ring, Live Streak Badge
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onExit,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Exit Session",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = state.topicName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (state.isRapidFire) {
                        "Rapid Fire: ${state.rapidFireRemainingSeconds}s"
                    } else {
                        "Question ${state.currentIndex + 1} of $totalQuestions"
                    },
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Streak Badge
                if (state.streak > 1) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Gold400.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Gold400)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Live Streak",
                                tint = Gold500,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${state.streak}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                color = Gold500
                            )
                        }
                    }
                }

                // Countdown Timer Ring
                TimerRing(
                    remainingSeconds = if (state.isRapidFire) state.rapidFireRemainingSeconds else state.remainingSeconds,
                    totalSeconds = if (state.isRapidFire) 60 else state.timerLimitSeconds,
                    size = 46.dp,
                    strokeWidth = 4.dp
                )
            }
        }

        // 2. Progress Indicator Dots / Bar
        if (!state.isRapidFire) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (i in 0 until totalQuestions) {
                    val isPast = i < state.currentIndex
                    val isCurrent = i == state.currentIndex
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                when {
                                    isPast -> Emerald500
                                    isCurrent -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }
                            )
                    )
                }
            }
        }

        // 3. Question & Input Hero Display
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("question_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    when (state.flashState) {
                        FlashFeedback.CORRECT -> SuccessGreen
                        FlashFeedback.WRONG -> MistakeRed
                        FlashFeedback.NONE -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    }
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 28.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Question Text (Hero Numerals)
                    Text(
                        text = currentQ?.questionText ?: "Loading...",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    // User Input Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(64.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                            .border(
                                2.dp,
                                if (state.currentInput.isNotEmpty()) MaterialTheme.colorScheme.primary else Color.Transparent,
                                RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (state.currentInput.isEmpty()) {
                                if (language == "hi") "उत्तर दर्ज करें..." else "Enter answer..."
                            } else {
                                state.currentInput
                            },
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (state.currentInput.isEmpty()) {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        )
                    }

                    // Wrong Answer Immediate Correction Banner
                    AnimatedVisibility(visible = state.flashState == FlashFeedback.WRONG) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MistakeRed.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MistakeRed)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = if (language == "hi") "सही उत्तर:" else "Correct Answer:",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MistakeRed
                                )
                                Text(
                                    text = state.flashWrongCorrectAnswer,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MistakeRed
                                )
                            }
                        }
                    }

                    // Correct Answer Checkmark Indicator
                    AnimatedVisibility(visible = state.flashState == FlashFeedback.CORRECT) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SuccessGreen.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Correct",
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = if (language == "hi") "शानदार!" else "Correct! ⚡",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Custom Speed Keypad
        val hasOperators = currentQ?.options?.isNotEmpty() == true
        CustomKeypad(
            onDigitClick = onDigit,
            onBackspaceClick = onBackspace,
            onClearClick = onClear,
            onSubmitClick = onSubmit,
            showOperatorKeys = hasOperators,
            onOperatorClick = onDigit,
            showSubmitButton = !hasOperators
        )
    }
}

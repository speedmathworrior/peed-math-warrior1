package com.example.ui.screens.battle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BattleUiState
import com.example.ui.FlashFeedback
import com.example.ui.components.CustomKeypad
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500
import com.example.ui.theme.MistakeRed
import com.example.ui.theme.SuccessGreen

@Composable
fun BattleScreen(
    battleState: BattleUiState,
    language: String,
    onStartBattle: (Boolean) -> Unit,
    onP1Digit: (String) -> Unit,
    onP1Backspace: () -> Unit,
    onP2Digit: (String) -> Unit,
    onP2Backspace: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!battleState.isBattleActive) {
        // Battle Mode Selector Menu
        BattleModePicker(
            language = language,
            onSelectPassAndPlay = { onStartBattle(false) },
            onSelectGhost = { onStartBattle(true) },
            modifier = modifier
        )
    } else if (battleState.isGhostMode) {
        // Solo Ghost Race Screen
        GhostBattleGameScreen(
            state = battleState,
            language = language,
            onDigit = onP1Digit,
            onBackspace = onP1Backspace,
            onExit = onExit
        )
    } else {
        // 2-Player Pass & Play Split Screen
        TwoPlayerSplitScreen(
            state = battleState,
            language = language,
            onP1Digit = onP1Digit,
            onP1Backspace = onP1Backspace,
            onP2Digit = onP2Digit,
            onP2Backspace = onP2Backspace,
            onExit = onExit
        )
    }

    // Victory / Game Finished Dialog
    if (battleState.isFinished) {
        AlertDialog(
            onDismissRequest = onExit,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Victory",
                        tint = Gold500,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = if (language == "hi") "युद्ध परिणाम ⚡" else "Battle Results ⚡",
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = battleState.winnerText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    if (!battleState.isGhostMode) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(text = "P1: ${battleState.p1Score} pts", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = "P2: ${battleState.p2Score} pts", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { onStartBattle(battleState.isGhostMode) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = if (language == "hi") "पुनः खेलें" else "Rematch")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = onExit) {
                    Text(text = if (language == "hi") "वापस जाएं" else "Exit")
                }
            }
        )
    }
}

@Composable
fun BattleModePicker(
    language: String,
    onSelectPassAndPlay: () -> Unit,
    onSelectGhost: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = if (language == "hi") "स्पीड मैथ युद्ध" else "Math Arena Battle",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (language == "hi") "दोस्तों के साथ आमने-सामने या अपने सर्वश्रेष्ठ रिकॉर्ड को चुनौती दें!" else "Head-to-head 2-player split screen or race against your personal best ghost pace!",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Mode Card 1: 2-Player Pass & Play
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectPassAndPlay() }
                .testTag("battle_pass_and_play"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsKabaddi,
                            contentDescription = "Pass and Play",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = "2-Player Split Screen",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Text(
                    text = if (language == "hi") "पास & प्ले (2 खिलाड़ी स्प्लिट स्क्रीन)" else "Pass & Play 2-Player Duel",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = if (language == "hi") "एक ही फोन पर आमने-सामने बैठकर 10 प्रश्नों की स्पीड रेस लगाएं!" else "Play simultaneously on the same device. First to solve correctly wins the round!",
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Mode Card 2: Ghost Pace Solo
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectGhost() }
                .testTag("battle_ghost_race"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Gold400)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Gold400.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Ghost Race",
                            tint = Gold500,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Gold500
                    ) {
                        Text(
                            text = "Beat Personal Best",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Text(
                    text = if (language == "hi") "घोस्ट रेस (सर्वश्रेष्ठ स्कोर को हराएं)" else "Ghost Solo Pace Race",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = if (language == "hi") "अपने पिछले सबसे तेज समय और सटीकता के विरुद्ध दौड़ें और नए रिकॉर्ड बनाएं!" else "Race against your personal best solving pace and break your high score limits!",
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun GhostBattleGameScreen(
    state: BattleUiState,
    language: String,
    onDigit: (String) -> Unit,
    onBackspace: () -> Unit,
    onExit: () -> Unit
) {
    val q = state.questions.getOrNull(state.p1Index)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onExit) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Exit")
            }

            Text(
                text = "Solo Ghost Race: Q ${state.p1Index + 1} / ${state.questions.size}",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Text(
                text = "Score: ${state.p1Score}",
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )
        }

        // Live Ghost Progress vs User Progress Meter
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // User progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "You", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Emerald500)
                Text(text = "${state.p1Index}/10", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth((state.p1Index.toFloat() / 10f).coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .background(Emerald500)
                )
            }

            // Ghost progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Ghost (PB Pace)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gold500)
                Text(text = "${(state.ghostProgress * 10).toInt()}/10", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(state.ghostProgress)
                        .fillMaxHeight()
                        .background(Gold500)
                )
            }
        }

        // Question Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = q?.questionText ?: "...",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = state.p1Input.ifEmpty { "..." },
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Custom Keypad
        CustomKeypad(
            onDigitClick = onDigit,
            onBackspaceClick = onBackspace,
            onClearClick = { },
            onSubmitClick = { },
            showSubmitButton = false
        )
    }
}

@Composable
fun TwoPlayerSplitScreen(
    state: BattleUiState,
    language: String,
    onP1Digit: (String) -> Unit,
    onP1Backspace: () -> Unit,
    onP2Digit: (String) -> Unit,
    onP2Backspace: () -> Unit,
    onExit: () -> Unit
) {
    val p1Q = state.questions.getOrNull(state.p1Index)
    val p2Q = state.questions.getOrNull(state.p2Index)

    Column(modifier = Modifier.fillMaxSize()) {
        // Player 1 Half (Top, Rotated 180 for face-to-face seating)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .rotate(180f)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                .padding(8.dp)
        ) {
            PlayerZone(
                playerName = "Player 1",
                score = state.p1Score,
                currentQIndex = state.p1Index,
                totalQ = state.questions.size,
                questionText = p1Q?.questionText ?: "Finished! 🎉",
                currentInput = state.p1Input,
                flashState = state.p1Flash,
                onDigit = onP1Digit,
                onBackspace = onP1Backspace
            )
        }

        // Dividing Center Line with Exit Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "  ⚡ 1v1 SPEED DUEL ⚡",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold
            )
            IconButton(onClick = onExit, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Exit", tint = Color.White)
            }
        }

        // Player 2 Half (Bottom, Normal orientation)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                .padding(8.dp)
        ) {
            PlayerZone(
                playerName = "Player 2",
                score = state.p2Score,
                currentQIndex = state.p2Index,
                totalQ = state.questions.size,
                questionText = p2Q?.questionText ?: "Finished! 🎉",
                currentInput = state.p2Input,
                flashState = state.p2Flash,
                onDigit = onP2Digit,
                onBackspace = onP2Backspace
            )
        }
    }
}

@Composable
fun PlayerZone(
    playerName: String,
    score: Int,
    currentQIndex: Int,
    totalQ: Int,
    questionText: String,
    currentInput: String,
    flashState: FlashFeedback,
    onDigit: (String) -> Unit,
    onBackspace: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$playerName (Score: $score)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = "Q ${currentQIndex + 1}/$totalQ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        // Question & Input banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = questionText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = currentInput.ifEmpty { "..." },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Compact Mini Keypad
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                MiniKey(text = "1", onClick = { onDigit("1") }, modifier = Modifier.weight(1f))
                MiniKey(text = "2", onClick = { onDigit("2") }, modifier = Modifier.weight(1f))
                MiniKey(text = "3", onClick = { onDigit("3") }, modifier = Modifier.weight(1f))
                MiniKey(text = "4", onClick = { onDigit("4") }, modifier = Modifier.weight(1f))
                MiniKey(text = "5", onClick = { onDigit("5") }, modifier = Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                MiniKey(text = "6", onClick = { onDigit("6") }, modifier = Modifier.weight(1f))
                MiniKey(text = "7", onClick = { onDigit("7") }, modifier = Modifier.weight(1f))
                MiniKey(text = "8", onClick = { onDigit("8") }, modifier = Modifier.weight(1f))
                MiniKey(text = "9", onClick = { onDigit("9") }, modifier = Modifier.weight(1f))
                MiniKey(text = "0", onClick = { onDigit("0") }, modifier = Modifier.weight(1f))
                MiniKey(text = "⌫", onClick = onBackspace, modifier = Modifier.weight(1f), isSpecial = true)
            }
        }
    }
}

@Composable
fun MiniKey(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSpecial: Boolean = false
) {
    Box(
        modifier = modifier
            .height(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSpecial) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = if (isSpecial) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface
        )
    }
}

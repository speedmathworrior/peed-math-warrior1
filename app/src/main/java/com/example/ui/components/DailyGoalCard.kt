package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math.LanguageStrings
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Emerald900
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500

@Composable
fun DailyGoalCard(
    currentStreak: Int,
    targetMinutes: Int,
    practicedSeconds: Int,
    targetTopicName: String,
    language: String = "en",
    userName: String = "Warrior",
    onStartTarget: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val totalTargetSeconds = targetMinutes * 60
    val progressRatio = (practicedSeconds.toFloat() / totalTargetSeconds.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    val practicedMinutes = practicedSeconds / 60

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_goal_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Emerald600),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Daily Goal Tag + Session Name + Streak Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = if (language == "hi") "दैनिक लक्ष्य" else "DAILY GOAL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Text(
                        text = if (language == "hi") "शुभ आरंभ" else "Shubh Aarambh",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Flame Streak Badge in white pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "🔥", fontSize = 14.sp)
                        Text(
                            text = "$currentStreak",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }

            // Time & Progress Percentage
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${practicedMinutes}m / ${targetMinutes}m session",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Text(
                        text = "${(progressRatio * 100).toInt()}% Done",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // 3-Segment Milestone Bar in Crisp White
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color.White.copy(alpha = 0.25f))
                        .padding(2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        for (i in 0 until 3) {
                            val segmentMin = i / 3f
                            val segmentMax = (i + 1) / 3f
                            val segFill = ((progressRatio - segmentMin) / (segmentMax - segmentMin)).coerceIn(0f, 1f)

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(
                                        if (segFill >= 1.0f) Color.White
                                        else if (segFill > 0f) Color.White.copy(alpha = 0.6f)
                                        else Color.Transparent
                                    )
                            )
                        }
                    }
                }
            }

            // Bottom row: Target Topic + Share Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onStartTarget() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🎯", fontSize = 12.sp)
                    }
                    Text(
                        text = "${if (language == "hi") "लक्ष्य" else "Target"}: $targetTopicName",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1
                    )
                }

                // White Share Button
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    modifier = Modifier
                        .clickable {
                            shareDailyProgress(context, userName, currentStreak, practicedMinutes, targetMinutes, progressRatio, language)
                        }
                        .testTag("share_daily_progress_button")
                ) {
                    Text(
                        text = if (language == "hi") "शेयर करें" else "Share",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Emerald700,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    )
                }
            }
        }
    }
}

private fun shareDailyProgress(
    context: Context,
    userName: String,
    streak: Int,
    practicedMinutes: Int,
    targetMinutes: Int,
    progressRatio: Float,
    language: String
) {
    val statusEmoji = if (progressRatio >= 1.0f) "🏆" else "⚡"
    val shareText = if (language == "hi") {
        """
        $statusEmoji स्पीड मैथ वॉरियर — दैनिक प्रगति $statusEmoji
        योद्धा: $userName
        🔥 स्ट्रीक: $streak दिन लगातार
        ⏱️ आज का अभ्यास: $practicedMinutes / $targetMinutes मिनट (${(progressRatio * 100).toInt()}%)
        🎯 लक्ष्य: SSC CGL, IBPS, Railways Mental Math
        
        #SpeedMathWarrior #MentalMath #CompetitiveExams #MathSprint
        """.trimIndent()
    } else {
        """
        $statusEmoji Speed Math Warrior — Daily Progress $statusEmoji
        Warrior: $userName
        🔥 Streak: $streak Days
        ⏱️ Today's Drill: $practicedMinutes / $targetMinutes mins (${(progressRatio * 100).toInt()}%)
        🎯 Target: SSC CGL, IBPS PO, RRB NTPC Mental Math Drills
        
        #SpeedMathWarrior #MentalMath #ExamAspirant #MathNinja
        """.trimIndent()
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Speed Math Warrior Daily Progress")
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(intent, "Share Progress via"))
}

package com.example.ui.screens.analytics

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SessionHistoryEntity
import com.example.data.local.UserProfileEntity
import com.example.ui.components.AccuracyTrendChart
import com.example.ui.components.TopicMasteryBar
import com.example.ui.screens.practice.MetricCard
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AnalyticsScreen(
    userProfile: UserProfileEntity?,
    sessionHistory: List<SessionHistoryEntity>,
    language: String,
    modifier: Modifier = Modifier
) {
    val profile = userProfile ?: UserProfileEntity()
    val scrollState = rememberScrollState()

    val totalPracticeSeconds = sessionHistory.sumOf { (it.totalTimeMillis / 1000).toInt() } + profile.todayPracticedSeconds
    val totalHours = totalPracticeSeconds / 3600
    val totalMins = (totalPracticeSeconds % 3600) / 60
    val totalTimeFormatted = if (totalHours > 0) "${totalHours}h ${totalMins}m" else "${totalMins}m"

    val totalQuestionsAttempted = sessionHistory.sumOf { it.totalQuestions }
    val totalCorrectAnswers = sessionHistory.sumOf { it.correctCount }

    val allTimeAccuracy = if (totalQuestionsAttempted > 0) {
        (totalCorrectAnswers.toFloat() / totalQuestionsAttempted.toFloat()) * 100f
    } else 0f

    // Accuracy points for recent 10 sessions (ordered oldest to newest)
    val recentSessions = sessionHistory.take(10).reversed()
    val accuracyPoints = recentSessions.map { it.accuracy }
    val labels = recentSessions.map {
        SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date(it.timestamp))
    }

    // Group history by topic for Topic Mastery Bars
    val topicGroups = sessionHistory.groupBy { it.topicName }
    val topicMasteries = topicGroups.map { (topic, sessions) ->
        val totalQ = sessions.sumOf { it.totalQuestions }
        val correctQ = sessions.sumOf { it.correctCount }
        val acc = if (totalQ > 0) (correctQ.toFloat() / totalQ.toFloat()) * 100f else 0f
        Triple(topic, acc, sessions.size)
    }.sortedByDescending { it.second }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = if (language == "hi") "प्रदर्शन विश्लेषण" else "Performance & Analytics",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (language == "hi") "आपकी मानसिक गणना की प्रगति" else "Real-time tracking of speed & accuracy",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Assessment,
                    contentDescription = "Analytics",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // All-Time Summary Stats (2x2 Grid)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard(
                label = if (language == "hi") "कुल अभ्यास समय" else "Total Drill Time",
                value = totalTimeFormatted,
                valueColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                label = if (language == "hi") "कुल हल किए प्रश्न" else "Questions Attempted",
                value = "$totalQuestionsAttempted",
                valueColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard(
                label = if (language == "hi") "औसत सटीकता" else "All-Time Accuracy",
                value = "${allTimeAccuracy.toInt()}%",
                valueColor = if (allTimeAccuracy >= 80f) Emerald500 else Gold500,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                label = if (language == "hi") "सर्वोच्च स्ट्रीक" else "Longest Streak",
                value = "🔥 ${profile.longestStreak}",
                valueColor = Gold500,
                modifier = Modifier.weight(1f)
            )
        }

        // Accuracy Trend Line Chart
        AccuracyTrendChart(
            accuracyPoints = accuracyPoints,
            labels = labels,
            title = if (language == "hi") "सटीकता का ग्राफ (पिछले सत्र)" else "Accuracy Trend (Recent Sessions)"
        )

        // Topic Mastery Progress Bars
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (language == "hi") "विषयवार पकड़ (Topic Mastery)" else "Topic Mastery Breakdown",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (topicMasteries.isEmpty()) {
                    Text(
                        text = if (language == "hi") "अभ्यास सत्र पूरा करने पर विषयवार डेटा यहाँ दिखेगा।" else "Complete practice drills across different topics to unlock mastery telemetry.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    topicMasteries.forEach { (topic, acc, attempts) ->
                        TopicMasteryBar(
                            topicName = topic,
                            accuracy = acc,
                            attemptsCount = attempts
                        )
                    }
                }
            }
        }

        // Exam Readiness Evaluation Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f))
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Readiness",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = if (language == "hi") "परीक्षा तैयारी मूल्यांकन" else "Exam Readiness Score",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (allTimeAccuracy >= 85f && totalQuestionsAttempted > 50) {
                            if (language == "hi") "उत्कृष्ट गति! आप SSC CGL और IBPS के क्वांट सेक्शन में शीर्ष गति प्राप्त कर सकते हैं।" else "Outstanding speed & precision! Well-prepared for SSC CGL & Banking quant sections."
                        } else {
                            if (language == "hi") "वैदिक ट्रिक्स और 1-30 वर्ग/घन का अधिक अभ्यास करें।" else "Keep practicing 1-30 squares/cubes and Vedic multiplication to boost calculation speed under pressure."
                        },
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

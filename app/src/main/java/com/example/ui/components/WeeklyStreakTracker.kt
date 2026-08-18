package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate900
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun WeeklyStreakTracker(
    completedDatesCsv: String,
    modifier: Modifier = Modifier,
    language: String = "en"
) {
    val completedSet = completedDatesCsv.split(",").map { it.trim() }.toSet()
    val cal = Calendar.getInstance()
    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // Align to Monday of current week
    cal.firstDayOfWeek = Calendar.MONDAY
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

    data class DayInfo(val letter: String, val dateNum: String, val dateStr: String, val isToday: Boolean)
    val daysOfWeek = mutableListOf<DayInfo>()
    val dayLabelsEn = listOf("M", "T", "W", "T", "F", "S", "S")
    val dayLabelsHi = listOf("सो", "मं", "बु", "गु", "शु", "श", "र")

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dayNumFormat = SimpleDateFormat("d", Locale.getDefault())

    for (i in 0 until 7) {
        val dateStr = dateFormat.format(cal.time)
        val dateNum = dayNumFormat.format(cal.time)
        val isToday = dateStr == todayStr
        val label = if (language == "hi") dayLabelsHi[i] else dayLabelsEn[i]
        daysOfWeek.add(DayInfo(label, dateNum, dateStr, isToday))
        cal.add(Calendar.DAY_OF_YEAR, 1)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Minimal Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == "hi") "साप्ताहिक स्ट्रीक" else "WEEKLY STREAK",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = Slate400
                )

                Text(
                    text = if (language == "hi") "निरंतरता ही सफलता है" else "Consistency is Key",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Emerald600
                )
            }

            // 7-Day Badges with Letter on top and Date number below
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                daysOfWeek.forEach { day ->
                    val isDone = completedSet.contains(day.dateStr)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        day.isToday && isDone -> Emerald600
                                        day.isToday -> Emerald600
                                        isDone -> Emerald100
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.letter,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    day.isToday -> Color.White
                                    isDone -> Emerald700
                                    else -> Slate400
                                }
                            )
                        }

                        Text(
                            text = day.dateNum,
                            fontSize = 10.sp,
                            fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Medium,
                            color = if (day.isToday) Slate900 else Slate400
                        )
                    }
                }
            }
        }
    }
}

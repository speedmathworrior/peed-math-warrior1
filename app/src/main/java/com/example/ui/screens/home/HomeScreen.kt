package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.WorkspacePremium
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ads.NativeAdCard
import com.example.data.local.UserProfileEntity
import com.example.math.LanguageStrings
import com.example.ui.components.DailyGoalCard
import com.example.ui.components.WeeklyStreakTracker
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Gold100
import com.example.ui.theme.Gold200
import com.example.ui.theme.Gold300
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500
import com.example.ui.theme.Gold700
import com.example.ui.theme.Gold900
import com.example.ui.theme.MistakeRed
import com.example.ui.theme.Slate900
import java.util.Calendar

@Composable
fun HomeScreen(
    userProfile: UserProfileEntity?,
    mistakeCount: Int,
    onStartPractice: (String) -> Unit,
    onNavigateTab: (String) -> Unit,
    onOpenBattle: () -> Unit,
    onOpenVault: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile = userProfile ?: UserProfileEntity()
    val language = profile.language
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. Top Header: Personalized Greeting & Aspirant Tag + Avatar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = LanguageStrings.getGreeting(language, profile.userName, currentHour),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (language == "hi") "आधा रास्ता तय, आगे बढ़ते रहें!" else "Halfway there, keep going!",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Clean Minimalist User Avatar
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (profile.userName.isNotEmpty()) profile.userName.take(1).uppercase() else "👤",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // 2. Daily Goal Card (Clean Emerald Hero Banner)
        DailyGoalCard(
            currentStreak = profile.currentStreak,
            targetMinutes = profile.dailyGoalMinutes,
            practicedSeconds = profile.todayPracticedSeconds,
            targetTopicName = profile.lastTopicName,
            userName = profile.userName,
            language = language,
            onStartTarget = { onStartPractice(profile.lastTopicId) }
        )

        // Native ad: visually integrated with the app, without using a banner.
        NativeAdCard()

        // 3. Weekly Streak Tracker (7-day row M-T-W-T-F-S-S)
        WeeklyStreakTracker(
            completedDatesCsv = profile.completedDatesCsv,
            language = language
        )

        // 4. "Resume Practice" Card (Clean Mint Container with Play Action)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onStartPractice(profile.lastTopicId) }
                .testTag("resume_practice_card"),
            shape = RoundedCornerShape(24.dp),
            color = Emerald50,
            border = androidx.compose.foundation.BorderStroke(1.dp, Emerald100)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⚡", fontSize = 20.sp)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = if (language == "hi") "अभ्यास जारी रखें" else "Resume Practice",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Text(
                            text = profile.lastTopicName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Emerald700
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Emerald600),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // 5. Two Feature Cards Side by Side: "Mistake Vault" (Slate-900) and "Battle Mode" (Amber-100)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Mistake Vault Card (Dark Minimal Slate-900)
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(145.dp)
                    .clickable { onOpenVault() }
                    .testTag("home_vault_card"),
                shape = RoundedCornerShape(24.dp),
                color = Slate900
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🛡️", fontSize = 16.sp)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = if (language == "hi") "गलती तिजोरी" else "Mistake Vault",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (mistakeCount > 0) "${mistakeCount} ${if (language == "hi") "कमजोर बिंदु" else "Weak points"}" else if (language == "hi") "सुरक्षित" else "0 Weak points",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.65f)
                        )
                    }
                }
            }

            // 1v1 Battle Card (Amber-100 Container)
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(145.dp)
                    .clickable { onOpenBattle() }
                    .testTag("home_battle_card"),
                shape = RoundedCornerShape(24.dp),
                color = Gold100,
                border = androidx.compose.foundation.BorderStroke(1.dp, Gold200)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⚔️", fontSize = 16.sp)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = if (language == "hi") "स्पीड युद्ध" else "Battle Mode",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Gold900
                        )
                        Text(
                            text = if (language == "hi") "1v1 स्पीड द्वंद्व" else "1v1 Speed Duel",
                            fontSize = 11.sp,
                            color = Gold700
                        )
                    }
                }
            }
        }

        // 6. Rapid Fire Quick Launch Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onStartPractice("rapid_fire_60s") }
                .testTag("rapid_fire_banner"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricBolt,
                            contentDescription = "Rapid Fire",
                            tint = Gold300,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = if (language == "hi") "रैपिड फायर स्पीड ड्रिल" else "Rapid Fire 60s Rush",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(
                        text = if (language == "hi") "60 सेकंड में अधिकतम सही उत्तर दें!" else "Answer as many questions as you can in 60s!",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Gold500
                ) {
                    Text(
                        text = if (language == "hi") "चुनौती ⚡" else "Play ⚡",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

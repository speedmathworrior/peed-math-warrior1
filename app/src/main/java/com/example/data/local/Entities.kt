package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mistakes")
data class MistakeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val topicId: String,
    val topicName: String,
    val question: String,
    val userAnswer: String,
    val correctAnswer: String,
    val explanation: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "session_history")
data class SessionHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val topicId: String,
    val topicName: String,
    val totalQuestions: Int,
    val correctCount: Int,
    val totalTimeMillis: Long,
    val accuracy: Float,
    val bestStreak: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val dateString: String // YYYY-MM-DD
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val userName: String = "Warrior",
    val currentStreak: Int = 1,
    val longestStreak: Int = 1,
    val lastActiveDate: String = "",
    val dailyGoalMinutes: Int = 15,
    val todayPracticedSeconds: Int = 0,
    val completedDatesCsv: String = "", // Comma-separated YYYY-MM-DD
    val language: String = "en", // "en" or "hi"
    val themeMode: String = "system", // "system", "light", "dark"
    val timerSeconds: Int = 10, // 5, 10, 15, 30, 0 (untimed)
    val autoSubmit: Boolean = true,
    val lastTopicId: String = "squares_1_25",
    val lastTopicName: String = "Squares (1-25)",
    val bestGhostTimePerQMillis: Long = 4000L,
    val bestGhostScore: Int = 9,
    val totalBattlesWon: Int = 0
)

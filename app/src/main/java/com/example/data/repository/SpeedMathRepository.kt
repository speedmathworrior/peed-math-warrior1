package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.MistakeEntity
import com.example.data.local.SessionHistoryEntity
import com.example.data.local.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SpeedMathRepository(private val database: AppDatabase) {
    val allMistakes: Flow<List<MistakeEntity>> = database.mistakeDao().getAllMistakes()
    val mistakeCount: Flow<Int> = database.mistakeDao().getMistakeCount()
    val allHistory: Flow<List<SessionHistoryEntity>> = database.historyDao().getAllHistory()
    val userProfile: Flow<UserProfileEntity?> = database.userProfileDao().getUserProfile()

    suspend fun saveMistake(mistake: MistakeEntity) {
        database.mistakeDao().insertMistake(mistake)
    }

    suspend fun deleteMistake(id: Long) {
        database.mistakeDao().deleteById(id)
    }

    suspend fun clearAllMistakes() {
        database.mistakeDao().clearAll()
    }

    suspend fun recordSession(
        topicId: String,
        topicName: String,
        totalQuestions: Int,
        correctCount: Int,
        totalTimeMillis: Long,
        bestStreak: Int,
        mistakes: List<MistakeEntity>
    ) {
        val todayStr = getTodayString()
        val accuracy = if (totalQuestions > 0) (correctCount.toFloat() / totalQuestions) * 100f else 0f

        // 1. Insert History
        val history = SessionHistoryEntity(
            topicId = topicId,
            topicName = topicName,
            totalQuestions = totalQuestions,
            correctCount = correctCount,
            totalTimeMillis = totalTimeMillis,
            accuracy = accuracy,
            bestStreak = bestStreak,
            dateString = todayStr
        )
        database.historyDao().insertHistory(history)

        // 2. Insert Mistakes
        mistakes.forEach { database.mistakeDao().insertMistake(it) }

        // 3. Update User Profile & Streak
        val existing = database.userProfileDao().getUserProfileOnce() ?: UserProfileEntity()
        val practicedSec = (totalTimeMillis / 1000).toInt()

        val isNewDay = existing.lastActiveDate != todayStr
        var streak = existing.currentStreak
        var longest = existing.longestStreak
        var todaySec = if (isNewDay) practicedSec else existing.todayPracticedSeconds + practicedSec

        val completedDatesList = existing.completedDatesCsv
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toMutableSet()

        if (isNewDay) {
            val yesterdayStr = getYesterdayString()
            streak = if (existing.lastActiveDate == yesterdayStr) {
                existing.currentStreak + 1
            } else if (existing.lastActiveDate.isEmpty()) {
                1
            } else {
                1 // streak reset
            }
            if (streak > longest) longest = streak
        }

        completedDatesList.add(todayStr)

        val updatedProfile = existing.copy(
            lastActiveDate = todayStr,
            currentStreak = streak,
            longestStreak = longest,
            todayPracticedSeconds = todaySec,
            completedDatesCsv = completedDatesList.joinToString(","),
            lastTopicId = topicId,
            lastTopicName = topicName
        )
        database.userProfileDao().saveUserProfile(updatedProfile)
    }

    suspend fun updateUserSettings(
        userName: String? = null,
        language: String? = null,
        themeMode: String? = null,
        timerSeconds: Int? = null,
        autoSubmit: Boolean? = null,
        dailyGoalMinutes: Int? = null
    ) {
        val existing = database.userProfileDao().getUserProfileOnce() ?: UserProfileEntity()
        val updated = existing.copy(
            userName = userName ?: existing.userName,
            language = language ?: existing.language,
            themeMode = themeMode ?: existing.themeMode,
            timerSeconds = timerSeconds ?: existing.timerSeconds,
            autoSubmit = autoSubmit ?: existing.autoSubmit,
            dailyGoalMinutes = dailyGoalMinutes ?: existing.dailyGoalMinutes
        )
        database.userProfileDao().saveUserProfile(updated)
    }

    suspend fun recordBattleWin() {
        val existing = database.userProfileDao().getUserProfileOnce() ?: UserProfileEntity()
        database.userProfileDao().saveUserProfile(existing.copy(totalBattlesWon = existing.totalBattlesWon + 1))
    }

    suspend fun updateGhostBest(timePerQMillis: Long, score: Int) {
        val existing = database.userProfileDao().getUserProfileOnce() ?: UserProfileEntity()
        if (score > existing.bestGhostScore || (score == existing.bestGhostScore && timePerQMillis < existing.bestGhostTimePerQMillis)) {
            database.userProfileDao().saveUserProfile(
                existing.copy(bestGhostTimePerQMillis = timePerQMillis, bestGhostScore = score)
            )
        }
    }

    suspend fun resetAllData() {
        database.mistakeDao().clearAll()
        database.historyDao().clearAllHistory()
        database.userProfileDao().saveUserProfile(UserProfileEntity())
    }

    private fun getTodayString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private fun getYesterdayString(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
    }
}

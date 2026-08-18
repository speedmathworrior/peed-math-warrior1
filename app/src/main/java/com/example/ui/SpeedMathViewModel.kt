package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.MistakeEntity
import com.example.data.local.SessionHistoryEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.PracticeMode
import com.example.data.model.PracticeQuestion
import com.example.data.model.QuestionReview
import com.example.data.model.SessionSummary
import com.example.data.model.TopicModule
import com.example.data.repository.SpeedMathRepository
import com.example.math.CatalogData
import com.example.math.QuestionGenerator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FlashFeedback {
    NONE, CORRECT, WRONG
}

data class PracticeSessionUiState(
    val isActive: Boolean = false,
    val isSummary: Boolean = false,
    val topicId: String = "",
    val topicName: String = "",
    val questions: List<PracticeQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val currentInput: String = "",
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val correctCount: Int = 0,
    val flashState: FlashFeedback = FlashFeedback.NONE,
    val flashWrongCorrectAnswer: String = "",
    val remainingSeconds: Int = 10,
    val timerLimitSeconds: Int = 10,
    val totalTimeMillis: Long = 0L,
    val isRapidFire: Boolean = false,
    val rapidFireRemainingSeconds: Int = 60,
    val reviews: List<QuestionReview> = emptyList(),
    val summary: SessionSummary? = null
)

data class BattleUiState(
    val isBattleActive: Boolean = false,
    val isFinished: Boolean = false,
    val isGhostMode: Boolean = false,
    val questions: List<PracticeQuestion> = emptyList(),
    // Player 1
    val p1Index: Int = 0,
    val p1Input: String = "",
    val p1Score: Int = 0,
    val p1Flash: FlashFeedback = FlashFeedback.NONE,
    val p1FinishedTimeMillis: Long = 0L,
    // Player 2 (for 2-player mode)
    val p2Index: Int = 0,
    val p2Input: String = "",
    val p2Score: Int = 0,
    val p2Flash: FlashFeedback = FlashFeedback.NONE,
    val p2FinishedTimeMillis: Long = 0L,
    // Ghost comparison (for solo mode)
    val ghostProgress: Float = 0f,
    val ghostScore: Int = 0,
    val winnerText: String = ""
)

class SpeedMathViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SpeedMathRepository(AppDatabase.getInstance(application))

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allMistakes: StateFlow<List<MistakeEntity>> = repository.allMistakes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allHistory: StateFlow<List<SessionHistoryEntity>> = repository.allHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Practice Session State
    private val _practiceState = MutableStateFlow(PracticeSessionUiState())
    val practiceState: StateFlow<PracticeSessionUiState> = _practiceState.asStateFlow()

    // Battle Mode State
    private val _battleState = MutableStateFlow(BattleUiState())
    val battleState: StateFlow<BattleUiState> = _battleState.asStateFlow()

    // Active Tab Navigation ("home", "learn", "practice", "vault", "analytics", "settings")
    private val _currentTab = MutableStateFlow("home")
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    // Shared Learn/Practice Catalog Switcher ("learn" or "practice")
    private val _catalogSegment = MutableStateFlow("learn")
    val catalogSegment: StateFlow<String> = _catalogSegment.asStateFlow()

    // Selected Topic for Learn Detail
    private val _selectedLearnModule = MutableStateFlow<TopicModule?>(null)
    val selectedLearnModule: StateFlow<TopicModule?> = _selectedLearnModule.asStateFlow()

    // Onboarding Name Dialog State
    private val _showNamePrompt = MutableStateFlow(false)
    val showNamePrompt: StateFlow<Boolean> = _showNamePrompt.asStateFlow()

    private var questionTimerJob: Job? = null
    private var sessionStartTimeMillis: Long = 0L

    init {
        viewModelScope.launch {
            val profile = repository.userProfile.stateIn(viewModelScope).value
            if (profile == null) {
                // Initialize default profile
                repository.updateUserSettings(userName = "Aspirant")
            }
        }
    }

    fun setTab(tab: String) {
        _currentTab.value = tab
    }

    fun setCatalogSegment(segment: String) {
        _catalogSegment.value = segment
    }

    fun selectLearnModule(module: TopicModule?) {
        _selectedLearnModule.value = module
    }

    fun showNameDialog(show: Boolean) {
        _showNamePrompt.value = show
    }

    fun saveUserName(name: String) {
        viewModelScope.launch {
            repository.updateUserSettings(userName = name.trim().ifEmpty { "Warrior" })
            _showNamePrompt.value = false
        }
    }

    fun updateLanguage(lang: String) {
        viewModelScope.launch {
            repository.updateUserSettings(language = lang)
        }
    }

    fun updateTheme(themeMode: String) {
        viewModelScope.launch {
            repository.updateUserSettings(themeMode = themeMode)
        }
    }

    fun updateTimerSetting(seconds: Int) {
        viewModelScope.launch {
            repository.updateUserSettings(timerSeconds = seconds)
        }
    }

    fun updateAutoSubmit(autoSubmit: Boolean) {
        viewModelScope.launch {
            repository.updateUserSettings(autoSubmit = autoSubmit)
        }
    }

    fun updateDailyGoal(goalMinutes: Int) {
        viewModelScope.launch {
            repository.updateUserSettings(dailyGoalMinutes = goalMinutes)
        }
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            repository.resetAllData()
        }
    }

    fun deleteMistake(id: Long) {
        viewModelScope.launch {
            repository.deleteMistake(id)
        }
    }

    fun clearAllMistakes() {
        viewModelScope.launch {
            repository.clearAllMistakes()
        }
    }

    // ==========================================
    // PRACTICE ENGINE
    // ==========================================

    fun startPracticeSession(modeId: String, customCount: Int? = null) {
        val mode = CatalogData.practiceModes.find { it.id == modeId }
        val count = customCount ?: mode?.defaultQuestionCount ?: 10
        val questions = QuestionGenerator.generateQuestions(modeId, count)
        val timerLimit = userProfile.value?.timerSeconds ?: 10
        val isRapid = modeId == "rapid_fire_60s"

        sessionStartTimeMillis = System.currentTimeMillis()

        _practiceState.value = PracticeSessionUiState(
            isActive = true,
            isSummary = false,
            topicId = modeId,
            topicName = mode?.nameEn ?: "Speed Drill",
            questions = questions,
            currentIndex = 0,
            currentInput = "",
            streak = 0,
            bestStreak = 0,
            correctCount = 0,
            flashState = FlashFeedback.NONE,
            remainingSeconds = if (isRapid) 60 else timerLimit,
            timerLimitSeconds = timerLimit,
            isRapidFire = isRapid,
            rapidFireRemainingSeconds = 60,
            reviews = emptyList(),
            summary = null
        )

        startTimerForCurrentQuestion()
    }

    fun startVaultPracticeSession(mistakes: List<MistakeEntity>) {
        if (mistakes.isEmpty()) return
        val questions = mistakes.mapIndexed { idx, m ->
            PracticeQuestion(
                id = "vault_${m.id}_$idx",
                questionText = m.question,
                answer = m.correctAnswer,
                explanation = m.explanation,
                topicId = m.topicId,
                topicName = m.topicName
            )
        }
        val timerLimit = userProfile.value?.timerSeconds ?: 10
        sessionStartTimeMillis = System.currentTimeMillis()

        _practiceState.value = PracticeSessionUiState(
            isActive = true,
            isSummary = false,
            topicId = "mistake_vault_drill",
            topicName = "Mistake Vault Drill",
            questions = questions,
            currentIndex = 0,
            currentInput = "",
            streak = 0,
            bestStreak = 0,
            correctCount = 0,
            flashState = FlashFeedback.NONE,
            remainingSeconds = timerLimit,
            timerLimitSeconds = timerLimit,
            isRapidFire = false,
            reviews = emptyList(),
            summary = null
        )

        startTimerForCurrentQuestion()
    }

    private fun startTimerForCurrentQuestion() {
        questionTimerJob?.cancel()
        val curState = _practiceState.value
        if (!curState.isActive) return

        if (curState.isRapidFire) {
            questionTimerJob = viewModelScope.launch {
                while (_practiceState.value.rapidFireRemainingSeconds > 0 && _practiceState.value.isActive) {
                    delay(1000)
                    _practiceState.value = _practiceState.value.copy(
                        rapidFireRemainingSeconds = _practiceState.value.rapidFireRemainingSeconds - 1,
                        remainingSeconds = _practiceState.value.rapidFireRemainingSeconds - 1
                    )
                }
                if (_practiceState.value.isActive) {
                    finishPracticeSession()
                }
            }
        } else if (curState.timerLimitSeconds > 0) {
            _practiceState.value = curState.copy(remainingSeconds = curState.timerLimitSeconds)
            questionTimerJob = viewModelScope.launch {
                while (_practiceState.value.remainingSeconds > 0 && _practiceState.value.isActive) {
                    delay(1000)
                    _practiceState.value = _practiceState.value.copy(
                        remainingSeconds = _practiceState.value.remainingSeconds - 1
                    )
                }
                if (_practiceState.value.isActive) {
                    // Time expired on this question -> process as timeout/wrong
                    submitAnswer(isTimeout = true)
                }
            }
        }
    }

    fun onKeypadDigit(digit: String) {
        val cur = _practiceState.value
        if (!cur.isActive || cur.flashState != FlashFeedback.NONE) return
        if (cur.currentInput.length >= 10) return

        val newInput = cur.currentInput + digit
        _practiceState.value = cur.copy(currentInput = newInput)

        // Check Auto-submit
        val autoSubmitEnabled = userProfile.value?.autoSubmit ?: true
        val targetAnswer = cur.questions.getOrNull(cur.currentIndex)?.answer ?: ""
        if (autoSubmitEnabled && newInput.length == targetAnswer.length && !targetAnswer.contains(".")) {
            submitAnswer()
        }
    }

    fun onKeypadBackspace() {
        val cur = _practiceState.value
        if (!cur.isActive || cur.flashState != FlashFeedback.NONE) return
        if (cur.currentInput.isNotEmpty()) {
            _practiceState.value = cur.copy(currentInput = cur.currentInput.dropLast(1))
        }
    }

    fun onKeypadClear() {
        val cur = _practiceState.value
        if (!cur.isActive || cur.flashState != FlashFeedback.NONE) return
        _practiceState.value = cur.copy(currentInput = "")
    }

    fun submitAnswer(isTimeout: Boolean = false) {
        val cur = _practiceState.value
        if (!cur.isActive || cur.flashState != FlashFeedback.NONE) return

        val question = cur.questions.getOrNull(cur.currentIndex) ?: return
        val userAnswer = if (isTimeout) "Time Expired" else cur.currentInput.trim()

        val isCorrect = !isTimeout && userAnswer.equals(question.answer.trim(), ignoreCase = true)

        val newStreak = if (isCorrect) cur.streak + 1 else 0
        val newBestStreak = maxOf(cur.bestStreak, newStreak)
        val newCorrectCount = if (isCorrect) cur.correctCount + 1 else cur.correctCount

        val review = QuestionReview(
            question = question.questionText,
            userAnswer = if (userAnswer.isEmpty()) "-" else userAnswer,
            correctAnswer = question.answer,
            isCorrect = isCorrect,
            explanation = question.explanation
        )
        val updatedReviews = cur.reviews + review

        questionTimerJob?.cancel()

        // Visual flash & feedback delay
        _practiceState.value = cur.copy(
            streak = newStreak,
            bestStreak = newBestStreak,
            correctCount = newCorrectCount,
            flashState = if (isCorrect) FlashFeedback.CORRECT else FlashFeedback.WRONG,
            flashWrongCorrectAnswer = if (!isCorrect) question.answer else "",
            reviews = updatedReviews
        )

        viewModelScope.launch {
            // If wrong and not timeout with empty, record to mistake vault
            if (!isCorrect) {
                repository.saveMistake(
                    MistakeEntity(
                        topicId = question.topicId,
                        topicName = question.topicName,
                        question = question.questionText,
                        userAnswer = userAnswer.ifEmpty { "None" },
                        correctAnswer = question.answer,
                        explanation = question.explanation
                    )
                )
            }

            delay(if (isCorrect) 450 else 900) // Give user time to see correct answer if wrong

            val nextIndex = cur.currentIndex + 1
            if (nextIndex < cur.questions.size && (!cur.isRapidFire || cur.rapidFireRemainingSeconds > 0)) {
                _practiceState.value = _practiceState.value.copy(
                    currentIndex = nextIndex,
                    currentInput = "",
                    flashState = FlashFeedback.NONE,
                    flashWrongCorrectAnswer = ""
                )
                startTimerForCurrentQuestion()
            } else {
                finishPracticeSession()
            }
        }
    }

    private fun finishPracticeSession() {
        questionTimerJob?.cancel()
        val cur = _practiceState.value
        val totalTime = System.currentTimeMillis() - sessionStartTimeMillis
        val totalQ = cur.reviews.size.coerceAtLeast(1)
        val accuracy = (cur.correctCount.toFloat() / totalQ.toFloat()) * 100f

        val summary = SessionSummary(
            topicId = cur.topicId,
            topicName = cur.topicName,
            totalQuestions = totalQ,
            correctCount = cur.correctCount,
            totalTimeMillis = totalTime,
            bestStreak = cur.bestStreak,
            accuracy = accuracy,
            questionReviews = cur.reviews
        )

        _practiceState.value = cur.copy(
            isActive = false,
            isSummary = true,
            flashState = FlashFeedback.NONE,
            summary = summary
        )

        // Save session history
        val mistakesToRecord = cur.reviews.filter { !it.isCorrect }.map {
            MistakeEntity(
                topicId = cur.topicId,
                topicName = cur.topicName,
                question = it.question,
                userAnswer = it.userAnswer,
                correctAnswer = it.correctAnswer,
                explanation = it.explanation
            )
        }

        viewModelScope.launch {
            repository.recordSession(
                topicId = cur.topicId,
                topicName = cur.topicName,
                totalQuestions = totalQ,
                correctCount = cur.correctCount,
                totalTimeMillis = totalTime,
                bestStreak = cur.bestStreak,
                mistakes = mistakesToRecord
            )
        }
    }

    fun exitPracticeSession() {
        questionTimerJob?.cancel()
        _practiceState.value = PracticeSessionUiState(isActive = false, isSummary = false)
    }

    // ==========================================
    // BATTLE MODE ENGINE
    // ==========================================

    fun startBattle(isGhost: Boolean = false) {
        val questions = QuestionGenerator.generateQuestions("mixed_speed_sprint", 10)
        sessionStartTimeMillis = System.currentTimeMillis()

        _battleState.value = BattleUiState(
            isBattleActive = true,
            isFinished = false,
            isGhostMode = isGhost,
            questions = questions,
            p1Index = 0,
            p1Input = "",
            p1Score = 0,
            p1Flash = FlashFeedback.NONE,
            p2Index = 0,
            p2Input = "",
            p2Score = 0,
            p2Flash = FlashFeedback.NONE,
            ghostProgress = 0f,
            ghostScore = 0,
            winnerText = ""
        )

        if (isGhost) {
            startGhostSimulation()
        }
    }

    private fun startGhostSimulation() {
        viewModelScope.launch {
            val totalQuestions = 10
            val ghostSpeedPerQ = userProfile.value?.bestGhostTimePerQMillis ?: 4000L
            val targetGhostScore = userProfile.value?.bestGhostScore ?: 8

            for (i in 1..totalQuestions) {
                delay(ghostSpeedPerQ)
                if (!_battleState.value.isBattleActive || _battleState.value.isFinished) break
                val newScore = if (i <= targetGhostScore) _battleState.value.ghostScore + 1 else _battleState.value.ghostScore
                _battleState.value = _battleState.value.copy(
                    ghostProgress = i.toFloat() / totalQuestions.toFloat(),
                    ghostScore = newScore
                )
            }
        }
    }

    fun onP1Digit(digit: String) {
        val cur = _battleState.value
        if (!cur.isBattleActive || cur.p1Flash != FlashFeedback.NONE) return
        val newInput = cur.p1Input + digit
        _battleState.value = cur.copy(p1Input = newInput)
        val target = cur.questions.getOrNull(cur.p1Index)?.answer ?: ""
        if (newInput.length == target.length) {
            submitP1Answer()
        }
    }

    fun onP1Backspace() {
        val cur = _battleState.value
        if (cur.p1Input.isNotEmpty()) {
            _battleState.value = cur.copy(p1Input = cur.p1Input.dropLast(1))
        }
    }

    fun submitP1Answer() {
        val cur = _battleState.value
        if (!cur.isBattleActive || cur.p1Index >= cur.questions.size) return
        val q = cur.questions[cur.p1Index]
        val isCorrect = cur.p1Input.trim().equals(q.answer.trim(), ignoreCase = true)
        val newScore = if (isCorrect) cur.p1Score + 1 else cur.p1Score

        _battleState.value = cur.copy(
            p1Score = newScore,
            p1Flash = if (isCorrect) FlashFeedback.CORRECT else FlashFeedback.WRONG
        )

        viewModelScope.launch {
            delay(350)
            val nextIdx = cur.p1Index + 1
            val p1Done = nextIdx >= cur.questions.size
            val finishedTime = if (p1Done) System.currentTimeMillis() - sessionStartTimeMillis else cur.p1FinishedTimeMillis

            _battleState.value = _battleState.value.copy(
                p1Index = nextIdx,
                p1Input = "",
                p1Flash = FlashFeedback.NONE,
                p1FinishedTimeMillis = finishedTime
            )

            checkBattleFinish()
        }
    }

    fun onP2Digit(digit: String) {
        val cur = _battleState.value
        if (!cur.isBattleActive || cur.p2Flash != FlashFeedback.NONE) return
        val newInput = cur.p2Input + digit
        _battleState.value = cur.copy(p2Input = newInput)
        val target = cur.questions.getOrNull(cur.p2Index)?.answer ?: ""
        if (newInput.length == target.length) {
            submitP2Answer()
        }
    }

    fun onP2Backspace() {
        val cur = _battleState.value
        if (cur.p2Input.isNotEmpty()) {
            _battleState.value = cur.copy(p2Input = cur.p2Input.dropLast(1))
        }
    }

    fun submitP2Answer() {
        val cur = _battleState.value
        if (!cur.isBattleActive || cur.p2Index >= cur.questions.size) return
        val q = cur.questions[cur.p2Index]
        val isCorrect = cur.p2Input.trim().equals(q.answer.trim(), ignoreCase = true)
        val newScore = if (isCorrect) cur.p2Score + 1 else cur.p2Score

        _battleState.value = cur.copy(
            p2Score = newScore,
            p2Flash = if (isCorrect) FlashFeedback.CORRECT else FlashFeedback.WRONG
        )

        viewModelScope.launch {
            delay(350)
            val nextIdx = cur.p2Index + 1
            val p2Done = nextIdx >= cur.questions.size
            val finishedTime = if (p2Done) System.currentTimeMillis() - sessionStartTimeMillis else cur.p2FinishedTimeMillis

            _battleState.value = _battleState.value.copy(
                p2Index = nextIdx,
                p2Input = "",
                p2Flash = FlashFeedback.NONE,
                p2FinishedTimeMillis = finishedTime
            )

            checkBattleFinish()
        }
    }

    private fun checkBattleFinish() {
        val cur = _battleState.value
        val totalQ = cur.questions.size
        val p1Done = cur.p1Index >= totalQ

        if (cur.isGhostMode) {
            if (p1Done) {
                val won = cur.p1Score > cur.ghostScore || (cur.p1Score == cur.ghostScore)
                val winner = if (won) "You Beat Your Ghost Record! ⚡" else "Ghost Record Was Faster! Try Again!"
                _battleState.value = cur.copy(isFinished = true, winnerText = winner)
                if (won) {
                    viewModelScope.launch {
                        val timePerQ = (cur.p1FinishedTimeMillis / totalQ.coerceAtLeast(1))
                        repository.updateGhostBest(timePerQ, cur.p1Score)
                        repository.recordBattleWin()
                    }
                }
            }
        } else {
            val p2Done = cur.p2Index >= totalQ
            if (p1Done && p2Done) {
                val winner = when {
                    cur.p1Score > cur.p2Score -> "Player 1 Wins! 🏆"
                    cur.p2Score > cur.p1Score -> "Player 2 Wins! 🏆"
                    cur.p1FinishedTimeMillis < cur.p2FinishedTimeMillis -> "Player 1 Wins on Speed! ⚡"
                    cur.p2FinishedTimeMillis < cur.p1FinishedTimeMillis -> "Player 2 Wins on Speed! ⚡"
                    else -> "It's an Exact Tie! 🤝"
                }
                _battleState.value = cur.copy(isFinished = true, winnerText = winner)
                viewModelScope.launch { repository.recordBattleWin() }
            }
        }
    }

    fun exitBattle() {
        _battleState.value = BattleUiState(isBattleActive = false, isFinished = false)
    }
}

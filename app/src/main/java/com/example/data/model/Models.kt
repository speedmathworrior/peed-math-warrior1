package com.example.data.model

data class PracticeQuestion(
    val id: String,
    val questionText: String,
    val answer: String,
    val explanation: String = "",
    val topicId: String,
    val topicName: String,
    val options: List<String> = emptyList() // For operator or multiple choice if applicable
)

data class LearnSection(
    val title: String,
    val explanation: String,
    val formulaOrTrick: String? = null,
    val examples: List<Pair<String, String>> = emptyList() // Question to Step-by-Step Answer
)

data class TopicModule(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val category: String,
    val summaryEn: String,
    val summaryHi: String,
    val trickFormula: String,
    val examTip: String,
    val sections: List<LearnSection>,
    val practiceModeId: String,
    val iconName: String
)

data class PracticeMode(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val category: String, // "Memory Target", "Vedic Tricks", "Advanced", "Speed Drills"
    val difficulty: String, // "Easy", "Medium", "Hard"
    val defaultQuestionCount: Int = 10,
    val iconName: String,
    val descriptionEn: String,
    val descriptionHi: String
)

data class SessionSummary(
    val topicId: String,
    val topicName: String,
    val totalQuestions: Int,
    val correctCount: Int,
    val totalTimeMillis: Long,
    val bestStreak: Int,
    val accuracy: Float,
    val questionReviews: List<QuestionReview>
)

data class QuestionReview(
    val question: String,
    val userAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val explanation: String
)

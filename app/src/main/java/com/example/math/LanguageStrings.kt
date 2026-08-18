package com.example.math

object LanguageStrings {

    fun getGreeting(lang: String, name: String, hour: Int): String {
        val timeOfDayEn = when (hour) {
            in 4..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..21 -> "Good Evening"
            else -> "Good Night"
        }
        val timeOfDayHi = when (hour) {
            in 4..11 -> "शुभ प्रभात"
            in 12..16 -> "शुभ दोपहर"
            in 17..21 -> "शुभ संध्या"
            else -> "शुभ रात्रि"
        }
        return if (lang == "hi") "$timeOfDayHi, $name ⚡" else "$timeOfDayEn, $name ⚡"
    }

    fun getMotivationalLine(lang: String, progressRatio: Float): String {
        return if (lang == "hi") {
            when {
                progressRatio >= 1.0f -> "लक्ष्य पूरा हुआ! शानदार अभ्यास, योद्धा!"
                progressRatio >= 0.66f -> "अंतिम पड़ाव पर हैं! थोड़ा और जोर लगाएं!"
                progressRatio >= 0.33f -> "आधा रास्ता तय हुआ, गति बनाए रखें!"
                else -> "शुभ आरंभ! आज का अभ्यास शुरू करें।"
            }
        } else {
            when {
                progressRatio >= 1.0f -> "Mission accomplished! Rest well, Warrior!"
                progressRatio >= 0.66f -> "Almost there! Keep the momentum going!"
                progressRatio >= 0.33f -> "Halfway there, keep pushing your speed!"
                else -> "Shubh Aarambh! Begin today's drill."
            }
        }
    }

    fun get(key: String, lang: String): String {
        val isHi = lang == "hi"
        return when (key) {
            "app_title" -> if (isHi) "स्पीड मैथ वॉरियर" else "Speed Math Warrior"
            "daily_goal" -> if (isHi) "दैनिक लक्ष्य" else "Daily Goal"
            "session_name" -> if (isHi) "शुभ आरंभ सत्र" else "Shubh Aarambh Session"
            "target" -> if (isHi) "लक्ष्य" else "Target"
            "share" -> if (isHi) "शेयर" else "Share"
            "resume_practice" -> if (isHi) "अभ्यास जारी रखें" else "Resume Practice"
            "streak" -> if (isHi) "लगातार दिन" else "Streak"
            "weekly_tracker" -> if (isHi) "साप्ताहिक निरंतरता" else "Weekly Streak Tracker"
            "mistake_vault" -> if (isHi) "गलती तिजोरी" else "Mistake Vault"
            "vault_subtitle" -> if (isHi) "कमजोरियों को ताकत बनाएं" else "Turn weaknesses into mastery"
            "battle_mode" -> if (isHi) "1v1 युद्ध मोड" else "1v1 Battle Mode"
            "battle_subtitle" -> if (isHi) "दोस्त या पिछले रिकॉर्ड से मुकाबला" else "Split-screen or Ghost race"
            "tab_home" -> if (isHi) "होम" else "Home"
            "tab_learn" -> if (isHi) "सीखें" else "Learn"
            "tab_practice" -> if (isHi) "अभ्यास" else "Practice"
            "tab_vault" -> if (isHi) "तिजोरी" else "Vault"
            "tab_analytics" -> if (isHi) "प्रगति" else "Analytics"
            "tab_settings" -> if (isHi) "सेटिंग्स" else "Settings"
            "learn_caps" -> if (isHi) "सीखें" else "Learn"
            "practice_caps" -> if (isHi) "अभ्यास" else "Practice"
            "all" -> if (isHi) "सभी" else "All"
            "filter" -> if (isHi) "फ़िल्टर" else "Filter"
            "search_hint" -> if (isHi) "मॉड्यूल खोजें..." else "Search modules..."
            "start_drill" -> if (isHi) "शुरू करें" else "Start Drill"
            "practice_now" -> if (isHi) "अभ्यास करें" else "Practice Now"
            "learn_now" -> if (isHi) "नियम सीखें" else "Learn Shortcut"
            "clear_all" -> if (isHi) "सब साफ करें" else "Clear All"
            "practice_these" -> if (isHi) "इनका अभ्यास करें" else "Practice These"
            "no_mistakes" -> if (isHi) "तिजोरी खाली है! कोई गलती दर्ज नहीं है — अभ्यास जारी रखें!" else "Vault is clear! No mistakes registered — keep practicing!"
            "accuracy" -> if (isHi) "सटीकता" else "Accuracy"
            "time_taken" -> if (isHi) "कुल समय" else "Total Time"
            "best_streak" -> if (isHi) "सर्वश्रेष्ठ स्ट्रीक" else "Best Streak"
            "avg_speed" -> if (isHi) "औसत गति" else "Avg Speed"
            "practice_again" -> if (isHi) "फिर से अभ्यास करें" else "Practice Again"
            "review_mistakes" -> if (isHi) "गलतियां देखें" else "Review Mistakes"
            "home" -> if (isHi) "होम पर जाएं" else "Return Home"
            "question" -> if (isHi) "प्रश्न" else "Question"
            "your_answer" -> if (isHi) "आपका उत्तर" else "Your Answer"
            "correct_answer" -> if (isHi) "सही उत्तर" else "Correct Answer"
            "explanation" -> if (isHi) "व्याख्या व ट्रिक" else "Explanation & Trick"
            "pass_and_play" -> if (isHi) "पास एंड प्ले (2 खिलाड़ी)" else "Pass & Play (2 Players)"
            "beat_your_best" -> if (isHi) "रिकॉर्ड तोड़ो (घोस्ट रेस)" else "Beat Your Best (Ghost Race)"
            "p1_ready" -> if (isHi) "खिलाड़ी 1" else "Player 1"
            "p2_ready" -> if (isHi) "खिलाड़ी 2" else "Player 2"
            "winner" -> if (isHi) "विजेता" else "Winner"
            "tie" -> if (isHi) "मुकाबला बराबरी पर रहा!" else "It's a Tie!"
            "settings_language" -> if (isHi) "भाषा (Language)" else "Language (भाषा)"
            "settings_theme" -> if (isHi) "थीम मोड" else "Theme Mode"
            "settings_timer" -> if (isHi) "प्रति प्रश्न समय सीमा" else "Timer per Question"
            "settings_autosubmit" -> if (isHi) "स्वतः सबमिट (अंक पूरे होने पर)" else "Auto-submit on full digits"
            "settings_daily_goal" -> if (isHi) "दैनिक लक्ष्य (मिनट)" else "Daily Practice Goal"
            "settings_username" -> if (isHi) "योद्धा का नाम" else "Warrior Name"
            "reset_data" -> if (isHi) "प्रगति रीसेट करें" else "Reset All Data"
            "reset_confirm" -> if (isHi) "क्या आप सभी प्रगति व गलतियों को मिटाना चाहते हैं?" else "Are you sure you want to reset all streaks, history and vault items?"
            "cancel" -> if (isHi) "रद्द करें" else "Cancel"
            "confirm" -> if (isHi) "स्वीकार करें" else "Confirm"
            "save" -> if (isHi) "सुरक्षित करें" else "Save"
            "total_questions" -> if (isHi) "कुल हल प्रश्न" else "Questions Attempted"
            "total_time" -> if (isHi) "कुल अभ्यास समय" else "Total Practice Time"
            "longest_streak" -> if (isHi) "सर्वोच्च स्ट्रीक" else "Longest Streak"
            "battles_won" -> if (isHi) "जीते गए युद्ध" else "Battles Won"
            "topic_mastery" -> if (isHi) "विषयवार दक्षता" else "Topic-wise Mastery"
            "accuracy_trend" -> if (isHi) "सटीकता का ग्राफ (पिछले 7 सत्र)" else "Accuracy Trend (Recent Sessions)"
            "untimed" -> if (isHi) "समय सीमा नहीं" else "Untimed"
            "seconds_short" -> if (isHi) "सेकंड" else "sec"
            "minutes_short" -> if (isHi) "मिनट" else "min"
            "exam_badge" -> if (isHi) "SSC / Banking / Railway" else "SSC / Banking / Railway"
            else -> key
        }
    }
}

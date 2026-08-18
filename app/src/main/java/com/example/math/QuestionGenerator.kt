package com.example.math

import com.example.data.model.PracticeQuestion
import kotlin.math.roundToInt
import kotlin.random.Random

object QuestionGenerator {

    fun generateQuestions(modeId: String, count: Int = 10): List<PracticeQuestion> {
        val questions = mutableListOf<PracticeQuestion>()
        for (i in 1..count) {
            questions.add(generateSingleQuestion(modeId, i))
        }
        return questions
    }

    private fun generateSingleQuestion(modeId: String, index: Int): PracticeQuestion {
        val qId = "${modeId}_${System.currentTimeMillis()}_$index"

        return when (modeId) {
            "squares_1_25" -> {
                val n = Random.nextInt(1, 26)
                PracticeQuestion(
                    id = qId,
                    questionText = "$n² = ?",
                    answer = "${n * n}",
                    explanation = "$n × $n = ${n * n}",
                    topicId = modeId,
                    topicName = "Squares (1-25)"
                )
            }
            "squares_26_50" -> {
                val n = Random.nextInt(26, 51)
                val diff = 50 - n
                val prefix = 25 - diff
                val suffix = String.format("%02d", diff * diff)
                PracticeQuestion(
                    id = qId,
                    questionText = "$n² = ?",
                    answer = "${n * n}",
                    explanation = "Base 50 Trick: (25 - $diff) | $diff² = $prefix$suffix = ${n * n}",
                    topicId = modeId,
                    topicName = "Squares (26-50)"
                )
            }
            "squares_51_100" -> {
                val n = Random.nextInt(51, 100)
                val isBelow100 = n < 100
                val diff = if (n >= 90) 100 - n else n - 50
                val exp = if (n >= 90) "Base 100 Trick: ($n - $diff) | $diff² = ${n * n}" else "Base 50 Trick: (25 + $diff) | $diff² = ${n * n}"
                PracticeQuestion(
                    id = qId,
                    questionText = "$n² = ?",
                    answer = "${n * n}",
                    explanation = exp,
                    topicId = modeId,
                    topicName = "Squares (51-100)"
                )
            }
            "cubes_1_15" -> {
                val n = Random.nextInt(1, 16)
                PracticeQuestion(
                    id = qId,
                    questionText = "$n³ = ?",
                    answer = "${n * n * n}",
                    explanation = "$n × $n × $n = ${n * n * n}",
                    topicId = modeId,
                    topicName = "Cubes (1-15)"
                )
            }
            "cubes_16_30" -> {
                val n = Random.nextInt(16, 31)
                PracticeQuestion(
                    id = qId,
                    questionText = "$n³ = ?",
                    answer = "${n * n * n}",
                    explanation = "$n × $n × $n = ${n * n * n}",
                    topicId = modeId,
                    topicName = "Cubes (16-30)"
                )
            }
            "square_roots_perfect" -> {
                val n = Random.nextInt(11, 100)
                val sq = n * n
                PracticeQuestion(
                    id = qId,
                    questionText = "√$sq = ?",
                    answer = "$n",
                    explanation = "Since $n² = $sq, √$sq = $n",
                    topicId = modeId,
                    topicName = "Square Roots"
                )
            }
            "cube_roots_instant" -> {
                val n = Random.nextInt(5, 50)
                val cb = n * n * n
                PracticeQuestion(
                    id = qId,
                    questionText = "∛$cb = ?",
                    answer = "$n",
                    explanation = "Unit digit of $cb determines last digit. Root = $n",
                    topicId = modeId,
                    topicName = "Cube Roots"
                )
            }
            "tables_12_20" -> {
                val a = Random.nextInt(12, 21)
                val b = Random.nextInt(3, 10)
                PracticeQuestion(
                    id = qId,
                    questionText = "$a × $b = ?",
                    answer = "${a * b}",
                    explanation = "Breakdown: ($a × $b) = (${a - a % 10} × $b) + (${a % 10} × $b) = ${a * b}",
                    topicId = modeId,
                    topicName = "Tables 12-20"
                )
            }
            "tables_21_30" -> {
                val a = Random.nextInt(21, 31)
                val b = Random.nextInt(3, 10)
                PracticeQuestion(
                    id = qId,
                    questionText = "$a × $b = ?",
                    answer = "${a * b}",
                    explanation = "Breakdown: ($a × $b) = (${a - a % 10} × $b) + (${a % 10} × $b) = ${a * b}",
                    topicId = modeId,
                    topicName = "Tables 21-30"
                )
            }
            "vedic_end_5" -> {
                val tens = Random.nextInt(1, 15)
                val n = tens * 10 + 5
                val prefix = tens * (tens + 1)
                PracticeQuestion(
                    id = qId,
                    questionText = "$n² = ?",
                    answer = "${n * n}",
                    explanation = "Ekadhikena Trick: $tens × ($tens + 1) = $prefix, append 25 -> $prefix" + "25 = ${n * n}",
                    topicId = modeId,
                    topicName = "Vedic: Squares ending in 5"
                )
            }
            "vedic_base_100" -> {
                val diff1 = Random.nextInt(1, 9)
                val diff2 = Random.nextInt(1, 9)
                val mode = Random.nextInt(2) // 0: below 100, 1: above 100
                if (mode == 0) {
                    val a = 100 - diff1
                    val b = 100 - diff2
                    val left = a - diff2
                    val right = String.format("%02d", diff1 * diff2)
                    PracticeQuestion(
                        id = qId,
                        questionText = "$a × $b = ?",
                        answer = "${a * b}",
                        explanation = "Nikhilam Base 100: ($a - $diff2) | ($diff1 × $diff2) = $left$right",
                        topicId = modeId,
                        topicName = "Vedic: Base 100"
                    )
                } else {
                    val a = 100 + diff1
                    val b = 100 + diff2
                    val left = a + diff2
                    val right = String.format("%02d", diff1 * diff2)
                    PracticeQuestion(
                        id = qId,
                        questionText = "$a × $b = ?",
                        answer = "${a * b}",
                        explanation = "Nikhilam Base 100: ($a + $diff2) | ($diff1 × $diff2) = $left$right",
                        topicId = modeId,
                        topicName = "Vedic: Base 100"
                    )
                }
            }
            "vedic_mult_11" -> {
                val n = Random.nextInt(12, 99)
                val d1 = n / 10
                val d2 = n % 10
                val mid = d1 + d2
                PracticeQuestion(
                    id = qId,
                    questionText = "$n × 11 = ?",
                    answer = "${n * 11}",
                    explanation = "Vedic 11 Trick: $d1 | ($d1 + $d2 = $mid) | $d2 = ${n * 11}",
                    topicId = modeId,
                    topicName = "Vedic: Multiply by 11"
                )
            }
            "vedic_crosswise_2x2" -> {
                val a = Random.nextInt(14, 45)
                val b = Random.nextInt(12, 35)
                PracticeQuestion(
                    id = qId,
                    questionText = "$a × $b = ?",
                    answer = "${a * b}",
                    explanation = "Vertically & Crosswise: (${a/10}×${b/10}) | (${a/10}×${b%10} + ${a%10}×${b/10}) | (${a%10}×${b%10}) = ${a * b}",
                    topicId = modeId,
                    topicName = "Vedic: 2x2 Multiplication"
                )
            }
            "percentage_fractions" -> {
                val fractions = listOf(
                    Triple("1/2", "50", "1/2 = 50%"),
                    Triple("1/3", "33.33", "1/3 = 33.33% or 33 1/3%"),
                    Triple("1/4", "25", "1/4 = 25%"),
                    Triple("1/5", "20", "1/5 = 20%"),
                    Triple("1/6", "16.66", "1/6 = 16.66% or 16 2/3%"),
                    Triple("1/7", "14.28", "1/7 = 14.28% or 14 2/7%"),
                    Triple("1/8", "12.5", "1/8 = 12.5%"),
                    Triple("1/9", "11.11", "1/9 = 11.11%"),
                    Triple("1/11", "9.09", "1/11 = 9.09%"),
                    Triple("1/12", "8.33", "1/12 = 8.33%"),
                    Triple("1/13", "7.69", "1/13 = 7.69%"),
                    Triple("1/14", "7.14", "1/14 = 7.14%"),
                    Triple("1/15", "6.66", "1/15 = 6.66%"),
                    Triple("1/16", "6.25", "1/16 = 6.25%"),
                    Triple("3/8", "37.5", "3/8 = 3 × 12.5 = 37.5%"),
                    Triple("5/8", "62.5", "5/8 = 5 × 12.5 = 62.5%"),
                    Triple("4/7", "57.14", "4/7 = 4 × 14.28 = 57.14%")
                )
                val item = fractions.random()
                PracticeQuestion(
                    id = qId,
                    questionText = "${item.first} in % (round to 2 decimals) = ?",
                    answer = item.second,
                    explanation = item.third,
                    topicId = modeId,
                    topicName = "Fraction to %"
                )
            }
            "percentage_of_number" -> {
                val pcts = listOf(10, 20, 25, 30, 35, 40, 50, 75, 15, 5)
                val pct = pcts.random()
                val base = Random.nextInt(4, 50) * 20
                val ans = (base * pct) / 100
                PracticeQuestion(
                    id = qId,
                    questionText = "$pct% of $base = ?",
                    answer = "$ans",
                    explanation = "Split method: 10% of $base = ${base / 10}. Hence $pct% = $ans",
                    topicId = modeId,
                    topicName = "Percentage of Numbers"
                )
            }
            "fast_fraction_ops" -> {
                val d1 = listOf(2, 3, 4, 5).random()
                val d2 = listOf(3, 4, 5, 6).random()
                val n1 = Random.nextInt(1, d1)
                val n2 = Random.nextInt(1, d2)
                // cross multiply: (n1*d2 + n2*d1) / (d1*d2)
                val num = (n1 * d2 + n2 * d1)
                val den = (d1 * d2)
                val g = gcd(num, den)
                val finalNum = num / g
                val finalDen = den / g
                PracticeQuestion(
                    id = qId,
                    questionText = "$n1/$d1 + $n2/$d2 = Numerator in lowest form?",
                    answer = "$finalNum",
                    explanation = "($n1×$d2 + $n2×$d1)/($d1×$d2) = $num/$den = $finalNum/$finalDen. Numerator = $finalNum",
                    topicId = modeId,
                    topicName = "Fraction Ops"
                )
            }
            "number_series" -> {
                val type = Random.nextInt(4)
                when (type) {
                    0 -> { // Arithmetic diff
                        val start = Random.nextInt(2, 20)
                        val diff = Random.nextInt(3, 9)
                        val s1 = start
                        val s2 = s1 + diff
                        val s3 = s2 + diff
                        val s4 = s3 + diff
                        val ans = s4 + diff
                        PracticeQuestion(
                            id = qId,
                            questionText = "$s1, $s2, $s3, $s4, ?",
                            answer = "$ans",
                            explanation = "Constant difference of +$diff. Next is $s4 + $diff = $ans",
                            topicId = modeId,
                            topicName = "Number Series"
                        )
                    }
                    1 -> { // n^2 + k
                        val k = Random.nextInt(-2, 4)
                        val s1 = 1 * 1 + k
                        val s2 = 2 * 2 + k
                        val s3 = 3 * 3 + k
                        val s4 = 4 * 4 + k
                        val ans = 5 * 5 + k
                        PracticeQuestion(
                            id = qId,
                            questionText = "$s1, $s2, $s3, $s4, ?",
                            answer = "$ans",
                            explanation = "Pattern is n² ${if (k >= 0) "+ $k" else "$k"}. For n=5: 25 ${if (k >= 0) "+ $k" else "$k"} = $ans",
                            topicId = modeId,
                            topicName = "Number Series"
                        )
                    }
                    2 -> { // Double difference
                        val start = Random.nextInt(3, 10)
                        val s1 = start
                        val s2 = s1 + 2
                        val s3 = s2 + 4
                        val s4 = s3 + 6
                        val ans = s4 + 8
                        PracticeQuestion(
                            id = qId,
                            questionText = "$s1, $s2, $s3, $s4, ?",
                            answer = "$ans",
                            explanation = "Differences are +2, +4, +6, +8. Next = $s4 + 8 = $ans",
                            topicId = modeId,
                            topicName = "Number Series"
                        )
                    }
                    else -> { // Multiplicative
                        val start = Random.nextInt(2, 5)
                        val mul = 2
                        val s1 = start
                        val s2 = s1 * mul + 1
                        val s3 = s2 * mul + 1
                        val s4 = s3 * mul + 1
                        val ans = s4 * mul + 1
                        PracticeQuestion(
                            id = qId,
                            questionText = "$s1, $s2, $s3, $s4, ?",
                            answer = "$ans",
                            explanation = "Pattern is (×2 + 1). Next = ($s4 × 2) + 1 = $ans",
                            topicId = modeId,
                            topicName = "Number Series"
                        )
                    }
                }
            }
            "find_the_operator" -> {
                val ops = listOf("+", "-", "×", "÷")
                val op = ops.random()
                var a = Random.nextInt(4, 25)
                var b = Random.nextInt(2, 12)
                var res = 0
                when (op) {
                    "+" -> res = a + b
                    "-" -> {
                        if (a < b) { val t = a; a = b; b = t }
                        res = a - b
                    }
                    "×" -> res = a * b
                    "÷" -> {
                        res = a
                        a = a * b
                    }
                }
                PracticeQuestion(
                    id = qId,
                    questionText = "$a [ ? ] $b = $res",
                    answer = op,
                    explanation = "$a $op $b = $res",
                    topicId = modeId,
                    topicName = "Find the Operator",
                    options = listOf("+", "-", "×", "÷")
                )
            }
            "approximation_drill" -> {
                val a = (Random.nextDouble(19.5, 99.5) * 10).roundToInt() / 10.0
                val b = (Random.nextDouble(11.5, 49.5) * 10).roundToInt() / 10.0
                val approxA = a.roundToInt()
                val approxB = b.roundToInt()
                val ans = approxA + approxB
                PracticeQuestion(
                    id = qId,
                    questionText = "$a + $b ≈ ?",
                    answer = "$ans",
                    explanation = "Round $a -> $approxA and $b -> $approxB. Sum ≈ $ans",
                    topicId = modeId,
                    topicName = "Approximation"
                )
            }
            "decimal_calc" -> {
                val a = Random.nextInt(2, 12) * 0.5
                val b = Random.nextInt(2, 8) * 0.2
                val ans = Math.round(a * b * 100.0) / 100.0
                PracticeQuestion(
                    id = qId,
                    questionText = "$a × $b = ?",
                    answer = if (ans % 1.0 == 0.0) "${ans.toInt()}" else "$ans",
                    explanation = "Calculate integers first then position decimal point. Result = $ans",
                    topicId = modeId,
                    topicName = "Decimal Calculation"
                )
            }
            "fast_addition_2digit" -> {
                val a = Random.nextInt(25, 99)
                val b = Random.nextInt(15, 99)
                PracticeQuestion(
                    id = qId,
                    questionText = "$a + $b = ?",
                    answer = "${a + b}",
                    explanation = "Left-to-right addition: (${a/10*10} + ${b/10*10}) + (${a%10} + ${b%10}) = ${a + b}",
                    topicId = modeId,
                    topicName = "2-Digit Addition"
                )
            }
            "fast_addition_3digit" -> {
                val a = Random.nextInt(120, 890)
                val b = Random.nextInt(110, 750)
                PracticeQuestion(
                    id = qId,
                    questionText = "$a + $b = ?",
                    answer = "${a + b}",
                    explanation = "Left-to-right: Hundreds (${a/100*100}+${b/100*100}) + Tens/Units = ${a + b}",
                    topicId = modeId,
                    topicName = "3-Digit Addition"
                )
            }
            "subtraction_complements" -> {
                val base = listOf(1000, 10000).random()
                val sub = if (base == 1000) Random.nextInt(101, 989) else Random.nextInt(1001, 9899)
                PracticeQuestion(
                    id = qId,
                    questionText = "$base - $sub = ?",
                    answer = "${base - sub}",
                    explanation = "All from 9 and last from 10 (Nikhilam Navatashcaramam Dashatah) = ${base - sub}",
                    topicId = modeId,
                    topicName = "Subtraction Complements"
                )
            }
            "quick_division" -> {
                val div = Random.nextInt(4, 13)
                val q = Random.nextInt(12, 95)
                val num = div * q
                PracticeQuestion(
                    id = qId,
                    questionText = "$num ÷ $div = ?",
                    answer = "$q",
                    explanation = "$div × $q = $num, so $num ÷ $div = $q",
                    topicId = modeId,
                    topicName = "Quick Division"
                )
            }
            "unit_digit_drill" -> {
                val a = Random.nextInt(123, 999)
                val b = Random.nextInt(123, 999)
                val c = Random.nextInt(12, 99)
                val ans = ((a % 10) * (b % 10) * (c % 10)) % 10
                PracticeQuestion(
                    id = qId,
                    questionText = "Unit digit of $a × $b × $c = ?",
                    answer = "$ans",
                    explanation = "Multiply unit digits only: (${a%10} × ${b%10} × ${c%10}) -> unit digit is $ans",
                    topicId = modeId,
                    topicName = "Unit Digit Drill"
                )
            }
            "digital_root_check" -> {
                val num = Random.nextInt(1234, 98765)
                var temp = num
                while (temp >= 10) {
                    var sum = 0
                    while (temp > 0) {
                        sum += temp % 10
                        temp /= 10
                    }
                    temp = sum
                }
                PracticeQuestion(
                    id = qId,
                    questionText = "Digital Root / Digit Sum of $num = ?",
                    answer = "$temp",
                    explanation = "Sum all digits repeatedly (cast out 9s) -> $temp",
                    topicId = modeId,
                    topicName = "Digital Root"
                )
            }
            "double_and_half" -> {
                val a = Random.nextInt(3, 18) * 5 // e.g. 15, 25, 35, 45
                val b = Random.nextInt(2, 10) * 2 // even number e.g. 12, 14, 16
                val doubleA = a * 2
                val halfB = b / 2
                PracticeQuestion(
                    id = qId,
                    questionText = "$a × $b = ?",
                    answer = "${a * b}",
                    explanation = "Double & Half: ($a × 2) × ($b ÷ 2) = $doubleA × $halfB = ${a * b}",
                    topicId = modeId,
                    topicName = "Double & Half"
                )
            }
            "circle_geometry_math" -> {
                val r = listOf(7, 14, 21, 28, 35).random()
                val isCircumference = Random.nextBoolean()
                if (isCircumference) {
                    val ans = 2 * 22 * (r / 7)
                    PracticeQuestion(
                        id = qId,
                        questionText = "Perimeter (2πr) of circle with radius r = $r (π=22/7) = ?",
                        answer = "$ans",
                        explanation = "2 × (22/7) × $r = 2 × 22 × ${r/7} = $ans",
                        topicId = modeId,
                        topicName = "Circle & Geometry"
                    )
                } else {
                    val ans = 22 * (r / 7) * r
                    PracticeQuestion(
                        id = qId,
                        questionText = "Area (πr²) of circle with radius r = $r (π=22/7) = ?",
                        answer = "$ans",
                        explanation = "(22/7) × $r × $r = 22 × ${r/7} × $r = $ans",
                        topicId = modeId,
                        topicName = "Circle & Geometry"
                    )
                }
            }
            "rapid_fire_60s" -> {
                val mixedModes = listOf("squares_1_25", "tables_12_20", "vedic_end_5", "fast_addition_2digit", "percentage_of_number")
                generateSingleQuestion(mixedModes.random(), index)
            }
            "bank_po_simplification" -> {
                // e.g. 25% of 320 + √144 = ?
                val sq = listOf(144, 169, 196, 225, 256, 400).random()
                val sqRoot = Math.sqrt(sq.toDouble()).toInt()
                val base = Random.nextInt(4, 20) * 20
                val pct = 25
                val pctVal = (base * pct) / 100
                val ans = pctVal + sqRoot
                PracticeQuestion(
                    id = qId,
                    questionText = "$pct% of $base + √$sq = ?",
                    answer = "$ans",
                    explanation = "$pctVal + $sqRoot = $ans",
                    topicId = modeId,
                    topicName = "Banking Simplification"
                )
            }
            "ssc_cgl_marathon" -> {
                val mixed = listOf("squares_26_50", "vedic_base_100", "tables_21_30", "subtraction_complements", "unit_digit_drill")
                generateSingleQuestion(mixed.random(), index)
            }
            else -> {
                // default mixed sprint
                val mixed = listOf("squares_1_25", "tables_12_20", "vedic_end_5", "vedic_mult_11", "fast_addition_2digit")
                generateSingleQuestion(mixed.random(), index)
            }
        }
    }

    private fun gcd(a: Int, b: Int): Int {
        var x = a
        var y = b
        while (y != 0) {
            val t = y
            y = x % y
            x = t
        }
        return x
    }
}

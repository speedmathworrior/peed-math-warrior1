package com.example.math

import com.example.data.model.LearnSection
import com.example.data.model.PracticeMode
import com.example.data.model.TopicModule

object CatalogData {

    val learnModules = listOf(
        TopicModule(
            id = "learn_squares",
            titleEn = "Learn Squares (1-100)",
            titleHi = "वर्ग सीखें (1-100)",
            category = "Squares & Roots",
            summaryEn = "Master 1-30 memory base and Vedic base-50 / base-100 lightning shortcuts.",
            summaryHi = "1 से 30 याद करने का आधार और वैदिक बेस-50 / बेस-100 ट्रिक्स।",
            trickFormula = "For 26-50: (25 - diff) | diff²   •   For 51-75: (25 + diff) | diff²",
            examTip = "In SSC & Banking, 80% of square questions fall between 25 and 75. Memorize 1-25 cold, then use Base 50.",
            sections = listOf(
                LearnSection(
                    title = "Base 50 Rule (Numbers 26 to 50)",
                    explanation = "Find difference 'd' from 50 (d = 50 - N). The square is formed by two parts:\nLeft Part = 25 - d\nRight Part = d² (written in 2 digits).",
                    formulaOrTrick = "N² = (25 - d) × 100 + d²",
                    examples = listOf(
                        "46²" to "d = 50 - 46 = 4\nLeft = 25 - 4 = 21\nRight = 4² = 16\nResult = 2116",
                        "41²" to "d = 50 - 41 = 9\nLeft = 25 - 9 = 16\nRight = 9² = 81\nResult = 1681",
                        "38²" to "d = 50 - 38 = 12\nLeft = 25 - 12 = 13\nRight = 12² = 144 (1 carries over!)\nLeft = 13 + 1 = 14\nResult = 1444"
                    )
                ),
                LearnSection(
                    title = "Base 50 Rule (Numbers 51 to 75)",
                    explanation = "Find excess 'd' above 50 (d = N - 50).\nLeft Part = 25 + d\nRight Part = d² (2 digits).",
                    formulaOrTrick = "N² = (25 + d) × 100 + d²",
                    examples = listOf(
                        "54²" to "d = 4\nLeft = 25 + 4 = 29\nRight = 4² = 16\nResult = 2916",
                        "62²" to "d = 12\nLeft = 25 + 12 = 37\nRight = 12² = 144 (carry 1)\nLeft = 37 + 1 = 38\nResult = 3844"
                    )
                ),
                LearnSection(
                    title = "Base 100 Rule (Numbers 76 to 100)",
                    explanation = "Find deficiency 'd' from 100 (d = 100 - N).\nLeft Part = N - d\nRight Part = d² (2 digits).",
                    formulaOrTrick = "N² = (N - d) × 100 + d²",
                    examples = listOf(
                        "96²" to "d = 4\nLeft = 96 - 4 = 92\nRight = 4² = 16\nResult = 9216",
                        "88²" to "d = 12\nLeft = 88 - 12 = 76\nRight = 12² = 144 (carry 1)\nResult = 7744"
                    )
                )
            ),
            practiceModeId = "squares_1_25",
            iconName = "square_foot"
        ),
        TopicModule(
            id = "learn_cubes",
            titleEn = "Learn Cubes (1-30)",
            titleHi = "घन सीखें (1-30)",
            category = "Cubes & Roots",
            summaryEn = "Unit digit cycles and algebraic breakdown for rapid cube estimation.",
            summaryHi = "इकाई अंक चक्र और तेजी से घन निकालने की विधियां।",
            trickFormula = "Unit digit cycles: 1->1, 2->8, 3->7, 4->4, 5->5, 6->6, 7->3, 8->2, 9->9, 0->0",
            examTip = "Banking DI and Simplification tests cubes heavily up to 25. Note that 2 & 8 swap, and 3 & 7 swap unit digits.",
            sections = listOf(
                LearnSection(
                    title = "Unit Digit Rule for Cubes",
                    explanation = "Notice the unique property: every single digit (0-9) produces a distinct, unique unit digit when cubed! This makes finding cube roots instantaneous.",
                    formulaOrTrick = "2↔8, 3↔7, all others stay the same (1, 4, 5, 6, 9, 0).",
                    examples = listOf(
                        "12³" to "1728 (ends in 8)",
                        "17³" to "4913 (ends in 3)",
                        "21³" to "9261 (ends in 1)",
                        "25³" to "15625 (ends in 5)"
                    )
                )
            ),
            practiceModeId = "cubes_1_15",
            iconName = "view_in_ar"
        ),
        TopicModule(
            id = "learn_square_roots",
            titleEn = "Square Roots Shortcut",
            titleHi = "वर्गमूल शॉर्टकट",
            category = "Squares & Roots",
            summaryEn = "Find perfect square roots in 2 seconds using the last-two-digits grouping method.",
            summaryHi = "अंतिम दो अंकों के समूह से 2 सेकंड में पूर्ण वर्गमूल निकालें।",
            trickFormula = "Group into (Ten-Thousands & Hundreds) | (Tens & Units)",
            examTip = "Eliminate impossible options in SSC exams by inspecting the last digit (no square ends in 2, 3, 7, 8).",
            sections = listOf(
                LearnSection(
                    title = "Two-Step Square Root Method",
                    explanation = "Step 1: Check unit digit of the number.\nIf it ends in 1 -> root ends in 1 or 9\nEnds in 4 -> 2 or 8\nEnds in 5 -> 5\nEnds in 6 -> 4 or 6\nEnds in 9 -> 3 or 7\nStep 2: Ignore last 2 digits, find highest square ≤ remaining prefix.",
                    examples = listOf(
                        "√7056" to "Ends in 6 -> root ends in 4 or 6\nPrefix = 70 -> Between 8²(64) and 9²(81). Tens digit is 8.\nCheck 8 × 9 = 72. Since 70 < 72, choose smaller unit (4).\nResult = 84"
                    )
                )
            ),
            practiceModeId = "square_roots_perfect",
            iconName = "looks_one"
        ),
        TopicModule(
            id = "learn_cube_roots",
            titleEn = "Instant Cube Roots",
            titleHi = "तत्काल घनमूल विधि",
            category = "Cubes & Roots",
            summaryEn = "Exact cube root in 1 second by pairing last 3 digits.",
            summaryHi = "अंतिम 3 अंकों को अलग कर 1 सेकंड में घनमूल निकालें।",
            trickFormula = "Prefix cube range | Unit digit conversion",
            examTip = "Because each cube unit digit is 100% unique, cube roots are even faster than square roots!",
            sections = listOf(
                LearnSection(
                    title = "Instant 3-Digit Split Method",
                    explanation = "Split number into: [Remaining Prefix] | [Last 3 Digits].\n1. Last 3 digits give the exact units digit.\n2. Prefix gives the tens digit (largest cube ≤ prefix).",
                    examples = listOf(
                        "∛175616" to "Last 3 digits: 616 (ends in 6 -> Unit is 6)\nPrefix: 175 (Between 5³=125 and 6³=216 -> Tens is 5)\nResult = 56"
                    )
                )
            ),
            practiceModeId = "cube_roots_instant",
            iconName = "looks_3"
        ),
        TopicModule(
            id = "learn_tables",
            titleEn = "Learn Tables 12-30",
            titleHi = "पहाड़े 12-30",
            category = "Speed Drills",
            summaryEn = "Deconstruct 2-digit multiplication mentally using distributed addition.",
            summaryHi = "मानसिक रूप से पहाड़े याद करने व तोड़ने की तकनीक।",
            trickFormula = "A × B = (A_tens × B) + (A_units × B)",
            examTip = "Never recite 19 × 7 in your head from 19 × 1. Think: 10×7 (70) + 9×7 (63) = 133.",
            sections = listOf(
                LearnSection(
                    title = "Split & Add Method",
                    explanation = "Mentally multiply the tens place first, then add the units place.",
                    examples = listOf(
                        "24 × 7" to "(20 × 7) + (4 × 7) = 140 + 28 = 168",
                        "29 × 6" to "(30 × 6) - (1 × 6) = 180 - 6 = 174"
                    )
                )
            ),
            practiceModeId = "tables_12_20",
            iconName = "grid_view"
        ),
        TopicModule(
            id = "learn_vedic_5",
            titleEn = "Vedic: Squares Ending in 5",
            titleHi = "वैदिक: 5 पर समाप्त वर्ग",
            category = "Vedic Math",
            summaryEn = "Ekadhikena Purvena सूत्र: Instant squaring for 15, 25, 35, 75, 115...",
            summaryHi = "एकाधिकेन पूर्वेण सूत्र से 5 पर खत्म संख्याओं का वर्ग निकालें।",
            trickFormula = "(N5)² = [N × (N + 1)] | 25",
            examTip = "The right side is ALWAYS 25. Multiply the leading number by its consecutive next integer.",
            sections = listOf(
                LearnSection(
                    title = "Ekadhikena Purvena Sutra",
                    explanation = "Multiply the digit(s) before 5 by (digit + 1), then suffix 25.",
                    examples = listOf(
                        "65²" to "Leading = 6. Next integer = 7.\n6 × 7 = 42.\nAppend 25 -> 4225",
                        "115²" to "Leading = 11. Next integer = 12.\n11 × 12 = 132.\nAppend 25 -> 13225"
                    )
                )
            ),
            practiceModeId = "vedic_end_5",
            iconName = "bolt"
        ),
        TopicModule(
            id = "learn_vedic_nikhilam",
            titleEn = "Vedic: Base 100 Nikhilam",
            titleHi = "वैदिक: आधार 100 गुणा",
            category = "Vedic Math",
            summaryEn = "Multiply numbers near 100 (e.g. 97 × 94 or 108 × 106) in your head in 3 seconds.",
            summaryHi = "100 के नजदीक संख्याओं का गुणनफल 3 सेकंड में।",
            trickFormula = "Cross-subtract deficit | Multiply deficits",
            examTip = "Nikhilam Navatashcaramam Dashatah ('All from 9 and last from 10') is ideal for 85-115 range calculations.",
            sections = listOf(
                LearnSection(
                    title = "Both Below 100",
                    explanation = "Deficits d1 and d2 from 100.\nLeft = a - d2 (or b - d1)\nRight = d1 × d2 (2 digits)",
                    examples = listOf(
                        "96 × 93" to "d1 = 4, d2 = 7\nLeft = 96 - 7 = 89\nRight = 4 × 7 = 28\nResult = 8928"
                    )
                ),
                LearnSection(
                    title = "Both Above 100",
                    explanation = "Surplus s1 and s2 above 100.\nLeft = a + s2 (or b + s1)\nRight = s1 × s2 (2 digits)",
                    examples = listOf(
                        "107 × 109" to "s1 = 7, s2 = 9\nLeft = 107 + 9 = 116\nRight = 7 × 9 = 63\nResult = 11663"
                    )
                )
            ),
            practiceModeId = "vedic_base_100",
            iconName = "auto_awesome"
        ),
        TopicModule(
            id = "learn_vedic_crosswise",
            titleEn = "Vedic: 2x2 Crosswise",
            titleHi = "वैदिक: ऊर्ध्वतिर्यग्भ्याम्",
            category = "Vedic Math",
            summaryEn = "Urdhva Tiryagbhyam - General cross-multiplication for any 2-digit numbers.",
            summaryHi = "किन्हीं भी 2-अंकीय संख्याओं का सीधा और तिर्यक गुणा।",
            trickFormula = "(Ten × Ten) | (Cross-Product Sum) | (Unit × Unit)",
            examTip = "Practice doing the cross product mentally without writing intermediate scratchpad steps.",
            sections = listOf(
                LearnSection(
                    title = "3-Step Crosswise Method",
                    explanation = "For ab × cd:\n1. Unit × Unit: b × d\n2. Cross: (a × d) + (b × c) + carry\n3. Tens × Tens: (a × c) + carry",
                    examples = listOf(
                        "23 × 41" to "1. 3 × 1 = 3\n2. (2×1) + (3×4) = 2 + 12 = 14 (write 4, carry 1)\n3. (2×4) + 1 = 9\nResult = 943"
                    )
                )
            ),
            practiceModeId = "vedic_crosswise_2x2",
            iconName = "shuffle"
        ),
        TopicModule(
            id = "learn_vedic_11_99",
            titleEn = "Vedic: Magic of 11 & 99",
            titleHi = "वैदिक: 11 और 99 का जादू",
            category = "Vedic Math",
            summaryEn = "Instant multiplication by 11, 111, and subtraction tricks for 99.",
            summaryHi = "11 और 99 से तुरंत गुणा करने की जादुई तरकीब।",
            trickFormula = "For ab × 11 = a | (a+b) | b",
            examTip = "When multiplying by 11, sandwich the sum of the digits between the original digits.",
            sections = listOf(
                LearnSection(
                    title = "Multiplying by 11",
                    explanation = "Keep the first and last digits outside, sum adjacent digits in the middle.",
                    examples = listOf(
                        "52 × 11" to "5 | (5+2) | 2 = 572",
                        "68 × 11" to "6 | (6+8=14) | 8 -> (6+1) | 4 | 8 = 748"
                    )
                )
            ),
            practiceModeId = "vedic_mult_11",
            iconName = "flare"
        ),
        TopicModule(
            id = "learn_percentage_concepts",
            titleEn = "Percentage Split Method",
            titleHi = "प्रतिशत विभाजन विधि",
            category = "Percentages",
            summaryEn = "Break down any complex % into standard chunks: 50%, 10%, 5%, 1%.",
            summaryHi = "किसी भी प्रतिशत को 50%, 10%, 5%, 1% में तोड़कर हल करें।",
            trickFormula = "X% of Y = (X × Y) / 100   •   X% of Y is equal to Y% of X",
            examTip = "Symmetry rule: 64% of 25 is difficult, but 25% of 64 is 64/4 = 16!",
            sections = listOf(
                LearnSection(
                    title = "The 10% & 1% Anchors",
                    explanation = "10% of N is shifting decimal left by 1.\n1% of N is shifting decimal left by 2.\n5% is half of 10%.\n15% is (10% + 5%).",
                    examples = listOf(
                        "35% of 420" to "10% = 42\n30% = 42 × 3 = 126\n5% = 42 / 2 = 21\nTotal = 126 + 21 = 147",
                        "19% of 300" to "(20% - 1%) = (60 - 3) = 57"
                    )
                )
            ),
            practiceModeId = "percentage_of_number",
            iconName = "percent"
        ),
        TopicModule(
            id = "learn_fraction_percentage",
            titleEn = "Fraction to % Table",
            titleHi = "भिन्न से प्रतिशत तालिका",
            category = "Percentages",
            summaryEn = "Essential fraction values (1/2 through 1/20) essential for Banking and SSC speed.",
            summaryHi = "बैंकिंग व SSC के लिए 1/2 से 1/20 के आवश्यक प्रतिशत मान।",
            trickFormula = "1/7 = 14.28% | 1/8 = 12.5% | 1/9 = 11.11% | 1/11 = 9.09%",
            examTip = "1/7 and 1/14 multiples appear frequently in Profit & Loss and Compound Interest questions.",
            sections = listOf(
                LearnSection(
                    title = "Key Fractions Hierarchy",
                    explanation = "1/2 = 50%\n1/3 = 33.33%\n1/4 = 25%\n1/5 = 20%\n1/6 = 16.66%\n1/7 = 14.28%\n1/8 = 12.5%\n1/9 = 11.11%\n1/11 = 9.09%\n1/12 = 8.33%\n1/16 = 6.25%",
                    examples = listOf(
                        "3/8 of 640" to "3 × (1/8 of 640) = 3 × 80 = 240",
                        "5/7 of 490" to "5 × 70 = 350"
                    )
                )
            ),
            practiceModeId = "percentage_fractions",
            iconName = "pie_chart"
        ),
        TopicModule(
            id = "learn_number_series",
            titleEn = "Number Series Logic",
            titleHi = "संख्या श्रृंखला तर्क",
            category = "Reasoning & Logic",
            summaryEn = "Master common patterns: Constant diff, double diff, Prime gaps, and n²±k / n³±k.",
            summaryHi = "सामान्य पैटर्न सीखें: अंतर, दोहरा अंतर, अभाज्य संख्याएं और n²±k।",
            trickFormula = "Always calculate tier-1 and tier-2 differences first.",
            examTip = "If numbers increase suddenly, check multiplication or cubes. If growth is steady, inspect differences.",
            sections = listOf(
                LearnSection(
                    title = "Pattern Checklist",
                    explanation = "1. Difference: +d, +2d, +3d\n2. Double Difference: Differences themselves form an AP\n3. Alternating: Two series intertwined\n4. Squares / Cubes: n² + 1, n³ - 1",
                    examples = listOf(
                        "2, 9, 28, 65, ?" to "1³+1, 2³+1, 3³+1, 4³+1 -> Next is 5³+1 = 126"
                    )
                )
            ),
            practiceModeId = "number_series",
            iconName = "timeline"
        ),
        TopicModule(
            id = "learn_approximation",
            titleEn = "Approximation Techniques",
            titleHi = "सन्निकटन तकनीक",
            category = "Advanced",
            summaryEn = "Rounding strategies for Bank PO Prelims and Mains calculation questions.",
            summaryHi = "बैंक पीओ और क्लर्क के लिए सन्निकटन और अनुमान की तकनीक।",
            trickFormula = "Round numbers to nearest 5, 10 or compatible fraction.",
            examTip = "If options are spaced apart by >5%, round aggressively (e.g. 49.8% -> 50%).",
            sections = listOf(
                LearnSection(
                    title = "Compatible Rounding",
                    explanation = "49.8% of 799.6 ≈ 50% of 800 = 400.\n√143.9 ≈ √144 = 12.",
                    examples = listOf(
                        "33.4% of 598.9" to "≈ (1/3) of 600 = 200"
                    )
                )
            ),
            practiceModeId = "approximation_drill",
            iconName = "calculate"
        ),
        TopicModule(
            id = "learn_digital_root",
            titleEn = "Digital Root / Navashesa",
            titleHi = "डिजिटल रूट / बीजांक",
            category = "Vedic Math",
            summaryEn = "Verify large additions, multiplications, and squares without re-calculating.",
            summaryHi = "बड़े गुणा व जोड़ को बीजांक (Digital Sum) से 2 सेकंड में जांचें।",
            trickFormula = "Sum digits repeatedly until single digit (treat 9 as 0).",
            examTip = "The Digital Root of the Question MUST equal the Digital Root of the Answer Option.",
            sections = listOf(
                LearnSection(
                    title = "Digital Root (Bijank) Rules",
                    explanation = "Sum of digits mod 9.\nExample: 457 -> 4+5+7 = 16 -> 1+6 = 7.\nDigital Root of (A × B) = DR(A) × DR(B).",
                    examples = listOf(
                        "34 × 21 = 714" to "DR(34) = 7\nDR(21) = 3\n7 × 3 = 21 -> DR = 3\nDR(714) = 7+1+4 = 12 -> 3 (Matched!)"
                    )
                )
            ),
            practiceModeId = "digital_root_check",
            iconName = "check_circle"
        ),
        TopicModule(
            id = "learn_summation",
            titleEn = "Left-to-Right Addition",
            titleHi = "बाएं से दाएं जोड़ विधि",
            category = "Speed Drills",
            summaryEn = "Calculate large sums left-to-right to match human speech and working memory.",
            summaryHi = "तेज गणना के लिए बाएं से दाएं जोड़ें।",
            trickFormula = "(Hundreds + Hundreds) -> (Tens + Tens) -> (Units + Units)",
            examTip = "Traditional right-to-left addition forces you to hold carry overs; left-to-right gives progressive estimates instantly.",
            sections = listOf(
                LearnSection(
                    title = "Chunking Method",
                    explanation = "Add the biggest value place first.",
                    examples = listOf(
                        "68 + 75" to "60 + 70 = 130\n130 + (8 + 5) = 130 + 13 = 143",
                        "340 + 580" to "300 + 500 = 800\n800 + 120 = 920"
                    )
                )
            ),
            practiceModeId = "fast_addition_2digit",
            iconName = "add_circle"
        ),
        TopicModule(
            id = "learn_circle_geometry",
            titleEn = "Circle & Geometry Shortcuts",
            titleHi = "वृत्त व ज्यामिति शॉर्टकट",
            category = "Advanced",
            summaryEn = "Standard radius tables for 2πr and πr² when r is a multiple of 7.",
            summaryHi = "त्रिज्या 7 के गुणज होने पर परिधि और क्षेत्रफल के मानक मान।",
            trickFormula = "When r=7: Perimeter=44, Area=154 | When r=14: Perimeter=88, Area=616",
            examTip = "In Mensuration, 90% of exam problems use r = 7, 14, 21, 28, 35. Memorize the ratio 7:44:154!",
            sections = listOf(
                LearnSection(
                    title = "The 7 - 44 - 154 Standard Ratio",
                    explanation = "Radius = 7 × k\nCircumference = 44 × k\nArea = 154 × k²",
                    examples = listOf(
                        "For r = 14 (k=2)" to "Perimeter = 44 × 2 = 88\nArea = 154 × 4 = 616",
                        "For r = 21 (k=3)" to "Perimeter = 44 × 3 = 132\nArea = 154 × 9 = 1386"
                    )
                )
            ),
            practiceModeId = "circle_geometry_math",
            iconName = "circle"
        )
    )

    val practiceModes = listOf(
        PracticeMode("squares_1_25", "Squares (1 to 25)", "वर्ग (1 से 25)", "Memory Target", "Easy", 10, "square_foot", "Essential base memorization", "मूल आधार याद करें"),
        PracticeMode("squares_26_50", "Squares (26 to 50)", "वर्ग (26 से 50)", "Memory Target", "Medium", 10, "square_foot", "Base 50 mental calculation", "बेस 50 से गणना"),
        PracticeMode("squares_51_100", "Squares (51 to 100)", "वर्ग (51 से 100)", "Memory Target", "Hard", 10, "square_foot", "Base 100 & 50 combinations", "बेस 100 व 50"),
        PracticeMode("cubes_1_15", "Cubes (1 to 15)", "घन (1 से 15)", "Memory Target", "Easy", 10, "view_in_ar", "Key cubes for DI & Arithmetic", "डीआई के लिए मुख्य घन"),
        PracticeMode("cubes_16_30", "Cubes (16 to 30)", "घन (16 से 30)", "Memory Target", "Hard", 10, "view_in_ar", "Advanced competitive cubes", "उन्नत प्रतियोगी घन"),
        PracticeMode("square_roots_perfect", "Square Roots (Perfect)", "वर्गमूल (पूर्ण वर्ग)", "Memory Target", "Medium", 10, "looks_one", "2-second last digit method", "2 सेकंड अंतिम अंक विधि"),
        PracticeMode("cube_roots_instant", "Cube Roots (Instant)", "घनमूल (तत्काल)", "Memory Target", "Medium", 10, "looks_3", "1-second 3-digit split", "1 सेकंड 3-अंक विधि"),
        PracticeMode("tables_12_20", "Tables 12-20 Drill", "पहाड़े 12-20", "Speed Drills", "Easy", 10, "grid_view", "Speed multiplication drill", "गति गुणा अभ्यास"),
        PracticeMode("tables_21_30", "Tables 21-30 Speed Drill", "पहाड़े 21-30", "Speed Drills", "Hard", 10, "grid_view", "High-speed mental breakdown", "तेज मानसिक अभ्यास"),
        PracticeMode("vedic_end_5", "Vedic: Squares Ending in 5", "वैदिक: 5 पर समाप्त वर्ग", "Vedic Tricks", "Easy", 10, "bolt", "Ekadhikena Purvena सूत्र", "एकाधिकेन पूर्वेण सूत्र"),
        PracticeMode("vedic_base_100", "Vedic: Base 100 Product", "वैदिक: आधार 100 गुणा", "Vedic Tricks", "Medium", 10, "auto_awesome", "Nikhilam multiplication", "निखिलं विधि"),
        PracticeMode("vedic_mult_11", "Vedic: Multiply by 11", "वैदिक: 11 से गुणा", "Vedic Tricks", "Easy", 10, "flare", "Digit sandwich shortcut", "डिजिट सैंडविच ट्रिक"),
        PracticeMode("vedic_crosswise_2x2", "Vedic: 2x2 Crosswise", "वैदिक: 2x2 तिर्यक गुणा", "Vedic Tricks", "Hard", 10, "shuffle", "Urdhva Tiryagbhyam", "ऊर्ध्वतिर्यग्भ्याम्"),
        PracticeMode("percentage_fractions", "Fraction to % Table", "भिन्न से प्रतिशत", "Percentages", "Medium", 10, "pie_chart", "1/2 to 1/20 standard values", "1/2 से 1/20 मान"),
        PracticeMode("percentage_of_number", "Percentage of Numbers", "संख्याओं का प्रतिशत", "Percentages", "Medium", 10, "percent", "10%, 5%, 1% chunking", "10%, 5%, 1% विभाजन"),
        PracticeMode("fast_fraction_ops", "Fast Fraction Ops", "तेज भिन्न संक्रियाएं", "Percentages", "Hard", 10, "calculate", "Cross-multiplication of fractions", "भिन्नों का जोड़"),
        PracticeMode("number_series", "Number Series (Missing)", "संख्या श्रृंखला (लुप्त पद)", "Reasoning & Logic", "Medium", 10, "timeline", "Identify pattern & next term", "पैटर्न पहचानें"),
        PracticeMode("find_the_operator", "Find the Operator", "ऑपरेटर खोजें (+, -, ×, ÷)", "Reasoning & Logic", "Easy", 10, "help", "Rapid equation balance", "समीकरण संतुलन"),
        PracticeMode("approximation_drill", "Approximation Drill", "सन्निकटन अभ्यास", "Advanced", "Medium", 10, "calculate", "Bank PO Prelims style", "बैंक पीओ शैली"),
        PracticeMode("decimal_calc", "Decimal Calculation", "दशमलव गणना", "Advanced", "Medium", 10, "dialpad", "Decimal place management", "दशमलव नियंत्रण"),
        PracticeMode("fast_addition_2digit", "2-Digit Fast Addition", "2-अंकीय त्वरित जोड़", "Speed Drills", "Easy", 10, "add_circle", "Left-to-right mental sum", "बाएं से दाएं जोड़"),
        PracticeMode("fast_addition_3digit", "3-Digit Fast Addition", "3-अंकीय त्वरित जोड़", "Speed Drills", "Hard", 10, "add_circle", "Hundreds and tens chunking", "सैकड़ा व दहाई जोड़"),
        PracticeMode("subtraction_complements", "Subtraction Complements", "घटाव पूरक (1000 - X)", "Speed Drills", "Medium", 10, "remove_circle", "All from 9 last from 10", "निखिलं नवतश्चरमं दशतः"),
        PracticeMode("quick_division", "Quick Division", "त्वरित भाग", "Speed Drills", "Medium", 10, "alt_route", "Instant integer division", "सीधा भाग अभ्यास"),
        PracticeMode("unit_digit_drill", "Unit Digit Determination", "इकाई अंक निर्धारण", "Advanced", "Easy", 10, "looks_one", "SSC CGL unit digit checks", "इकाई अंक ट्रिक्स"),
        PracticeMode("digital_root_check", "Digital Root / Digit Sum", "डिजिटल रूट / बीजांक", "Vedic Tricks", "Easy", 10, "check_circle", "Vedic Navashesa verification", "बीजांक गणना"),
        PracticeMode("double_and_half", "Double & Half Technique", "दोगुना व आधा तकनीक", "Vedic Tricks", "Medium", 10, "swap_horiz", "Transform tough products", "गुणा को सरल बनाएं"),
        PracticeMode("circle_geometry_math", "Geometry Mensuration", "क्षेत्रमिति मानसिक गणित", "Advanced", "Medium", 10, "circle", "Radius 7 multiples speed math", "त्रिज्या 7 के गुणज"),
        PracticeMode("mixed_speed_sprint", "Mixed Speed Sprint", "मिश्रित स्पीड स्प्रिंट", "Speed Drills", "Medium", 20, "speed", "20 mixed questions challenge", "20 मिश्रित प्रश्नों की चुनौती"),
        PracticeMode("rapid_fire_60s", "Rapid Fire (60 Seconds)", "रैपिड फायर (60 सेकंड)", "Speed Drills", "Hard", 25, "timer", "Answer as many in 60s", "60 सेकंड में अधिकतम उत्तर"),
        PracticeMode("bank_po_simplification", "Bank PO Simplification", "बैंक पीओ सरलीकरण", "Banking/SSC Special", "Hard", 10, "account_balance", "Mixed BODMAS & Powers", "मिश्रित सरलीकरण"),
        PracticeMode("ssc_cgl_marathon", "SSC CGL Math Marathon", "एसएससी सीजीएल मैराथन", "Banking/SSC Special", "Hard", 20, "workspace_premium", "Exam-grade mental calculations", "परीक्षा स्तर की गणना")
    )
}

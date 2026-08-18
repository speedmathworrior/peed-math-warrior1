package com.example.ui.screens.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PracticeMode
import com.example.math.CatalogData
import com.example.ui.screens.learn.SegmentControl
import com.example.ui.screens.learn.getModuleIcon
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Gold500
import com.example.ui.theme.MistakeRed

@Composable
fun PracticeCatalogScreen(
    language: String,
    onStartPractice: (String) -> Unit,
    onSwitchToLearn: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Memory Target", "Vedic Tricks", "Speed Drills", "Percentages", "Advanced", "Banking/SSC Special")

    val allModes = CatalogData.practiceModes
    val filteredModes = if (selectedCategory == "All") {
        allModes
    } else {
        allModes.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    val learnCount = CatalogData.learnModules.size

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Shared Toggle Switcher
        SegmentControl(
            selectedSegment = "practice",
            learnCount = learnCount,
            practiceCount = allModes.size,
            language = language,
            onSelectSegment = { if (it == "learn") onSwitchToLearn() }
        )

        // Secondary Filter Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )

            categories.forEach { cat ->
                val isSelected = selectedCategory == cat
                val catLabel = when (cat) {
                    "All" -> if (language == "hi") "सभी" else "All"
                    "Memory Target" -> if (language == "hi") "मेमोरी" else "Memory Target"
                    "Vedic Tricks" -> if (language == "hi") "वैदिक ट्रिक्स" else "Vedic Tricks"
                    "Speed Drills" -> if (language == "hi") "स्पीड ड्रिल" else "Speed Drills"
                    "Percentages" -> if (language == "hi") "प्रतिशत" else "Percentages"
                    "Advanced" -> if (language == "hi") "उन्नत" else "Advanced"
                    "Banking/SSC Special" -> if (language == "hi") "परीक्षा स्पेशल" else "Banking/SSC Special"
                    else -> cat
                }

                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = cat },
                    label = {
                        Text(
                            text = catLabel,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_chip_$cat")
                )
            }
        }

        // 2-Column Grid of Practice Modes
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredModes, key = { it.id }) { mode ->
                PracticeModeCard(
                    mode = mode,
                    language = language,
                    onStart = { onStartPractice(mode.id) }
                )
            }
        }
    }
}

@Composable
fun PracticeModeCard(
    mode: PracticeMode,
    language: String,
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val diffColor = when (mode.difficulty) {
        "Easy" -> Emerald500
        "Medium" -> Gold500
        else -> MistakeRed
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onStart() }
            .testTag("practice_card_${mode.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getModuleIcon(mode.iconName),
                            contentDescription = mode.nameEn,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = diffColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = mode.difficulty,
                            color = diffColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = if (language == "hi") mode.nameHi else mode.nameEn,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )

                Text(
                    text = if (language == "hi") mode.descriptionHi else mode.descriptionEn,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == "hi") "शुरू ➔" else "Start ➔",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

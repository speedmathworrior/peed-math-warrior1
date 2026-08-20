package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500

@Composable
fun SegmentedProgressBar(
    progressRatio: Float, // 0.0 to 1.0
    modifier: Modifier = Modifier,
    height: Dp = 10.dp,
    segments: Int = 3
) {
    val clampedProgress = progressRatio.coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = clampedProgress, label = "seg_progress")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val segmentFraction = 1f / segments
        for (i in 0 until segments) {
            val segmentStart = i * segmentFraction
            val segmentEnd = (i + 1) * segmentFraction

            val segmentFillRatio = when {
                animatedProgress >= segmentEnd -> 1f
                animatedProgress <= segmentStart -> 0f
                else -> (animatedProgress - segmentStart) / segmentFraction
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(height / 2))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                if (segmentFillRatio > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(segmentFillRatio)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(height / 2))
                            .background(
                                if (i == 2 && segmentFillRatio >= 1f) Gold500 else Emerald500
                            )
                    )
                }
            }
        }
    }
}

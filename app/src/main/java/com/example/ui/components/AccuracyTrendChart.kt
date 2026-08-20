package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600

@Composable
fun AccuracyTrendChart(
    accuracyPoints: List<Float>, // Values 0 to 100
    labels: List<String>,
    modifier: Modifier = Modifier,
    title: String = "Accuracy Trend"
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                val avg = if (accuracyPoints.isNotEmpty()) (accuracyPoints.sum() / accuracyPoints.size).toInt() else 0
                Text(
                    text = "Avg: $avg%",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (accuracyPoints.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Complete practice sessions to view accuracy trend",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                val primaryColor = MaterialTheme.colorScheme.primary
                val gridColor = MaterialTheme.colorScheme.surfaceVariant

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    val paddingX = 24f
                    val paddingY = 20f

                    val usableWidth = width - (paddingX * 2)
                    val usableHeight = height - (paddingY * 2)

                    // Draw 3 horizontal guideline grids (100%, 50%, 0%)
                    val y100 = paddingY
                    val y50 = paddingY + usableHeight / 2
                    val y0 = paddingY + usableHeight

                    drawLine(gridColor, Offset(paddingX, y100), Offset(width - paddingX, y100), strokeWidth = 1f)
                    drawLine(gridColor, Offset(paddingX, y50), Offset(width - paddingX, y50), strokeWidth = 1f)
                    drawLine(gridColor, Offset(paddingX, y0), Offset(width - paddingX, y0), strokeWidth = 1f)

                    val stepX = if (accuracyPoints.size > 1) usableWidth / (accuracyPoints.size - 1) else usableWidth

                    val path = Path()
                    val fillPath = Path()

                    val coordinates = accuracyPoints.mapIndexed { index, acc ->
                        val clampedAcc = acc.coerceIn(0f, 100f)
                        val x = paddingX + (index * stepX)
                        val y = paddingY + usableHeight * (1f - (clampedAcc / 100f))
                        Offset(x, y)
                    }

                    if (coordinates.isNotEmpty()) {
                        path.moveTo(coordinates[0].x, coordinates[0].y)
                        fillPath.moveTo(coordinates[0].x, y0)
                        fillPath.lineTo(coordinates[0].x, coordinates[0].y)

                        for (i in 1 until coordinates.size) {
                            val pPrev = coordinates[i - 1]
                            val pCur = coordinates[i]
                            val control1 = Offset(pPrev.x + (pCur.x - pPrev.x) / 2, pPrev.y)
                            val control2 = Offset(pPrev.x + (pCur.x - pPrev.x) / 2, pCur.y)
                            path.cubicTo(control1.x, control1.y, control2.x, control2.y, pCur.x, pCur.y)
                            fillPath.cubicTo(control1.x, control1.y, control2.x, control2.y, pCur.x, pCur.y)
                        }

                        fillPath.lineTo(coordinates.last().x, y0)
                        fillPath.close()

                        // Draw shaded gradient area under curve
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(primaryColor.copy(alpha = 0.35f), Color.Transparent),
                                startY = paddingY,
                                endY = y0
                            )
                        )

                        // Draw main line
                        drawPath(
                            path = path,
                            color = primaryColor,
                            style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Draw data points
                        coordinates.forEach { pt ->
                            drawCircle(color = Color.White, radius = 5.dp.toPx(), center = pt)
                            drawCircle(color = primaryColor, radius = 3.5.dp.toPx(), center = pt)
                        }
                    }
                }

                // Bottom Labels Row
                if (labels.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        labels.forEach { label ->
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

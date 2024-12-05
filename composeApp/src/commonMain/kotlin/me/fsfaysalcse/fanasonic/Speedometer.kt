package me.fsfaysalcse.fanasonic

import kotlin.math.*
import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Speedometer(
    @FloatRange(from = 0.0, to = 100.0) currentSpeed: Float,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .drawBehind {
                // Create a glow effect using radial gradient
                val glowRadius = size.height / 2
                val glowColor = when {
                    currentSpeed < 50 -> Color.Green
                    currentSpeed < 80 -> Color.Yellow
                    else -> Color.Red
                }

                // Dynamic alpha based on speed (higher speed = brighter glow)
                val alpha = currentSpeed / 100f

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            glowColor.copy(alpha = 0.3f * alpha),
                            glowColor.copy(alpha = 0f)
                        ),
                        center = center,
                        radius = glowRadius + 40f
                    ),
                    radius = glowRadius + 40f,
                    center = center
                )
            }
    ) {
        val circleRadius = size.height / 2
        val arcStrokeWidth = 20.dp.toPx()
        val startAngle = 130f
        val sweepAngle = 280f

        val mainColor = when {
            currentSpeed < 50 -> Color(0xFF3FA244)
            currentSpeed < 80 -> Color(0xFF3FA244)
            else -> Color(0xFF00796B)
        }

        // Secondary arc
        drawArc(
            color = Color.LightGray,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(
                width = arcStrokeWidth,
                cap = StrokeCap.Round
            )
        )

        // Main arc
        drawArc(
            color = mainColor,
            startAngle = startAngle,
            sweepAngle = (currentSpeed / 100f) * sweepAngle,
            useCenter = false,
            style = Stroke(
                width = arcStrokeWidth,
                cap = StrokeCap.Round
            )
        )

        for (speed in 0..100 step 10) {

            val angleInRad = (startAngle + (speed / 100.0) * sweepAngle) * (kotlin.math.PI / 180.0)

            val lineLength = if (speed % 20 == 0) {
                20f
            } else {
                10f
            }

            val lineThickness = if (speed % 20 == 0) {
                3.dp.toPx()
            } else {
                2.dp.toPx()
            }

            // Adjust markers to avoid overlapping
            val startOffset = calculateOffSet(
                angleInRad, circleRadius - arcStrokeWidth / 2 - 5f, center
            )

            val endOffset = calculateOffSet(
                angleInRad, circleRadius - arcStrokeWidth / 2 - 5f - lineLength, center
            )

            // Draw the main marker line
            drawLine(
                color = Color.Black,
                start = startOffset,
                end = endOffset,
                strokeWidth = lineThickness
            )

            // Draw the rounded bottom of the marker
            drawCircle(
                color = Color.Black,
                radius = lineThickness / 2,
                center = startOffset
            )

            // Draw the rounded top of the marker
            drawCircle(
                color = Color.Black,
                radius = lineThickness / 2,
                center = endOffset
            )

            // Draw text for every step of 10 with bold style
            if (speed % 10 == 0) {
                val textMarker = textMeasurer.measure(
                    text = speed.toString(),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                val textWidth = textMarker.size.width
                val textHeight = textMarker.size.height

                val textOffset = calculateOffSet(
                    angleInRad, circleRadius - arcStrokeWidth - 50f, center
                )

                drawText(
                    textMarker,
                    color = Color.Black,
                    topLeft = Offset(
                        textOffset.x - textWidth / 2,
                        textOffset.y - textHeight / 2
                    ),
                )
            }
        }
    }
}

fun calculateOffSet(
    degrees: Double,
    radius: Float,
    center: Offset
): Offset {
    val x = (radius * cos(degrees) + center.x).toFloat()
    val y = (radius * sin(degrees) + center.x).toFloat()
    return Offset(x, y)
}



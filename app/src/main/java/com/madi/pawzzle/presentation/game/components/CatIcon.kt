package com.madi.pawzzle.presentation.game.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun CatIcon(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = modifier.size(32.dp)) {
        val width = size.width
        val height = size.height

        val leftEar = Path().apply {
            moveTo(width * 0.2f, height * 0.4f)
            lineTo(width * 0.1f, height * 0.15f)
            lineTo(width * 0.4f, height * 0.25f)
            close()
        }
        drawPath(leftEar, color)

        val rightEar = Path().apply {
            moveTo(width * 0.8f, height * 0.4f)
            lineTo(width * 0.9f, height * 0.15f)
            lineTo(width * 0.6f, height * 0.25f)
            close()
        }
        drawPath(rightEar, color)

        drawCircle(
            color = color,
            radius = width * 0.35f,
            center = center
        )

        drawCircle(
            color = Color.White,
            radius = width * 0.05f,
            center = center.copy(x = center.x - width * 0.15f, y = center.y - height * 0.05f)
        )
        drawCircle(
            color = Color.White,
            radius = width * 0.05f,
            center = center.copy(x = center.x + width * 0.15f, y = center.y - height * 0.05f)
        )
    }
}

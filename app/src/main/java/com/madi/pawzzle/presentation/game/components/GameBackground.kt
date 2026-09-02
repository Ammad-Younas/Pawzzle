package com.madi.pawzzle.presentation.game.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun GameBackground(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    
    val animX1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animX1"
    )
    
    val animY1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(35000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animY1"
    )

    val animX2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(40000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animX2"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val radius1 = size.minDimension * 0.7f
            val radius2 = size.minDimension * 0.9f

            // Blob 1
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFE0B2).copy(alpha = 0.45f),
                        Color.Transparent
                    ),
                    center = Offset(w * animX1, h * animY1),
                    radius = radius1
                ),
                radius = radius1,
                center = Offset(w * animX1, h * animY1)
            )

            // Blob 2
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFC8E6C9).copy(alpha = 0.35f),
                        Color.Transparent
                    ),
                    center = Offset(w * animX2, h * (1f - animY1)),
                    radius = radius2
                ),
                radius = radius2,
                center = Offset(w * animX2, h * (1f - animY1))
            )
        }
    }
}

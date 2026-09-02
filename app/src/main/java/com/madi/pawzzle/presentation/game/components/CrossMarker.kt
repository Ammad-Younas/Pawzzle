package com.madi.pawzzle.presentation.game.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CrossMarker(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Default.Clear,
        contentDescription = null,
        tint = Color.Gray.copy(alpha = 0.5f),
        modifier = modifier.size(24.dp)
    )
}

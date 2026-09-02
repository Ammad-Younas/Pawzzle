package com.madi.pawzzle.presentation.game.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.madi.pawzzle.domain.model.Position
import com.madi.pawzzle.domain.model.Puzzle

@Composable
fun PuzzleBoard(
    puzzle: Puzzle,
    cats: Set<Position>,
    crosses: Set<Position>,
    onCellTapped: (Position) -> Unit,
    onCellDoubleTapped: (Position) -> Unit,
    modifier: Modifier = Modifier
) {
    val regionColors = listOf(
        Color(0xFFFFECB3),
        Color(0xFFF8BBD0),
        Color(0xFFB2EBF2),
        Color(0xFFC8E6C9),
        Color(0xFFFFCCBC),
        Color(0xFFD1C4E9),
        Color(0xFFCFD8DC),
        Color(0xFFDCEDC8)
    )

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val boardSize = min(maxWidth, maxHeight)

        Card(
            modifier = Modifier
                .size(boardSize)
                .aspectRatio(1f),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                for (r in 0 until puzzle.size) {
                    Row(modifier = Modifier.weight(1f)) {
                        for (c in 0 until puzzle.size) {
                            val position = Position(r, c)
                            val regionId = puzzle.regions[r][c]
                            val color = regionColors[regionId % regionColors.size]

                            PuzzleCell(
                                color = color,
                                hasCat = cats.contains(position),
                                hasCross = crosses.contains(position),
                                onTap = { onCellTapped(position) },
                                onDoubleTap = { onCellDoubleTapped(position) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

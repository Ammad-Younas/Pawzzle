package com.madi.pawzzle.presentation.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madi.pawzzle.domain.model.GameState
import com.madi.pawzzle.domain.model.GameStatus
import com.madi.pawzzle.presentation.game.components.GameBackground
import com.madi.pawzzle.presentation.game.components.GameToolbar
import com.madi.pawzzle.presentation.game.components.PuzzleBoard
import com.madi.pawzzle.presentation.game.components.WinDialog

@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isWon = uiState.gameState?.status == GameStatus.WON

    Box(modifier = Modifier.fillMaxSize()) {
        GameBackground()
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            val isLandscape = maxWidth > maxHeight
            val gameState = uiState.gameState

            if (uiState.isLoading) {
                LoadingState()
            } else if (gameState != null) {
                if (isLandscape) {
                    LandscapeGameLayout(gameState, viewModel)
                } else {
                    PortraitGameLayout(gameState, viewModel)
                }
            }
        }

        if (isWon) {
            WinDialog(
                onDismiss = {  },
                onNewGame = { viewModel.onEvent(GameEvent.OnNewGameClicked) }
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Preparing Puzzle...",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun PortraitGameLayout(
    state: GameState,
    viewModel: GameViewModel
) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            GameToolbar(
                catsCount = state.cats.size,
                targetCount = state.puzzle.size,
                status = state.status,
                onRestart = { viewModel.onEvent(GameEvent.OnRestartClicked) },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PuzzleBoard(
                puzzle = state.puzzle,
                cats = state.cats,
                crosses = state.crosses,
                onCellTapped = { viewModel.onEvent(GameEvent.OnCellTapped(it)) },
                onCellDoubleTapped = { viewModel.onEvent(GameEvent.OnCellDoubleTapped(it)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun LandscapeGameLayout(
    state: GameState,
    viewModel: GameViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Pawzzle",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Cats: ${state.cats.size} / ${state.puzzle.size}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
        PuzzleBoard(
            puzzle = state.puzzle,
            cats = state.cats,
            crosses = state.crosses,
            onCellTapped = { viewModel.onEvent(GameEvent.OnCellTapped(it)) },
            onCellDoubleTapped = { viewModel.onEvent(GameEvent.OnCellDoubleTapped(it)) },
            modifier = Modifier.weight(2f).fillMaxHeight()
        )

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = { viewModel.onEvent(GameEvent.OnRestartClicked) }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Restart",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

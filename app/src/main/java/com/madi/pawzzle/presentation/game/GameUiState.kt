package com.madi.pawzzle.presentation.game

import com.madi.pawzzle.domain.model.GameState

data class GameUiState(
    val gameState: GameState? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

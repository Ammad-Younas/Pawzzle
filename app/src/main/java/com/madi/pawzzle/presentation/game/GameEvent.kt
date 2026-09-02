package com.madi.pawzzle.presentation.game

import com.madi.pawzzle.domain.model.Position

sealed interface GameEvent {
    data class OnCellTapped(val position: Position) : GameEvent
    data class OnCellDoubleTapped(val position: Position) : GameEvent
    data object OnRestartClicked : GameEvent
    data object OnNewGameClicked : GameEvent
}

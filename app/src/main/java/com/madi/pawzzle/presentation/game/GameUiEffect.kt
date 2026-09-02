package com.madi.pawzzle.presentation.game

sealed interface GameUiEffect {
    data object ShowGameWonDialog : GameUiEffect
}

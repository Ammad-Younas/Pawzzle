package com.madi.pawzzle.core.navigation

sealed class Screen (val route: String) {
    object GameScreen : Screen("game_screen")
}


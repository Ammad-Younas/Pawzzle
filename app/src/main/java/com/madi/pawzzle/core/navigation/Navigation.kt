package com.madi.pawzzle.core.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.madi.pawzzle.presentation.game.GameScreen
import com.madi.pawzzle.presentation.game.GameViewModel

@Composable
fun Navigation(
    navController: NavHostController,
){
    NavHost(
        navController = navController,
        startDestination = Screen.GameScreen.route
    ) {
        composable(Screen.GameScreen.route) {
            GameScreen(viewModel = hiltViewModel<GameViewModel>())
        }
    }
}

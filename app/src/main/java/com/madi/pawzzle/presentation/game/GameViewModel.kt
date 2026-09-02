package com.madi.pawzzle.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.pawzzle.domain.engine.RuleValidator
import com.madi.pawzzle.domain.model.Difficulty
import com.madi.pawzzle.domain.model.GameState
import com.madi.pawzzle.domain.model.GameStatus
import com.madi.pawzzle.domain.model.Position
import com.madi.pawzzle.domain.model.Puzzle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class GameViewModel @Inject constructor(
    private val ruleValidator: RuleValidator
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<GameUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        startNewGame()
    }

    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.OnCellTapped -> handleCellTapped(event.position)
            is GameEvent.OnCellDoubleTapped -> handleCellDoubleTapped(event.position)
            GameEvent.OnRestartClicked -> restartGame()
            GameEvent.OnNewGameClicked -> startNewGame()
        }
    }

    private fun startNewGame() {
        val size = 5
        val regions = listOf(
            listOf(0, 0, 1, 1, 1),
            listOf(0, 2, 2, 1, 1),
            listOf(0, 2, 3, 3, 3),
            listOf(4, 4, 4, 3, 3),
            listOf(4, 4, 4, 4, 4)
        )
        val puzzle = Puzzle(
            id = 1L,
            size = size,
            regions = regions,
            solution = emptySet(),
            difficulty = Difficulty.EASY
        )
        _uiState.update {
            it.copy(
                gameState = GameState(puzzle = puzzle),
                isLoading = false
            )
        }
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (isActive) {
                delay(1000.milliseconds)
                _uiState.update {
                    val currentState = it.gameState ?: return@update it
                    if (currentState.status == GameStatus.PLAYING) {
                        it.copy(gameState = currentState.copy(elapsedSeconds = currentState.elapsedSeconds + 1))
                    } else {
                        it
                    }
                }
            }
        }
    }

    private fun handleCellTapped(position: Position) {
        val currentState = _uiState.value.gameState ?: return
        if (currentState.status != GameStatus.PLAYING) return

        val crosses = currentState.crosses
        val newCrosses = if (crosses.contains(position)) {
            crosses - position
        } else {
            crosses + position
        }

        _uiState.update {
            it.copy(
                gameState = currentState.copy(crosses = newCrosses)
            )
        }
    }

    private fun handleCellDoubleTapped(position: Position) {
        val currentState = _uiState.value.gameState ?: return
        if (currentState.status != GameStatus.PLAYING) return

        val cats = currentState.cats
        val newCats = if (cats.contains(position)) {
            cats - position
        } else {
            if (ruleValidator.canPlaceCat(currentState.puzzle, cats, position)) {
                cats + position
            } else {
                cats
            }
        }

        val isWon = ruleValidator.isValidSolution(currentState.puzzle, newCats)

        _uiState.update {
            it.copy(
                gameState = currentState.copy(
                    cats = newCats,
                    status = if (isWon) GameStatus.WON else GameStatus.PLAYING
                )
            )
        }

        if (isWon) {
            viewModelScope.launch {
                _uiEffect.emit(GameUiEffect.ShowGameWonDialog)
            }
        }
    }

    private fun restartGame() {
        val currentState = _uiState.value.gameState ?: return
        _uiState.update {
            it.copy(
                gameState = currentState.copy(
                    cats = emptySet(),
                    crosses = emptySet(),
                    elapsedSeconds = 0,
                    status = GameStatus.PLAYING
                )
            )
        }
        startTimer()
    }
}

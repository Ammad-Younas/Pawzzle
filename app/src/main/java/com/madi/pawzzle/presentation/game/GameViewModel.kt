package com.madi.pawzzle.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.pawzzle.domain.engine.PuzzleGenerator
import com.madi.pawzzle.domain.engine.RuleValidator
import com.madi.pawzzle.domain.model.GameState
import com.madi.pawzzle.domain.model.GameStatus
import com.madi.pawzzle.domain.model.Position
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class GameViewModel @Inject constructor(
    private val ruleValidator: RuleValidator,
    private val puzzleGenerator: PuzzleGenerator
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<GameUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private var timerJob: Job? = null

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
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val puzzle = withContext(Dispatchers.IO) {
                puzzleGenerator.generate(size = 5)
            }
            _uiState.update {
                it.copy(
                    gameState = GameState(puzzle = puzzle),
                    isLoading = false
                )
            }
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
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

        _uiState.update { state ->
            val gs = state.gameState ?: return@update state
            val isWon = ruleValidator.isValidSolution(gs.puzzle, newCats)
            state.copy(
                gameState = gs.copy(
                    cats = newCats,
                    status = if (isWon) GameStatus.WON else GameStatus.PLAYING
                )
            )
        }

        // We can check the latest state to see if we won
        if (_uiState.value.gameState?.status == GameStatus.WON) {
            timerJob?.cancel()
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

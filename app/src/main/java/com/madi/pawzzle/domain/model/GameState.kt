package com.madi.pawzzle.domain.model

data class GameState (
    val puzzle: Puzzle,
    val cats: Set<Position> = emptySet(),
    val crosses: Set<Position> = emptySet(),
    val mistakes: Int = 0,
    val elapsedSeconds: Long = 0,
    val hintsUsed: Int = 0,
    val status: GameStatus = GameStatus.PLAYING
)


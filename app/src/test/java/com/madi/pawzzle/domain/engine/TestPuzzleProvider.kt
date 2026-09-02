package com.madi.pawzzle.domain.engine

import com.madi.pawzzle.domain.model.Difficulty
import com.madi.pawzzle.domain.model.Position
import com.madi.pawzzle.domain.model.Puzzle

object TestPuzzleProvider {
    val easy5x5 = Puzzle(
        id = 1L,
        size = 5,
        regions = listOf(
            listOf(0, 0, 1, 1, 1),
            listOf(2, 0, 0, 3, 1),
            listOf(2, 4, 3, 3, 1),
            listOf(2, 4, 4, 3, 3),
            listOf(2, 2, 4, 4, 4)
        ),
        solution = setOf(
            Position(0, 2),
            Position(1, 4),
            Position(2, 0),
            Position(3, 3),
            Position(4, 1)
        ),
        difficulty = Difficulty.EASY
    )
}
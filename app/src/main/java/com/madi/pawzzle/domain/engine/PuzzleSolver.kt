package com.madi.pawzzle.domain.engine

import com.madi.pawzzle.domain.model.Position
import com.madi.pawzzle.domain.model.Puzzle
import javax.inject.Inject

class PuzzleSolver @Inject constructor(
    private val ruleValidator: RuleValidator
) {
    fun solve(puzzle: Puzzle): Set<Position>? {
        fun backtrack(row: Int, cats: Set<Position>): Set<Position>? {
            if (row == puzzle.size) {
                return cats
            }
            for (column in 0 until puzzle.size) {
                val position = Position(row = row, column = column)
                if (ruleValidator.canPlaceCat(puzzle = puzzle, cats = cats, position = position)) {
                    val solution = backtrack(row = row + 1, cats = cats + position)
                    if (solution != null) {
                        return solution
                    }
                }
            }
            return null
        }
        return backtrack(row = 0, cats = emptySet())
    }
}
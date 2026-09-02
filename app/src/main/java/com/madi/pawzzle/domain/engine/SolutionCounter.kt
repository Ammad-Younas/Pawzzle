package com.madi.pawzzle.domain.engine

import com.madi.pawzzle.domain.model.Position
import com.madi.pawzzle.domain.model.Puzzle
import javax.inject.Inject

class SolutionCounter @Inject constructor(
    private val ruleValidator: RuleValidator
) {
    fun countSolutions(puzzle: Puzzle, maxSolutions: Int = 2): Int {
        var count = 0
        fun backtrack(row: Int, cats: Set<Position>) {
            if (count >= maxSolutions) return
            if (row == puzzle.size) {
                count++
                return
            }
            for (column in 0 until puzzle.size) {
                val position = Position(row = row, column = column)
                if (ruleValidator.canPlaceCat(puzzle = puzzle, cats = cats, position = position)) {
                    backtrack(row = row + 1, cats = cats + position)
                }
            }
        }
        backtrack(row = 0, cats = emptySet())
        return count
    }
}
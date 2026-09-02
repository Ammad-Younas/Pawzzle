package com.madi.pawzzle.domain.engine

import com.madi.pawzzle.domain.model.Position
import com.madi.pawzzle.domain.model.Puzzle
import javax.inject.Inject
import kotlin.math.abs


class RuleValidator @Inject constructor() {

    fun isValidSolution(puzzle: Puzzle, cats: Set<Position>): Boolean {
        if (cats.size != puzzle.size) {
            return false
        }
        cats.forEach { cat ->
            val others = cats - cat
            if (!canPlaceCat(puzzle = puzzle, cats = others, position = cat)) {
                return false
            }
        }
        return true
    }


    fun canPlaceCat(puzzle: Puzzle, cats: Set<Position>, position: Position): Boolean {
        if (!isInsideBoard(puzzle, position)) {
            return false
        }
        return !hasRowConflict(cats, position) && !hasColumnConflict(cats, position) && !hasRegionConflict(puzzle, cats, position) && !hasAdjacentCat(cats, position)
    }

    fun isInsideBoard(puzzle: Puzzle, position: Position): Boolean {
        return position.row in 0 until puzzle.size && position.column in 0 until puzzle.size
    }

    fun hasRowConflict(cats: Set<Position>, position: Position): Boolean {
        return cats.any { cat ->
            cat.row == position.row
        }
    }

    fun hasColumnConflict(cats: Set<Position>, position: Position): Boolean {
        return cats.any { cat ->
            cat.column == position.column
        }
    }

    fun hasRegionConflict(puzzle: Puzzle, cats: Set<Position>, position: Position): Boolean {
        val regionId = puzzle.regions[position.row][position.column]
        return cats.any { cat ->
            puzzle.regions[cat.row][cat.column] == regionId
        }
    }

    fun hasAdjacentCat(cats: Set<Position>, position: Position): Boolean {
        return cats.any { cat ->
            val rowDistance = abs(cat.row - position.row)
            val columnDistance = abs(cat.column - position.column)

            rowDistance <= 1 && columnDistance <= 1
        }
    }
}


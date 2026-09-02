package com.madi.pawzzle.domain.engine

import com.madi.pawzzle.domain.model.Difficulty
import com.madi.pawzzle.domain.model.Position
import com.madi.pawzzle.domain.model.Puzzle
import javax.inject.Inject
import kotlin.random.Random

class PuzzleGenerator @Inject constructor(
    private val ruleValidator: RuleValidator,
    private val solutionCounter: SolutionCounter
) {
    fun generate(size: Int, difficulty: Difficulty = Difficulty.EASY): Puzzle {
        var attempts = 0
        var lastValid: Puzzle?

        while (true) {
            attempts++
            val solution = generateValidCatPositions(size) ?: continue
            val regions = generateRegions(size, solution)
            
            val puzzle = Puzzle(
                id = Random.nextLong(),
                size = size,
                regions = regions,
                solution = solution,
                difficulty = difficulty
            )

            lastValid = puzzle

            if (solutionCounter.countSolutions(puzzle, maxSolutions = 2) == 1 || attempts > 100) {
                return lastValid
            }
        }
    }

    private fun generateValidCatPositions(size: Int): Set<Position>? {
        val rows = (0 until size).toList().shuffled()
        fun backtrack(rowIdx: Int, currentCats: Set<Position>): Set<Position>? {
            if (rowIdx == size) return currentCats
            val row = rows[rowIdx]
            val cols = (0 until size).toList().shuffled()
            for (col in cols) {
                val pos = Position(row, col)
                val hasConflict = ruleValidator.hasRowConflict(currentCats, pos) || ruleValidator.hasColumnConflict(currentCats, pos) || ruleValidator.hasAdjacentCat(currentCats, pos)
                if (!hasConflict) {
                    val result = backtrack(rowIdx + 1, currentCats + pos)
                    if (result != null) return result
                }
            }
            return null
        }
        return backtrack(0, emptySet())
    }

    private fun generateRegions(size: Int, solution: Set<Position>): List<List<Int>> {
        val grid = MutableList(size) { MutableList(size) { -1 } }
        val solutionList = solution.toList()
        
        solutionList.forEachIndexed { index, pos ->
            grid[pos.row][pos.column] = index
        }
        val queue = mutableListOf<Pair<Position, Int>>()
        solutionList.forEachIndexed { index, pos -> 
            queue.add(pos to index) 
        }
        queue.shuffle()
        val directions = listOf(
            Position(0, 1), Position(0, -1), Position(1, 0), Position(-1, 0)
        )
        while (queue.isNotEmpty()) {
            val (current, regionId) = queue.removeAt(0)
            directions.shuffled().forEach { dir ->
                val next = Position(current.row + dir.row, current.column + dir.column)
                if (next.row in 0 until size && next.column in 0 until size && grid[next.row][next.column] == -1) {
                    grid[next.row][next.column] = regionId
                    queue.add(next to regionId)
                }
            }
        }

        return grid.map { it.toList() }
    }
}

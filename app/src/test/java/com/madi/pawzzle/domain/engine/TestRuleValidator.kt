package com.madi.pawzzle.domain.engine

import com.madi.pawzzle.domain.model.Position
import com.madi.pawzzle.domain.model.Puzzle
import com.madi.pawzzle.domain.model.Difficulty
import org.junit.Test

class TestRuleValidator {
    @Test
    fun main() {
    val validator = RuleValidator()
    
    // Using regions from second screenshot:
    // (0,0) pink, (0,1) green, (0,2) green, (0,3) green, (0,4) green
    // (1,0) pink, (1,1) pink, (1,2) green, (1,3) green, (1,4) orange
    // (2,0) pink, (2,1) yellow, (2,2) green, (2,3) orange, (2,4) orange
    // (3,0) yellow, (3,1) yellow, (3,2) yellow, (3,3) blue, (3,4) orange
    // (4,0) yellow, (4,1) yellow, (4,2) blue, (4,3) blue, (4,4) blue
    
    val regions = listOf(
        listOf(0, 1, 1, 1, 1),
        listOf(0, 0, 1, 1, 2),
        listOf(0, 3, 1, 2, 2),
        listOf(3, 3, 3, 4, 2),
        listOf(3, 3, 4, 4, 4)
    )
    
    val cats = setOf(
        Position(0, 2), // green (1)
        Position(1, 0), // pink (0)
        Position(2, 4), // orange (2)
        Position(3, 1), // yellow (3)
        Position(4, 3)  // blue (4)
    )
    
    val puzzle = Puzzle(1L, 5, regions, cats, Difficulty.EASY)
    
    val isValid = validator.isValidSolution(puzzle, cats)
    val fw0 = java.io.FileWriter("d:/MaDi/Practice/App_Development/Pawzzle/test_debug.txt", true)
    fw0.write("Is valid: $isValid\n")
    fw0.close()
    
    cats.forEach { cat ->
        val others = cats - cat
        val canPlace = validator.canPlaceCat(puzzle, others, cat)
        val fw = java.io.FileWriter("d:/MaDi/Practice/App_Development/Pawzzle/test_debug.txt", true)
        fw.write("Can place $cat: $canPlace\n")
        fw.write("  Inside: ${validator.isInsideBoard(puzzle, cat)}\n")
        fw.write("  Row conflict: ${validator.hasRowConflict(others, cat)}\n")
        fw.close()
        val fw2 = java.io.FileWriter("d:/MaDi/Practice/App_Development/Pawzzle/test_debug.txt", true)
        fw2.write("  Col conflict: ${validator.hasColumnConflict(others, cat)}\n")
        fw2.write("  Region conflict: ${validator.hasRegionConflict(puzzle, others, cat)}\n")
        fw2.write("  Adj conflict: ${validator.hasAdjacentCat(others, cat)}\n")
        fw2.close()
    }
}
}

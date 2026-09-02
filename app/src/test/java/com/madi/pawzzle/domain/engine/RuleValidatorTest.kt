package com.madi.pawzzle.domain.engine

import com.madi.pawzzle.domain.model.Position
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

class RuleValidatorTest {

    private lateinit var validator: RuleValidator

    @Before
    fun setup() {
        validator = RuleValidator()
    }

    @Test
    fun `placing first cat is allowed`() {

        val puzzle = TestPuzzleProvider.easy5x5

        val result = validator.canPlaceCat(
            puzzle = puzzle,
            cats = emptySet(),
            position = Position(0, 2)
        )

        assertTrue(result)
    }

    @Test
    fun `cat in same row is not allowed`() {

        val puzzle = TestPuzzleProvider.easy5x5

        val cats = setOf(
            Position(0, 2)
        )

        val result = validator.canPlaceCat(
            puzzle = puzzle,
            cats = cats,
            position = Position(0, 4)
        )

        assertFalse(result)
    }


    @Test
    fun `cat in same column is not allowed`() {

        val puzzle = TestPuzzleProvider.easy5x5

        val cats = setOf(
            Position(
                row = 0,
                column = 2
            )
        )

        val result = validator.canPlaceCat(
            puzzle = puzzle,
            cats = cats,
            position = Position(
                row = 3,
                column = 2
            )
        )

        assertFalse(result)
    }


    @Test
    fun `adjacent diagonal cat is not allowed`() {

        val puzzle = TestPuzzleProvider.easy5x5

        val cats = setOf(
            Position(0, 2)
        )

        val result = validator.canPlaceCat(
            puzzle = puzzle,
            cats = cats,
            position = Position(1, 3)
        )

        assertFalse(result)
    }

    @Test
    fun `test puzzle has two solutions`() {
        val validator = RuleValidator()
        val counter = SolutionCounter(
            ruleValidator = validator
        )
        val puzzle = TestPuzzleProvider.easy5x5
        val count = counter.countSolutions(puzzle)
        assertEquals(
            2,
            count
        )
    }
}
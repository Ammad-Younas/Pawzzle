package com.madi.pawzzle.domain.model

data class Puzzle (
    val id: Long,
    val size: Int,
    val regions: List<List<Int>>,
    val solution: Set<Position>,
    val difficulty: Difficulty
)
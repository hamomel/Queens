package com.hamomel.queens.game.ui

import com.hamomel.queens.data.Board
import com.hamomel.queens.data.Position

data class QueensGameViewState(
    val board: Board,
    val conflicts: List<Position> = emptyList()
)

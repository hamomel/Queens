package com.hamomel.queens.game.ui

import com.hamomel.queens.data.Board
import com.hamomel.queens.data.Position

data class QueensGameViewState(
    val board: Board,
    @Suppress("ArrayInDataClass") // I intentionally use array so that it is compared by reference
    val conflicts: Array<Position> = emptyArray(),
)

package com.hamomel.queens.game

import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.hamomel.queens.data.Board
import com.hamomel.queens.data.Position

data class QueensGameViewState(
    val board: Board,
    @Suppress("ArrayInDataClass") // I intentionally use array so that it is compared by reference
    val conflicts: Array<Position> = emptyArray(),
    val hapticFeedbackType: HapticFeedbackType? = null,
    val isWin: Boolean = false,
)

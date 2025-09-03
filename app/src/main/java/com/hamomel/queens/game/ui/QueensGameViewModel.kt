package com.hamomel.queens.game.ui

import androidx.lifecycle.ViewModel
import com.hamomel.queens.data.Board
import com.hamomel.queens.data.Position
import com.hamomel.queens.data.WhiteQueen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QueensGameViewModel(
    boardSize: Int
) : ViewModel() {
    private val _viewState = MutableStateFlow(QueensGameViewState(board = Board(boardSize)))
    val viewState: StateFlow<QueensGameViewState> = _viewState.asStateFlow()

    fun onSquareClick(position: Position) {
        val pieceOnPosition = _viewState.value.board.getPiece(position)
        if (pieceOnPosition == null) {
            trySetQueen(position)
        } else {
            mutateState { it.board.removePiece(position) }
        }
    }

    private fun trySetQueen(position: Position) {
        val conflicts = findConflicts(_viewState.value.board, position)

        if (conflicts.isNotEmpty()) {
            mutateState { it.copy(conflicts = conflicts) }
        } else {
            mutateState { it.board.setPiece(WhiteQueen, position) }
        }
    }

    private fun findConflicts(board: Board, position: Position): List<Position> {
        val conflicts = mutableListOf<Position>()

        // Check row and column
        for (i in 0 until board.size) {
            val line = Position(position.line, i)
            val column = Position(i, position.column)

            if (i != position.column && board.getPiece(line) != null) {
                conflicts.add(line)
            }
            if (i != position.line && board.getPiece(column) != null) {
                conflicts.add(column)
            }
        }

        // Check diagonals
        for (i in -board.size until board.size) {
            if (i != 0) {
                val diagonal1 = Position(position.line + i, position.column + i)
                val diagonal2 = Position(position.line + i, position.column - i)

                if (diagonal1.line in 0 until board.size
                    && diagonal1.column in 0 until board.size
                    && board.getPiece(diagonal1) != null
                ) {
                    conflicts.add(diagonal1)
                }
                if (diagonal2.line in 0 until board.size
                    && diagonal2.column in 0 until board.size
                    && board.getPiece(diagonal2) != null
                ) {
                    conflicts.add(diagonal2)
                }
            }
        }

        return conflicts
    }

    private fun mutateState(mutation: (QueensGameViewState) -> Unit) {
        val currentState = _viewState.value
        mutation(currentState)
        _viewState.value = currentState
    }
}
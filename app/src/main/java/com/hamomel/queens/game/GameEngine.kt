package com.hamomel.queens.game

import com.hamomel.queens.data.Board
import com.hamomel.queens.data.Position

class GameEngine {
    fun findConflicts(board: Board, position: Position): List<Position> {
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

    fun isWin(board: Board): Boolean {
        val pieces = board.getAllPieces()
        for (piece in pieces) {
            val conflicts = findConflicts(board, piece.position)
            if (conflicts.isNotEmpty()) {
                return false
            }
        }
        return pieces.size == board.size
    }
}
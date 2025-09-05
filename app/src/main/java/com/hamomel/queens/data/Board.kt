package com.hamomel.queens.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class Board(
    val size: Int,
    private val board: SnapshotStateList<SnapshotStateList<Piece?>> =
        mutableStateListOf<SnapshotStateList<Piece?>>().apply {
            repeat(size) {
                add(mutableStateListOf<Piece?>().apply {
                    repeat(size) { add(null) }
                })
            }
        }
) {

    fun setPiece(piece: Piece, position: Position) {
        require(isOnBoard(position)) {
            "Coordinates are out of the board. Board size: $size, line: ${position.line}, column: ${position.column}"
        }
        board[position.line][position.column] = piece
    }

    fun getPiece(position: Position): Piece? = board[position.line][position.column]

    fun removePiece(position: Position) {
        require(isOnBoard(position)) {
            "Coordinates are out of the board. Board size: $size, line: ${position.line}, column: ${position.column}"
        }
        board[position.line][position.column] = null
    }

    private fun isOnBoard(position: Position) =
        position.line >= 0 && position.line <= size && position.column >= 0 && position.column <= size

    companion object {
        const val  DEFAULT_SIZE = 8
    }
}

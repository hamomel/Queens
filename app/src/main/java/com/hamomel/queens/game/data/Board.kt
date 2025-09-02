package com.hamomel.queens.game.data

data class Board(
    val size: Int,
    private val board: MutableList<MutableList<Piece?>> = MutableList(size = size) { MutableList(size = size){null} }
) {

    fun setPiece(piece: Piece, line: Int, column: Int) {
        require(line >= 0 && line <= size && column >= 0 && column <= size) {
            "Coordinates are out of the board. Board size: $size, line: $line, column: $column"
        }
        board[line][column] = piece
    }

    fun getPiece(line: Int, column: Int): Piece? = board[line][column]
}
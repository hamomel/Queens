package com.hamomel.queens.data

import androidx.annotation.DrawableRes
import com.hamomel.queens.R

sealed class Piece(
    open val color: PieceColor,
    @param:DrawableRes val iconRes: Int
)

data object WhiteQueen : Piece(PieceColor.WHITE,R.drawable.piece_queen_white)
data object BlackQueen : Piece(PieceColor.WHITE,R.drawable.piece_queen_black)

enum class PieceColor {
    BLACK, WHITE
}

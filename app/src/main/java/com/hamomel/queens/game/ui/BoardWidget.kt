package com.hamomel.queens.game.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.hamomel.queens.game.data.BlackQueen
import com.hamomel.queens.game.data.Board
import com.hamomel.queens.game.data.WhiteQueen
import com.hamomel.queens.ui.theme.LocalCustomColors
import com.hamomel.queens.ui.theme.QueensTheme

@Composable
fun BoardWidget(
    board: Board,
    modifier: Modifier
) {
    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f)
    ) {
        val squareSize = this.maxWidth / board.size

        Column {
            repeat(board.size) { rowIndex ->
                val rowNumber = board.size - rowIndex - 1 // start  rows count from 0, for better compatibility
                Row {
                    repeat(board.size) { columnNumber ->
                        Square(rowNumber, columnNumber, squareSize, board)
                    }
                }
            }
        }
    }
}

@Composable
private fun Square(
    rowNumber: Int,
    columnNumber: Int,
    squareSize: Dp,
    board: Board
) {
    val isWhite = (rowNumber + columnNumber) % 2 == 1
    val squareColor = if (isWhite) {
        LocalCustomColors.current.whiteSquare
    } else {
        LocalCustomColors.current.blackSquare
    }

    val labelColor = if (isWhite) {
        LocalCustomColors.current.blackSquare
    } else {
        LocalCustomColors.current.whiteSquare
    }

    Box(
        modifier = Modifier.Companion
            .size(squareSize)
            .background(squareColor)
    ) {
        if (columnNumber == 0) {
            val label = (rowNumber + 1).toString()
            SquareLabel(label, labelColor, Alignment.Companion.TopStart)
        }
        if (rowNumber == 0) {
            val label = (Char(97) + columnNumber).toString() // 97 = 'a'
            SquareLabel(label, labelColor, Alignment.Companion.BottomEnd)
        }

        val piece = board.getPiece(rowNumber, columnNumber)

        piece?.let { piece ->
            Image(
                painter = painterResource(piece.iconRes),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .padding(squareSize * 0.1f),
            )
        }
    }
}

@Composable
private fun BoxScope.SquareLabel(
    label: String,
    color: Color,
    alignment: Alignment,
) {
    Text(
        text = label,
        modifier = Modifier.Companion
            .padding(2.dp)
            .align(alignment),
        style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 12.sp / LocalDensity.current.fontScale, // prevent font from scaling
            lineHeight = 1.em,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.Both
            )
        ),
        color = color,
    )
}

@Preview
@Composable
private fun BoardPreview() {
    val board = Board(6)
    board.setPiece(WhiteQueen, 0, 0)
    board.setPiece(BlackQueen, 5, 0)
    board.setPiece(WhiteQueen, 2, 5)
    board.setPiece(BlackQueen, 5, 5)

    QueensTheme {
        BoardWidget(board = board, modifier = Modifier)
    }
}
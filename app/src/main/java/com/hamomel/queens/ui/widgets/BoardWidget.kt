package com.hamomel.queens.ui.widgets

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.hamomel.queens.data.BlackQueen
import com.hamomel.queens.data.Board
import com.hamomel.queens.data.Piece
import com.hamomel.queens.data.Position
import com.hamomel.queens.data.WhiteQueen
import com.hamomel.queens.ui.theme.LocalCustomColors
import com.hamomel.queens.ui.theme.QueensTheme
import kotlinx.coroutines.CancellationException

@Composable
fun BoardWidget(
    board: Board,
    conflicts: Array<Position>,
    modifier: Modifier = Modifier,
    onSquareClick: (Position) -> Unit,
    onConflictsShown: () -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f)
    ) {
        val squareSize = this.maxWidth / board.size

        Column {
            repeat(board.size) { rowIndex ->
                val rowNumber =
                    board.size - rowIndex - 1 // start  rows count from 0, for better compatibility

                Row {
                    repeat(board.size) { columnNumber ->

                        val isWhite = (rowNumber + columnNumber) % 2 == 1
                        val isConflict =
                            conflicts.any { it.line == rowNumber && it.column == columnNumber }
                        val squareColor = getSquareColor(isWhite)
                        var animatedSquareColor by remember(board.size) { mutableStateOf(squareColor) }

                        if (isConflict) {
                            val errorColor = LocalCustomColors.current.error

                            LaunchedEffect(conflicts) {
                                try {
                                    animate(
                                        initialValue = 0f,
                                        targetValue = 1f,
                                        animationSpec = tween(
                                            durationMillis = 2000,
                                            easing = FastOutSlowInEasing
                                        )
                                    ) { value, _ ->
                                        animatedSquareColor =
                                            lerp(errorColor, squareColor, value)
                                    }
                                    onConflictsShown()
                                } catch (e: CancellationException) {
                                    animatedSquareColor = squareColor
                                    throw e
                                }
                            }
                        }

                        Square(
                            size = squareSize,
                            color = animatedSquareColor,
                            piece = board.getPiece(Position(rowNumber, columnNumber)),
                            leftLabel = getLeftLabel(columnNumber, rowNumber),
                            bottomLabel = getBottomLabel(columnNumber, rowNumber),
                            labelColor = getLabelColor(isWhite),
                            onClick = { onSquareClick(Position(rowNumber, columnNumber)) }
                        )
                    }
                }
            }
        }
    }
}


private fun getLeftLabel(columnNumber: Int, rowNumber: Int): String? =
    if (columnNumber == 0) {
        (rowNumber + 1).toString()
    } else {
        null
    }

fun getBottomLabel(columnNumber: Int, rowNumber: Int): String? =
    if (rowNumber == 0) {
        (Char(97) + columnNumber).toString() // 97 = 'a'
    } else {
        null
    }

@Composable
private fun getLabelColor(isWhite: Boolean): Color = if (isWhite) {
    LocalCustomColors.current.blackSquare
} else {
    LocalCustomColors.current.whiteSquare
}

@Composable
private fun getSquareColor(isWhite: Boolean): Color = if (isWhite) {
    LocalCustomColors.current.whiteSquare
} else {
    LocalCustomColors.current.blackSquare
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Square(
    size: Dp,
    color: Color,
    piece: Piece? = null,
    pieceTint: Color? = null,
    leftLabel: String? = null,
    bottomLabel: String? = null,
    labelColor: Color = LocalCustomColors.current.blackSquare,
    onClick: () -> Unit
) {
    CompositionLocalProvider(
        LocalRippleConfiguration provides RippleConfiguration(color = labelColor)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .background(color)
                .clickable { onClick() },
        ) {
            leftLabel?.let { label ->
                SquareLabel(label, labelColor, Alignment.TopStart)
            }
            bottomLabel?.let { label ->
                SquareLabel(label, labelColor, Alignment.BottomEnd)
            }
            piece?.let { piece ->
                Image(
                    painter = painterResource(piece.iconRes),
                    contentDescription = null,
                    colorFilter = pieceTint?.let { ColorFilter.tint(it) },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .padding(size * 0.1f),
                )
            }
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
    val board = Board(11)
    board.setPiece(WhiteQueen, Position(0, 0))
    board.setPiece(BlackQueen, Position(5, 0))
    board.setPiece(WhiteQueen, Position(2, 5))
    board.setPiece(BlackQueen, Position(5, 5))

    QueensTheme {
        BoardWidget(
            board = board,
            modifier = Modifier,
            conflicts = arrayOf(Position(5, 0)),
            onSquareClick = {},
            onConflictsShown = {}
        )
    }
}
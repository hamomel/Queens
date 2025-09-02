package com.hamomel.queens.game

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hamomel.queens.ui.theme.LocalCustomColors
import com.hamomel.queens.ui.theme.QueensTheme

@Composable
fun Board(
    boardSize: Int,
    modifier: Modifier
) {
    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f)
    ) {

        val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

        val squareSize = if (isPortrait) {
            this.maxWidth / boardSize
        } else {
            this.maxHeight / boardSize
        }

        Column {
            repeat(boardSize) { rowIndex ->
                val rowNumber =
                    boardSize - rowIndex - 1 // start  rows count from 0, for better compatibility
                Row(
                    modifier = Modifier.Companion
                        .height(squareSize)
                ) {
                    repeat(boardSize) { columnIndex ->
                        val isWhite = (rowNumber + columnIndex) % 2 == 1
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
                            if (columnIndex == 0) {
                                val label = (rowNumber + 1).toString()
                                SquareLabel(label, labelColor, Alignment.Companion.TopStart)
                            }
                            if (rowNumber == 0) {
                                val label = (Char(97) + columnIndex).toString() // 97 = 'a'
                                SquareLabel(label, labelColor, Alignment.Companion.BottomEnd)
                            }
                        }
                    }
                }
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
            .padding(4.dp)
            .align(alignment),
        style = MaterialTheme.typography.bodySmall,
        color = color,
    )
}

@Preview
@Composable
private fun BoardPreview() {
    QueensTheme {
        Board(boardSize = 8, modifier = Modifier)
    }
}
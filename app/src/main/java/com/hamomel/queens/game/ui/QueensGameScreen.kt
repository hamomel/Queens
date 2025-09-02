package com.hamomel.queens.game.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hamomel.queens.game.data.Board
import com.hamomel.queens.ui.theme.QueensTheme

@Composable
fun QueensGameScreen(
    board: Board
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            BoardWidget(
                board= board,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
@Preview
private fun QueensGamePreview() {
    QueensTheme {
        QueensGameScreen(Board(8))
    }
}
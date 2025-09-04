package com.hamomel.queens.game.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hamomel.queens.data.Board
import com.hamomel.queens.data.Position
import com.hamomel.queens.ui.theme.QueensTheme
import com.hamomel.queens.ui.widgets.BoardWidget
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun QueensGameScreen(boardSize: Int) {
    val viewModel = koinViewModel<QueensGameViewModel> { parametersOf(boardSize) }
    val viewState by viewModel.viewState.collectAsState()

    QueensGameContent(
        state = viewState,
        onSquareClick = viewModel::onSquareClick,
        onConflictsShown = viewModel::onConflictsShown
    )
}

@Composable
private fun QueensGameContent(
    state: QueensGameViewState,
    onSquareClick: (Position) -> Unit,
    onConflictsShown: () -> Unit,
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            BoardWidget(
                board = state.board,
                conflicts = state.conflicts,
                modifier = Modifier.align(Alignment.Center),
                onSquareClick = onSquareClick,
                onConflictsShown = onConflictsShown
            )
        }
    }
}

@Composable
@Preview
private fun QueensGamePreview() {
    QueensTheme {
        QueensGameContent(
            state = QueensGameViewState(board = Board(8)),
            onSquareClick = {},
            onConflictsShown = {}
        )
    }
}

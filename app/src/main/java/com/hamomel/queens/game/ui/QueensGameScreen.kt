package com.hamomel.queens.game.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hamomel.queens.R
import com.hamomel.queens.data.Board
import com.hamomel.queens.data.Position
import com.hamomel.queens.ui.theme.QueensTheme
import com.hamomel.queens.ui.widgets.BoardWidget
import com.hamomel.queens.ui.widgets.QueensButtonLarge
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun QueensGameScreen(boardSize: Int) {
    val viewModel = koinViewModel<QueensGameViewModel> { parametersOf(boardSize) }
    val viewState by viewModel.viewState.collectAsState()

    QueensGameContent(
        state = viewState,
        onSquareClick = viewModel::onSquareClick,
        onResetClick = viewModel::onResetClick,
        onConflictsShown = viewModel::onConflictsShown
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QueensGameContent(
    state: QueensGameViewState,
    onSquareClick: (Position) -> Unit,
    onResetClick: () -> Unit ,
    onConflictsShown: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                Text(
                    text = stringResource(R.string.game_screen_title, state.board.size),
                    style = MaterialTheme.typography.titleLarge
                )
            })
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            BoardWidget(
                board = state.board,
                conflicts = state.conflicts,
                modifier = Modifier,
                onSquareClick = onSquareClick,
                onConflictsShown = onConflictsShown
            )

            Spacer(Modifier.height(64.dp))

            QueensButtonLarge(
                onClick = onResetClick,
                text = stringResource(R.string.reset_game_button_label),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
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
            onResetClick = {},
            onConflictsShown = {}
        )
    }
}

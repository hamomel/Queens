package com.hamomel.queens.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hamomel.queens.R
import com.hamomel.queens.data.Board
import com.hamomel.queens.data.Position
import com.hamomel.queens.ui.theme.QueensTheme
import com.hamomel.queens.ui.widgets.BoardWidget
import com.hamomel.queens.ui.widgets.QueensButtonLarge
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object GameRoute

@Composable
fun QueensGameScreen(
    boardSize: StateFlow<Int>,
    onChoseBoardSizeNavigate: (Int) -> Unit,
    onWinNavigate: (Int) -> Unit,
    viewModel: QueensGameViewModel = koinViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        boardSize.collect { size ->
            viewModel.onBoardSizeChanged(size)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is QueensGameNavigationEvent.ChooseBoardSize -> onChoseBoardSizeNavigate(event.currentSize)
                is QueensGameNavigationEvent.Win -> onWinNavigate(event.boardSize)
            }
        }
    }

    val hapticFeedbackType = viewState.hapticFeedbackType
    if (hapticFeedbackType != null) {
        val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
        haptic.performHapticFeedback(hapticFeedbackType)
        viewModel.onHapticFeedbackConsumed()
    }

    QueensGameContent(
        state = viewState,
        onSquareClick = viewModel::onSquareClick,
        onResetClick = viewModel::onResetClick,
        onConflictsShown = viewModel::onConflictsShown,
        onChooseBoardSizeClick = { viewModel.onChooseBoardSizeClick() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QueensGameContent(
    state: QueensGameViewState,
    onSquareClick: (Position) -> Unit,
    onResetClick: () -> Unit,
    onConflictsShown: () -> Unit,
    onChooseBoardSizeClick: () -> Unit = { }
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.game_screen_title, state.board.size),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = onChooseBoardSizeClick) {
                        Icon(
                            painter = painterResource(R.drawable.icon_chess_board),
                            contentDescription = stringResource(R.string.game_choose_board_size_button_content_description),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 32.dp)
        ) {
            val countToWinText = if (state.isWin) {
                stringResource(R.string.game_win_message)
            } else {
                (state.board.size - state.board.getAllPieces().size).toString()
            }

            Text(
                text = countToWinText,
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = if (!state.isWin) stringResource(R.string.game_queens_to_win) else "",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(16.dp))

            BoardWidget(
                board = state.board,
                conflicts = state.conflicts,
                modifier = Modifier.weight(1f),
                onSquareClick = onSquareClick,
                onConflictsShown = onConflictsShown
            )

            Spacer(Modifier.height(16.dp))

            QueensButtonLarge(
                onClick = onResetClick,
                text = if (state.isWin) {
                    stringResource(R.string.game_new_game)
                } else {
                    stringResource(R.string.game_reset_game_button_label)
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
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

package com.hamomel.queens.game

import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamomel.queens.data.Board
import com.hamomel.queens.data.Position
import com.hamomel.queens.data.WhiteQueen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val BOARD_SIZE_KEY = "boardSize"

class QueensGameViewModel(
    val savedStateHandle: SavedStateHandle,
    val gameEngine: GameEngine
) : ViewModel() {
    private val boardSize: Int
        get() = savedStateHandle.get<Int>(BOARD_SIZE_KEY)
            ?.takeIf { it > 0 }
            ?: Board.DEFAULT_SIZE

    private val _viewState = MutableStateFlow(QueensGameViewState(board = Board(boardSize)))
    val viewState: StateFlow<QueensGameViewState> = _viewState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<QueensGameNavigationEvent>()
    val navigationEvents: Flow<QueensGameNavigationEvent> = _navigationEvents

    fun onSquareClick(position: Position) {
        if (_viewState.value.isWin) return

        val pieceOnPosition = _viewState.value.board.getPiece(position)

        if (pieceOnPosition == null) {
            trySetQueen(position)
        } else {
            mutateState { it.apply { board.removePiece(position) } }
        }
    }

    private fun trySetQueen(position: Position) {
        val conflicts = gameEngine.findConflicts(_viewState.value.board, position)

        if (conflicts.isNotEmpty()) {
            mutateState {
                it.copy(
                    conflicts = conflicts.toTypedArray(),
                    hapticFeedbackType = HapticFeedbackType.Reject
                )
            }
        } else {
            val currentBoard = _viewState.value.board
            currentBoard.setPiece(WhiteQueen, position)

            val isWin = gameEngine.isWin(currentBoard)

            mutateState { state ->
                state.copy(
                    conflicts = emptyArray(),
                    hapticFeedbackType = HapticFeedbackType.Confirm,
                    isWin = isWin
                )
            }

            if (isWin) {
                navigateToWinScreen()
            }
        }
    }

    private fun navigateToWinScreen() {
        val currentState = _viewState.value
        viewModelScope.launch {
            _navigationEvents.emit(QueensGameNavigationEvent.Win(currentState.board.size))
        }
    }

    fun onBoardSizeChanged(newSize: Int) {
        if (newSize != _viewState.value.board.size) {
            savedStateHandle[BOARD_SIZE_KEY] = newSize
            mutateState { QueensGameViewState(board = Board(newSize)) }
        }
    }

    fun onResetClick() {
        mutateState {
            QueensGameViewState(
                board = Board(boardSize),
                hapticFeedbackType = HapticFeedbackType.LongPress
            )
        }
    }

    fun onConflictsShown() {
        mutateState { it.copy(conflicts = emptyArray()) }
    }

    fun onHapticFeedbackConsumed() {
        mutateState { it.copy(hapticFeedbackType = null) }
    }

    fun onChooseBoardSizeClick() {
        viewModelScope.launch {
            _navigationEvents.emit(QueensGameNavigationEvent.ChooseBoardSize(boardSize))
        }
    }

    private fun mutateState(mutation: (QueensGameViewState) -> QueensGameViewState) {
        val currentState = _viewState.value
        _viewState.value = mutation(currentState)
    }
}
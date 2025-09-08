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
        val conflicts = findConflicts(_viewState.value.board, position)

        if (conflicts.isNotEmpty()) {
            mutateState { it.copy(
                conflicts = conflicts.toTypedArray(),
                hapticFeedbackType = HapticFeedbackType.Reject
            ) }
        } else {
            mutateState { state ->
                state.apply { board.setPiece(WhiteQueen, position) }
                state.copy(
                    conflicts = emptyArray(),
                    hapticFeedbackType = HapticFeedbackType.Confirm
                )
            }
            checkWinCondition()
        }
    }

    private fun checkWinCondition() {
        val currentState = _viewState.value
        val allPieces = currentState.board.getAllPieces()

        if (allPieces.size == currentState.board.size) {
            viewModelScope.launch {
                _navigationEvents.emit(QueensGameNavigationEvent.Win(currentState.board.size))
            }
        }
    }

    fun onBoardSizeChanged(newSize: Int) {
        if (newSize != _viewState.value.board.size) {
            savedStateHandle[BOARD_SIZE_KEY] = newSize
            mutateState { QueensGameViewState(board = Board(newSize)) }
        }
    }

    fun onResetClick() {
        mutateState { QueensGameViewState(board = Board(boardSize)) }
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

    private fun findConflicts(board: Board, position: Position): List<Position> {
        val conflicts = mutableListOf<Position>()

        // Check row and column
        for (i in 0 until board.size) {
            val line = Position(position.line, i)
            val column = Position(i, position.column)

            if (i != position.column && board.getPiece(line) != null) {
                conflicts.add(line)
            }
            if (i != position.line && board.getPiece(column) != null) {
                conflicts.add(column)
            }
        }

        // Check diagonals
        for (i in -board.size until board.size) {
            if (i != 0) {
                val diagonal1 = Position(position.line + i, position.column + i)
                val diagonal2 = Position(position.line + i, position.column - i)

                if (diagonal1.line in 0 until board.size
                    && diagonal1.column in 0 until board.size
                    && board.getPiece(diagonal1) != null
                ) {
                    conflicts.add(diagonal1)
                }
                if (diagonal2.line in 0 until board.size
                    && diagonal2.column in 0 until board.size
                    && board.getPiece(diagonal2) != null
                ) {
                    conflicts.add(diagonal2)
                }
            }
        }

        return conflicts
    }

    private fun mutateState(mutation: (QueensGameViewState) -> QueensGameViewState) {
        val currentState = _viewState.value
        _viewState.value = mutation(currentState)
    }
}
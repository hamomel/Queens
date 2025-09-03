package com.hamomel.queens.game.ui

import androidx.lifecycle.ViewModel
import com.hamomel.queens.data.Board
import com.hamomel.queens.data.Position
import com.hamomel.queens.data.WhiteQueen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QueensGameViewModel(
    boardSize: Int
) : ViewModel() {
    private val _viewState = MutableStateFlow(QueensGameViewState(board = Board(boardSize)))
    val viewState: StateFlow<QueensGameViewState> = _viewState.asStateFlow()

    fun onSquareClick(position: Position) {
        val pieceOnPosition = _viewState.value.board.getPiece(position)
        if (pieceOnPosition == null) {
            mutateState {  it.board.setPiece(WhiteQueen, position) }
        } else {
            mutateState { it.board.removePiece(position) }
        }
    }

    private fun mutateState(mutation: (QueensGameViewState) -> Unit) {
        val currentState = _viewState.value
        mutation(currentState)
        _viewState.value = currentState
    }
}

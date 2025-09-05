package com.hamomel.queens.game.ui

sealed class QueensGameNavigationEvent {
    class ChooseBoardSize(val currentSize: Int) : QueensGameNavigationEvent()
}

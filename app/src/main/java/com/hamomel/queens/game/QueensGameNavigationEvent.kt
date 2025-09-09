package com.hamomel.queens.game

sealed class QueensGameNavigationEvent {
    data class ChooseBoardSize(val currentSize: Int) : QueensGameNavigationEvent()
    data class Win(val boardSize: Int) : QueensGameNavigationEvent()
}
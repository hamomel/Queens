package com.hamomel.queens.game

sealed class QueensGameNavigationEvent {
    class ChooseBoardSize(val currentSize: Int) : QueensGameNavigationEvent()
    class Win(val boardSize: Int) : QueensGameNavigationEvent()
}
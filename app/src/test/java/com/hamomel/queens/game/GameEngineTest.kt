package com.hamomel.queens.game

import com.hamomel.queens.data.Board
import com.hamomel.queens.data.Position
import com.hamomel.queens.data.WhiteQueen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameEngineTest {

    private lateinit var gameEngine: GameEngine
    private lateinit var board: Board

    @Before
    fun setup() {
        gameEngine = GameEngine()
        board = Board(8)
    }

    @Test
    fun `findConflicts returns empty list when no pieces on board`() {
        val position = Position(0, 0)
        val conflicts = gameEngine.findConflicts(board, position)
        assertTrue(conflicts.isEmpty())
    }

    @Test
    fun `findConflicts detects horizontal conflict`() {
        board.setPiece(WhiteQueen, Position(0, 1))

        val conflicts = gameEngine.findConflicts(board, Position(0, 3))

        assertEquals(1, conflicts.size)
        assertEquals(Position(0, 1), conflicts[0])
    }

    @Test
    fun `findConflicts detects vertical conflict`() {
        board.setPiece(WhiteQueen, Position(1, 0))

        val conflicts = gameEngine.findConflicts(board, Position(3, 0))

        assertEquals(1, conflicts.size)
        assertEquals(Position(1, 0), conflicts[0])
    }

    @Test
    fun `findConflicts detects diagonal conflict top-left to bottom-right`() {
        board.setPiece(WhiteQueen, Position(1, 1))

        val conflicts = gameEngine.findConflicts(board, Position(3, 3))

        assertEquals(1, conflicts.size)
        assertEquals(Position(1, 1), conflicts[0])
    }

    @Test
    fun `findConflicts detects diagonal conflict top-right to bottom-left`() {
        board.setPiece(WhiteQueen, Position(1, 6))

        val conflicts = gameEngine.findConflicts(board, Position(3, 4))

        assertEquals(1, conflicts.size)
        assertEquals(Position(1, 6), conflicts[0])
    }

    @Test
    fun `findConflicts detects multiple conflicts`() {
        board.setPiece(WhiteQueen, Position(0, 1)) // horizontal
        board.setPiece(WhiteQueen, Position(2, 0)) // vertical
        board.setPiece(WhiteQueen, Position(1, 1)) // diagonal

        val conflicts = gameEngine.findConflicts(board, Position(0, 0))

        assertEquals(3, conflicts.size)
        assertTrue(conflicts.contains(Position(0, 1)))
        assertTrue(conflicts.contains(Position(2, 0)))
        assertTrue(conflicts.contains(Position(1, 1)))
    }

    @Test
    fun `findConflicts handles corner positions`() {
        board.setPiece(WhiteQueen, Position(1, 1))

        val conflicts = gameEngine.findConflicts(board, Position(7, 7))

        assertTrue(conflicts.size == 1)
        assertEquals(Position(1, 1), conflicts[0])
    }

    @Test
    fun `findConflicts handles edge positions with diagonal conflicts`() {
        board.setPiece(WhiteQueen, Position(0, 0))

        val conflicts = gameEngine.findConflicts(board, Position(7, 7))

        assertEquals(1, conflicts.size)
        assertEquals(Position(0, 0), conflicts[0])
    }

    @Test
    fun `isWin returns false when board is empty`() {
        assertFalse(gameEngine.isWin(board))
    }

    @Test
    fun `isWin returns false when not enough pieces`() {
        board.setPiece(WhiteQueen, Position(0, 0))
        board.setPiece(WhiteQueen, Position(1, 2))

        assertFalse(gameEngine.isWin(board))
    }

    @Test
    fun `isWin returns true when pieces equal board size and no conflicts`() {
        // Place 8 queens in a valid solution for 8x8 board
        board.setPiece(WhiteQueen, Position(0, 0))
        board.setPiece(WhiteQueen, Position(1, 4))
        board.setPiece(WhiteQueen, Position(2, 7))
        board.setPiece(WhiteQueen, Position(3, 5))
        board.setPiece(WhiteQueen, Position(4, 2))
        board.setPiece(WhiteQueen, Position(5, 6))
        board.setPiece(WhiteQueen, Position(6, 1))
        board.setPiece(WhiteQueen, Position(7, 3))

        assertTrue(gameEngine.isWin(board))
    }

    @Test
    fun `isWin returns false when pieces equal board size but have conflicts`() {
        // Place 8 queens on diagonal (all conflict with each other)
        for (i in 0 until 8) {
            board.setPiece(WhiteQueen, Position(i, i))
        }

        assertFalse(gameEngine.isWin(board))
    }

    @Test
    fun `isWin works with different board sizes`() {
        val smallBoard = Board(4)

        assertFalse(gameEngine.isWin(smallBoard))

        // Place 4 queens in a valid solution for 4x4 board
        smallBoard.setPiece(WhiteQueen, Position(0, 1))
        smallBoard.setPiece(WhiteQueen, Position(1, 3))
        smallBoard.setPiece(WhiteQueen, Position(2, 0))
        smallBoard.setPiece(WhiteQueen, Position(3, 2))

        assertTrue(gameEngine.isWin(smallBoard))
    }

    @Test
    fun `findConflicts handles all directions from center position`() {
        val centerPosition = Position(4, 4)

        // Place queens in all 8 directions
        board.setPiece(WhiteQueen, Position(4, 0)) // horizontal left
        board.setPiece(WhiteQueen, Position(4, 7)) // horizontal right
        board.setPiece(WhiteQueen, Position(0, 4)) // vertical up
        board.setPiece(WhiteQueen, Position(7, 4)) // vertical down
        board.setPiece(WhiteQueen, Position(0, 0)) // diagonal up-left
        board.setPiece(WhiteQueen, Position(7, 7)) // diagonal down-right
        board.setPiece(WhiteQueen, Position(1, 7)) // diagonal up-right
        board.setPiece(WhiteQueen, Position(7, 1)) // diagonal down-left

        val conflicts = gameEngine.findConflicts(board, centerPosition)

        assertEquals(8, conflicts.size)
    }

    @Test
    fun `findConflicts does not include same position as conflict`() {
        board.setPiece(WhiteQueen, Position(0, 0))

        val conflicts = gameEngine.findConflicts(board, Position(0, 0))

        assertTrue(conflicts.isEmpty())
    }
}
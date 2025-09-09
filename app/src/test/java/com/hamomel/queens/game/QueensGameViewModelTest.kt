package com.hamomel.queens.game

import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.hamomel.queens.data.Board
import com.hamomel.queens.data.Position
import com.hamomel.queens.data.WhiteQueen
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QueensGameViewModelTest {

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var gameEngine: GameEngine
    private lateinit var viewModel: QueensGameViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = mockk(relaxed = true)
        gameEngine = mockk()

        every { savedStateHandle.get<Int>("boardSize") } returns null

        viewModel = QueensGameViewModel(savedStateHandle, gameEngine)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has default board size`() {
        val initialState = viewModel.viewState.value
        assertEquals(Board.DEFAULT_SIZE, initialState.board.size)
        assertFalse(initialState.isWin)
        assertEquals(0, initialState.conflicts.size)
        assertNull(initialState.hapticFeedbackType)
    }

    @Test
    fun `board size from saved state is used`() {
        every { savedStateHandle.get<Int>("boardSize") } returns 10

        val newViewModel = QueensGameViewModel(savedStateHandle, gameEngine)
        assertEquals(10, newViewModel.viewState.value.board.size)
    }

    @Test
    fun `onSquareClick adds queen when no conflicts`() = runTest {
        val position = Position(0, 0)
        every { gameEngine.findConflicts(any(), position) } returns emptyList()
        every { gameEngine.isWin(any()) } returns false

        viewModel.onSquareClick(position)

        val state = viewModel.viewState.value
        assertEquals(WhiteQueen, state.board.getPiece(position))
        assertEquals(HapticFeedbackType.Confirm, state.hapticFeedbackType)
        assertEquals(0, state.conflicts.size)
    }

    @Test
    fun `onSquareClick shows conflicts when queen placement conflicts`() = runTest {
        val position = Position(0, 0)
        val conflictPosition = Position(0, 1)
        val conflicts = listOf(conflictPosition)

        every { gameEngine.findConflicts(any(), position) } returns conflicts

        viewModel.onSquareClick(position)

        val state = viewModel.viewState.value
        assertNull(state.board.getPiece(position))
        assertEquals(HapticFeedbackType.Reject, state.hapticFeedbackType)
        assertEquals(1, state.conflicts.size)
        assertEquals(conflictPosition, state.conflicts[0])
    }

    @Test
    fun `onSquareClick removes queen when piece exists on position`() = runTest {
        val position = Position(0, 0)
        // First place a queen
        every { gameEngine.findConflicts(any(), position) } returns emptyList()
        every { gameEngine.isWin(any()) } returns false
        viewModel.onSquareClick(position)

        // Then remove it
        viewModel.onSquareClick(position)

        val state = viewModel.viewState.value
        assertNull(state.board.getPiece(position))
    }

    @Test
    fun `onSquareClick triggers win when game is won`() = runTest {
        val position = Position(0, 0)
        every { gameEngine.findConflicts(any(), position) } returns emptyList()
        every { gameEngine.isWin(any()) } returns true

        viewModel.navigationEvents.test {
            viewModel.onSquareClick(position)

            val state = viewModel.viewState.value
            assertTrue(state.isWin)

            val event = awaitItem()
            assertTrue(event is QueensGameNavigationEvent.Win)
            assertEquals(Board.DEFAULT_SIZE, (event as QueensGameNavigationEvent.Win).boardSize)
        }
    }

    @Test
    fun `onSquareClick does nothing when game is already won`() = runTest {
        // Set up win state
        val position = Position(0, 0)
        every { gameEngine.findConflicts(any(), position) } returns emptyList()
        every { gameEngine.isWin(any()) } returns true
        viewModel.onSquareClick(position)

        // Try to click again
        val previousState = viewModel.viewState.value
        viewModel.onSquareClick(Position(1, 1))

        assertEquals(previousState, viewModel.viewState.value)
    }

    @Test
    fun `onBoardSizeChanged updates board size and saves to state handle`() = runTest {
        val newSize = 10

        viewModel.onBoardSizeChanged(newSize)

        verify { savedStateHandle["boardSize"] = newSize }
        assertEquals(newSize, viewModel.viewState.value.board.size)
    }

    @Test
    fun `onBoardSizeChanged does nothing when size is same`() = runTest {
        val currentSize = viewModel.viewState.value.board.size

        viewModel.onBoardSizeChanged(currentSize)

        verify(exactly = 0) { savedStateHandle["boardSize"] = any<Int>() }
    }

    @Test
    fun `onResetClick resets board and triggers haptic feedback`() = runTest {
        // Place a queen first
        val position = Position(0, 0)
        every { gameEngine.findConflicts(any(), position) } returns emptyList()
        every { gameEngine.isWin(any()) } returns false
        viewModel.onSquareClick(position)

        viewModel.onResetClick()

        val state = viewModel.viewState.value
        assertNull(state.board.getPiece(position))
        assertEquals(HapticFeedbackType.LongPress, state.hapticFeedbackType)
        assertFalse(state.isWin)
    }

    @Test
    fun `onConflictsShown clears conflicts`() = runTest {
        // Set up conflicts
        val position = Position(0, 0)
        val conflicts = listOf(Position(0, 1))
        every { gameEngine.findConflicts(any(), position) } returns conflicts
        viewModel.onSquareClick(position)

        viewModel.onConflictsShown()

        assertEquals(0, viewModel.viewState.value.conflicts.size)
    }

    @Test
    fun `onHapticFeedbackConsumed clears haptic feedback`() = runTest {
        // Trigger haptic feedback
        viewModel.onResetClick()

        viewModel.onHapticFeedbackConsumed()

        assertNull(viewModel.viewState.value.hapticFeedbackType)
    }

    @Test
    fun `onChooseBoardSizeClick emits navigation event`() = runTest {
        every { savedStateHandle.get<Int>("boardSize") } returns 6

        viewModel.navigationEvents.test {
            viewModel.onChooseBoardSizeClick()

            val event = awaitItem()
            assertTrue(event is QueensGameNavigationEvent.ChooseBoardSize)
            assertEquals(6, (event as QueensGameNavigationEvent.ChooseBoardSize).currentSize)
        }
    }

    @Test
    fun `onChooseBoardSizeClick uses default size when no saved size`() = runTest {
        viewModel.navigationEvents.test {
            viewModel.onChooseBoardSizeClick()

            val event = awaitItem()
            assertEquals(
                Board.DEFAULT_SIZE,
                (event as QueensGameNavigationEvent.ChooseBoardSize).currentSize
            )
        }
    }
}
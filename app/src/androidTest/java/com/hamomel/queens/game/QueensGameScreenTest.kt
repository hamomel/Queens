package com.hamomel.queens.game

import android.content.Context
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.hamomel.queens.R
import com.hamomel.queens.data.Board
import com.hamomel.queens.data.Position
import com.hamomel.queens.data.WhiteQueen
import com.hamomel.queens.ui.theme.QueensTheme
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QueensGameScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private lateinit var boardSizeFlow: MutableStateFlow<Int>
    private lateinit var mockViewModel: QueensGameViewModel
    private var onChoseBoardSizeNavigateCalled = false
    private var onWinNavigateCalled = false
    private var boardSizeNavigateParam = 0
    private var winNavigateParam = 0

    @Before
    fun setup() {
        boardSizeFlow = MutableStateFlow(8)
        mockViewModel = mockk(relaxed = true)
        onChoseBoardSizeNavigateCalled = false
        onWinNavigateCalled = false
        boardSizeNavigateParam = 0
        winNavigateParam = 0

        every { mockViewModel.navigationEvents } returns emptyFlow()
    }

    private fun setScreenContent(
        viewState: QueensGameViewState,
        onChoseBoardSizeNavigate: (Int) -> Unit = { onChoseBoardSizeNavigateCalled = true },
        onWinNavigate: (Int) -> Unit = { onWinNavigateCalled = true }
    ) {
        every { mockViewModel.viewState } returns MutableStateFlow(viewState)

        composeTestRule.setContent {
            QueensTheme {
                QueensGameScreen(
                    boardSize = boardSizeFlow,
                    onChoseBoardSizeNavigate = onChoseBoardSizeNavigate,
                    onWinNavigate = onWinNavigate,
                    viewModel = mockViewModel
                )
            }
        }
    }

    @Test
    fun displaysGameTitleWithBoardSize() {
        val viewState = QueensGameViewState(board = Board(8))
        setScreenContent(viewState)

        val gameTitle = context.getString(R.string.game_screen_title, 8)
        composeTestRule.onNodeWithText(gameTitle).assertIsDisplayed()
    }

    @Test
    fun displaysQueensRemainingCount() {
        val viewState = QueensGameViewState(board = Board(8))
        setScreenContent(viewState)

        composeTestRule.onNodeWithTag(QueensGameScreenTestTags.COUNT_TO_WIN_TEXT).assertIsDisplayed()
        val queensToWinText = context.getString(R.string.game_queens_to_win)
        composeTestRule.onNodeWithText(queensToWinText).assertIsDisplayed()
    }

    @Test
    fun displaysResetButton() {
        val viewState = QueensGameViewState(board = Board(8))
        setScreenContent(viewState)

        val resetButtonText = context.getString(R.string.game_reset_game_button_label)
        composeTestRule.onNodeWithText(resetButtonText).assertIsDisplayed()
    }

    @Test
    fun displaysNewGameButtonWhenWon() {
        val viewState = QueensGameViewState(board = Board(8), isWin = true)
        setScreenContent(viewState)

        val newGameText = context.getString(R.string.game_new_game_button_label)
        composeTestRule.onNodeWithText(newGameText).assertIsDisplayed()
        val winMessageText = context.getString(R.string.game_win_message)
        composeTestRule.onNodeWithText(winMessageText).assertIsDisplayed()
    }

    @Test
    fun hidesQueensToWinTextWhenWon() {
        val viewState = QueensGameViewState(board = Board(8), isWin = true)
        setScreenContent(viewState)

        val queensToWinText = context.getString(R.string.game_queens_to_win)
        composeTestRule.onNodeWithText(queensToWinText).assertDoesNotExist()
    }

    @Test
    fun displaysUpdatedQueensCount() {
        val board = Board(8)
        board.setPiece(WhiteQueen, Position(0, 0))
        board.setPiece(WhiteQueen, Position(1, 1))
        val viewState = QueensGameViewState(board = board)
        setScreenContent(viewState)

        composeTestRule.onNodeWithTag(QueensGameScreenTestTags.COUNT_TO_WIN_TEXT).assertIsDisplayed()
    }

    @Test
    fun boardSizeIconIsDisplayedInTopBar() {
        val viewState = QueensGameViewState(board = Board(8))
        setScreenContent(viewState)

        val chooseBoardSizeDescription = context.getString(R.string.game_choose_board_size_button_content_description)
        composeTestRule.onNodeWithContentDescription(chooseBoardSizeDescription).assertIsDisplayed()
    }

    @Test
    fun chooseBoardSizeClickCallsViewModel() {
        val viewState = QueensGameViewState(board = Board(8))
        setScreenContent(viewState)

        val chooseBoardSizeDescription = context.getString(R.string.game_choose_board_size_button_content_description)
        composeTestRule.onNodeWithContentDescription(chooseBoardSizeDescription).performClick()

        verify { mockViewModel.onChooseBoardSizeClick() }
    }

    @Test
    fun resetButtonClickCallsViewModel() {
        val viewState = QueensGameViewState(board = Board(8))
        setScreenContent(viewState)

        val resetButtonText = context.getString(R.string.game_reset_game_button_label)
        composeTestRule.onNodeWithText(resetButtonText).performClick()

        verify { mockViewModel.onResetClick() }
    }

    @Test
    fun newGameButtonClickCallsViewModel() {
        val viewState = QueensGameViewState(board = Board(8), isWin = true)
        setScreenContent(viewState)

        val newGameText = context.getString(R.string.game_new_game_button_label)
        composeTestRule.onNodeWithText(newGameText).performClick()

        verify { mockViewModel.onResetClick() }
    }

    @Test
    fun displaysDifferentBoardSizesCorrectly() {
        val viewState = QueensGameViewState(board = Board(12))
        setScreenContent(viewState, onChoseBoardSizeNavigate = { boardSizeNavigateParam = it }, onWinNavigate = { winNavigateParam = it })

        val titleText = context.getString(R.string.game_screen_title, 12)
        composeTestRule.onNodeWithText(titleText).assertIsDisplayed()
        composeTestRule.onNodeWithTag(QueensGameScreenTestTags.COUNT_TO_WIN_TEXT).assertIsDisplayed()
    }

    @Test
    fun displaysCorrectCountWithSmallerBoard() {
        val viewState = QueensGameViewState(board = Board(4))
        setScreenContent(viewState, onChoseBoardSizeNavigate = { boardSizeNavigateParam = it }, onWinNavigate = { winNavigateParam = it })

        val titleText = context.getString(R.string.game_screen_title, 4)
        composeTestRule.onNodeWithText(titleText).assertIsDisplayed()
        val queensToWinText = context.getString(R.string.game_queens_to_win)
        composeTestRule.onNodeWithText(queensToWinText).assertIsDisplayed()
    }

    @Test
    fun callsOnHapticFeedbackConsumedWhenHapticFeedbackPresent() {
        val viewState = QueensGameViewState(
            board = Board(8),
            hapticFeedbackType = HapticFeedbackType.LongPress
        )
        setScreenContent(viewState)

        verify { mockViewModel.onHapticFeedbackConsumed() }
    }
}
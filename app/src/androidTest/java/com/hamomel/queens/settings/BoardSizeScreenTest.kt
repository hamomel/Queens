package com.hamomel.queens.settings

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.hamomel.queens.R
import com.hamomel.queens.ui.theme.QueensTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BoardSizeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private var onBoardSizeChosenCalled = false
    private var onCloseCalled = false
    private var boardSizeChosenParam = 0

    @Before
    fun setup() {
        onBoardSizeChosenCalled = false
        onCloseCalled = false
        boardSizeChosenParam = 0
    }

    private fun setScreenContent(
        currentSize: Int = 8,
        maxSize: Int = 16,
        minSize: Int = 4,
        onBoardSizeChosen: (Int) -> Unit = {
            onBoardSizeChosenCalled = true
            boardSizeChosenParam = it
        },
        onClose: () -> Unit = { onCloseCalled = true }
    ) {
        composeTestRule.setContent {
            QueensTheme {
                BoardSizeScreen(
                    currentSize = currentSize,
                    maxSize = maxSize,
                    minSize = minSize,
                    onBoardSizeChosen = onBoardSizeChosen,
                    onClose = onClose
                )
            }
        }
    }

    @Test
    fun displaysBoardSizeTitle() {
        setScreenContent()

        composeTestRule.onNodeWithText("Choose Board Size").assertIsDisplayed()
    }

    @Test
    fun displaysCurrentBoardSize() {
        setScreenContent(currentSize = 8)

        composeTestRule.onNodeWithText("8").assertIsDisplayed()
    }

    @Test
    fun displaysPlayButton() {
        setScreenContent()

        val playButtonText = context.getString(R.string.board_size_play_button_title)
        composeTestRule.onNodeWithText(playButtonText).assertIsDisplayed()
    }

    @Test
    fun displaysCloseButton() {
        setScreenContent()

        val closeButtonDescription = context.getString(R.string.close_button_content_description)
        composeTestRule.onNodeWithContentDescription(closeButtonDescription).assertIsDisplayed()
    }

    @Test
    fun displaysSmallerButton() {
        setScreenContent()

        val makeSmallerDescription = context.getString(R.string.board_size_make_smaller_button_content_description)
        composeTestRule.onNodeWithContentDescription(makeSmallerDescription).assertIsDisplayed()
    }

    @Test
    fun displaysLargerButton() {
        setScreenContent()

        val makeLargerDescription = context.getString(R.string.board_size_make_larger_button_content_description)
        composeTestRule.onNodeWithContentDescription(makeLargerDescription).assertIsDisplayed()
    }

    @Test
    fun playButtonClickCallsOnBoardSizeChosen() {
        setScreenContent(currentSize = 8)

        val playButtonText = context.getString(R.string.board_size_play_button_title)
        composeTestRule.onNodeWithText(playButtonText).performClick()

        assert(onBoardSizeChosenCalled)
        assert(boardSizeChosenParam == 8)
    }

    @Test
    fun closeButtonClickCallsOnClose() {
        setScreenContent()

        val closeButtonDescription = context.getString(R.string.close_button_content_description)
        composeTestRule.onNodeWithContentDescription(closeButtonDescription).performClick()

        assert(onCloseCalled)
    }

    @Test
    fun largerButtonIncreasesSize() {
        setScreenContent(currentSize = 8)

        val makeLargerDescription = context.getString(R.string.board_size_make_larger_button_content_description)
        composeTestRule.onNodeWithContentDescription(makeLargerDescription).performClick()

        composeTestRule.onNodeWithText("9").assertIsDisplayed()
    }

    @Test
    fun smallerButtonDecreasesSize() {
        setScreenContent(currentSize = 8)

        val makeSmallerDescription = context.getString(R.string.board_size_make_smaller_button_content_description)
        composeTestRule.onNodeWithContentDescription(makeSmallerDescription).performClick()

        composeTestRule.onNodeWithText("7").assertIsDisplayed()
    }

    @Test
    fun smallerButtonDisabledAtMinSize() {
        setScreenContent(currentSize = 4, minSize = 4)

        val makeSmallerDescription = context.getString(R.string.board_size_make_smaller_button_content_description)
        composeTestRule.onNodeWithContentDescription(makeSmallerDescription).assertIsNotEnabled()
    }

    @Test
    fun largerButtonDisabledAtMaxSize() {
        setScreenContent(currentSize = 16, maxSize = 16)

        val makeLargerDescription = context.getString(R.string.board_size_make_larger_button_content_description)
        composeTestRule.onNodeWithContentDescription(makeLargerDescription).assertIsNotEnabled()
    }

    @Test
    fun smallerButtonEnabledAboveMinSize() {
        setScreenContent(currentSize = 5, minSize = 4)

        val makeSmallerDescription = context.getString(R.string.board_size_make_smaller_button_content_description)
        composeTestRule.onNodeWithContentDescription(makeSmallerDescription).assertIsEnabled()
    }

    @Test
    fun largerButtonEnabledBelowMaxSize() {
        setScreenContent(currentSize = 15, maxSize = 16)

        val makeLargerDescription = context.getString(R.string.board_size_make_larger_button_content_description)
        composeTestRule.onNodeWithContentDescription(makeLargerDescription).assertIsEnabled()
    }

    @Test
    fun displaysDifferentBoardSizesCorrectly() {
        setScreenContent(currentSize = 12)

        composeTestRule.onNodeWithText("12").assertIsDisplayed()
    }

    @Test
    fun playButtonPassesCorrectSizeAfterChanges() {
        setScreenContent(currentSize = 8)

        val makeLargerDescription = context.getString(R.string.board_size_make_larger_button_content_description)
        composeTestRule.onNodeWithContentDescription(makeLargerDescription).performClick()
        composeTestRule.onNodeWithContentDescription(makeLargerDescription).performClick()

        val playButtonText = context.getString(R.string.board_size_play_button_title)
        composeTestRule.onNodeWithText(playButtonText).performClick()

        assert(onBoardSizeChosenCalled)
        assert(boardSizeChosenParam == 10)
    }

    @Test
    fun sizeDoesNotGoBelowMinimum() {
        setScreenContent(currentSize = 4, minSize = 4)

        val makeSmallerDescription = context.getString(R.string.board_size_make_smaller_button_content_description)
        composeTestRule.onNodeWithContentDescription(makeSmallerDescription).performClick()

        composeTestRule.onNodeWithText("4").assertIsDisplayed()
    }

    @Test
    fun sizeDoesNotGoAboveMaximum() {
        setScreenContent(currentSize = 16, maxSize = 16)

        val makeLargerDescription = context.getString(R.string.board_size_make_larger_button_content_description)
        composeTestRule.onNodeWithContentDescription(makeLargerDescription).performClick()

        composeTestRule.onNodeWithText("16").assertIsDisplayed()
    }
}
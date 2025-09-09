package com.hamomel.queens.winscreeen

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
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

class WinScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private var onCloseCalled = false

    @Before
    fun setup() {
        onCloseCalled = false
    }

    private fun setScreenContent(
        boardSize: Int = 8,
        onClose: () -> Unit = { onCloseCalled = true }
    ) {
        composeTestRule.setContent {
            QueensTheme {
                WinScreen(
                    boardSize = boardSize,
                    onClose = onClose
                )
            }
        }
    }

    @Test
    fun displaysCongratulationsText() {
        setScreenContent()

        val congratulationsText = context.getString(R.string.win_congratulations)
        composeTestRule.onNodeWithText(congratulationsText).assertIsDisplayed()
    }

    @Test
    fun displaysTrophyEmoji() {
        setScreenContent()

        composeTestRule.onNodeWithText("🏆").assertIsDisplayed()
    }

    @Test
    fun displaysSuccessMessageWithBoardSize() {
        setScreenContent(boardSize = 8)

        val successMessage = context.getString(R.string.win_success_message, 8)
        composeTestRule.onNodeWithText(successMessage).assertIsDisplayed()
    }

    @Test
    fun displaysCloseButton() {
        setScreenContent()

        val closeButtonDescription = context.getString(R.string.close_button_content_description)
        composeTestRule.onNodeWithContentDescription(closeButtonDescription).assertIsDisplayed()
    }

    @Test
    fun closeButtonClickCallsOnClose() {
        setScreenContent()

        val closeButtonDescription = context.getString(R.string.close_button_content_description)
        composeTestRule.onNodeWithContentDescription(closeButtonDescription).performClick()

        assert(onCloseCalled)
    }
}
package com.hamomel.queens.navigatioin

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.hamomel.queens.R
import com.hamomel.queens.game.GameRoute
import com.hamomel.queens.game.QueensGameScreenTestTags
import com.hamomel.queens.settings.BoardSizeRoute
import com.hamomel.queens.ui.theme.QueensTheme
import com.hamomel.queens.winscreeen.WinRoute
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QueensNavHostTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: TestNavHostController

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setupAppNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            QueensTheme {
                QueensNavHost(navController = navController)
            }
        }
    }

    @Test
    fun displaysGameScreenByDefault() {
        val gameTitle = context.getString(R.string.game_screen_title, 8)
        composeTestRule.onNodeWithText(gameTitle).assertIsDisplayed()

        composeTestRule.onNodeWithTag(QueensGameScreenTestTags.BOARD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(QueensGameScreenTestTags.COUNT_TO_WIN_TEXT)
            .assertIsDisplayed()

        val resetButtonText = context.getString(R.string.game_reset_game_button_label)
        composeTestRule.onNodeWithText(resetButtonText).assertIsDisplayed()
    }

    @Test
    fun navigatesToBoardSizeScreenWhenChoseBoardSizeClicked() {
        val chooseBoardSizeDescription =
            context.getString(R.string.game_choose_board_size_button_content_description)
        composeTestRule.onNodeWithContentDescription(chooseBoardSizeDescription).performClick()
        composeTestRule.waitForIdle()

        val chooseBoardSizeTitle = context.getString(R.string.board_size_screen_title)
        composeTestRule.onNodeWithText(chooseBoardSizeTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText("8").assertIsDisplayed()

        val playButtonText = context.getString(R.string.board_size_play_button_title)
        composeTestRule.onNodeWithText(playButtonText).assertIsDisplayed()
    }

    @Test
    fun navigatesBackToGameScreenFromBoardSizeScreen() {
        // Navigate to board size screen
        val chooseBoardSizeDescription =
            context.getString(R.string.game_choose_board_size_button_content_description)
        composeTestRule.onNodeWithContentDescription(chooseBoardSizeDescription).performClick()
        composeTestRule.waitForIdle()

        // Navigate back via close button
        val closeButtonDescription = context.getString(R.string.close_button_content_description)
        composeTestRule.onNodeWithContentDescription(closeButtonDescription).performClick()
        composeTestRule.waitForIdle()

        // Verify we're back on game screen
        val gameTitle = context.getString(R.string.game_screen_title, 8)
        composeTestRule.onNodeWithText(gameTitle).assertIsDisplayed()
        composeTestRule.onNodeWithTag(QueensGameScreenTestTags.BOARD).assertIsDisplayed()
    }

    @Test
    fun navigatesBackToGameScreenWhenPlayButtonClicked() {
        // Navigate to board size screen
        val chooseBoardSizeDescription =
            context.getString(R.string.game_choose_board_size_button_content_description)
        composeTestRule.onNodeWithContentDescription(chooseBoardSizeDescription).performClick()
        composeTestRule.waitForIdle()

        // Click play button
        val playButtonText = context.getString(R.string.board_size_play_button_title)
        composeTestRule.onNodeWithText(playButtonText).performClick()
        composeTestRule.waitForIdle()

        // Verify we're back on game screen
        val gameTitle = context.getString(R.string.game_screen_title, 8)
        composeTestRule.onNodeWithText(gameTitle).assertIsDisplayed()
        composeTestRule.onNodeWithTag(QueensGameScreenTestTags.BOARD).assertIsDisplayed()
    }

    @Test
    fun navigatesToWinScreenProgrammatically() {
        // Navigate to win screen programmatically since win state cannot be achieved through UI
        composeTestRule.runOnIdle {
            navController.navigate(WinRoute(8))
        }
        composeTestRule.waitForIdle()

        val congratulationsText = context.getString(R.string.win_congratulations)
        composeTestRule.onNodeWithText(congratulationsText).assertIsDisplayed()

        composeTestRule.onNodeWithText("🏆").assertIsDisplayed()

        val successMessage = context.getString(R.string.win_success_message, 8)
        composeTestRule.onNodeWithText(successMessage).assertIsDisplayed()
    }

    @Test
    fun navigatesBackToGameScreenFromWinScreen() {
        // Navigate to win screen programmatically
        composeTestRule.runOnIdle {
            navController.navigate(WinRoute(8))
        }
        composeTestRule.waitForIdle()

        // Navigate back via close button
        val closeButtonDescription = context.getString(R.string.close_button_content_description)
        composeTestRule.onNodeWithContentDescription(closeButtonDescription).performClick()
        composeTestRule.waitForIdle()

        // Verify we're back on game screen
        val gameTitle = context.getString(R.string.game_screen_title, 8)
        composeTestRule.onNodeWithText(gameTitle).assertIsDisplayed()
        composeTestRule.onNodeWithTag(QueensGameScreenTestTags.BOARD).assertIsDisplayed()
    }

    @Test
    fun boardSizeChangePersistsAfterNavigation() {
        // Navigate to board size screen
        val chooseBoardSizeDescription =
            context.getString(R.string.game_choose_board_size_button_content_description)
        composeTestRule.onNodeWithContentDescription(chooseBoardSizeDescription).performClick()
        composeTestRule.waitForIdle()

        // Change board size to 5
        val makeSmallerDescription =
            context.getString(R.string.board_size_make_smaller_button_content_description)
        repeat(3) {
            composeTestRule.onNodeWithContentDescription(makeSmallerDescription).performClick()
        }
        composeTestRule.onNodeWithText("5").assertIsDisplayed()

        // Navigate back
        val playButtonText = context.getString(R.string.board_size_play_button_title)
        composeTestRule.onNodeWithText(playButtonText).performClick()
        composeTestRule.waitForIdle()

        // Verify game screen shows new board size
        val newGameTitle = context.getString(R.string.game_screen_title, 5)
        composeTestRule.onNodeWithText(newGameTitle).assertIsDisplayed()
    }

    @Test
    fun displaysBoardSizeScreenWithCorrectCurrentSize() {
        composeTestRule.runOnIdle {
            navController.navigate(BoardSizeRoute(12))
        }
        composeTestRule.waitForIdle()

        val chooseBoardSizeTitle = context.getString(R.string.board_size_screen_title)
        composeTestRule.onNodeWithText(chooseBoardSizeTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText("12").assertIsDisplayed()

        val makeSmallerDescription =
            context.getString(R.string.board_size_make_smaller_button_content_description)
        composeTestRule.onNodeWithContentDescription(makeSmallerDescription).assertIsDisplayed()
    }

    @Test
    fun displaysWinScreenWithCorrectBoardSize() {
        composeTestRule.runOnIdle {
            navController.navigate(WinRoute(16))
        }
        composeTestRule.waitForIdle()

        val congratulationsText = context.getString(R.string.win_congratulations)
        composeTestRule.onNodeWithText(congratulationsText).assertIsDisplayed()

        val successMessage = context.getString(R.string.win_success_message, 16)
        composeTestRule.onNodeWithText(successMessage).assertIsDisplayed()
    }

    @Test
    fun gameScreenDisplaysAllRequiredElements() {
        composeTestRule.runOnIdle {
            navController.navigate(GameRoute)
        }
        composeTestRule.waitForIdle()

        val gameTitle = context.getString(R.string.game_screen_title, 8)
        composeTestRule.onNodeWithText(gameTitle).assertIsDisplayed()

        composeTestRule.onNodeWithTag(QueensGameScreenTestTags.BOARD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(QueensGameScreenTestTags.COUNT_TO_WIN_TEXT)
            .assertIsDisplayed()

        val chooseBoardSizeDescription =
            context.getString(R.string.game_choose_board_size_button_content_description)
        composeTestRule.onNodeWithContentDescription(chooseBoardSizeDescription).assertIsDisplayed()

        val resetButtonText = context.getString(R.string.game_reset_game_button_label)
        composeTestRule.onNodeWithText(resetButtonText).assertIsDisplayed()
    }
}
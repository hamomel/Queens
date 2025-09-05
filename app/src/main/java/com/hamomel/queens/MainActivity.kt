package com.hamomel.queens

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.hamomel.queens.data.Board
import com.hamomel.queens.game.ui.GameRoute
import com.hamomel.queens.game.ui.QueensGameScreen
import com.hamomel.queens.settings.BoardSizeRoute
import com.hamomel.queens.settings.BoardSizeScreen
import com.hamomel.queens.ui.theme.QueensTheme
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            QueensTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = GameRoute) {
                    composable<GameRoute>() {
                        val boardSize = navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.getStateFlow(BoardSizeRoute.RESULT_KEY, Board.DEFAULT_SIZE)
                            ?: MutableStateFlow(Board.DEFAULT_SIZE)

                        QueensGameScreen(
                            boardSize = boardSize,
                            onChoseBoardSizeNavigate = { currentSize ->
                                navController.navigate(BoardSizeRoute(currentSize))
                            }
                        )
                    }

                    composable<BoardSizeRoute>() { backStackEntry ->
                        BoardSizeScreen(
                            currentSize = backStackEntry.toRoute<BoardSizeRoute>().currentSize,
                            maxSize = calculateMaxBoardSize(),
                            onBoardSizeChosen = { newSize ->
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    key = BoardSizeRoute.RESULT_KEY,
                                    value = newSize
                                )
                                navController.popBackStack()
                            },
                            onClose = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun calculateMaxBoardSize(): Int {
    val screenWidthDp = LocalWindowInfo.current.containerSize.width
    val screenHeightDp = LocalWindowInfo.current.containerSize.height

    val availableSpaceDp = minOf(screenWidthDp, screenHeightDp) / LocalDensity.current.density

    val minSquareSizeDp = 48 // Minimum size of a clickable element according to accessibility guidelines
    return (availableSpaceDp / minSquareSizeDp).toInt()
}


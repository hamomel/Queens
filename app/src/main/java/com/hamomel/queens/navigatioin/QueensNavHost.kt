package com.hamomel.queens.navigatioin

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hamomel.queens.data.Board
import com.hamomel.queens.game.GameRoute
import com.hamomel.queens.game.QueensGameScreen
import com.hamomel.queens.settings.BoardSizeRoute
import com.hamomel.queens.settings.BoardSizeScreen
import com.hamomel.queens.ui.widgets.BoardSizeUtils
import com.hamomel.queens.winscreeen.WinRoute
import com.hamomel.queens.winscreeen.WinScreen
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun QueensNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = GameRoute) {
        composable<GameRoute>() {
            val boardSize = navController.currentBackStackEntry
                ?.savedStateHandle
                ?.getStateFlow(BoardSizeRoute.Companion.RESULT_KEY, Board.Companion.DEFAULT_SIZE)
                ?: MutableStateFlow(Board.Companion.DEFAULT_SIZE)

            QueensGameScreen(
                boardSize = boardSize,
                onChoseBoardSizeNavigate = { currentSize ->
                    navController.navigate(BoardSizeRoute(currentSize))
                },
                onWinNavigate = { boardSize ->
                    navController.navigate(WinRoute(boardSize))
                }
            )
        }

        composable<BoardSizeRoute>() { backStackEntry ->
            BoardSizeScreen(
                currentSize = backStackEntry.toRoute<BoardSizeRoute>().currentSize,
                maxSize = BoardSizeUtils.calculateMaxBoardSize(),
                onBoardSizeChosen = { newSize ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        key = BoardSizeRoute.Companion.RESULT_KEY,
                        value = newSize
                    )
                    navController.popBackStack()
                },
                onClose = { navController.popBackStack() }
            )
        }

        composable<WinRoute>() { backStackEntry ->
            WinScreen(
                boardSize = backStackEntry.toRoute<WinRoute>().boardSize,
                onClose = { navController.popBackStack() }
            )
        }
    }
}
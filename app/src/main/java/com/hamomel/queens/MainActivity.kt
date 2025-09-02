package com.hamomel.queens

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hamomel.queens.game.data.BlackQueen
import com.hamomel.queens.game.data.Board
import com.hamomel.queens.game.data.WhiteQueen
import com.hamomel.queens.game.ui.QueensGameScreen
import com.hamomel.queens.ui.theme.QueensTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        val board = Board(8)
        board.setPiece(WhiteQueen, 2, 2)
        board.setPiece(BlackQueen, 5, 2)


        setContent {
            QueensTheme {
                QueensGameScreen(board)
            }
        }
    }
}

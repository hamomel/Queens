package com.hamomel.queens.ui.theme

import androidx.compose.ui.graphics.Color


val Background = Color(81, 80, 86)
val WhiteSquare = Color(238, 238, 210)
val BlackSquare = Color(117, 150, 86)
val Accent = Color(33, 169, 75)

data class QueensColorScheme(
    val whiteSquare: Color,
    val blackSquare: Color
)

val lightQueensColorScheme = QueensColorScheme(
    whiteSquare = WhiteSquare,
    blackSquare = BlackSquare
)
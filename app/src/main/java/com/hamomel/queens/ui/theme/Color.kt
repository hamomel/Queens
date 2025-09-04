package com.hamomel.queens.ui.theme

import androidx.compose.ui.graphics.Color


val Background = Color(81, 80, 86)
val Surface = Color(42, 41, 38)
val OnSurface = Color(194, 194, 194)
val WhiteSquare = Color(238, 238, 210)
val BlackSquare = Color(117, 150, 86)
val Accent = Color(33, 169, 75)
val Error = Color(255, 0, 0)

data class QueensColorScheme(
    val whiteSquare: Color,
    val blackSquare: Color,
    val error: Color
)

val lightQueensColorScheme = QueensColorScheme(
    whiteSquare = WhiteSquare,
    blackSquare = BlackSquare,
    error = Error
)

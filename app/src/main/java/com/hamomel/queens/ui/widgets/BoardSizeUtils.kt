package com.hamomel.queens.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo

// Minimum size of a clickable element according to accessibility guidelines
private const val  MIN_SQUARE_SIZE_DP = 48

object BoardSizeUtils {
    @Composable
    fun calculateMaxBoardSize(): Int {
        val screenWidthDp = LocalWindowInfo.current.containerSize.width
        val screenHeightDp = LocalWindowInfo.current.containerSize.height

        val availableSpaceDp = minOf(screenWidthDp, screenHeightDp) / LocalDensity.current.density

        return (availableSpaceDp / MIN_SQUARE_SIZE_DP).toInt()
    }
}
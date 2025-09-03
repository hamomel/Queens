package com.hamomel.queens

import com.hamomel.queens.game.ui.QueensGameViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { (boardSize: Int) -> QueensGameViewModel(boardSize) }
}

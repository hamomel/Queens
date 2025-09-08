package com.hamomel.queens

import com.hamomel.queens.game.QueensGameViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {  QueensGameViewModel(get()) }
}

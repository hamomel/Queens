package com.hamomel.queens

import com.hamomel.queens.game.GameEngine
import com.hamomel.queens.game.QueensGameViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    singleOf(::GameEngine)
    viewModel {
        QueensGameViewModel(
            savedStateHandle = get(),
            gameEngine = get()
        )
    }
}

package com.hamomel.queens.game.di

import androidx.lifecycle.SavedStateHandle
import com.hamomel.queens.appModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class KoinModulesTest : KoinTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkModules() {
        appModule.verify(
            extraTypes = listOf(SavedStateHandle::class)
        )
    }
}
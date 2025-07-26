package com.techtactoe.ayna.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            dataModule,
            viewModelModule,
        )
    }
}
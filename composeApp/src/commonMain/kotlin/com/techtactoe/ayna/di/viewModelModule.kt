package com.techtactoe.ayna.di

import org.koin.dsl.module

val viewModelModule = module {
    factory { HomeViewModel(get()) }
}
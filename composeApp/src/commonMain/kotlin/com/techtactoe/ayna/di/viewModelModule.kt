package com.techtactoe.ayna.di

import com.techtactoe.ayna.presentation.ui.screens.home.HomeViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { HomeViewModel(get(), get()) }
}
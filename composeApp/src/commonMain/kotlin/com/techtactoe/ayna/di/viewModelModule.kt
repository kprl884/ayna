package com.techtactoe.ayna.di

import com.techtactoe.ayna.presentation.viewmodel.HomeViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { HomeViewModel(get()) }
}
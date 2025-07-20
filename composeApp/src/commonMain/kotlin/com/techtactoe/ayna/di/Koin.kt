package com.techtactoe.ayna.di

import com.techtactoe.ayna.data.InMemoryMuseumStorage
import com.techtactoe.ayna.data.KtorMuseumApi
import com.techtactoe.ayna.data.MockSalonRepository
import com.techtactoe.ayna.data.MuseumApi
import com.techtactoe.ayna.data.MuseumRepository
import com.techtactoe.ayna.data.MuseumStorage
import com.techtactoe.ayna.domain.repository.SalonRepository
import com.techtactoe.ayna.domain.usecase.GetNearbySalonsUseCase
import com.techtactoe.ayna.presentation.viewmodel.HomeViewModel
import com.techtactoe.ayna.screens.detail.DetailViewModel
import com.techtactoe.ayna.screens.list.ListViewModel

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json

import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                // TODO Fix API so it serves application/json
                json(json, contentType = ContentType.Any)
            }
        }
    }

    single<MuseumApi> { KtorMuseumApi(get()) }
    single<MuseumStorage> { InMemoryMuseumStorage() }
    single {
        MuseumRepository(get(), get()).apply {
            initialize()
        }
    }
    
    // Salon repository ve use case'leri
    single<SalonRepository> { MockSalonRepository() }
    single { GetNearbySalonsUseCase(get()) }
}

val viewModelModule = module {
    factoryOf(::ListViewModel)
    factoryOf(::DetailViewModel)
    factory { HomeViewModel(get()) }
}

fun initKoin() {
    startKoin {
        modules(
            dataModule,
            viewModelModule,
        )
    }
}

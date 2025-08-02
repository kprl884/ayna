package com.techtactoe.ayna.di

import com.techtactoe.ayna.data.InMemoryMuseumStorage
import com.techtactoe.ayna.data.KtorMuseumApi
import com.techtactoe.ayna.data.MuseumApi
import com.techtactoe.ayna.data.MuseumRepository
import com.techtactoe.ayna.data.MuseumStorage
import com.techtactoe.ayna.data.repository.MockSalonRepositoryImpl
import com.techtactoe.ayna.domain.repository.SalonRepository
import com.techtactoe.ayna.domain.usecase.GetNearbySalonsUseCase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
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
    single<SalonRepository> { MockSalonRepositoryImpl() }
    single { GetNearbySalonsUseCase(get()) }
}

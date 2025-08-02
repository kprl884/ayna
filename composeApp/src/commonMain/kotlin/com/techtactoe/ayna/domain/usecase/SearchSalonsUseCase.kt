package com.techtactoe.ayna.domain.usecase

import com.techtactoe.ayna.domain.model.Salon
import com.techtactoe.ayna.domain.repository.SalonRepository
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for searching salons by query
 */
class SearchSalonsUseCase(
    private val repository: SalonRepository
) {
    operator fun invoke(query: String): Flow<Resource<List<Salon>>> = flow {
        if (query.isBlank()) {
            emit(Resource.Success(emptyList()))
            return@flow
        }
        
        emit(Resource.Loading())
        try {
            val salons = repository.searchSalons(query)
            emit(Resource.Success(salons))
        } catch (e: Exception) {
            println("SearchSalonsUseCase: ${e.message}")
            emit(Resource.Error("Search failed. Check your internet connection."))
        }
    }
}

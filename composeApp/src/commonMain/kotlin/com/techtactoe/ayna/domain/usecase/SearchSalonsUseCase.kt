package com.techtactoe.ayna.domain.usecase

import com.techtactoe.ayna.domain.model.SalonV2
import com.techtactoe.ayna.domain.repository.SalonRepositoryV2
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for searching salons by query
 */
class SearchSalonsUseCase(
    private val repository: SalonRepositoryV2
) {
    operator fun invoke(query: String): Flow<Resource<List<SalonV2>>> = flow {
        if (query.isBlank()) {
            emit(Resource.Success(emptyList()))
            return@flow
        }
        
        emit(Resource.Loading())
        try {
            val salons = repository.searchSalons(query)
            emit(Resource.Success(salons))
        } catch (e: Exception) {
            emit(Resource.Error("Search failed. Check your internet connection."))
        }
    }
}

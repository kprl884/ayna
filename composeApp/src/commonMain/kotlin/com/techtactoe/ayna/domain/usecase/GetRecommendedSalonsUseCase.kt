package com.techtactoe.ayna.domain.usecase

import com.techtactoe.ayna.domain.model.SalonV2
import com.techtactoe.ayna.domain.repository.SalonRepositoryV2
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for fetching recommended salons
 */
class GetRecommendedSalonsUseCase(
    private val repository: SalonRepositoryV2
) {
    operator fun invoke(): Flow<Resource<List<SalonV2>>> = flow {
        emit(Resource.Loading())
        try {
            val salons = repository.getRecommendedSalons()
            emit(Resource.Success(salons))
        } catch (e: Exception) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}

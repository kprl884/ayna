package com.techtactoe.ayna.domain.usecase

import com.techtactoe.ayna.domain.model.Salon
import com.techtactoe.ayna.domain.repository.SalonRepository
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for fetching recommended salons
 */
class GetRecommendedSalonsUseCase(
    private val repository: SalonRepository
) {
    operator fun invoke(): Flow<Resource<List<Salon>>> = flow {
        emit(Resource.Loading())
        try {
            val salons = repository.getRecommendedSalons()
            emit(Resource.Success(salons))
        } catch (e: Exception) {
            println("GetRecommendedSalonsUseCase: ${e.message}")
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}

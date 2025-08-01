package com.techtactoe.ayna.domain.usecase

import com.techtactoe.ayna.domain.model.SalonV2
import com.techtactoe.ayna.domain.repository.SalonRepositoryV2
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for fetching detailed information about a specific salon
 */
class GetSalonDetailsUseCase(
    private val repository: SalonRepositoryV2
) {
    operator fun invoke(salonId: String): Flow<Resource<SalonV2>> = flow {
        emit(Resource.Loading())
        try {
            val salon = repository.getSalonDetails(salonId)
            if (salon != null) {
                emit(Resource.Success(salon))
            } else {
                emit(Resource.Error("Salon not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Couldn't load salon details. Check your internet connection."))
        }
    }
}

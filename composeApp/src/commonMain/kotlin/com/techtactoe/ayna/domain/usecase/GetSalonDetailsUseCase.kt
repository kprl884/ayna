package com.techtactoe.ayna.domain.usecase

import com.techtactoe.ayna.domain.model.Salon
import com.techtactoe.ayna.domain.repository.SalonRepository
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for fetching detailed information about a specific salon
 */
class GetSalonDetailsUseCase(
    private val repository: SalonRepository
) {
    operator fun invoke(salonId: String): Flow<Resource<Salon>> = flow {
        emit(Resource.Loading())
        try {
            val salon = repository.getSalonDetails(salonId)
            if (salon != null) {
                emit(Resource.Success(salon))
            } else {
                emit(Resource.Error("Salon not found"))
            }
        } catch (e: Exception) {
            println("GetSalonDetailsUseCase: ${e.message}")
            emit(Resource.Error("Couldn't load salon details. Check your internet connection."))
        }
    }
}

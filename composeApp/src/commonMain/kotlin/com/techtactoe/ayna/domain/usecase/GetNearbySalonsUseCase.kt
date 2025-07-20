package com.techtactoe.ayna.domain.usecase

import com.techtactoe.ayna.domain.model.Salon
import com.techtactoe.ayna.domain.repository.SalonRepository

class GetNearbySalonsUseCase(
    private val salonRepository: SalonRepository
) {
    suspend operator fun invoke(): Result<List<Salon>> {
        return try {
            val salons = salonRepository.getNearbySalons()
            Result.success(salons)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 
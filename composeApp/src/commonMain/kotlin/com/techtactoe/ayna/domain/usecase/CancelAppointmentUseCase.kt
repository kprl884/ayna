package com.techtactoe.ayna.domain.usecase

import com.techtactoe.ayna.domain.repository.AppointmentRepository
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for cancelling an existing appointment
 */
class CancelAppointmentUseCase(
    private val repository: AppointmentRepository
) {
    operator fun invoke(appointmentId: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val success = repository.cancelAppointment(appointmentId)
            if (success) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("Failed to cancel appointment. Please try again."))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Cancellation failed. Check your internet connection."))
        }
    }
}

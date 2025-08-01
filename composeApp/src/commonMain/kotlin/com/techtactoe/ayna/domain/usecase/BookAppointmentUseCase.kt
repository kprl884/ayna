package com.techtactoe.ayna.domain.usecase

import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.domain.repository.AppointmentRepository
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for booking a new appointment
 */
class BookAppointmentUseCase(
    private val repository: AppointmentRepository
) {
    operator fun invoke(appointment: Appointment): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val success = repository.createAppointment(appointment)
            if (success) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("Failed to book appointment. Please try again."))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Booking failed. Check your internet connection."))
        }
    }
}

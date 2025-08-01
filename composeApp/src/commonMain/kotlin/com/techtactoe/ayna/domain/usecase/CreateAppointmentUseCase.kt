package com.techtactoe.ayna.domain.usecase

import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.domain.repository.AppointmentRepository
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for creating a new appointment
 */
class CreateAppointmentUseCase(
    private val repository: AppointmentRepository
) {
    operator fun invoke(appointment: Appointment): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val success = repository.createAppointment(appointment)
            if (success) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("Failed to create appointment. Please try again."))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Couldn't create appointment. Check your internet connection."))
        }
    }
}

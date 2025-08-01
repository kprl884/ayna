package com.techtactoe.ayna.domain.usecase

import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.domain.repository.AppointmentRepository
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for fetching user's appointments
 */
class GetUserAppointmentsUseCase(
    private val repository: AppointmentRepository
) {
    operator fun invoke(userId: String): Flow<Resource<List<Appointment>>> = flow {
        emit(Resource.Loading())
        try {
            val appointments = repository.getUserAppointments(userId)
            emit(Resource.Success(appointments))
        } catch (e: Exception) {
            emit(Resource.Error("Couldn't load appointments. Check your internet connection."))
        }
    }
    
    /**
     * Get only upcoming appointments
     */
    fun getUpcoming(userId: String): Flow<Resource<List<Appointment>>> = flow {
        emit(Resource.Loading())
        try {
            val appointments = repository.getUpcomingAppointments(userId)
            emit(Resource.Success(appointments))
        } catch (e: Exception) {
            emit(Resource.Error("Couldn't load upcoming appointments. Check your internet connection."))
        }
    }
}

package com.techtactoe.ayna.domain.usecase

import com.techtactoe.ayna.domain.model.TimeSlot
import com.techtactoe.ayna.domain.repository.AppointmentRepository
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for fetching available time slots for booking
 */
class GetAvailableTimeSlotsUseCase(
    private val repository: AppointmentRepository
) {
    operator fun invoke(salonId: String, serviceId: String, date: Long): Flow<Resource<List<TimeSlot>>> = flow {
        emit(Resource.Loading())
        try {
            val timeSlots = repository.getAvailableTimeSlots(salonId, serviceId, date)
            emit(Resource.Success(timeSlots))
        } catch (e: Exception) {
            emit(Resource.Error("Couldn't load available time slots. Check your internet connection."))
        }
    }
}

package com.techtactoe.ayna.domain.usecase

import com.techtactoe.ayna.domain.model.WaitlistRequest
import com.techtactoe.ayna.domain.repository.AppointmentRepository
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for joining a waitlist when no time slots are available
 */
class JoinWaitlistUseCase(
    private val repository: AppointmentRepository
) {
    operator fun invoke(request: WaitlistRequest): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val success = repository.joinWaitlist(request)
            if (success) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("Failed to join waitlist. Please try again."))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Couldn't join waitlist. Check your internet connection."))
        }
    }
}

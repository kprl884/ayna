package com.techtactoe.ayna.domain.usecase

import com.techtactoe.ayna.domain.model.UserProfile
import com.techtactoe.ayna.domain.repository.ProfileRepository
import com.techtactoe.ayna.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for fetching user profile information
 */
class GetUserProfileUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(userId: String): Flow<Resource<UserProfile>> = flow {
        emit(Resource.Loading())
        try {
            val profile = repository.getUserProfile(userId)
            if (profile != null) {
                emit(Resource.Success(profile))
            } else {
                emit(Resource.Error("User profile not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Couldn't load profile. Check your internet connection."))
        }
    }
}

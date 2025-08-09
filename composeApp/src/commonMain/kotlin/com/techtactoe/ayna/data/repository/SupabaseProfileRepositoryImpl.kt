package com.techtactoe.ayna.data.repository

import com.techtactoe.ayna.data.supabase.AynaSupabaseClient
import com.techtactoe.ayna.data.supabase.dto.ProfileDto
import com.techtactoe.ayna.data.supabase.mapper.*
import com.techtactoe.ayna.data.supabase.mapper.profileDtoToUserProfileDomainModel
import com.techtactoe.ayna.domain.model.PaymentMethod
import com.techtactoe.ayna.domain.model.PaymentMethodType
import com.techtactoe.ayna.domain.model.UserProfile
import com.techtactoe.ayna.domain.repository.ProfileRepository
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

/**
 * Supabase implementation of ProfileRepository
 * Handles all user profile-related data operations
 */
class SupabaseProfileRepositoryImpl : ProfileRepository {

    private val client = AynaSupabaseClient.database

    override suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            val profileResponse: ProfileDto? = client
                .from("profiles")
                .select(columns = Columns.ALL) {
                    filter { eq("id", userId) }
                }
                .decodeSingleOrNull<ProfileDto>()

            val result: UserProfile? = if (profileResponse != null) {
                val paymentMethods = getPaymentMethodsForUser(userId)
                val favoriteServices = getFavoriteServicesForUser(userId)

                val dto: ProfileDto = profileResponse
                dto.profileDtoToUserProfileDomainModel(
                    paymentMethods = paymentMethods,
                    favoriteServices = favoriteServices
                )
            } else {
                null
            }
            result
        } catch (e: Exception) {
            println("Error fetching user profile: ${e.message}")
            null
        }
    }

    override suspend fun updateUserProfile(profile: UserProfile): Boolean {
        return try {
            client
                .from("profiles")
                .update(profile.toUpdateDto()) {
                    filter { eq("id", profile.id) }
                }

            true
        } catch (e: Exception) {
            println("Error updating user profile: ${e.message}")
            false
        }
    }

    override suspend fun addPaymentMethod(userId: String, paymentMethod: PaymentMethod): Boolean {
        return try {
            // If this is set as default, unset other defaults first
            if (paymentMethod.isDefault) {
                client
                    .from("payment_methods")
                    .update(mapOf("is_default" to false)) {
                        filter { eq("user_id", userId) }
                    }
            }

            client
                .from("payment_methods")
                .insert(
                    mapOf(
                        "user_id" to userId,
                        "type" to paymentMethod.type.name,
                        "last_four_digits" to paymentMethod.lastFourDigits,
                        "expiry_date" to paymentMethod.expiryDate,
                        "is_default" to paymentMethod.isDefault
                    )
                )

            true
        } catch (e: Exception) {
            println("Error adding payment method: ${e.message}")
            false
        }
    }

    override suspend fun removePaymentMethod(userId: String, paymentMethodId: String): Boolean {
        return try {
            client
                .from("payment_methods")
                .delete {
                    filter {
                        eq("id", paymentMethodId)
                        eq("user_id", userId)
                    }
                }

            true
        } catch (e: Exception) {
            println("Error removing payment method: ${e.message}")
            false
        }
    }

    override suspend fun updateFavoriteServices(userId: String, serviceIds: List<String>): Boolean {
        return try {
            // Remove existing favorites
            client
                .from("user_favorites")
                .delete {
                    filter { eq("user_id", userId) }
                }

            // Add new favorites
            if (serviceIds.isNotEmpty()) {
                val favoritesToInsert = serviceIds.map { serviceId ->
                    mapOf(
                        "user_id" to userId,
                        "salon_id" to serviceId // TODO: This should be salon_id, need to map from service_id
                    )
                }

                client
                    .from("user_favorites")
                    .insert(favoritesToInsert)
            }

            true
        } catch (e: Exception) {
            println("Error updating favorite services: ${e.message}")
            false
        }
    }

    // Helper functions
    private suspend fun getPaymentMethodsForUser(userId: String): List<PaymentMethod> {
        return try {
            val paymentMethodsResponse = client
                .from("payment_methods")
                .select(columns = Columns.ALL) {
                    filter { eq("user_id", userId) }
                    order("is_default", order = Order.DESCENDING)
                }
                .decodeList<Map<String, Any>>()

            paymentMethodsResponse.map { paymentMap: Map<String, Any> ->
                PaymentMethod(
                    id = paymentMap["id"] as String,
                    type = PaymentMethodType.valueOf(paymentMap["type"] as String),
                    lastFourDigits = paymentMap["last_four_digits"] as String,
                    expiryDate = paymentMap["expiry_date"] as? String ?: "",
                    isDefault = paymentMap["is_default"] as Boolean
                )
            }
        } catch (e: Exception) {
            println("Error fetching payment methods: ${e.message}")
            emptyList()
        }
    }

    private suspend fun getFavoriteServicesForUser(userId: String): List<String> {
        return try {
            val favoritesResponse = client
                .from("user_favorites")
                .select(columns = Columns.raw("salon_id")) {
                    filter { eq("user_id", userId) }
                }
                .decodeList<Map<String, String>>()

            favoritesResponse.mapNotNull { map: Map<String, String> -> map["salon_id"] }
        } catch (e: Exception) {
            println("Error fetching favorite services: ${e.message}")
            emptyList()
        }
    }
}
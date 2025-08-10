package com.techtactoe.ayna.data.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage

/**
 * Supabase client configuration for Ayna app
 * Provides centralized access to all Supabase services
 */
object AynaSupabaseClient {

    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SupabaseEnv.url,
            supabaseKey = SupabaseEnv.anonKey
        ) {
            install(Auth) {
                // Configure authentication settings
                autoLoadFromStorage = true
                autoSaveToStorage = true
                // Enable session refresh if available in the current supabase-kt version
                // Some versions use alwaysAutoRefresh, others refresh automatically when possible
            }
            install(Postgrest) {
                // Default schema
                // For newer versions: defaultSchema = "public"
            }
            install(Realtime) {
                // Configure real-time subscriptions
            }
            install(Storage) {
                // Configure file storage
            }
        }
    }

    // Convenience accessors
    val auth get() = client.auth
    val database get() = client.postgrest
    val realtime get() = client.realtime
    val storage get() = client.storage
}
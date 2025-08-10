package com.techtactoe.ayna.data.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.FlowType
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
                // Enhanced authentication configuration
                autoLoadFromStorage = true
                autoSaveToStorage = true
                flowType = FlowType.PKCE
                scheme = "com.techtactoe.ayna"
                host = "auth"
                
                // Configure providers
                providers {
                    google {
                        // Will be configured with platform-specific client IDs
                    }
                    apple {
                        // Will be configured with Apple credentials
                    }
                }
            }
            install(Postgrest) {
                // Configure database settings
            }
            install(Realtime) {
                // Real-time configuration for live updates
            }
            install(Storage) {
                // File storage for profile pictures and salon images
            }
        }
    }

    // Convenience accessors
    val auth get() = client.auth
    val database get() = client.postgrest
    val realtime get() = client.realtime
    val storage get() = client.storage
}
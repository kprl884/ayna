package com.techtactoe.ayna.data.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

/**
 * Supabase client configuration for Ayna app
 * Provides centralized access to all Supabase services
 */
object AynaSupabaseClient {
    
    private const val SUPABASE_URL = "YOUR_SUPABASE_URL" // TODO: Replace with actual URL
    private const val SUPABASE_ANON_KEY = "YOUR_SUPABASE_ANON_KEY" // TODO: Replace with actual key
    
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_ANON_KEY
        ) {
            install(Auth) {
                // Configure authentication settings
                autoLoadFromStorage = true
                autoSaveToStorage = true
            }
            install(Postgrest) {
                // Configure database settings
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
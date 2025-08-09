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
    
    private const val SUPABASE_URL = "https://nrlzmqptvjcrndnzysqv.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5ybHptcXB0dmpjcm5kbnp5c3F2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTQ3NTI1ODIsImV4cCI6MjA3MDMyODU4Mn0.Z-kpn3PQ8bHwj-2NOvVlcrCxpQ9zqm9be_c4d43WHCg"
    
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
package com.techtactoe.ayna.data.supabase

/**
 * Environment provider for Supabase configuration.
 * Provide actual implementations in platform-specific source sets.
 */
expect object SupabaseEnv {
    val url: String
    val anonKey: String
}
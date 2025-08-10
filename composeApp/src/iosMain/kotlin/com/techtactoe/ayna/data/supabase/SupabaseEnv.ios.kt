package com.techtactoe.ayna.data.supabase

actual object SupabaseEnv {
    // Replace these placeholders per-environment via secure injection (e.g., Xcode build settings/Config files)
    actual val url: String = "<SUPABASE_URL>"
    actual val anonKey: String = "<SUPABASE_ANON_KEY>"
}
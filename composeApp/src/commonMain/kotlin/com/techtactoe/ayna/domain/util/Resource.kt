package com.techtactoe.ayna.domain.util

/**
 * A generic wrapper class that represents the state of a data operation
 * Provides type-safe handling of loading, success, and error states
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}

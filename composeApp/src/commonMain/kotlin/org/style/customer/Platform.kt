package org.style.customer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
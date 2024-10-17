package dev.cryptospace

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
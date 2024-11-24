package dev.cryptospace

object Environment {
    val HOST by lazy { System.getenv("HOST") ?: "http://localhost:8080" }
    val ALLOWED_HOST by lazy { System.getenv("ALLOWED_HOST") ?: null }
    val JWT_SECRET by lazy { checkNotNull(System.getenv("JWT_SECRET")) { "JWT_SECRET is not set" } }
}

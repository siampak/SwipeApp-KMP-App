package org.example.swipe_app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
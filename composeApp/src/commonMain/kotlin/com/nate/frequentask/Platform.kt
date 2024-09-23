package com.nate.frequentask

interface Platform {
    val os: OS
    val name: String
}

enum class OS {
    IOS,
    ANDROID
}

expect fun getPlatform(): Platform
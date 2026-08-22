package com.thesua7.kmpplayground

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
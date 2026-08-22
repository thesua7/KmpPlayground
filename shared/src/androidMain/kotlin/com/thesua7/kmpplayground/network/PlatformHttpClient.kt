package com.thesua7.kmpplayground.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual fun createHttpClient(): HttpClient {
    return HttpClient(OkHttp)
}
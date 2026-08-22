package com.thesua7.kmpplayground.repository

import com.thesua7.kmpplayground.model.ApiError
import com.thesua7.kmpplayground.model.LoginRequest
import com.thesua7.kmpplayground.model.LoginResponse
import com.thesua7.kmpplayground.network.ApiClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class LoginRepository(
    private val apiClient: ApiClient
) {

    suspend fun login(userName: String, password: String): LoginResponse {
       try {
           val response = apiClient.client.post("https://dummyjson.com/auth/login") {
               contentType(ContentType.Application.Json)
               setBody(
                   LoginRequest(
                       username = userName,
                       password = password
                   )
               )
           }

           println("HTTP status: ${response.status}")
           println("Content-Type: ${response.contentType()}")

           return response.body()
       }catch (e: ClientRequestException){
           val error = e.response.body<ApiError>()
           throw Exception(error.message)
       }
    }
}

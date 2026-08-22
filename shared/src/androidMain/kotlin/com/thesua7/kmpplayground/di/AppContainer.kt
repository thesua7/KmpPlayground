package com.thesua7.kmpplayground.di

import com.thesua7.kmpplayground.network.ApiClient
import com.thesua7.kmpplayground.repository.LoginRepository
import com.thesua7.kmpplayground.viewmodel.LoginViewmodel

class AppContainer {

    private val apiClient = ApiClient()
    private val loginRepository = LoginRepository(apiClient)

    fun createLoginViewModel(): LoginViewmodel {
        return LoginViewmodel(loginRepository)
    }
}
//
// Created by Sani Ahamed on 23/8/26.
//

import Foundation
import Combine
import Shared

final class LoginViewModelWrapper: ObservableObject {

    let viewModel: LoginViewmodel

    @Published var username = ""
    @Published var password = ""

    @Published var isLoading = false
    @Published var error: String? = nil
    @Published var isLoggedIn = false

    init() {
        let apiClient = ApiClient()

        let repository = LoginRepository(
            apiClient: apiClient
        )

        self.viewModel = LoginViewmodel(
            loginRepository: repository
        )

        observeUiState()
    }

    private func observeUiState() {
        viewModel.observeUiState { state in
            DispatchQueue.main.async {
                self.isLoading = state.isLoading
                self.error = state.error
                self.isLoggedIn = state.isLoggedIn
            }
        }
    }

    func onUsernameChanged(_ username: String) {
        viewModel.onUsernameChanged(username: username)
    }

    func onPasswordChanged(_ password: String) {
        viewModel.onPasswordChanged(password: password)
    }

    func login() {
        viewModel.login()
    }
}
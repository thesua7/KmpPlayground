import UIKit
import SwiftUI
import Shared

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Self.Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(
        _ uiViewController: UIViewController,
        context: Self.Context
    ) {}
}

import SwiftUI

struct ContentView: View {

    @StateObject private var viewModel =
        LoginViewModelWrapper()

    var body: some View {

        VStack(spacing: 16) {

            Text("Login")
                .font(.largeTitle)

            TextField(
                "Username",
                text: $viewModel.username
            )
                .textFieldStyle(.roundedBorder)
                .onChange(of: viewModel.username) { value in
                    viewModel.onUsernameChanged(value)
                }

            SecureField(
                "Password",
                text: $viewModel.password
            )
                .textFieldStyle(.roundedBorder)
                .onChange(of: viewModel.password) { value in
                    viewModel.onPasswordChanged(value)
                }

            Button {

                viewModel.login()

            } label: {

                if viewModel.isLoading {

                    ProgressView()

                } else {

                    Text("Login")
                }
            }
            .disabled(viewModel.isLoading)


            if let error = viewModel.error {

                Text(error)
            }


            if viewModel.isLoggedIn {

                Text("Login successful!")
            }
        }
        .padding()
    }
}
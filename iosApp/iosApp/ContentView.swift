import SwiftUI
import UIKit
import Shared


// MARK: - Compose View Controller Bridge
// Keep this if you also need to open your shared Compose UI
// somewhere from the iOS application.

struct ComposeView: UIViewControllerRepresentable {

    func makeUIViewController(
        context: Context
    ) -> UIViewController {

        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(
        _ uiViewController: UIViewController,
        context: Context
    ) {
        // Nothing required here for now.
    }
}


// MARK: - Login Screen

struct ContentView: View {

    @StateObject private var viewModel =
        LoginViewModelWrapper()

    var body: some View {

        ZStack {

            // Animated background
            ParticleFluidBackground()
                .ignoresSafeArea()

            ScrollView {

                VStack(spacing: 28) {

                    Spacer()
                        .frame(height: 70)

                    LoginHeader()

                    LoginGlassCard(
                        viewModel: viewModel
                    )

                    Spacer()
                        .frame(height: 40)
                }
            }
            .scrollDismissesKeyboard(.interactively)
        }
        .preferredColorScheme(.dark)
        .animation(
            .spring(
                response: 0.45,
                dampingFraction: 0.80
            ),
            value: viewModel.error
        )
        .animation(
            .spring(
                response: 0.45,
                dampingFraction: 0.75
            ),
            value: viewModel.isLoggedIn
        )
    }
}


// MARK: - Header

private struct LoginHeader: View {

    @State private var animateIcon = false

    var body: some View {

        VStack(spacing: 14) {

            ZStack {

                Circle()
                    .fill(
                        Color.white.opacity(0.08)
                    )
                    .frame(
                        width: 88,
                        height: 88
                    )

                Circle()
                    .stroke(
                        Color.white.opacity(0.18),
                        lineWidth: 1
                    )
                    .frame(
                        width: 88,
                        height: 88
                    )

                Circle()
                    .fill(
                        Color.blue.opacity(0.20)
                    )
                    .frame(
                        width: 64,
                        height: 64
                    )
                    .blur(radius: 15)

                Image(systemName: "person.fill")
                    .font(
                        .system(
                            size: 32,
                            weight: .medium
                        )
                    )
                    .foregroundStyle(.white)
            }
            .scaleEffect(
                animateIcon ? 1.05 : 0.96
            )
            .offset(
                y: animateIcon ? -3 : 3
            )
            .onAppear {

                withAnimation(
                    .easeInOut(duration: 2)
                        .repeatForever(
                            autoreverses: true
                        )
                ) {
                    animateIcon = true
                }
            }

            Text("Welcome Back")
                .font(
                    .system(
                        size: 31,
                        weight: .bold,
                        design: .rounded
                    )
                )
                .foregroundStyle(.white)

            Text("Sign in to continue")
                .font(
                    .system(
                        size: 15,
                        weight: .regular
                    )
                )
                .foregroundStyle(
                    .white.opacity(0.60)
                )
        }
    }
}


// MARK: - Glass Login Card

private struct LoginGlassCard: View {

    @ObservedObject var viewModel:
        LoginViewModelWrapper

    var body: some View {

        VStack(spacing: 18) {

            GlassInputField(
                title: "Username",
                icon: "person",
                text: $viewModel.username,
                keyboardType: .default
            ) { value in

                viewModel.onUsernameChanged(value)
            }

            GlassInputField(
                title: "Password",
                icon: "lock",
                text: $viewModel.password,
                isSecure: true
            ) { value in

                viewModel.onPasswordChanged(value)
            }

            LoginButton(
                isLoading: viewModel.isLoading
            ) {

                hideKeyboard()

                viewModel.login()
            }

            if let error = viewModel.error {

                StatusMessage(
                    text: error,
                    icon:
                    "exclamationmark.triangle.fill",
                    color: .red
                )
                    .transition(
                        .move(edge: .top)
                            .combined(with: .opacity)
                    )
            }

            if viewModel.isLoggedIn {

                StatusMessage(
                    text: "Login successful!",
                    icon: "checkmark.circle.fill",
                    color: .green
                )
                    .transition(
                        .scale
                            .combined(with: .opacity)
                    )
            }
        }
        .padding(24)
        .background {

            RoundedRectangle(
                cornerRadius: 30,
                style: .continuous
            )
                .fill(
                    Color.white.opacity(0.06)
                )
                .background(
                    .ultraThinMaterial,
                    in: RoundedRectangle(
                        cornerRadius: 30,
                        style: .continuous
                    )
                )
        }
        .overlay {

            RoundedRectangle(
                cornerRadius: 30,
                style: .continuous
            )
                .stroke(
                    LinearGradient(
                        colors: [
                            Color.white.opacity(0.25),
                            Color.white.opacity(0.05)
                        ],
                        startPoint: .topLeading,
                        endPoint: .bottomTrailing
                    ),
                    lineWidth: 1
                )
        }
        .shadow(
            color: Color.black.opacity(0.30),
            radius: 35,
            x: 0,
            y: 20
        )
        .padding(.horizontal, 22)
    }

    private func hideKeyboard() {

        UIApplication.shared.sendAction(
            #selector(
            UIResponder.resignFirstResponder
            ),
            to: nil,
            from: nil,
            for: nil
        )
    }
}


// MARK: - Reusable Input Field

private struct GlassInputField: View {

    let title: String
    let icon: String

    @Binding var text: String

    var isSecure: Bool = false

    var keyboardType:
        UIKeyboardType = .default

    var onChanged:
        (String) -> Void

    @FocusState private var focused: Bool

    var body: some View {

        HStack(spacing: 14) {

            Image(systemName: icon)
                .font(
                    .system(
                        size: 18,
                        weight: .medium
                    )
                )
                .foregroundStyle(
                    focused
                        ? Color.white
                        : Color.white.opacity(0.50)
                )
                .frame(width: 24)

            Group {

                if isSecure {

                    SecureField(
                        title,
                        text: $text
                    )

                } else {

                    TextField(
                        title,
                        text: $text
                    )
                        .keyboardType(keyboardType)
                        .textInputAutocapitalization(
                            .never
                        )
                        .autocorrectionDisabled()
                }
            }
            .focused($focused)
            .foregroundStyle(.white)
            .tint(.white)
            .onChange(of: text) { value in

                onChanged(value)
            }
        }
        .padding(.horizontal, 18)
        .frame(height: 58)
        .background {

            RoundedRectangle(
                cornerRadius: 18,
                style: .continuous
            )
                .fill(
                    Color.white.opacity(
                        focused
                            ? 0.13
                            : 0.065
                    )
                )
        }
        .overlay {

            RoundedRectangle(
                cornerRadius: 18,
                style: .continuous
            )
                .stroke(
                    focused
                        ? Color.white.opacity(0.45)
                        : Color.white.opacity(0.10),
                    lineWidth: 1
                )
        }
        .shadow(
            color:
            focused
                ? Color.blue.opacity(0.18)
                : Color.clear,
            radius: 16
        )
        .scaleEffect(
            focused ? 1.012 : 1
        )
        .animation(
            .spring(
                response: 0.30,
                dampingFraction: 0.72
            ),
            value: focused
        )
    }
}


// MARK: - Login Button

private struct LoginButton: View {

    let isLoading: Bool
    let action: () -> Void

    var body: some View {

        Button(action: action) {

            ZStack {

                LinearGradient(
                    colors: [
                        Color(
                            red: 0.28,
                            green: 0.36,
                            blue: 1
                        ),
                        Color(
                            red: 0.55,
                            green: 0.27,
                            blue: 0.95
                        )
                    ],
                    startPoint: .leading,
                    endPoint: .trailing
                )

                HStack(spacing: 10) {

                    if isLoading {

                        ProgressView()
                            .tint(.white)
                    }

                    Text(
                        isLoading
                            ? "Signing in..."
                            : "Login"
                    )
                        .font(
                            .system(
                                size: 16,
                                weight: .semibold
                            )
                        )

                    if !isLoading {

                        Image(
                            systemName:
                            "arrow.right"
                        )
                            .font(
                                .system(
                                    size: 14,
                                    weight: .bold
                                )
                            )
                    }
                }
                .foregroundStyle(.white)
            }
            .frame(
                maxWidth: .infinity
            )
            .frame(height: 56)
            .clipShape(
                RoundedRectangle(
                    cornerRadius: 18,
                    style: .continuous
                )
            )
            .shadow(
                color:
                Color.blue.opacity(0.30),
                radius: 18,
                y: 8
            )
        }
        .buttonStyle(
            PressScaleButtonStyle()
        )
        .disabled(isLoading)
        .opacity(
            isLoading ? 0.80 : 1
        )
    }
}


// MARK: - Button Press Micro Interaction

private struct PressScaleButtonStyle:
    ButtonStyle {

    func makeBody(
        configuration: Configuration
    ) -> some View {

        configuration.label
            .scaleEffect(
                configuration.isPressed
                    ? 0.965
                    : 1
            )
            .brightness(
                configuration.isPressed
                    ? -0.06
                    : 0
            )
            .animation(
                .spring(
                    response: 0.22,
                    dampingFraction: 0.65
                ),
                value:
                configuration.isPressed
            )
    }
}


// MARK: - Status Message

private struct StatusMessage: View {

    let text: String
    let icon: String
    let color: Color

    var body: some View {

        HStack(spacing: 11) {

            Image(systemName: icon)
                .font(
                    .system(
                        size: 16,
                        weight: .semibold
                    )
                )

            Text(text)
                .font(
                    .system(
                        size: 14,
                        weight: .medium
                    )
                )

            Spacer()
        }
        .foregroundStyle(color)
        .padding(14)
        .background {

            RoundedRectangle(
                cornerRadius: 14,
                style: .continuous
            )
                .fill(
                    color.opacity(0.10)
                )
        }
        .overlay {

            RoundedRectangle(
                cornerRadius: 14,
                style: .continuous
            )
                .stroke(
                    color.opacity(0.20),
                    lineWidth: 1
                )
        }
    }
}


// MARK: - Particle Model

private struct ParticleData {

    let x: CGFloat
    let y: CGFloat

    let size: CGFloat

    let speed: Double

    let opacity: Double

    let waveAmount: Double
}


// MARK: - Particle List

private let particles:
    [ParticleData] = [

    ParticleData(
        x: 0.08,
        y: 0.10,
        size: 3,
        speed: 0.028,
        opacity: 0.55,
        waveAmount: 14
    ),

    ParticleData(
        x: 0.18,
        y: 0.28,
        size: 5,
        speed: 0.035,
        opacity: 0.35,
        waveAmount: 20
    ),

    ParticleData(
        x: 0.30,
        y: 0.52,
        size: 2.5,
        speed: 0.045,
        opacity: 0.70,
        waveAmount: 12
    ),

    ParticleData(
        x: 0.42,
        y: 0.14,
        size: 4,
        speed: 0.032,
        opacity: 0.45,
        waveAmount: 18
    ),

    ParticleData(
        x: 0.54,
        y: 0.72,
        size: 3,
        speed: 0.040,
        opacity: 0.65,
        waveAmount: 15
    ),

    ParticleData(
        x: 0.66,
        y: 0.38,
        size: 6,
        speed: 0.025,
        opacity: 0.30,
        waveAmount: 22
    ),

    ParticleData(
        x: 0.78,
        y: 0.82,
        size: 2.5,
        speed: 0.050,
        opacity: 0.75,
        waveAmount: 10
    ),

    ParticleData(
        x: 0.88,
        y: 0.46,
        size: 4,
        speed: 0.038,
        opacity: 0.50,
        waveAmount: 18
    ),

    ParticleData(
        x: 0.94,
        y: 0.17,
        size: 3,
        speed: 0.044,
        opacity: 0.65,
        waveAmount: 13
    ),

    ParticleData(
        x: 0.13,
        y: 0.87,
        size: 5,
        speed: 0.030,
        opacity: 0.35,
        waveAmount: 21
    ),

    ParticleData(
        x: 0.25,
        y: 0.68,
        size: 2,
        speed: 0.052,
        opacity: 0.80,
        waveAmount: 10
    ),

    ParticleData(
        x: 0.37,
        y: 0.33,
        size: 3,
        speed: 0.041,
        opacity: 0.60,
        waveAmount: 15
    ),

    ParticleData(
        x: 0.49,
        y: 0.92,
        size: 4,
        speed: 0.034,
        opacity: 0.45,
        waveAmount: 16
    ),

    ParticleData(
        x: 0.60,
        y: 0.19,
        size: 2,
        speed: 0.048,
        opacity: 0.75,
        waveAmount: 11
    ),

    ParticleData(
        x: 0.72,
        y: 0.59,
        size: 5,
        speed: 0.028,
        opacity: 0.40,
        waveAmount: 20
    ),

    ParticleData(
        x: 0.83,
        y: 0.27,
        size: 2.5,
        speed: 0.050,
        opacity: 0.75,
        waveAmount: 12
    ),

    ParticleData(
        x: 0.91,
        y: 0.73,
        size: 4,
        speed: 0.036,
        opacity: 0.50,
        waveAmount: 17
    ),

    ParticleData(
        x: 0.05,
        y: 0.57,
        size: 2,
        speed: 0.055,
        opacity: 0.70,
        waveAmount: 9
    )
]


// MARK: - Single Animated Particle

private struct ParticleView: View {

    let particle: ParticleData

    let index: Int

    let time: TimeInterval

    let screenSize: CGSize

    var body: some View {

        let movement =
            time * particle.speed

        let rawY =
            particle.y - movement

        let normalizedY =
            rawY.truncatingRemainder(
                dividingBy: 1
            )

        let finalNormalizedY =
            normalizedY < 0
                ? normalizedY + 1
                : normalizedY

        let wave =
            sin(
                time * 0.8 +
                    Double(index)
            )
                * particle.waveAmount

        let pulse =
            0.65 +
                (
                    sin(
                        time * 1.5 +
                            Double(index)
                    ) + 1
                ) * 0.175

        ZStack {

            Circle()
                .fill(
                    Color.cyan.opacity(0.20)
                )
                .frame(
                    width:
                    particle.size * 4,
                    height:
                    particle.size * 4
                )
                .blur(radius: 5)

            Circle()
                .fill(Color.white)
                .frame(
                    width:
                    particle.size,
                    height:
                    particle.size
                )
        }
        .opacity(
            particle.opacity * pulse
        )
        .position(
            x:
            screenSize.width *
                particle.x +
                wave,

            y:
            screenSize.height *
                finalNormalizedY
        )
    }
}


// MARK: - Full Animated Background

private struct ParticleFluidBackground:
    View {

    @State private var moveBlobs = false

    var body: some View {

        TimelineView(.animation) { timeline in

            GeometryReader { geometry in

                let time =
                    timeline.date
                        .timeIntervalSinceReferenceDate

                ZStack {

                    // Dark base gradient
                    LinearGradient(
                        colors: [

                            Color(
                                red: 0.018,
                                green: 0.025,
                                blue: 0.075
                            ),

                            Color(
                                red: 0.035,
                                green: 0.045,
                                blue: 0.13
                            ),

                            Color(
                                red: 0.01,
                                green: 0.012,
                                blue: 0.035
                            )
                        ],
                        startPoint:
                        .topLeading,
                        endPoint:
                        .bottomTrailing
                    )

                    // Blue fluid glow
                    Circle()
                        .fill(
                            Color.blue.opacity(
                                0.35
                            )
                        )
                        .frame(
                            width:
                            geometry.size.width
                                * 1.25,

                            height:
                            geometry.size.width
                                * 1.25
                        )
                        .blur(radius: 110)
                        .offset(
                            x:
                            moveBlobs
                                ? 120
                                : -130,

                            y:
                            moveBlobs
                                ? -190
                                : -60
                        )

                    // Purple fluid glow
                    Circle()
                        .fill(
                            Color.purple.opacity(
                                0.28
                            )
                        )
                        .frame(
                            width:
                            geometry.size.width
                                * 1.35,

                            height:
                            geometry.size.width
                                * 1.35
                        )
                        .blur(radius: 125)
                        .offset(
                            x:
                            moveBlobs
                                ? -130
                                : 130,

                            y:
                            moveBlobs
                                ? 320
                                : 150
                        )

                    // Cyan glow
                    Circle()
                        .fill(
                            Color.cyan.opacity(
                                0.12
                            )
                        )
                        .frame(
                            width:
                            geometry.size.width
                                * 0.95,

                            height:
                            geometry.size.width
                                * 0.95
                        )
                        .blur(radius: 100)
                        .offset(
                            x:
                            moveBlobs
                                ? 120
                                : -100,

                            y:
                            moveBlobs
                                ? 500
                                : 400
                        )

                    // Moving particles
                    ForEach(
                        Array(
                            particles.enumerated()
                        ),
                        id: \.offset
                    ) { index, particle in

                        ParticleView(
                            particle: particle,
                            index: index,
                            time: time,
                            screenSize:
                            geometry.size
                        )
                    }
                }
                .onAppear {

                    withAnimation(
                        .easeInOut(
                            duration: 9
                        )
                            .repeatForever(
                                autoreverses: true
                            )
                    ) {

                        moveBlobs = true
                    }
                }
            }
        }
    }
}


// MARK: - Preview

#Preview {
    ContentView()
}
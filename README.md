# KmpPlayground

Kotlin Multiplatform playground targeting **Android** and **iOS**.

Shared business logic, networking, and models live in `:shared`. UI is platform-specific:

- **Android:** Jetpack Compose (`LoginScreen`)
- **iOS:** SwiftUI (`ContentView`) wrapping the shared `LoginViewmodel`

## Architecture

```text
LoginViewModel
    ↓
LoginRepository
    ↓
ApiClient
    ↓
expect/actual HttpClient
    ├── Android: OkHttp
    └── iOS: Darwin
```

Login calls `POST https://dummyjson.com/auth/login` with kotlinx.serialization JSON (`Content-Type: application/json`).

Test credentials:

- username: `emilys`
- password: `emilyspass`

## Directory structure

```text
kmpPlayground/
├── androidApp/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── kotlin/com/thesua7/kmpplayground/
│       │   └── MainActivity.kt
│       └── res/
│           ├── drawable/
│           ├── drawable-v24/
│           ├── mipmap-*/
│           └── values/strings.xml
├── iosApp/
│   ├── Configuration/
│   │   └── Config.xcconfig
│   ├── iosApp/
│   │   ├── Assets.xcassets/
│   │   ├── Preview Content/
│   │   ├── ContentView.swift
│   │   ├── LoginViewModelWrapper.swift
│   │   ├── iOSApp.swift
│   │   └── Info.plist
│   └── iosApp.xcodeproj/
├── shared/
│   ├── build.gradle.kts
│   └── src/
│       ├── commonMain/
│       │   ├── composeResources/drawable/
│       │   └── kotlin/com/thesua7/kmpplayground/
│       │       ├── App.kt
│       │       ├── Greeting.kt
│       │       ├── GreetingUtil.kt
│       │       ├── Platform.kt
│       │       ├── model/
│       │       │   ├── ApiError.kt
│       │       │   ├── LoginRequest.kt
│       │       │   └── LoginResponse.kt
│       │       ├── network/
│       │       │   ├── ApiClient.kt
│       │       │   └── PlatformHttpClient.kt          # expect
│       │       ├── repository/
│       │       │   └── LoginRepository.kt
│       │       └── viewmodel/
│       │           ├── LoginUiState.kt
│       │           └── LoginViewmodel.kt
│       ├── androidMain/kotlin/com/thesua7/kmpplayground/
│       │   ├── LoginScreen.kt
│       │   ├── Platform.android.kt
│       │   ├── di/AppContainer.kt
│       │   └── network/PlatformHttpClient.kt          # actual OkHttp
│       ├── iosMain/kotlin/com/thesua7/kmpplayground/
│       │   ├── MainViewController.kt
│       │   ├── Platform.ios.kt
│       │   └── network/PlatformHttpClient.kt          # actual Darwin
│       ├── commonTest/
│       ├── androidHostTest/
│       └── iosTest/
├── gradle/
│   ├── libs.versions.toml
│   ├── gradle-daemon-jvm.properties
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

### Module notes

| Path | Role |
|---|---|
| `shared/src/commonMain` | Shared models, repository, ViewModel, Ktor `ApiClient` |
| `shared/src/androidMain` | Android Compose login UI, OkHttp engine, `AppContainer` |
| `shared/src/iosMain` | Darwin engine, Compose `MainViewController` (unused by the current SwiftUI screen) |
| `androidApp` | Android application entry point |
| `iosApp` | Xcode/SwiftUI app that calls shared Kotlin via `LoginViewModelWrapper` |
| `gradle/libs.versions.toml` | Version Catalog (Kotlin, AGP, Ktor, Compose, serialization) |

## Stack

- Kotlin `2.4.10`
- AGP `9.0.1`
- Compose Multiplatform `1.11.1`
- Ktor `3.3.1` (OkHttp on Android, Darwin on iOS)
- kotlinx.serialization JSON

## Running the apps

Use the run configurations in the IDE toolbar, or:

- Android: `./gradlew :androidApp:assembleDebug`
- iOS: open `iosApp/` in Xcode and run it from there

## Running tests

- Android host tests: `./gradlew :shared:testAndroidHostTest`
- iOS tests: `./gradlew :shared:iosSimulatorArm64Test`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html).

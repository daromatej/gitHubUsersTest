# StackOverflow Users

Android app that shows the top 20 StackOverflow users by reputation. You can follow/unfollow users — this is stored locally and persists between sessions.

If the network is unavailable, an error screen is shown with a retry button.

## Build & Run

Open in Android Studio, sync Gradle, run on a device.

```
./gradlew assembleDebug
./gradlew test
```

## Architecture

MVVM + Repository pattern.

- `data/` — Retrofit API service, DTOs, SharedPreferences based follow storage, repository 
  implementation
- `domain/` — `User` model and `UserRepository` interface
- `di/` — Hilt modules
- `ui/` — ViewModel, Compose screen, UI state

The ViewModel exposes a `StateFlow<UiState>` (Loading | Success | Error) and the screen just renders whatever state it gets. The repository interface sits between the ViewModel and data sources, which makes it easy to mock in tests.

I didn't add a use-case layer — for a single-screen app it would just be a pass-through without adding value.

## Libraries

- **Retrofit + Gson** — network calls and JSON parsing
- **Coil** — image loading
- **Hilt** — dependency injection
- **Jetpack Compose** — UI


## Decisions

- **SharedPreferences over DataStore** — we're just storing a set of user IDs, no need for anything more complex

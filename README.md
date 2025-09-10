# N-Queens Problem Game

This is a simple Android application allowing users to solve the N-Queens problem.
The user can choose the size of the board from 4x4 to maximum that fits on their screen.

## Building and Testing

**Building the game:**

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Build the project using the "Run" button in Android Studio.

Alternatively, you can clone the repository and use Gradle from the command line:

```bash
./gradlew assembleDebug
```
Debug APK will be generated in `app/build/outputs/apk/debug/`.

**Running tests:**
There are two ways to run tests:
1. Using Android Studio:
   - Open the project in Android Studio.
   - Right-click on the `app/src/test` directory and select "Run 'Tests in Queens.app...'" to run unit tests.
   - Right-click on the `app/src/androidTest` directory and select "Run 'All Tests'" to run UI tests.
2. Using Gradle from the command line:

```bash
./gradlew testDebugUnitTest  # For unit tests
./gradlew connectedDebugAndroidTest  # For UI tests
```

## Architecture
The application follows the MVVM architecture pattern, ensuring a clear separation of concerns and making the codebase more maintainable and testable.
The application is built using Jetpack Compose for the UI and Navigation Compose for navigation.
The game logic is separated in a simple GameEngine class, though not storing the game state, only providing functions to check the game state on a provided board for the sake of simplicity.

The project is organized into several packages:
- `ui` contains core UI components, such as QueensTheme, colors, and typography. Also, it contains custom widgets, currently it is only a chess board widget and a button.
- `data` package contains core data structures, such as the Board, Piece and Position classes.
- `navigation` package contains the navigation graph, that is separated due to better testability.
- Other packages contain screens UI and logic. One package per screen. The GameEngine class is in the `game` package, as it is only used in the game screen.


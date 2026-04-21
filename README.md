# Nor4x4

Nor4x4 is a simple Android Wear OS application designed to act as a dedicated timer for the Norwegian 4x4 exercise protocol. It helps users easily keep track of their warm-up, high-intensity intervals, recovery periods, and cool-down right from their wrist.

**Note:** This app is not yet available in the Google Play Store as it is pending approval.

## Features

- Dedicated timer for the 4x4 interval training protocol.
- Customizable settings for reps, warm-up, interval, recovery, and cool-down durations.
- Runs on Wear OS devices.

## The Norwegian 4x4 Protocol

The Norwegian 4x4 protocol is a highly effective High-Intensity Interval Training (HIIT) method designed to improve cardiovascular fitness (VO2 max).

A typical session consists of:
1. **Warm-up**: 10 minutes at a light to moderate pace.
2. **Intervals**: 4 repetitions of 4 minutes at high intensity (typically 85-95% of maximum heart rate).
3. **Active Recovery**: 3 minutes of lighter exercise (typically around 70% of maximum heart rate) between each high-intensity interval.
4. **Cool-down**: 5 minutes at a light pace.

*The app's default configuration matches these standard durations.*

## Installation and Building

Since the app is not currently available on the Google Play Store, you can build and sideload it onto your Wear OS device using Android Studio or the command line.

### Prerequisites

- Android Studio installed on your computer.
- A Wear OS device with Developer Options and ADB Debugging enabled (or a Wear OS Emulator).

### Building with Android Studio

1. Clone this repository to your local machine:
   ```bash
   git clone <repository_url>
   ```
2. Open the project in Android Studio.
3. Connect your Wear OS watch via USB or Wi-Fi debugging.
4. Select your Wear OS device from the target device drop-down menu in the toolbar.
5. Click the **Run** button (green play icon) or press `Shift + F10` to build and install the app on your watch.

### Building from Command Line (Gradle)

1. Clone this repository to your local machine.
2. Navigate to the root directory of the project in your terminal.
3. Run the following command to build the APK:
   ```bash
   ./gradlew assembleDebug
   ```
4. Once the build is successful, install the APK on your connected watch using ADB:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

## License

This project is licensed under the [LICENSE](LICENSE) file in the root directory.
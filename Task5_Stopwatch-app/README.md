# Stopwatch Application

A professional, high-precision time-tracking tool for Android, designed with a sleek dark aesthetic and robust functionality.

## 🚀 Core Functionality

- **High Precision Timing**: Measures elapsed time with millisecond accuracy using `SystemClock.uptimeMillis()`.
- **Start / Resume**: Begin a new session or resume from a paused state.
- **Pause**: Temporarily hold the current timer value.
- **Lap System**: Record specific time intervals without stopping the main timer. Laps are displayed in a scrollable list with the most recent at the top.
- **Reset**: Clear all elapsed time and lap history to return to a fresh state.

## 🎨 Professional Design & UI

- **Dark Mode Aesthetic**: A pure black background (`#000000`) for high contrast and battery efficiency on OLED screens.
- **Custom Green Highlights**: Timer display and lap history use a vibrant green (`#41AB41`) for maximum readability.
- **Pill-Shaped Buttons**: Custom-designed buttons with a 45-degree green gradient and 40dp rounded corners.
- **Haptic Feedback**: Tactile vibrations on button interactions for a more responsive and premium feel.
- **Responsive Layout**: Built using `ConstraintLayout` to ensure a consistent experience across different screen sizes.

## 🛠 Technical Features

- **State Persistence**: Handles configuration changes (like screen rotation) seamlessly. Your timer and lap data won't be lost when you rotate your device.
- **Modern Java**: Implemented using clean, modern Android practices including Lambda expressions and `ViewCompat` for edge-to-edge support.
- **Smooth Refresh Rate**: UI updates every 10ms to provide a fluid, "live" feel while maintaining CPU efficiency.
- **Resource Management**: Fully localized-ready strings and colors stored in dedicated XML resources.

## 📱 Getting Started

1. Clone this repository.
2. Open the project in **Android Studio**.
3. Build and Run on your Android device or Emulator.

## 📝 Format
The timer is displayed in the following format:
`Minutes : Seconds : Milliseconds` (e.g., `00:00:000`)

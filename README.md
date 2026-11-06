# Movement Plan

Android app for the treadmill, strength, and digestion planner. The UI is `app/src/main/assets/movement_plan.html`, shown in a WebView (`com.rafi.movementplan`).

- minSdk 26
- targetSdk 36
- compileSdk 37
- versionName 1.0.3 (versionCode 4)

Daylight UI with Home, Train, Track, and Plan. Settings stores height (default 179 cm) and an optional goal weight on the device. The weight log stays on the device and starts empty. Until the first entry, Home shows 135 kg at 179 cm (BMI 42.1). Goal lines stay hidden until a goal is set.

## Build

Open the project in Android Studio, or from this directory:

```bash
./gradlew assembleDebug
```

The debug APK is signed with the debug keystore using JAR v1 plus APK Signature Scheme v2 and v3, so a copy that has been emailed or downloaded still installs.

Output: `app/build/outputs/apk/debug/app-debug.apk`

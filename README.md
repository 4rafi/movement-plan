# Movement Plan

Android app for the treadmill, strength, and digestion planner. The UI is `app/src/main/assets/movement_plan.html`, shown in a WebView (`com.rafi.movementplan`).

- minSdk 26
- targetSdk 36
- compileSdk 37
- versionName 1.0.2 (versionCode 3)

The Weight tab logs kilograms on the device, draws them as a line, and points the header weight and BMI gauge at the latest entry. Height stays 179 cm. The log starts empty.

## Build

Open the project in Android Studio, or from this directory:

```bash
./gradlew assembleDebug
```

The debug APK is signed with the debug keystore using JAR v1 plus APK Signature Scheme v2 and v3, so a copy that has been emailed or downloaded still installs.

Output: `app/build/outputs/apk/debug/app-debug.apk`

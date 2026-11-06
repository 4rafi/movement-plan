plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.rafi.movementplan"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.rafi.movementplan"
        minSdk = 26
        targetSdk = 36
        versionCode = 5
        versionName = "1.0.4"
    }

    // minSdk 26 disables JAR v1 by default. Email clients and some sideload
    // paths still expect a v1 signature alongside APK Signature Scheme v2/v3.
    // The keystore is committed on purpose: cloud build VMs do not share
    // ~/.android/debug.keystore, so a fresh machine key would block in-place
    // updates. These are the public Android debug credentials, not a Play
    // upload key.
    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("keystore/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = false
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
}

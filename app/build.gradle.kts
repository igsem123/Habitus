plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "br.com.app.src.main.kotlin.com.habitus"
    compileSdk = 35

    defaultConfig {
        applicationId = "br.com.app.src.main.kotlin.com.habitus"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //implementation(libs.firebase.common.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Hilt dependencies
    implementation(libs.hilt.android)
    kapt(libs.google.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose) // Para hiltViewModel() no Compose

    // For instrumentation tests
    androidTestImplementation(libs.dagger.hilt.android.testing)
    kaptAndroidTest(libs.google.hilt.compiler)

    // For local unit tests
    testImplementation(libs.dagger.hilt.android.testing)
    kaptTest(libs.google.hilt.compiler)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)

    // Google Fonts
    implementation (libs.androidx.ui.text.google.fonts)

    // Icons
    implementation(libs.font.awesome)
    implementation(libs.line.awesome)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Calendar
    implementation(libs.calendar)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.14.0"))
    implementation("com.google.firebase:firebase-analytics")

    // Authentication
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    val credentialManagerVersion = "1.5.0-alpha05"
    implementation("androidx.credentials:credentials:$credentialManagerVersion")
    implementation("androidx.credentials:credentials-play-services-auth:$credentialManagerVersion")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    // Ranking Graphics
    implementation(libs.mpandroidchart)
}
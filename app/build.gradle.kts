plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //ksp plugin
    alias(libs.plugins.ksp)

//hilt plugin
    alias(libs.plugins.hilt)

}

android {
    namespace = "com.practice.reelbreak"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.practice.reelbreak"
        minSdk = 27
        targetSdk = 36
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

    //Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    //Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.runtime)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    //Compose UI
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.ui.text)

    //Camera
    implementation(libs.androidx.camera.camera2.pipe)

    //unit test
    testImplementation(libs.junit)

    //Android Test
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    //Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Accompanist pager dependency for onboarding screen
    val accompanistVersion = "0.36.0"
    implementation("com.google.accompanist:accompanist-pager:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-pager-indicators:$accompanistVersion")


    // Navigation
    implementation(libs.androidx.navigation.runtime.ktx)
    val nav_version = "2.9.3"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    //Room Database
       implementation(libs.androidx.room.ktx)

    //DataStore dependency
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    //Splash screen dependency
    implementation("androidx.core:core-splashscreen:1.0.1")

    //Lifecycle ViewModel (Compose) dependency
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.1")

    //Coroutines (ViewModel scope) dependency
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Material Icons
    implementation("androidx.compose.material:material-icons-extended")

    // hilt
    implementation(libs.androidx.hilt.android)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.compose)
}
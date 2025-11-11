plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.yempe.financeapps.basicconverter"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.yempe.financeapps.basicconverter"
        minSdk = 26
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
        buildConfig = true
        compose = true
    }
}

dependencies {

    implementation(project(":core:core-navigation"))
    implementation(project(":feature:feature-converter"))
    implementation(project(":feature:feature-settings"))

    //compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.material3.icons)
    implementation(libs.compose.preview)
    implementation(libs.activity.compose)
    implementation(libs.compose.navigation)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation)

    // coroutine
    implementation(libs.coroutines.android)

    // timber
    implementation(libs.timber.log)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

room {
    schemaDirectory("$projectDir/schemas")
}

android {
    namespace = "com.yempe.financeapps.core.database"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":core:core-common"))

    //room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.test.core.ktx)
    ksp(libs.room.compiler)

    //coroutine
    implementation(libs.coroutines.android)

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)

    // test
    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.coroutines.test)
    testImplementation(kotlin("test"))

    //android test
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.room.testing)
}
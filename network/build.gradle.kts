plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-android")
    id("com.google.devtools.ksp")
}

apply(from = "$rootDir/secret.gradle.kts")
val stagingApi: List<Pair<String, String>> by project.extra

android {
    namespace = "com.fcerio.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            stagingApi.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    api(libs.moshi)

    // Retrofit
    api(libs.retrofit)
    api(libs.retrofit.moshi)
    api(libs.retrofit.scalars)

    // OkHttp
    api(libs.okhttp)
    api(libs.okhttp.logging)

    // Dagger-Hilt
    ksp(libs.hilt.compiler)
    api(libs.hilt.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
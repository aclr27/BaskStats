plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")

}

android {
    namespace = "com.example.baskstatsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.baskstatsapp"
        minSdk = 26
        targetSdk = 34
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
        isCoreLibraryDesugaringEnabled = true
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

    // Dependencias base y de actividad Compose
    implementation(libs.androidx.core.ktx)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // Actualizado
    implementation("androidx.activity:activity-compose:1.8.2") // Si no tienes la última

    // Compose BOM para gestionar versiones de Compose
    implementation(platform(libs.androidx.compose.bom))

    // Módulos de Compose que dependen del BOM (no necesitan versión explícita aquí)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation ("androidx.compose.material:material-icons-extended")

    // Dependencia de Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.0") // O 2.7.7 si estás usando libs.versions.toml

    implementation(libs.androidx.foundation.android)

    // Dependencias de Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Para java.time en APIs antiguas
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // Room components
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Kotlin Coroutines (¡Asegúrate de añadir estas!)
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0" )
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // ViewModel para Compose (necesitarás esto para los ViewModels que vengan)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
}
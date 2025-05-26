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
        minSdk = 24
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
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM para gestionar versiones de Compose
    // Asegúrate de que esta línea esté, y que en libs.versions.toml, 'compose-bom' tenga una versión actualizada.
    implementation(platform(libs.androidx.compose.bom))

    // Módulos de Compose que dependen del BOM (no necesitan versión explícita aquí)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation ("androidx.compose.material:material-icons-extended") // Esta está bien así

    // Dependencia de Navigation Compose (esta es una dependencia independiente del BOM de Compose UI/Material)
    implementation("androidx.navigation:navigation-compose:2.7.0") // O la 2.9.0 que ya tienes

    implementation(libs.androidx.foundation.android) // Esta también está bien

    // Dependencias de Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom)) // Aquí también, a través del BOM
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Para java.time en APIs antiguas
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation("androidx.room:room-runtime:2.6.1") // Versión estable actual, puedes buscar la última
    kapt("androidx.room:room-compiler:2.6.1") // Para el procesador de anotaciones
    implementation("androidx.room:room-ktx:2.6.1") // Para
}
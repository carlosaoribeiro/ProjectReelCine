import org.gradle.kotlin.dsl.implementation

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.gms.google-services")
}

val tmdbApiKey = project.findProperty("TMDB_API_KEY")?.toString() ?: ""

android {
    namespace = "com.carlosribeiro.reelcineproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.carlosribeiro.reelcineproject"
        minSdk = 24
        targetSdk = 35
        versionCode = 2
        versionName = "1.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")

        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
        languageVersion = "1.9"
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    // --- AndroidX Core ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material) // Fornece MaterialCardView, substituindo o antigo CardView
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    // implementation(libs.androidx.cardview) // Removido, pois o 'material' já oferece o MaterialCardView
    implementation(libs.androidx.drawerlayout)

    // ADICIONE a nova dependência para o Google Identity Services
    implementation(libs.googleid)


    // --- Firebase ---
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)

    // App Check: Dependências separadas para Debug e Release
    debugImplementation(libs.firebase.appcheck.debug) // Usado apenas ao rodar em modo Debug
    releaseImplementation(libs.firebase.appcheck.playintegrity) // Usado apenas na versão de Release

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // --- Rede / API ---
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // --- Imagens ---
    // implementation(libs.coil) // Removido para padronizar o uso de apenas uma biblioteca de imagem (Glide)
    implementation(libs.glide)
    kapt(libs.glide.compiler)
    implementation(libs.circleimageview)

    // --- Animações ---
    implementation(libs.lottie)

    // --- Testes ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
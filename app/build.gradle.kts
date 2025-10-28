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
        versionCode = 7
        versionName = "1.0.7"

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
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // ⚠️ Se já tiver o keystore, descomente e configure aqui:
            // signingConfig = signingConfigs.getByName("release")
        }

        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
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
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.drawerlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.credentials)

    // --- Google Identity Services ---
    implementation(libs.googleid)

    // --- Firebase (via BOM) ---
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)

    // --- App Check (modo seguro para Release e Debug) ---
    debugImplementation("com.google.firebase:firebase-appcheck-debug:17.1.1")
    releaseImplementation("com.google.firebase:firebase-appcheck-playintegrity:17.1.1")

    // --- Rede / API ---
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // --- Imagens ---
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

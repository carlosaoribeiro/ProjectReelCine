plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt") // 👈 direto aqui, sem alias
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
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ✅ Injeta a TMDB API KEY corretamente aqui
        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
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
}

dependencies {
    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)

    // Imagens
    implementation(libs.coil)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor.v4120)


    // Testes
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.ktx) // ou versão recente

    implementation(libs.google.firebase.auth.ktx)
    implementation(libs.play.services.auth)


    implementation(libs.circleimageview)

    implementation(libs.androidx.drawerlayout)
    implementation(libs.material.v1110)

    implementation(libs.androidx.fragment.ktx) // Ou versão mais recente

    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    implementation (libs.androidx.cardview)

    implementation (libs.material) // ou versão mais recente




}

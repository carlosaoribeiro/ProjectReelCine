package com.carlosribeiro.reelcineproject

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.AppCheckProviderFactory
import com.carlosribeiro.reelcineproject.BuildConfig

class ReelCineApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        val appCheck = FirebaseAppCheck.getInstance()

        if (BuildConfig.DEBUG) {
            // 🔹 Usa reflexão para evitar erro de compilação no release
            try {
                val debugFactoryClass = Class.forName(
                    "com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory"
                )
                val getInstanceMethod = debugFactoryClass.getMethod("getInstance")
                val factoryInstance = getInstanceMethod.invoke(null) as AppCheckProviderFactory

                appCheck.installAppCheckProviderFactory(factoryInstance)
                Log.d("AppCheck", "Modo DEBUG: usando DebugAppCheckProviderFactory")
            } catch (e: Exception) {
                Log.e("AppCheck", "DebugAppCheckProviderFactory indisponível: ${e.message}")
            }
        } else {
            try {
                val playIntegrityFactoryClass = Class.forName(
                    "com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory"
                )
                val getInstanceMethod = playIntegrityFactoryClass.getMethod("getInstance")
                val factoryInstance = getInstanceMethod.invoke(null) as AppCheckProviderFactory

                appCheck.installAppCheckProviderFactory(factoryInstance)
                Log.d("AppCheck", "Modo RELEASE: usando PlayIntegrityAppCheckProviderFactory")
            } catch (e: Exception) {
                Log.e("AppCheck", "Erro ao configurar AppCheck RELEASE: ${e.message}")
            }
        }
    }
}

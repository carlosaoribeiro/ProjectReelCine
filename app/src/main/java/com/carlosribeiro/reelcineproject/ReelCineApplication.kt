package com.carlosribeiro.reelcineproject

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory


class ReelCineApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        val appCheck = FirebaseAppCheck.getInstance()

        // ...
        if (BuildConfig.DEBUG) {
            Log.d("AppCheck", "Modo DEBUG: usando DebugAppCheckProviderFactory")
            appCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance()
            )
        }
        /* else { // Comentado para focar no design
            Log.d("AppCheck", "Modo RELEASE: usando PlayIntegrityAppCheckProviderFactory")
            appCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance()
            )
        } */
    }
}

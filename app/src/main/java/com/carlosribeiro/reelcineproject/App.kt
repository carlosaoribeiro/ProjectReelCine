package com.carlosribeiro.reelcineproject

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        // O App Check com Play Integrity não precisa desta inicialização manual aqui.
        // A configuração principal é feita no console do Firebase.
    }
}
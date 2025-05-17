package com.carlosribeiro.reelcineproject.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_UID = "uid"
        private const val KEY_EMAIL = "email"
        private const val KEY_NOME = "nome"
    }

    fun saveUser(nome: String, email: String, uid: String) {
        prefs.edit().apply {
            putString(KEY_NOME, nome)
            putString(KEY_EMAIL, email)
            putString(KEY_UID, uid)
            apply()
        }
    }

    fun getUserName(): String? = prefs.getString(KEY_NOME, null)
    fun getUserEmail(): String? = prefs.getString(KEY_EMAIL, null)
    fun getUserUid(): String? = prefs.getString(KEY_UID, null)

    fun clear() {
        prefs.edit().clear().apply()
    }
}

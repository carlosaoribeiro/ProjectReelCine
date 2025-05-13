package com.carlosribeiro.reelcineproject.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUser(nome: String, email: String, uid: String) {
        prefs.edit().apply {
            putString("nome", nome)
            putString("email", email)
            putString("uid", uid)
            apply()
        }
    }

    fun getUserName(): String? = prefs.getString("nome", null)
    fun getUserEmail(): String? = prefs.getString("email", null)
    fun getUserUid(): String? = prefs.getString("uid", null)

    fun clear() {
        prefs.edit().clear().apply()
    }
}

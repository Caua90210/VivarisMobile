package br.senai.sp.jandira.vivaris.security

import android.content.Context

class TokenManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    fun deleteToken() {
        sharedPreferences.edit().remove("jwt_token").apply()
    }
}
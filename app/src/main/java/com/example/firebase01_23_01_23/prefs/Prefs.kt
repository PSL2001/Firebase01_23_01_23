package com.example.firebase01_23_01_23.prefs

import android.content.Context

class Prefs(c: Context) {
    val store = c.getSharedPreferences("LOGIN", 0)

    public fun guardarEmail(email: String) {
        store.edit().putString("EMAIL", email).apply()
    }

    public fun leerEmail(): String? {
        return store.getString("EMAIL", null)
    }

    public fun borradoTotal() {
        store.edit().clear().apply()
    }
}
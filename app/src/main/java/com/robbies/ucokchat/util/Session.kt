package com.robbies.ucokchat.util

import android.content.Context
import java.util.UUID

class Session(context: Context) {
    private val sharedPreferences = SecureSharedPrefs.getSharedPreferences(context)
    private val sessionPrefKey = "session"

    fun initSessionID() {
        val session = getSessionID()
        if (session.isEmpty()) {
            val newSessionId = UUID.randomUUID().toString()
            sharedPreferences.edit().putString(sessionPrefKey, newSessionId).apply()
        }
    }

    fun getSessionID(): String {
        val result = sharedPreferences.getString(sessionPrefKey, "")
        return if (!result.isNullOrEmpty()) result else ""
    }
}
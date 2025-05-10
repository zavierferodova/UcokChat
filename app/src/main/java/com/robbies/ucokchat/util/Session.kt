package com.robbies.ucokchat.util

import android.content.Context
import java.util.UUID
import androidx.core.content.edit

class Session(context: Context) {
    private val sharedPreferences = SecureSharedPrefs.getSharedPreferences(context)
    private val sessionPrefKey = "session"

    fun initSessionID() {
        val session = getSessionID()
        if (session.isEmpty()) {
            val newSessionId = UUID.randomUUID().toString()
            sharedPreferences.edit { putString(sessionPrefKey, newSessionId) }
        }
    }

    fun getSessionID(): String {
        val result = sharedPreferences.getString(sessionPrefKey, "")
        return if (!result.isNullOrEmpty()) result else ""
    }
}
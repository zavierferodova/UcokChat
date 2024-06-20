package com.robbies.ucokchat.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.robbies.ucokchat.data.entity.SessionEntity
import com.robbies.ucokchat.util.SecureSharedPrefs
import com.robbies.ucokchat.util.getNowTimestampString
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.UUID

open class FirebaseRepository(
    private val database: FirebaseDatabase,
    private val context: Context
) {
    private val sharedPreferences = SecureSharedPrefs.getSharedPreferences(context)
    private val sessionPrefKey = "session"

    init {
        initSessionID()
    }

    private fun initSessionID() {
        val session = getSessionID()
        if (session.isEmpty()) {
            val newSessionId = UUID.randomUUID().toString()
            sharedPreferences.edit().putString(sessionPrefKey, newSessionId).apply()
        }

        createFirebaseSession()
    }

    protected fun getSessionID(): String {
        val result = sharedPreferences.getString(sessionPrefKey, "")
        return if (!result.isNullOrEmpty()) result else ""
    }

    private fun getFirebaseSession(callback: RepositoryCallback<SessionEntity?>) {
        val sessionID = getSessionID()
        val reference = database.getReference("sessions")

        callback.onResult(Resource.Loading())
        reference.child(sessionID).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    callback.onResult(Resource.Success(it.getValue<SessionEntity>()))
                } else {
                    callback.onResult(Resource.Success(null))
                }
            }
            .addOnFailureListener {
                callback.onResult(Resource.Error(it.toString()))
            }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createFirebaseSession() {
        val sessionID = getSessionID()
        val reference = database.getReference("sessions")
        val timestamp = getNowTimestampString()

        val session = SessionEntity(
            createdAt = timestamp,
            updatedAt = timestamp
        )

        getFirebaseSession(
            object : RepositoryCallback<SessionEntity?> {
                override fun onResult(result: Resource<SessionEntity?>) {
                    when (result) {
                        is Resource.Loading -> {
                            // pass
                        }

                        is Resource.Success -> {
                            if (result.data == null) {
                                reference.child(sessionID).setValue(session)
                            }
                        }

                        is Resource.Error -> {
                            // pass
                        }
                    }
                }
            }
        )
    }
}
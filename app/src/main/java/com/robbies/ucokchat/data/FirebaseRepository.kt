package com.robbies.ucokchat.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.robbies.ucokchat.data.entity.SessionEntityDocument
import com.robbies.ucokchat.util.SecureSharedPrefs
import com.robbies.ucokchat.util.getNowTimestampString
import java.util.UUID

open class FirebaseRepository(
    private val database: FirebaseFirestore,
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

    private fun getFirebaseSession(callback: RepositoryCallback<SessionEntityDocument?>) {
        val sessionID = getSessionID()
        val collection = database.collection("sessions")

        callback.onResult(Resource.Loading())
        collection.document(sessionID).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    callback.onResult(Resource.Success(it.toObject(SessionEntityDocument::class.java)))
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
        val collection = database.collection("sessions")
        val timestamp = getNowTimestampString()

        val session = SessionEntityDocument(
            createdAt = timestamp,
            updatedAt = timestamp
        )

        getFirebaseSession(
            object : RepositoryCallback<SessionEntityDocument?> {
                override fun onResult(result: Resource<SessionEntityDocument?>) {
                    when (result) {
                        is Resource.Loading -> {
                            // pass
                        }

                        is Resource.Success -> {
                            if (result.data == null) {
                                collection.document(sessionID).set(session)
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
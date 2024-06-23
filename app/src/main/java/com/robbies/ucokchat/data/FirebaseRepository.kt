package com.robbies.ucokchat.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.robbies.ucokchat.data.document.SessionDocument
import com.robbies.ucokchat.util.Session
import com.robbies.ucokchat.util.getNowTimestampString
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class FirebaseRepository(
    private val database: FirebaseFirestore,
    private val context: Context
): KoinComponent {
    private val session: Session by inject()

    init {
        initSessionID()
    }

    private fun initSessionID() {
        session.initSessionID()
        createFirebaseSession()
    }

    fun getSessionID(): String {
        return session.getSessionID()
    }

    private fun getFirebaseSession(callback: RepositoryCallback<SessionDocument?>) {
        val sessionID = getSessionID()
        val collection = database.collection("sessions")

        callback.onResult(Resource.Loading())
        collection.document(sessionID).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    callback.onResult(Resource.Success(it.toObject(SessionDocument::class.java)))
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

        val session = SessionDocument(
            createdAt = timestamp,
            updatedAt = timestamp
        )

        getFirebaseSession(
            object : RepositoryCallback<SessionDocument?> {
                override fun onResult(result: Resource<SessionDocument?>) {
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
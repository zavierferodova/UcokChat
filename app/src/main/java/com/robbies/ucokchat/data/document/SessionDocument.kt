package com.robbies.ucokchat.data.document

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class SessionDocument(
    val createdAt: String = "",
    val updatedAt: String = ""
)

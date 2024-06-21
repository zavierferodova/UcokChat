package com.robbies.ucokchat.data.entity

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class SessionEntityDocument(
    val createdAt: String = "",
    val updatedAt: String = ""
)

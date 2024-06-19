package com.robbies.ucokchat.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class SessionEntity(
    val createdAt: String = "",
    val updatedAt: String = ""
)

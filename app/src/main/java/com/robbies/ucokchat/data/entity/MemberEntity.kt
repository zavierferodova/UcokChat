package com.robbies.ucokchat.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class MemberEntity(
    val sessionId: String = "",
    val username: String = "",
    val isAdmin: Boolean = false,
    val isCreator: Boolean? = null,
    val joinedAt: String = "",
)
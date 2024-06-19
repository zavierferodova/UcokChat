package com.robbies.ucokchat.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class GroupChatEntity(
    val name: String,
    val createdAt: String,
    val updatedAt: String,
)

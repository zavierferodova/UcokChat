package com.robbies.ucokchat.data.entity

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class GroupChatDocument(
    val name: String = "",
    val key: String = "",
    val memberIds: List<String> = listOf(),
    val lastMessageTimestamp: String = "",
    val createdAt: String = "",
    val updatedAt: String = ""
)

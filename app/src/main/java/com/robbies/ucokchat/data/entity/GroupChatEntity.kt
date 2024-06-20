package com.robbies.ucokchat.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class GroupChatEntity(
    val name: String = "",
    val key: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val members: Map<String, MemberEntity> = mapOf()
)

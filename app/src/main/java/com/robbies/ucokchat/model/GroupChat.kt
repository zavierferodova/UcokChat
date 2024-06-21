package com.robbies.ucokchat.model

data class GroupChat(
    val id: String,
    val name: String,
    val key: String,
    val createdAt: String,
    val updatedAt: String,
    val members: List<Member> = emptyList(),
    val latestMessage: Message? = null
)


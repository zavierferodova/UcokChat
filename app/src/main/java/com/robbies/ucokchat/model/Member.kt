package com.robbies.ucokchat.model

data class Member(
    val id: String,
    val username: String,
    val isAdmin: Boolean,
    val isCreator: Boolean?,
    val joinedAt: String,
    val leave: Boolean
)

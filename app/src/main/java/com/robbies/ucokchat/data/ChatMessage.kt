package com.robbies.ucokchat.data

data class ChatMessage(
    val sender: String,
    val message: String,
    val timestamp: String,
    val isSentByCurrentUser: Boolean
)
package com.robbies.ucokchat.model

data class ChatMessage(
    val sender: String,
    val message: String,
    val timestamp: String,
    val isSentByCurrentUser: Boolean
)
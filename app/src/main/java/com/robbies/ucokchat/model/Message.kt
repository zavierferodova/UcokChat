package com.robbies.ucokchat.model

data class Message(
    val id: String,
    val text: String,
    val sender: String,
    val systemAnnouncement: Boolean?,
    val timestamp: String
)

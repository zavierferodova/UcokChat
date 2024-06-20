package com.robbies.ucokchat.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class MessageEntity(
    val text: String = "",
    val sender: String = "",
    val systemAnnouncement: Boolean? = null,
    val timestamp: String = ""
)

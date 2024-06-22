package com.robbies.ucokchat.data.document

import com.google.firebase.firestore.IgnoreExtraProperties
import com.robbies.ucokchat.model.Message

@IgnoreExtraProperties
data class MessageEntity(
    val text: String = "",
    val sender: String = "",
    val systemAnnouncement: Boolean? = null,
    val timestamp: String = ""
)

fun MessageEntity.toMessage(id: String): Message {
    return Message(
        id = id,
        text = text,
        sender = sender,
        systemAnnouncement = systemAnnouncement,
        timestamp = timestamp
    )
}
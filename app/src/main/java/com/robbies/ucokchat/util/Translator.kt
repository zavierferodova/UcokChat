package com.robbies.ucokchat.util

fun translateChatAnnouncement(placeholder: String): String {
    if (placeholder == "{{groupCreated}}")
        return "Group created"

    return ""
}
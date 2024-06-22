package com.robbies.ucokchat.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val id: String,
    val text: String,
    val sender: String,
    val systemAnnouncement: Boolean?,
    val timestamp: String
): Parcelable

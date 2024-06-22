package com.robbies.ucokchat.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Member(
    val id: String,
    val username: String,
    val isAdmin: Boolean,
    val isCreator: Boolean?,
    val joinedAt: String,
    val leave: Boolean
): Parcelable

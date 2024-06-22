package com.robbies.ucokchat.model

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupChat(
    val id: String,
    val name: String,
    val key: String,
    val createdAt: String,
    val updatedAt: String,
    val members: List<Member> = emptyList(),
    val latestMessage: Message? = null
): Parcelable {
    override fun toString(): String {
        return Uri.encode(Gson().toJson(this))
    }
}

class GroupChatArgType : NavType<GroupChat>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): GroupChat? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): GroupChat {
        return Gson().fromJson(Uri.decode(value), GroupChat::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: GroupChat) {
        bundle.putParcelable(key, value)
    }
}

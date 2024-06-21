package com.robbies.ucokchat.data.entity

import com.google.firebase.firestore.IgnoreExtraProperties
import com.robbies.ucokchat.model.Member

@IgnoreExtraProperties
data class MemberEntity(
    val username: String = "",
    val isAdmin: Boolean = false,
    val isCreator: Boolean? = null,
    val joinedAt: String = "",
    val leave: Boolean = false
)

fun MemberEntity.toMember(id: String): Member {
    return Member(
        id = id,
        username = username,
        isAdmin = isAdmin,
        isCreator = isCreator,
        joinedAt = joinedAt,
        leave = leave
    )
}
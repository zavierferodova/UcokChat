package com.robbies.ucokchat.data.response

import com.robbies.ucokchat.model.GroupChat

enum class GroupAvailabilityStatus {
    AVAILABLE,
    ALREADY_JOIN,
    NOT_FOUND
}

data class GroupAvailabilityResponse(
    val status: GroupAvailabilityStatus,
    val groupChat: GroupChat?
)
enum class JoinGroupStatus {
    SUCCESS,
    GROUP_NOT_FOUND,
    USERNAME_NOT_AVAILABLE
}

data class JoinGroupResponse(
    val status: JoinGroupStatus,
    val groupChat: GroupChat?
)
package com.robbies.ucokchat

data class GroupChar(
    var groupImage: Int,
    var groupName: String,
    var lastChat: String
)

fun getAllGroupList() : List<GroupChar> {
    return listOf<GroupChar>(
        GroupChar(R.drawable.profile_picture, "PDIP", "Rivai keren abis bro."),
        GroupChar(R.drawable.profile_picture, "PDIP", "Rivai keren abis bro."),
        GroupChar(R.drawable.profile_picture, "PDIP", "Rivai keren abis bro."),
        GroupChar(R.drawable.profile_picture, "PDIP", "Rivai keren abis bro."),
        GroupChar(R.drawable.profile_picture, "PDIP", "Rivai keren abis bro."),
        GroupChar(R.drawable.profile_picture, "PDIP", "Rivai keren abis bro."),
        GroupChar(R.drawable.profile_picture, "PDIP", "Rivai keren abis bro."),
        GroupChar(R.drawable.profile_picture, "PDIP", "Rivai keren abis bro."),
        GroupChar(R.drawable.profile_picture, "PDIP", "Rivai keren abis bro."),
        GroupChar(R.drawable.profile_picture, "PDIP", "Rivai keren abis bro."),
    )
}
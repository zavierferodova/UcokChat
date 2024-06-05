package com.robbies.ucokchat

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString

data class GroupChar(
    var groupImage: Int,
    var groupName: AnnotatedString,
    var lastChat: AnnotatedString
)

fun getAllGroupList() : List<GroupChar> {
    return listOf(
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("PDIP") }, buildAnnotatedString { append("Rivai keren abis bro.") }),
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("Group 2") }, buildAnnotatedString { append("This is the last chat in Group 2.") }),
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("Group 3") }, buildAnnotatedString { append("This is the last chat in Group 3.") }),
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("Group 4") }, buildAnnotatedString { append("This is the last chat in Group 4.") }),
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("Group 5") }, buildAnnotatedString { append("This is the last chat in Group 5.") }),
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("Group 6") }, buildAnnotatedString { append("This is the last chat in Group 6.") }),
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("Group 7") }, buildAnnotatedString { append("This is the last chat in Group 7.") }),
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("Group 8") }, buildAnnotatedString { append("This is the last chat in Group 8.") }),
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("Group 9") }, buildAnnotatedString { append("This is the last chat in Group 9.") }),
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("Group 10") }, buildAnnotatedString { append("This is the last chat in Group 10.") }),
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("Group 11") }, buildAnnotatedString { append("This is the last chat in Group 11.") }),
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("Group 12") }, buildAnnotatedString { append("This is the last chat in Group 12.") }),
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("Group 13") }, buildAnnotatedString { append("This is the last chat in Group 13.") }),
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("Group 14") }, buildAnnotatedString { append("This is the last chat in Group 14.") }),
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("Group 15") }, buildAnnotatedString { append("This is the last chat in Group 15.") }),
        GroupChar(R.drawable.profile_picture, buildAnnotatedString { append("Group 16") }, buildAnnotatedString { append("This is the last chat in Group 16.") })
    )
}
package com.robbies.ucokchat.controller

import androidx.navigation.NamedNavArgument
import androidx.navigation.navArgument
import com.robbies.ucokchat.model.GroupChat
import com.robbies.ucokchat.model.GroupChatArgType

object RouteNames {
    const val HOME = "home"
    const val CHAT = "chat/{argument}"
    const val DETAIL_GROUP = "detailGroup/{argument}"
    const val JOIN_GROUP = "joinGroup"
}

object RouteArguments {
    val home: List<NamedNavArgument> = emptyList()
    val chat: List<NamedNavArgument> = listOf(
        navArgument("argument") {
            type = GroupChatArgType()
        }
    )
    val detailGroup: List<NamedNavArgument> = listOf(
        navArgument("argument") {
            type = GroupChatArgType()
        }
    )
    val joinGroup: List<NamedNavArgument> = emptyList()
}

object RouteActions {
    fun home() = "home"
    fun chat(argument: GroupChat) = "chat/${argument}"
    fun detailGroup(argument: GroupChat) = "detailGroup/${argument}"
    fun joinGroup() = "joinGroup"
}

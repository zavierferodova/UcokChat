package com.robbies.ucokchat.controller

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.robbies.ucokchat.model.GroupChat
import com.robbies.ucokchat.ui.screen.detailgroup.DetailGroupScreen
import com.robbies.ucokchat.ui.screen.groupchat.ChatScreen
import com.robbies.ucokchat.ui.screen.home.HomeScreen
import com.robbies.ucokchat.ui.screen.join.JoinGroupScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(startDestination: String = RouteNames.HOME) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier
    ) {
        composable(RouteNames.HOME) {
            HomeScreen(navController)
        }
        composable(
            RouteNames.CHAT,
            arguments = RouteArguments.chat
        ) { backStackEntry ->
            val groupChat = backStackEntry.arguments?.getParcelable<GroupChat>("argument")!!
            ChatScreen(navController, groupChat)
        }
        composable(
            RouteNames.DETAIL_GROUP,
            arguments = RouteArguments.detailGroup
        ) { backStackEntry ->
            val groupChat = backStackEntry.arguments?.getParcelable<GroupChat>("argument")!!
            DetailGroupScreen(groupChat)
        }
        composable(RouteNames.JOIN_GROUP) {
            JoinGroupScreen(navController)
        }
    }
}
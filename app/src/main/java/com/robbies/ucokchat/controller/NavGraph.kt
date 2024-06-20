package com.robbies.ucokchat.controller

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.robbies.ucokchat.ui.screen.chat.ChatScreen
import com.robbies.ucokchat.ui.screen.home.HomeScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(startDestination: String = "home") {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable(
            "chat/{groupName}/{groupImage}",
            arguments = listOf(
                navArgument("groupName") { type = NavType.StringType },
                navArgument("groupImage") { type = NavType.IntType },
            )
        ) {backStackEntry ->
            val groupName = backStackEntry.arguments?.getString("groupName") ?: ""
            val groupImage = backStackEntry.arguments?.getInt("groupImage") ?: 0
            ChatScreen(groupName, groupImage)
        }
    }
}
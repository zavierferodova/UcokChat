package com.robbies.ucokchat

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.robbies.ucokchat.controller.NavGraph
import com.robbies.ucokchat.ui.theme.UcokChatTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UcokChatTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                NavGraph()
            }
        }
    }
}

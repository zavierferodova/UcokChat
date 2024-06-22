package com.robbies.ucokchat.ui.screen.detailgroup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robbies.ucokchat.model.GroupChat
import com.robbies.ucokchat.util.rememberQrBitmapPainter

@Composable
fun DetailGroupScreen(groupChat: GroupChat) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = groupChat.name,
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                painter = rememberQrBitmapPainter(groupChat.key),
                contentDescription = "Join Group QR Code",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(180.dp),
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Scan to join group")
        }
    }
}
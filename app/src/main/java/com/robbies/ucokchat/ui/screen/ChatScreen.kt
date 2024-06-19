package com.robbies.ucokchat.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robbies.ucokchat.R
import com.robbies.ucokchat.model.ChatMessage

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ChatScreenPreview() {
    ChatScreen(groupImage = R.drawable.profile_picture, groupName = "Group One")
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(groupName: String, groupImage: Int) {
    val messages = remember { mutableStateListOf<ChatMessage>() }
    var currentMessage by remember { mutableStateOf(TextFieldValue()) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.aqua)
                ),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = groupImage),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .clip(CircleShape)
                                .size(40.dp)
                        )
                        Text(
                            text = groupName,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(it)
                ) {
                    items(messages) { message ->
                        ChatMessageItem(message = message)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Box(Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        BasicTextField(
                            value = currentMessage,
                            onValueChange = { currentMessage = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 10.dp)
                                .background(Color.LightGray, RoundedCornerShape(24.dp))
                                .padding(15.dp),
                            textStyle = TextStyle(fontSize = 18.sp)
                        )
                        if (currentMessage.text.isEmpty())
                            Text(
                                "Ketik pesan",
                                color = Color.DarkGray,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 15.dp),
                            )
                    }
                    IconButton(
                        onClick = {
                            if (currentMessage.text.isNotEmpty()) {
                                messages.add(
                                    ChatMessage(
                                        sender = "You",
                                        message = currentMessage.text,
                                        timestamp = "Now",
                                        isSentByCurrentUser = true
                                    )
                                )
                                currentMessage = TextFieldValue()
                            }
                        }, modifier = Modifier
                            .clip(
                                CircleShape
                            )
                            .background(colorResource(id = R.color.aqua))
                    ) {
                        Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White)
                    }
                }
            }
        }
    )
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    Column(
        horizontalAlignment = if (message.isSentByCurrentUser) Alignment.End else Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = message.sender,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Box(
            modifier = Modifier
                .background(
                    if (message.isSentByCurrentUser) Color(0xFFDCF8C6) else Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = message.message,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Text(
                    text = message.timestamp,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}
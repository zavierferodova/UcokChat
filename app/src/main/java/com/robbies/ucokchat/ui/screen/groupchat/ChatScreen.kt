package com.robbies.ucokchat.ui.screen.groupchat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.robbies.ucokchat.R
import com.robbies.ucokchat.controller.RouteActions
import com.robbies.ucokchat.model.ChatMessage
import com.robbies.ucokchat.model.GroupChat

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, groupChat: GroupChat) {
    val messages = remember { mutableStateListOf<ChatMessage>() }
    var currentMessage by remember { mutableStateOf(TextFieldValue()) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Row(
                        modifier = Modifier
                            .height(60.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .height(60.dp)
                                .weight(0.2f)
                                .clickable {
                                    navController.popBackStack()
                                }
                                .padding(end = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back Arrow",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Image(
                                painter = painterResource(id = R.drawable.profile_picture),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(40.dp)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(0.9f)
                                .height(60.dp)
                                .padding(
                                    end = 15.dp
                                )
                                .clickable {
                                    navController.navigate(RouteActions.detailGroup(groupChat))
                                }
                                .padding(start = 5.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = groupChat.name,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                lineHeight = 10.sp,
                                modifier = Modifier.wrapContentSize(unbounded = true)
                            )
                            Text(
                                text = groupChat.members.fold("") { acc, member ->
                                    "$acc ${member.username}"
                                },
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.wrapContentSize(unbounded = true)
                            )
                        }
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xfffef9f0))
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
                                .shadow(
                                    elevation = 1.dp,
                                    ambientColor = Color.LightGray,
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .background(Color.White, RoundedCornerShape(24.dp))
                                .padding(15.dp),
                            textStyle = TextStyle(fontSize = 18.sp)
                        )
                        if (currentMessage.text.isEmpty())
                            Text(
                                "Write a message",
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
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White
                        )
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
                .padding(
                    horizontal = 15.dp,
                    vertical = 10.dp
                )
        ) {
            Column {
                Text(
                    text = message.message,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Text(
                    text = message.timestamp,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}
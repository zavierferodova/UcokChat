package com.robbies.ucokchat.ui.screen.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.robbies.ucokchat.R
import com.robbies.ucokchat.controller.RouteActions
import com.robbies.ucokchat.model.GroupChat
import com.robbies.ucokchat.ui.component.DialogLoading
import com.robbies.ucokchat.ui.component.fab.FabIcon
import com.robbies.ucokchat.ui.component.fab.FabOption
import com.robbies.ucokchat.ui.component.fab.MultiFabItem
import com.robbies.ucokchat.ui.component.fab.MultiFloatingActionButton
import com.robbies.ucokchat.util.translateChatAnnouncement
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val groupChats by viewModel.groupChats.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary // get color from theme
                ),
                title = {
                    Text(
                        text = "Ucok Chat",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        },
        floatingActionButton = {
            MultiFloatingActionButton(
                items = listOf(
                    MultiFabItem(
                        id = 1,
                        iconRes = R.drawable.baseline_add_24,
                        label = "Create Group"
                    ),
                    MultiFabItem(
                        id = 2,
                        iconRes = R.drawable.baseline_group_add_24,
                        label = "Join Group"
                    ),
                ),
                fabIcon = FabIcon(iconRes = R.drawable.baseline_add_24, iconRotate = 45f),
                onFabItemClicked = {
                    if (it.id == 1) {
                        viewModel.openDialogCreateGroup()
                    } else {
                        navController.navigate(RouteActions.joinGroup())
                    }
                },
                fabOption = FabOption(
                    iconTint = Color.White,
                    showLabel = true
                )
            )
        }
    ) { innerPadding ->
        if (uiState.showGroupChatLoading)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }

        if (!uiState.showGroupChatLoading)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                content = {
                    itemsIndexed(groupChats,
                        itemContent = { _, item ->
                            GroupItem(item = item, navController = navController)
                        })
                }
            )

        DialogLoading(
            open = uiState.showDialogLoading,
            title = uiState.dialogLoadingTitle,
            message = uiState.dialogLoadingMessage
        )

        DialogCreateGroup(
            open = uiState.showDialogCreateGroup,
            onDismissRequest = {
                viewModel.closeDialogCreateGroup()
            },
            onConfirmation = { groupName, username ->
                viewModel.createGroupChat(groupName, username)
            }
        )
    }
}

@Composable
fun GroupItem(item: GroupChat, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(RouteActions.chat(item))
            }
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_picture),
            contentDescription = item.name,
            modifier = Modifier
                .clip(CircleShape)
                .size(45.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 12.dp,
                ),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = item.name,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            Text(
                text = if (item.latestMessage != null)
                    if (item.latestMessage.systemAnnouncement == true)
                        translateChatAnnouncement(item.latestMessage.text)
                    else item.latestMessage.text
                else "No message",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray
                ),
            )
        }
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = Color.LightGray
    )
}

@Composable
fun DialogCreateGroup(
    open: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: (groupName: String, username: String) -> Unit,
) {
    var groupName by remember {
        mutableStateOf("")
    }
    var isGroupNameError by remember {
        mutableStateOf(false)
    }
    var username by remember {
        mutableStateOf("")
    }
    var isUsernameError by remember {
        mutableStateOf(false)
    }

    val resetState = {
        groupName = ""
        isGroupNameError = false
        username = ""
        isUsernameError = false
    }

    val callConfirmation = {
        if (groupName.isEmpty()) {
            isGroupNameError = true
        } else if (username.isEmpty()) {
            isUsernameError = true
        } else {
            onConfirmation(groupName, username)
            onDismissRequest()
            resetState()
        }
    }

    if (open) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(Modifier.padding(15.dp)) {
                    Text(
                        text = "Create a Group Chat",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    OutlinedTextField(
                        value = groupName,
                        isError = isGroupNameError,
                        supportingText = {
                            if (isGroupNameError) {
                                Text(text = "Group name cannot empty")
                            }
                        },
                        onValueChange = {
                            groupName = it
                        },
                        label = {
                            Text(text = "Group Name")
                        })
                    OutlinedTextField(
                        value = username,
                        isError = isUsernameError,
                        supportingText = {
                            if (isUsernameError) {
                                Text(text = "Username cannot empty")
                            }
                        },
                        onValueChange = {
                            username = it
                        },
                        label = {
                            Text(text = "Username")
                        })
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = onDismissRequest) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        TextButton(onClick = {
                            callConfirmation()
                        }) {
                            Text(text = "Ok")
                        }
                    }
                }
            }
        }
    }
}
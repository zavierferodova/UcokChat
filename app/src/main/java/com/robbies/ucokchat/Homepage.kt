package com.robbies.ucokchat

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robbies.ucokchat.floatingactionbutton.FabIcon
import com.robbies.ucokchat.floatingactionbutton.FabOption
import com.robbies.ucokchat.floatingactionbutton.MultiFabItem
import com.robbies.ucokchat.floatingactionbutton.MultiFloatingActionButton

@Preview
@Composable
fun HomepageDemo(){
    Homepage()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun Homepage() {
    val groupList = getAllGroupList()
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = colorResource(id = R.color.aqua)
                ),
                title = {
                    Text(
                        text = "UcokChat",
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

                },
                fabOption = FabOption(
                    iconTint = Color.White,
                    showLabel = true
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            content = {
                itemsIndexed(groupList, itemContent = { _, item ->
                    GroupItem(item = item)
                })
            }
        )
    }
}

@Composable
fun GroupItem(item : GroupChar) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = item.groupImage),
            contentDescription = item.groupName.toString(),
            modifier = Modifier
                .clip(CircleShape)
                .size(48.dp)
            )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                ),
            horizontalAlignment = Alignment.Start
        ) {
            ClickableText(
                text = item.groupName,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                onClick = {},
            )
            ClickableText(
                text = item.lastChat,
                style = TextStyle(
                    fontSize = 16.sp,
                ),
                onClick = {},
            )
        }
    }
}

@Composable
fun BubbleButton() {
    var isClicked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(10.dp)
    ) {
        FloatingActionButton(
            onClick = { isClicked = !isClicked },
            containerColor = colorResource(id = R.color.aqua),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.align(Alignment.Center),
        ) {
            Icon(if (isClicked) Icons.Filled.Close else Icons.Filled.Add, "Floating action button.")
        }

        if (isClicked) {
            Column {
                SmallFloatingActionButton(
                    onClick = { /* Handle first button click */ },
                ) {
                    Text("1")
                }

                SmallFloatingActionButton(
                    onClick = { /* Handle second button click */ },
                ) {
                    Text("2")
                }
            }
        }
    }
}

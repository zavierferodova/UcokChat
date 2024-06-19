package com.robbies.ucokchat.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.robbies.ucokchat.R
import com.robbies.ucokchat.data.GroupChar
import com.robbies.ucokchat.data.getAllGroupList
import com.robbies.ucokchat.ui.component.fab.FabIcon
import com.robbies.ucokchat.ui.component.fab.FabOption
import com.robbies.ucokchat.ui.component.fab.MultiFabItem
import com.robbies.ucokchat.ui.component.fab.MultiFloatingActionButton

@Preview
@Composable
fun HomeScreenDemo() {
    HomeScreen(navController = rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val groupList = getAllGroupList()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = colorResource(id = R.color.aqua)
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
                itemsIndexed(groupList,
                    itemContent = { _, item ->
                        GroupItem(item = item, navController = navController)
                    })
            }
        )
    }
}

@Composable
fun GroupItem(item: GroupChar, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("chat/${item.groupName}/${item.groupImage}")
            }
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.groupImage),
            contentDescription = item.groupName.toString(),
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
                text = item.groupName,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            Text(
                text = item.lastChat,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray
                ),
            )
        }
    }
    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
    )
}
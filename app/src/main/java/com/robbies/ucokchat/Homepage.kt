package com.robbies.ucokchat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun HomepageDemo(){
    Homepage()
}

@Composable
fun Homepage() {
    val groupList = getAllGroupList()
    LazyColumn( content = {
        itemsIndexed(groupList, itemContent = { _, item ->
            GroupItem(item = item)
        })
    })
}

@Composable
fun GroupItem(item : GroupChar) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = item.groupImage),
            contentDescription = item.groupName,
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp)
            )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                ),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = item.groupName,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = item.lastChat,
                style = TextStyle(
                    fontSize = 16.sp,
                )
            )
        }

    }
}


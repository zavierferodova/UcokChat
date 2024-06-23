package com.robbies.ucokchat.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogAlert(
    open: Boolean,
    title: String,
    message: String,
    onDismissRequest: () -> Unit
) {
    if (open) {
        Dialog(onDismissRequest = {}) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(
                        top = 15.dp,
                        start = 15.dp,
                        end = 15.dp,
                        bottom = 5.dp
                    )
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = message,
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                        TextButton(onClick = onDismissRequest) {
                            Text(text = "Ok")
                        }
                    }
                }
            }
        }
    }
}
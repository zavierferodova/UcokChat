package com.robbies.ucokchat.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogLoading(
    open: Boolean,
    title: String,
    message: String
) {
    if (open) {
        Dialog(onDismissRequest = {}) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(15.dp)) {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(25.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = message,
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
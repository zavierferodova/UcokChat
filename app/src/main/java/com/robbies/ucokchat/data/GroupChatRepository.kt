package com.robbies.ucokchat.data

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GroupChatRepository(
    private val database: FirebaseDatabase,
    private val context: Context
) : FirebaseRepository(database, context) {
    val reference = database.getReference("groups")
    fun createGroupChat(): Flow<Resource<Boolean>> = flow {

    }
}
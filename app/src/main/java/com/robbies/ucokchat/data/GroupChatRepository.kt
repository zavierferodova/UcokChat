package com.robbies.ucokchat.data

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.robbies.ucokchat.data.entity.GroupChatEntity
import com.robbies.ucokchat.data.entity.MemberEntity
import com.robbies.ucokchat.data.entity.MessageEntity
import com.robbies.ucokchat.util.getNowTimestampString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class GroupChatRepository(
    private val database: FirebaseDatabase,
    private val context: Context
) : FirebaseRepository(database, context) {
    private val reference = database.getReference("groups")
    fun createGroupChat(groupName: String, username: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())

        val groupChat = GroupChatEntity(
            name = groupName,
            key = UUID.randomUUID().toString(),
            createdAt = getNowTimestampString(),
            updatedAt = getNowTimestampString()
        )

        val member = MemberEntity(
            username = username,
            sessionId = getSessionID(),
            isAdmin = true,
            isCreator = true,
            joinedAt = getNowTimestampString()
        )

        val message = MessageEntity(
            text = "{{groupCreated}}",
            sender = "",
            systemAnnouncement = true,
            timestamp = getNowTimestampString()
        )

        try {
            val groupRef = reference.push()
            groupRef.setValue(groupChat).await()
            groupRef.child("members").push().setValue(member).await()
            groupRef.child("messages").push().setValue(message).await()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString(), exception = e))
        }
    }

    fun getGroupChat(page: Int, limit: Int = 15): Flow<Resource<List<GroupChatEntity>>> = flow {
        emit(Resource.Loading())
        val startAt = (page - 1) * limit
        val endAt = startAt + limit
        val query = reference.orderByChild("createdAt").limitToFirst(endAt)

        try {
            val snapshot = query.get().await()
            val dataList = snapshot.children.drop(startAt).take(limit).toList()
            val groupChats = dataList.map {
                it.getValue(GroupChatEntity::class.java)!!
            }

            emit(Resource.Success(groupChats))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString(), exception = e))
        }
    }
}
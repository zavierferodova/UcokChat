package com.robbies.ucokchat.data

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.robbies.ucokchat.data.document.GroupChatDocument
import com.robbies.ucokchat.data.document.MemberDocument
import com.robbies.ucokchat.data.document.MessageDocument
import com.robbies.ucokchat.data.document.toMember
import com.robbies.ucokchat.data.document.toMessage
import com.robbies.ucokchat.data.response.GroupAvailabilityResponse
import com.robbies.ucokchat.data.response.GroupAvailabilityStatus
import com.robbies.ucokchat.data.response.JoinGroupResponse
import com.robbies.ucokchat.data.response.JoinGroupStatus
import com.robbies.ucokchat.model.GroupChat
import com.robbies.ucokchat.util.getNowTimestampString
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class GroupChatRepository(
    private val database: FirebaseFirestore,
    private val context: Context
) : FirebaseRepository(database, context) {
    private val groupCollection = database.collection("groups")

    private fun getMemberCollection(groupId: String) =
        groupCollection.document(groupId).collection("members")

    private fun getMessagesCollection(groupId: String) =
        groupCollection.document(groupId).collection("messages")

    fun createGroupChat(groupName: String, username: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())

        val creationTimestamp = getNowTimestampString()

        val message = MessageDocument(
            text = "{{groupCreated}}",
            sender = "",
            systemAnnouncement = true,
            timestamp = creationTimestamp
        )

        val member = MemberDocument(
            username = username.trim(),
            isAdmin = true,
            isCreator = true,
            joinedAt = creationTimestamp
        )

        val groupChat = GroupChatDocument(
            name = groupName.trim(),
            memberIds = listOf(getSessionID()),
            key = UUID.randomUUID().toString(),
            lastMessageTimestamp = creationTimestamp,
            createdAt = creationTimestamp,
            updatedAt = creationTimestamp
        )

        try {
            val groupDocument = groupCollection.add(groupChat).await()
            groupDocument.collection("members").document(getSessionID()).set(member).await()
            groupDocument.collection("messages").add(message).await()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString(), exception = e))
        }
    }

    fun listenSessionGroupChat(): Flow<Resource<List<GroupChat>>> = callbackFlow {
        trySend(Resource.Loading())

        val querySnapshot = groupCollection.whereArrayContains("memberIds", getSessionID())
            .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
        val listenerRegistration =
            querySnapshot.addSnapshotListener(MetadataChanges.INCLUDE) { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    launch {
                        try {
                            val groupChats = mutableListOf<GroupChat>()

                            val members = snapshots.documents.map {
                                async {
                                    val groupDoc = it
                                    val groupId = groupDoc.id
                                    val membersRef = getMemberCollection(groupId)
                                    val membersSnapshot = membersRef.get()
                                    membersSnapshot.await()
                                }
                            }.awaitAll()

                            val latestMessages = snapshots.documents.map {
                                async {
                                    val groupDoc = it
                                    val groupId = groupDoc.id
                                    val messagesRef = getMessagesCollection(groupId)
                                    val messageSnapshot =
                                        messagesRef.orderBy("timestamp", Query.Direction.DESCENDING)
                                            .limit(1)
                                            .get()
                                            .await()
                                    messageSnapshot
                                }
                            }.awaitAll()

                            for (i in snapshots.documents.indices) {
                                val groupDoc = snapshots.documents[i]
                                val groupId = groupDoc.id
                                val groupData = groupDoc.toObject(GroupChatDocument::class.java)!!

                                var groupChat = GroupChat(
                                    id = groupId,
                                    key = groupData.key,
                                    name = groupData.name,
                                    createdAt = groupData.createdAt,
                                    updatedAt = groupData.updatedAt,
                                )

                                groupChat = groupChat.copy(
                                    members = members[i].documents.map {
                                        val memberData = it.toObject(MemberDocument::class.java)!!
                                        memberData.toMember(it.id)
                                    })

                                groupChat = groupChat.copy(
                                    latestMessage = latestMessages[i].documents[0].toObject(
                                        MessageDocument::class.java
                                    )!!.toMessage(latestMessages[i].documents[0].id)
                                )

                                groupChats.add(groupChat)
                            }

                            trySend(Resource.Success(groupChats))
                        } catch (e: Exception) {
                            trySend(Resource.Error(e.message.toString(), exception = e))
                        }
                    }
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }

    fun checkGroupAvailability(keyBarcode: String): Flow<Resource<GroupAvailabilityResponse>> =
        flow {
            emit(Resource.Loading())

            try {
                val querySnapshot = groupCollection.whereEqualTo("key", keyBarcode).get().await()
                val data = GroupAvailabilityResponse(
                    status = GroupAvailabilityStatus.NOT_FOUND,
                    groupChat = null
                )

                if (querySnapshot.isEmpty) {
                    emit(Resource.Success(data))
                } else {
                    val groupDoc = querySnapshot.documents[0]
                    val groupId = groupDoc.id
                    val groupData = groupDoc.toObject(GroupChatDocument::class.java)!!

                    // Already join group
                    if (groupData.memberIds.contains(getSessionID())) {
                        val membersRef = getMemberCollection(groupId)
                        val membersSnapshot = membersRef.get().await()

                        val groupChat = GroupChat(
                            id = groupId,
                            key = groupData.key,
                            name = groupData.name,
                            members = membersSnapshot.documents.map {
                                val memberDocument = it.toObject(MemberDocument::class.java)!!
                                memberDocument.toMember(it.id)
                            },
                            createdAt = groupData.createdAt,
                            updatedAt = groupData.updatedAt,
                        )

                        emit(
                            Resource.Success(
                                data.copy(
                                    status = GroupAvailabilityStatus.ALREADY_JOIN,
                                    groupChat = groupChat
                                )
                            )
                        )
                    } else {
                        emit(
                            Resource.Success(
                                data.copy(
                                    status = GroupAvailabilityStatus.AVAILABLE
                                )
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString(), exception = e))
            }
        }

    fun joinGroupChat(keyBarcode: String, username: String): Flow<Resource<JoinGroupResponse>> =
        flow {
            emit(Resource.Loading())

            try {
                val memberUsername = username.trim()
                val data = JoinGroupResponse(
                    status = JoinGroupStatus.GROUP_NOT_FOUND,
                    groupChat = null
                )
                val querySnapshot =
                    groupCollection.whereEqualTo("key", keyBarcode)
                        .limit(1)
                        .get()
                        .await()

                // Group chat is empty
                if (querySnapshot.isEmpty) {
                    emit(Resource.Success(data))
                } else {
                    val groupDoc = querySnapshot.documents[0]
                    val groupId = groupDoc.id

                    val membersRef = getMemberCollection(groupId)
                    val membersSnapshot =
                        membersRef.whereEqualTo("username", memberUsername)
                            .get()
                            .await()

                    // Username available
                    if (membersSnapshot.isEmpty) {
                        val groupDocRef = groupCollection.document(groupId)
                        val member = MemberDocument(
                            username = memberUsername,
                            isAdmin = false,
                            isCreator = false,
                            joinedAt = getNowTimestampString()
                        )

                        membersRef.add(member)
                            .await()
                        groupDocRef.update("memberIds", FieldValue.arrayUnion(getSessionID()))
                            .await()
                        val membersSnapshot = membersRef.get().await()
                        val groupData = groupDocRef.get()
                            .await()
                            .toObject(GroupChatDocument::class.java)!!

                        val groupChat = GroupChat(
                            id = groupId,
                            key = groupData.key,
                            name = groupData.name,
                            members = membersSnapshot.documents.map {
                                val memberDocument = it.toObject(MemberDocument::class.java)!!
                                memberDocument.toMember(it.id)
                            },
                            createdAt = groupData.createdAt,
                            updatedAt = groupData.updatedAt,
                        )

                        emit(
                            Resource.Success(
                                data.copy(
                                    status = JoinGroupStatus.SUCCESS,
                                    groupChat = groupChat
                                )
                            )
                        )
                    } else { // Username not available
                        emit(
                            Resource.Success(
                                data.copy(
                                    status = JoinGroupStatus.USERNAME_NOT_AVAILABLE
                                )
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString(), exception = e))
            }
        }
}
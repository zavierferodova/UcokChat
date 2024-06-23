package com.robbies.ucokchat.ui.screen.groupchat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbies.ucokchat.data.GroupChatRepository
import com.robbies.ucokchat.data.Resource
import com.robbies.ucokchat.model.GroupChat
import com.robbies.ucokchat.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val groupChatRepository: GroupChatRepository): ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    private val _groupChat = MutableStateFlow<GroupChat?>(null)
    val message: StateFlow<List<Message>> = _messages
    val groupChat: StateFlow<GroupChat?> = _groupChat
    fun sendMessage(groupId: String, message: String) {
        viewModelScope.launch {
            groupChatRepository.sendMessage(groupId, message).collect {
                when (it) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {}
                }
            }
        }
    }

    fun listenGroupData(groupId: String) {
        viewModelScope.launch {
            groupChatRepository.listenGroupChat(groupId).collect {
                when (it) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        if (it.data != null) {
                            _groupChat.value = it.data
                        }
                    }
                }
            }
        }
    }

    fun listenMessages(groupId: String) {
        viewModelScope.launch {
            groupChatRepository.listenGroupChatMessages(groupId).collect {
                when(it) {
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        if (it.data != null) {
                            _messages.value = it.data
                        }
                    }
                }
            }
        }
    }
}
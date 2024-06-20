package com.robbies.ucokchat.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbies.ucokchat.data.GroupChatRepository
import com.robbies.ucokchat.data.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: GroupChatRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState

    fun openDialogCreateGroup() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                showDialogCreateGroup = true
            )
        }
    }

    fun closeDialogCreateGroup() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                showDialogCreateGroup = false
            )
        }
    }

    private fun openDialogLoading(title: String, message: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                showDialogLoading = true,
                dialogLoadingTitle = title,
                dialogLoadingMessage = message
            )
        }
    }

    private fun closeDialogLoading() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                showDialogLoading = false,
                dialogLoadingTitle = "",
                dialogLoadingMessage = ""
            )
        }
    }

    fun createGroupChat(groupName: String, username: String) {
        viewModelScope.launch {
            repository.createGroupChat(groupName, username).collect {
                when (it) {
                    is Resource.Error -> {
                        closeDialogLoading()
                    }

                    is Resource.Loading -> {
                        openDialogLoading("Loading", "Creating group chat!")
                    }

                    is Resource.Success -> {
                        closeDialogLoading()
                    }
                }
            }
        }
    }
}
package com.robbies.ucokchat.ui.screen.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.robbies.ucokchat.controller.RouteActions
import com.robbies.ucokchat.data.GroupChatRepository
import com.robbies.ucokchat.data.Resource
import com.robbies.ucokchat.data.response.GroupAvailabilityStatus
import com.robbies.ucokchat.data.response.JoinGroupStatus
import com.robbies.ucokchat.model.GroupChat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JoinGroupViewModel(private val groupChatRepository: GroupChatRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(JoinGroupUIState())
    var navController: NavController? = null

    val uiState: StateFlow<JoinGroupUIState> = _uiState
    fun checkGroupAvailability(keyBarcode: String) {
        viewModelScope.launch {
            groupChatRepository.checkGroupChatAvailability(keyBarcode).collect {
                when (it) {
                    is Resource.Error -> {
                        allowScanningBarcode(true)
                        closeDialogLoading()
                        openDialogAlert("Error", "Failed to check group!")
                    }

                    is Resource.Loading -> {
                        allowScanningBarcode(false)
                        openDialogLoading("Loading", "Checking group availability")
                    }

                    is Resource.Success -> {
                        closeDialogLoading()

                        when (it.data?.status) {
                            GroupAvailabilityStatus.AVAILABLE -> {
                                allowScanningBarcode(false)
                                stateInsertUsername(keyBarcode)
                            }

                            GroupAvailabilityStatus.ALREADY_JOIN -> {
                                routeToGroupChat(it.data.groupChat!!)
                            }

                            GroupAvailabilityStatus.NOT_FOUND -> {
                                allowScanningBarcode(true)
                                openDialogAlert("Warning", "Group not found!")
                            }

                            null -> {}
                        }
                    }
                }
            }
        }
    }

    fun joinGroupChat(groupKey: String, username: String) {
        viewModelScope.launch {
            groupChatRepository.joinGroupChat(groupKey, username).collect {
                when (it) {
                    is Resource.Error -> {
                        stateCancelJoinGroup()
                        closeDialogLoading()
                        openDialogAlert("Error", "Failed to join group!")
                    }

                    is Resource.Loading -> {
                        allowScanningBarcode(false)
                        openDialogLoading("Loading", "Joining group")
                    }

                    is Resource.Success -> {
                        stateCancelJoinGroup()
                        closeDialogLoading()

                        when (it.data?.status) {
                            JoinGroupStatus.SUCCESS -> {
                                routeToGroupChat(it.data.groupChat!!)
                            }

                            JoinGroupStatus.GROUP_NOT_FOUND -> {
                                openDialogAlert("Warning", "Group not found!")
                            }

                            JoinGroupStatus.USERNAME_NOT_AVAILABLE -> {
                                openDialogAlert("Warning", "Username not available!")
                            }

                            null -> {}
                        }
                    }
                }
            }
        }
    }

    private fun allowScanningBarcode(value: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                allowScanBarcode = value
            )
        }
    }

    private fun stateInsertUsername(keyBarcode: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                groupKey = keyBarcode
            )
        }
    }

    private fun stateCancelJoinGroup() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                groupKey = null,
                allowScanBarcode = true
            )
        }
    }

    fun cancelInsertUsername() {
        stateCancelJoinGroup()
    }

    private fun routeToGroupChat(groupChat: GroupChat) {
        navController?.navigate(RouteActions.chat(groupChat)) {
            popUpTo(RouteActions.home()) {
                inclusive = false
            }
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

    private fun openDialogAlert(title: String, message: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                showDialogAlert = true,
                dialogAlertTitle = title,
                dialogAlertMessage = message
            )
        }
    }

    fun closeDialogAlert() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                showDialogAlert = false,
                dialogAlertTitle = "",
                dialogAlertMessage = ""
            )
        }
    }
}
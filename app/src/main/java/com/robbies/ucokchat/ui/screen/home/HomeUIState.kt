package com.robbies.ucokchat.ui.screen.home

data class HomeUIState(
    val showDialogCreateGroup: Boolean = false,
    val showGroupChatLoading: Boolean = false,
    val showDialogLoading: Boolean = false,
    val dialogLoadingTitle: String = "",
    val dialogLoadingMessage: String = ""
)
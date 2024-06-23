package com.robbies.ucokchat.ui.screen.join

data class JoinGroupUIState(
    val allowScanBarcode: Boolean = true,
    val groupKey: String? = null,
    val showDialogLoading: Boolean = false,
    val dialogLoadingTitle: String = "",
    val dialogLoadingMessage: String = "",
    val showDialogAlert: Boolean = false,
    val dialogAlertTitle: String = "",
    val dialogAlertMessage: String = ""
)

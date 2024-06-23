package com.robbies.ucokchat.ui.screen.join

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.robbies.ucokchat.ui.component.DialogAlert
import com.robbies.ucokchat.ui.component.DialogLoading
import com.robbies.ucokchat.util.BarcodeAnalyzer
import org.koin.androidx.compose.koinViewModel

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun JoinGroupScreen(navController: NavController) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    Scaffold { innerPadding ->
        if (cameraPermissionState.status.isGranted) {
            CameraScreen(innerPadding, navController)
        } else if (cameraPermissionState.status.shouldShowRationale) {
            Box(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Text(
                    "Camera Permission permanently denied",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            SideEffect {
                cameraPermissionState.run { launchPermissionRequest() }
            }
            Box(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Text(
                    "No Camera Permission",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun CameraScreen(
    innerPadding: PaddingValues,
    navController: NavController,
    viewModel: JoinGroupViewModel = koinViewModel()
) {
    viewModel.navController = navController
    val uiState by viewModel.uiState.collectAsState()
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(localContext)
    }

    Box(Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            factory = { context ->
                val previewView = PreviewView(context)
                val preview = Preview.Builder().build()
                val selector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                preview.setSurfaceProvider(previewView.surfaceProvider)

                val imageAnalysis = ImageAnalysis.Builder().build()
                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    BarcodeAnalyzer {
                        if (uiState.allowScanBarcode) {
                            viewModel.checkGroupAvailability(it)
                        }
                    }
                )

                runCatching {
                    cameraProviderFuture.get().bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        imageAnalysis
                    )
                }.onFailure {
                    Log.e("CameraScreen", "Camera bind error ${it.localizedMessage}", it)
                }
                previewView
            }
        )
        Column(
            Modifier
                .size(320.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .size(280.dp)
                    .border(
                        width = 4.dp,
                        color = Color(0xE6FFFFFF),
                        shape = RoundedCornerShape(20.dp)
                    )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "Scan to join",
                color = Color(0xE6FFFFFF),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        DialogCreateUsername(
            open = uiState.groupKey != null,
            onDismissRequest = { viewModel.cancelInsertUsername() },
            onConfirmation = {
                viewModel.joinGroupChat(uiState.groupKey!!, it)
            })

        DialogLoading(
            open = uiState.showDialogLoading,
            title = uiState.dialogLoadingTitle,
            message = uiState.dialogLoadingMessage
        )

        DialogAlert(
            open = uiState.showDialogAlert,
            title = uiState.dialogAlertTitle,
            message = uiState.dialogAlertMessage,
            onDismissRequest = {
                viewModel.closeDialogAlert()
            }
        )
    }
}

@Composable
fun DialogCreateUsername(
    open: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: (username: String) -> Unit,
) {
    var username by remember {
        mutableStateOf("")
    }
    var isUsernameError by remember {
        mutableStateOf(false)
    }

    val resetState = {
        username = ""
        isUsernameError = false
    }

    val callConfirmation = {
        if (username.isEmpty()) {
            isUsernameError = true
        } else {
            onConfirmation(username)
            onDismissRequest()
            resetState()
        }
    }

    if (open) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(Modifier.padding(15.dp)) {
                    Text(
                        text = "Enter username",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    OutlinedTextField(
                        value = username,
                        isError = isUsernameError,
                        supportingText = {
                            if (isUsernameError) {
                                Text(text = "Username cannot empty")
                            }
                        },
                        onValueChange = {
                            username = it
                        },
                        label = {
                            Text(text = "Username")
                        })
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = onDismissRequest) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        TextButton(onClick = {
                            callConfirmation()
                        }) {
                            Text(text = "Ok")
                        }
                    }
                }
            }
        }
    }
}


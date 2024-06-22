package com.robbies.ucokchat.ui.screen.join

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.robbies.ucokchat.util.BarcodeAnalyzer

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun JoinGroupScreen() {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    Scaffold { innerPadding ->
        if (cameraPermissionState.status.isGranted) {
            CameraScreen(innerPadding)
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
fun CameraScreen(innerPadding: PaddingValues) {
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
                        Log.d("CameraScreen", "Barcode: $it")
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
    }
}


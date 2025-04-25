package com.ms.mangaapp.presentation.main_screen.face_detection

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat


//how to implement this full code with kotlin dsl gradle setup. The code must be using latest dependencies. Face Recognition Screen (Face Detection Using MediaPipe) - (30 Points)
//- Use the front camera to display a live camera feed.
//- Integrate the MediaPipe Face Detection model.
//https://ai.google.dev/edge/mediapipe/solutions/vision/face_detector/android
//- Detect the user's face in real time.
//- Draw a reference rectangle on the screen.
//- If the detected face is within the reference rectangle, display the rectangle in green, otherwise,
//display it in red.


@Composable
fun CameraPermissionWrapper(
    onPermissionHave: @Composable () -> Unit
) {
    val context = LocalContext.current

    // Track permission status
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasCameraPermission = isGranted
        }
    )

    // Check for permission when the composable is first launched
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Show appropriate screen based on permission status
    if (hasCameraPermission) {
        //FaceDetectionScreen()

        onPermissionHave()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Camera permission is required for face detection")

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
            ) {
                Text("Grant permission")
            }
        }
    }
}


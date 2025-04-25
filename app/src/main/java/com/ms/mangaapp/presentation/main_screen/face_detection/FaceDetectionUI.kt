package com.ms.mangaapp.presentation.main_screen.face_detection

import android.annotation.SuppressLint
import android.media.FaceDetector
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@SuppressLint("ContextCastToActivity")
@Composable
fun FaceDetectionUI(){

    val activity = LocalContext.current as ComponentActivity
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    FaceDetectorComposeBuilder(
        context = context,
        scope = scope
    ).Build(
        modifier = Modifier.fillMaxWidth(0.9f),
        containerShape = RoundedCornerShape(12.dp)
    )

}
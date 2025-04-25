package com.ms.mangaapp.presentation.sign_in

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ms.mangaapp.R

@Composable
fun GoogleButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = CircleShape,
        border = BorderStroke(1.dp, Color.Gray),
        modifier = Modifier
            .size(48.dp), // Adjust size as needed
        contentPadding = PaddingValues(
            start = 8.dp,
            top = 8.dp,
            end = 8.dp,
            bottom = 8.dp
        )
        //.clip(CircleShape) // Apply CircleShape to the button itself
    ) {
        Image(
            painter = painterResource(R.drawable.google_day), // Replace with your image resource
            contentDescription = "Sign in with Google",// Adjust image size as needed // Use Crop to fill the circular area
        )
    }
}

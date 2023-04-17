package com.example.myapp.ui.match

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Match(
    accessId: String
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = accessId)
    }
}
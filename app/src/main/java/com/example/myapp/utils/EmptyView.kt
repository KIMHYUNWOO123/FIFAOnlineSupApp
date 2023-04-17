package com.example.myapp.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyView() {
    Box(modifier = Modifier.wrapContentSize().padding(top = 15.dp)) {
        Text(text = "잘못된 정보입니다. 다시 시도해주세요!", fontSize = 20.sp, color = Color.Red)
    }
}
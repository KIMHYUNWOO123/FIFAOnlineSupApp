package com.example.myapp.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapp.R

@Composable
fun EmptyView() {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(top = 15.dp)
    ) {
        Text(text = "검색결과가 없습니다. 다시 시도해주세요!", fontSize = 20.sp, color = colorResource(id = R.color.defeat))
    }
}